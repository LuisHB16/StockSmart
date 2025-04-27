package stocksmart;

import codigo.Unidad;
import codigo.movimientod;
import codigo.productos;
import codigo.productosd;
import inicio.FrameMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import static stocksmart.FontLoader.customFont;
import static stocksmart.FontLoader.customFontBold;
import static stocksmart.FontLoader.customFontBold2;

public final class FramePrediccionNEW extends javax.swing.JFrame {


    FrameMenu fmenu ;
    private ModeloRegresion modeloRegresion;
    private PrediccionRegresion prediccionRegresion;
    private final Connection conn; // Añadir conexión


    public FramePrediccionNEW(FrameMenu Menu) {
        
        initComponents();
        this.fmenu = Menu;
        
         // Define el tamaño de la ventana (ancho x alto)
        this.setLocationRelativeTo(null);
       System.out.println("Prediccion: Iniciando...");

        // Obtener conexión a la base de datos
        conn = ConexionPrediccion.obtenerConexion();
        if (conn == null) {
            jLabelPrediccion.setText("Error: No se pudo conectar a la base de datos.");
            System.err.println("Prediccion: Error de conexión a BD.");
            return; // Salir si no hay conexión
        }
        System.out.println("Prediccion: Conexión a BD establecida.");

        // Inicializar modelo y predicción
        modeloRegresion = new ModeloRegresion();
        System.out.println("Prediccion: ModeloRegresion instanciado.");

        // Cargar o entrenar modelo
        if (!modeloRegresion.cargarUltimoModelo(conn)) {
            System.out.println("Prediccion: No se encontró modelo, entrenando uno nuevo...");
            if (!modeloRegresion.entrenarYGuardar(conn)) {
                jLabelPrediccion.setText("Error: No se pudo entrenar el modelo.");
                System.err.println("Prediccion: Error al entrenar y guardar modelo.");
                // Considerar qué hacer si el entrenamiento falla (¿usar coeficientes 0?)
            } else {
                System.out.println("Prediccion: Nuevo modelo entrenado y guardado.");
            }
        } else if (!modeloRegresion.modeloActualizado()) {
            System.out.println("Prediccion: Modelo desactualizado, re-entrenando...");
            if (!modeloRegresion.entrenarYGuardar(conn)) {
                jLabelPrediccion.setText("Error: No se pudo re-entrenar el modelo.");
                System.err.println("Prediccion: Error al re-entrenar y guardar modelo.");
            } else {
                System.out.println("Prediccion: Modelo re-entrenado y guardado.");
            }
        } else {
            System.out.println("Prediccion: Modelo cargado y actualizado.");
        }

        prediccionRegresion = new PrediccionRegresion(modeloRegresion);
        System.out.println("Prediccion: PrediccionRegresion instanciado.");

        // Generar predicciones futuras si no existen
        prediccionRegresion.generarYGuardarPrediccionesFuturas(conn, 6); // Generar para los próximos 6 meses
        System.out.println("Prediccion: Predicciones futuras verificadas/generadas.");

        // Cargar datos y mostrar gráfica/predicción
        cargarYMostrarDatos();
        System.out.println("Prediccion: Datos cargados y mostrados.");

    }
     private void cargarYMostrarDatos() {
        System.out.println("Prediccion: Cargando datos históricos y predicciones...");
        ArrayList<RegistroVentaMes> ventasHistoricas = obtenerVentasHistoricas(conn, 12); // Últimos 12 meses
        ArrayList<RegistroPrediccion> prediccionesFuturas = obtenerPrediccionesFuturas(conn, 6); // Próximos 6 meses

        if (ventasHistoricas.isEmpty() && prediccionesFuturas.isEmpty()) {
            System.out.println("Prediccion: No hay datos históricos ni predicciones para mostrar.");
            // Podrías mostrar un mensaje en el panel del gráfico
            jPanel1.removeAll();
            // Añadir un JLabel indicando que no hay datos
            jPanel1.revalidate();
            jPanel1.repaint();
            jLabelPrediccion.setText("No hay datos disponibles para la predicción.");
            return;
        }

        System.out.println("Prediccion: Datos cargados. Históricos: " + ventasHistoricas.size() + ", Futuros: " + prediccionesFuturas.size());

        graficarVentas(ventasHistoricas, prediccionesFuturas);
        mostrarPrediccionSiguienteMes();
    }

