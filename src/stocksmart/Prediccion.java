package stocksmart;


import codigo.ventad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

// Quitar imports no usados si es necesario después de refactorizar
// import codigo.ventad; // Ya no se usa directamente aquí
// import java.text.SimpleDateFormat; // Reemplazado por LocalDate
// import java.util.Calendar; // Reemplazado por LocalDate
// import java.util.Date; // Reemplazado por LocalDate
// import org.jfree.chart.plot.XYPlot; // Reemplazado por CategoryPlot
// import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer; // Reemplazado por LineAndShapeRenderer
// import org.jfree.data.xy.XYSeries; // Reemplazado por DefaultCategoryDataset
// import org.jfree.data.xy.XYSeriesCollection; // Reemplazado por DefaultCategoryDataset

import static stocksmart.FontLoader.customFontBold2; // Mantener si se usa

public class Prediccion extends javax.swing.JFrame {

    // Eliminar variables de la regresión lineal simple anterior
    // private double[] ventas;
    // private double w;
    // private double b;
    // private String[] nombresMeses;
    // private ventad ventaDAO;
    // private boolean modeloEntrenado = false;

    // Nuevas variables para el modelo de regresión múltiple
    private ModeloRegresion modeloRegresion;
    private PrediccionRegresion prediccionRegresion;
    private Connection conn; // Añadir conexión

    public Prediccion() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

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

    // Nuevo método para cargar datos y actualizar UI
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
        chart.getTitle().setFont(customFontBold2);

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

    // --- Métodos antiguos eliminados --- 
    // private void cargarDatosVentas() { ... }
    // private void generarNombresMeses() { ... }
    // private void entrenarModelo() { ... }
    // private double calcularError(double[] datos, int numMuestras) { ... }
    // private double[] normalizarDatos(double[] datos, int numMuestras) { ... }
    // private double predecirVenta(int indice) { ... }
    // private void predecirVentasSiguienteMes() { ... }
    // private double calcularErrorEstimado() { ... }
    // private double predecirVentaHistorica(int indice) { ... }

    // NO TOCAR - initComponents generado por NetBeans
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelPrediccion = new javax.swing.JLabel();
        a1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 100, 590, 570));

        jLabelPrediccion.setBackground(new java.awt.Color(255, 255, 255));
        jLabelPrediccion.setFont(customFontBold2);
        jLabelPrediccion.setText("jLabel1");
        getContentPane().add(jLabelPrediccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 70, 290, 30));

        a1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout a1Layout = new javax.swing.GroupLayout(a1);
        a1.setLayout(a1Layout);
        a1Layout.setHorizontalGroup(
            a1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1920, Short.MAX_VALUE)
        );
        a1Layout.setVerticalGroup(
            a1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );

        getContentPane().add(a1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Prediccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Prediccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Prediccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Prediccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Prediccion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a1;
    private javax.swing.JLabel jLabelPrediccion;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}