    // Clase interna auxiliar para almacenar ventas históricas
    private static class RegistroVentaMes {
        int anio;
        int mes;
        double ventas;

        RegistroVentaMes(int anio, int mes, double ventas) {
            this.anio = anio;
            this.mes = mes;
            this.ventas = ventas;
        }
    }

    // Clase interna auxiliar para almacenar predicciones
    private static class RegistroPrediccion {
        int anio;
        int mes;
        double prediccion;

        RegistroPrediccion(int anio, int mes, double prediccion) {
            this.anio = anio;
            this.mes = mes;
            this.prediccion = prediccion;
        }
    }

    // Método para obtener ventas históricas (similar a ModeloRegresion pero limitado)
    private ArrayList<RegistroVentaMes> obtenerVentasHistoricas(Connection conn, int mesesAtras) {
        System.out.println("Prediccion: Obteniendo ventas históricas (" + mesesAtras + " meses)...");
        ArrayList<RegistroVentaMes> ventas = new ArrayList<>();
        LocalDate fechaLimite = LocalDate.now().minusMonths(mesesAtras);
        String sql = "SELECT YEAR(Fecha) as anio, MONTH(Fecha) as mes, SUM(Total) as ventas " +
                     "FROM venta WHERE Fecha >= ? GROUP BY anio, mes ORDER BY anio, mes";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fechaLimite.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ventas.add(new RegistroVentaMes(rs.getInt("anio"), rs.getInt("mes"), rs.getDouble("ventas")));
            }
            System.out.println("Prediccion: Ventas históricas obtenidas: " + ventas.size());
        } catch (SQLException ex) {
            System.err.println("Prediccion: Error al obtener ventas históricas: " + ex.getMessage());
            ex.printStackTrace();
        }
        return ventas;
    }

    // Método para obtener predicciones futuras
    private ArrayList<RegistroPrediccion> obtenerPrediccionesFuturas(Connection conn, int mesesAdelante) {
        System.out.println("Prediccion: Obteniendo predicciones futuras (" + mesesAdelante + " meses)...");
        ArrayList<RegistroPrediccion> predicciones = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        String sql = "SELECT anio, mes, prediccion FROM predicciones " +
                     "WHERE (anio > ? OR (anio = ? AND mes >= ?)) " +
                     "ORDER BY anio, mes LIMIT ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fechaActual.getYear());
            ps.setInt(2, fechaActual.getYear());
            ps.setInt(3, fechaActual.getMonthValue());
            ps.setInt(4, mesesAdelante);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                predicciones.add(new RegistroPrediccion(rs.getInt("anio"), rs.getInt("mes"), rs.getDouble("prediccion")));
            }
            System.out.println("Prediccion: Predicciones futuras obtenidas: " + predicciones.size());
        } catch (SQLException ex) {
            System.err.println("Prediccion: Error al obtener predicciones futuras: " + ex.getMessage());
            ex.printStackTrace();
        }
        return predicciones;
    }

    // Método graficarVentas refactorizado para usar CategoryDataset
    public void graficarVentas(ArrayList<RegistroVentaMes> ventasHistoricas, ArrayList<RegistroPrediccion> prediccionesFuturas) {
        System.out.println("Prediccion: Iniciando graficación...");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Locale localeSpanish = new Locale("es", "ES");

        // Añadir datos históricos
        System.out.println("Prediccion: Añadiendo datos históricos al dataset...");
        for (RegistroVentaMes venta : ventasHistoricas) {
            if (venta.mes >= 1 && venta.mes <= 12) { // Añadir comprobación de mes válido
                try {
                    LocalDate fecha = LocalDate.of(venta.anio, venta.mes, 1);
                    String mesNombre = fecha.getMonth().getDisplayName(TextStyle.SHORT, localeSpanish) + " " + venta.anio;
                    dataset.addValue(venta.ventas, "Ventas Históricas", mesNombre);
                    System.out.println("  - Histórico: " + mesNombre + " = " + venta.ventas);
                } catch (java.time.DateTimeException e) {
                    System.err.println("Prediccion: Error al procesar fecha histórica: " + venta.anio + "-" + venta.mes + ". " + e.getMessage());
                }
            } else {
                System.err.println("Prediccion: Mes inválido encontrado en datos históricos: " + venta.mes + " para año " + venta.anio);
            }
        }

        // Añadir datos de predicciones futuras
        System.out.println("Prediccion: Añadiendo predicciones futuras al dataset...");
        for (RegistroPrediccion pred : prediccionesFuturas) {
            if (pred.mes >= 1 && pred.mes <= 12) { // Añadir comprobación de mes válido
                try {
                    LocalDate fecha = LocalDate.of(pred.anio, pred.mes, 1);
                    String mesNombre = fecha.getMonth().getDisplayName(TextStyle.SHORT, localeSpanish) + " " + pred.anio;
                    // Asegurarse de no añadir una predicción para un mes histórico ya presente
                    // (Aunque la consulta SQL debería evitar esto si se limita a futuro)
                    try {
                        if (dataset.getValue("Ventas Históricas", mesNombre) == null) {
                            dataset.addValue(pred.prediccion, "Predicción Futura", mesNombre);
                            System.out.println("  - Futuro: " + mesNombre + " = " + pred.prediccion);
                        }
                    } catch (Exception e) { // Captura la excepción si la clave no existe (getValue lanza excepción)
                        dataset.addValue(pred.prediccion, "Predicción Futura", mesNombre);
                        System.out.println("  - Futuro (nuevo): " + mesNombre + " = " + pred.prediccion);
                    }
                } catch (java.time.DateTimeException e) {
                    System.err.println("Prediccion: Error al procesar fecha de predicción: " + pred.anio + "-" + pred.mes + ". " + e.getMessage());
                }
            } else {
                System.err.println("Prediccion: Mes inválido encontrado en predicciones futuras: " + pred.mes + " para año " + pred.anio);
            }
        }

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createLineChart(
                "Ventas Históricas y Predicción Futura", // Título
                "Mes", // Eje X
                "Ventas ($)", // Eje Y
                dataset,
                PlotOrientation.VERTICAL,
                true, // Leyenda
                true, // Tooltips
                false // URLs
        );
        System.out.println("Prediccion: Gráfico creado.");

        // Personalizar el gráfico
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // Ejes
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(customFontBold2); // Usar fuente si está disponible
        domainAxis.setTickLabelFont(customFontBold2.deriveFont(10f)); // Fuente más pequeña para etiquetas de mes
        domainAxis.setCategoryLabelPositions(org.jfree.chart.axis.CategoryLabelPositions.UP_45); // Rotar etiquetas si son muchas

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLabelFont(customFontBold2);
        rangeAxis.setTickLabelFont(customFontBold2);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // Asegurar ticks enteros si aplica

        // Renderer (líneas y formas)
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLUE); // Históricas
        renderer.setSeriesPaint(1, Color.RED); // Predicciones
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setFillPaint(Color.white);

        // Leyenda
        chart.getLegend().setItemFont(customFontBold2);

        // Título
        if (chart.getTitle() != null) { // Añadir comprobación de nulidad
            chart.getTitle().setFont(customFontBold2);
        }

        // Crear panel con el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(jPanel1.getWidth(), jPanel1.getHeight()));

        // Añadir panel al jPanel1
        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout()); // Usar BorderLayout para que el ChartPanel se expanda
        jPanel1.add(chartPanel, BorderLayout.CENTER);
        jPanel1.validate();
        jPanel1.repaint();
        System.out.println("Prediccion: Gráfico mostrado en jPanel1.");
    }

    // Método para mostrar la predicción del siguiente mes en la etiqueta
    private void mostrarPrediccionSiguienteMes() {
        LocalDate hoy = LocalDate.now();
        LocalDate siguienteMesFecha = hoy.plusMonths(1);
        int anioSiguiente = siguienteMesFecha.getYear();
        int mesSiguiente = siguienteMesFecha.getMonthValue();

        System.out.println("Prediccion: Calculando predicción para el siguiente mes (" + anioSiguiente + "-" + mesSiguiente + ")...");

        // Usar el modelo cargado/entrenado para predecir
        double prediccion = prediccionRegresion.predecir(anioSiguiente, mesSiguiente);

        // Formatear nombre del mes
        Locale localeSpanish = new Locale("es", "ES");
        String mesNombre = siguienteMesFecha.getMonth().getDisplayName(TextStyle.FULL, localeSpanish);

        // Formatear resultado
        String prediccionFormateada = String.format(localeSpanish, "%.2f", prediccion);

        // Actualizar etiqueta
        jLabelPrediccion.setText("Predicción para " + mesNombre + " " + anioSiguiente + ": $" + prediccionFormateada);
        System.out.println("Prediccion: Etiqueta actualizada: " + jLabelPrediccion.getText());

        // Opcional: Guardar esta predicción específica si no se hizo en generarYGuardarPrediccionesFuturas
        // if (!prediccionRegresion.existePrediccion(conn, anioSiguiente, mesSiguiente)) {
        //     prediccionRegresion.guardarPrediccion(conn, anioSiguiente, mesSiguiente, prediccion);
        // }
    }

   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrame = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
        pnlAltas = new javax.swing.JPanel();
        lblAltas = new javax.swing.JLabel();
        jLabelPrediccion = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        logoLoginIcon = new javax.swing.JLabel();
        btnMenuBack = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                // Super llama al comportamiento predeterminado
                super.paintComponent(g);

                // Habilitar gráficos avanzados
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo transparente o del mismo color que el contenedor
                g2d.setColor(getParent().getBackground()); // Color del contenedor padre
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Crear el gradiente de negro a azul
                GradientPaint gradiente = new GradientPaint(
                    0, 0, new Color(0, 0, 0, 175),         // Inicio: negro
                    getWidth()-(getWidth()/7), 0, new Color(50,130,233)  // Fin: azul
                );

                // Dibujar el fondo redondeado con gradiente
                g2d.setPaint(gradiente);
                int arcWidth = 25;  // Curvatura horizontal
                int arcHeight = 25; // Curvatura vertical
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            }

            @Override
            public boolean isOpaque() {
                return false; // Permitir transparencia
            }
        };
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AltasFrame");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFrame.setBackground(new java.awt.Color(50, 130, 233));
        pnlFrame.setMaximumSize(new java.awt.Dimension(1231, 716));

        stockSmartLoginLabel.setBackground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setFont(customFontBold);
        stockSmartLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stockSmartLoginLabel.setText("StockSmart");

        pnlAltas.setBackground(new java.awt.Color(255, 255, 255));

        lblAltas.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblAltas.setForeground(new java.awt.Color(50, 130, 233));
        lblAltas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAltas.setText("Prediccion");

        jLabelPrediccion.setBackground(new java.awt.Color(255, 255, 255));
        jLabelPrediccion.setFont(customFontBold2);
        jLabelPrediccion.setText("jLabel1");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlAltasLayout = new javax.swing.GroupLayout(pnlAltas);
        pnlAltas.setLayout(pnlAltasLayout);
        pnlAltasLayout.setHorizontalGroup(
            pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAltasLayout.createSequentialGroup()
                .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAltasLayout.createSequentialGroup()
                        .addGap(321, 321, 321)
                        .addComponent(lblAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelPrediccion, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAltasLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlAltasLayout.setVerticalGroup(
            pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAltasLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAltas)
                    .addComponent(jLabelPrediccion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logoLoginIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/images/logoStockSmart50.png"))); // NOI18N

        btnMenuBack.setForeground(new java.awt.Color(0, 153, 204));
        btnMenuBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMenuBack.setDoubleBuffered(false);
        btnMenuBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMenuBackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenuBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMenuBackMouseExited(evt);
            }
        });
        btnMenuBack.setLayout(null);

        jLabel1.setFont(customFont);
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/home-icon24.png"))); // NOI18N
        jLabel1.setText("Menu");
        jLabel1.setIconTextGap(10);
        btnMenuBack.add(jLabel1);
        jLabel1.setBounds(10, 0, 230, 50);

        javax.swing.GroupLayout pnlFrameLayout = new javax.swing.GroupLayout(pnlFrame);
        pnlFrame.setLayout(pnlFrameLayout);
        pnlFrameLayout.setHorizontalGroup(
            pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlFrameLayout.createSequentialGroup()
                        .addComponent(logoLoginIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnMenuBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlAltas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlFrameLayout.setVerticalGroup(
            pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFrameLayout.createSequentialGroup()
                .addGroup(pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFrameLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(logoLoginIcon))
                    .addGroup(pnlFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenuBack, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(557, Short.MAX_VALUE))
            .addComponent(pnlAltas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);

        getContentPane().add(pnlFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        this.fmenu.setVisible(true);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    private void btnMenuBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseEntered
        // TODO add your handlings code here:

    }//GEN-LAST:event_btnMenuBackMouseEntered

    private void btnMenuBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_btnMenuBackMouseExited

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelPrediccion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAltas;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlAltas;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JLabel stockSmartLoginLabel;
    // End of variables declaration//GEN-END:variables
}
