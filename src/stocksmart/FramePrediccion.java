package stocksmart;

import inicio.FrameMenu; // Añadido
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter; // Añadido
import java.awt.event.MouseEvent; // Añadido
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
// Imports para JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

// Imports de FontLoader y otros necesarios
import static stocksmart.FontLoader.customFont;
import static stocksmart.FontLoader.customFontBold;
import static stocksmart.FontLoader.customFontBold2;

public class FramePrediccion extends JFrame {
    private FrameMenu fmenu; // Añadido
    private JLabel labelPrediccion;
    private JPanel panelGrafica; // Panel que contendrá el ChartPanel
    private DefaultListModel<String> modeloMeses;
    private JList<String> listaMeses;

    // Nuevos componentes UI
    private JPanel pnlFrame;
    private JPanel pnlSidebar;
    private JPanel pnlContent;
    private JLabel stockSmartLoginLabel;
    private JLabel logoLoginIcon;
    private JPanel btnMenuBack;
    private JLabel lblMenu;
    private JLabel lblTituloPrediccion; // Título específico para esta ventana

    // Constructor actualizado
    public FramePrediccion(FrameMenu menu, Connection conn) {
        this.fmenu = menu;
        System.out.println("FramePrediccion: Constructor llamado.");
        initComponents(); // Llamar a initComponents para configurar la UI
        setLocationRelativeTo(null);
        System.out.println("FramePrediccion: Cargando datos...");
        cargarDatos(conn);
        System.out.println("FramePrediccion: Datos cargados.");
    }

    // Constructor por defecto actualizado
    public FramePrediccion(FrameMenu menu) {
        this(menu, ConexionPrediccion.obtenerConexion());
    }

    // Método para inicializar componentes (similar a NetBeans initComponents)
    private void initComponents() {
        setTitle("Predicción de Ventas - StockSmart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(1306, 716); // Tamaño similar a FrameTrabajadores

        pnlFrame = new JPanel();
        pnlFrame.setBackground(new Color(50, 130, 233));
        pnlFrame.setLayout(null); // Usar layout absoluto para posicionar paneles

        // --- Panel Lateral (Sidebar) ---
        pnlSidebar = new JPanel();
        pnlSidebar.setBackground(new Color(50, 130, 233));
        pnlSidebar.setBounds(0, 0, 250, 716); // Posición y tamaño de la barra lateral
        pnlSidebar.setLayout(null);
        pnlFrame.add(pnlSidebar);

        logoLoginIcon = new JLabel();
        logoLoginIcon.setHorizontalAlignment(SwingConstants.CENTER);
        logoLoginIcon.setIcon(new ImageIcon(getClass().getResource("/assests/images/logoStockSmart50.png"))); // Asegúrate que la ruta es correcta
        logoLoginIcon.setBounds(0, 30, 250, 100); // Ajusta según el tamaño del logo
        pnlSidebar.add(logoLoginIcon);

        stockSmartLoginLabel = new JLabel("StockSmart");
        stockSmartLoginLabel.setFont(customFontBold); // Usar fuente personalizada
        stockSmartLoginLabel.setForeground(Color.WHITE);
        stockSmartLoginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stockSmartLoginLabel.setBounds(0, 130, 250, 30);
        pnlSidebar.add(stockSmartLoginLabel);

        // Botón de Menú (Panel con gradiente)
        btnMenuBack = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradiente = new GradientPaint(
                    0, 0, new Color(0, 0, 0, 175),
                    getWidth()-(getWidth()/7), 0, new Color(50,130,233)
                );
                g2d.setPaint(gradiente);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        btnMenuBack.setBounds(20, 200, 210, 50); // Posición del botón de menú
        btnMenuBack.setLayout(new BorderLayout());
        btnMenuBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlSidebar.add(btnMenuBack);

        lblMenu = new JLabel("Menu");
        lblMenu.setFont(customFontBold2); // Usar fuente personalizada
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
        lblMenu.setIcon(new ImageIcon(getClass().getResource("/assests/icons/home-icon24.png"))); // Asegúrate que la ruta es correcta
        lblMenu.setIconTextGap(15);
        btnMenuBack.add(lblMenu, BorderLayout.CENTER);

        // Acción para el botón de menú
        btnMenuBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fmenu != null) {
                    fmenu.setVisible(true);
                }
                dispose(); // Cierra la ventana actual
            }
        });

        // --- Panel de Contenido ---
        pnlContent = new JPanel();
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBounds(250, 0, 1056, 716); // Posición y tamaño del área de contenido
        pnlContent.setLayout(new BorderLayout(10, 10)); // Usar BorderLayout para organizar contenido
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Añadir padding
        pnlFrame.add(pnlContent);

        // Título específico de la ventana
        lblTituloPrediccion = new JLabel("Predicción de Ventas");
        lblTituloPrediccion.setFont(new Font("Segoe UI", Font.BOLD, 48)); // Fuente similar a FrameTrabajadores
        lblTituloPrediccion.setForeground(new Color(50, 130, 233));
        lblTituloPrediccion.setHorizontalAlignment(SwingConstants.CENTER);
        pnlContent.add(lblTituloPrediccion, BorderLayout.NORTH);

        // Panel para la lista y la gráfica
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(Color.WHITE);
        pnlContent.add(panelCentral, BorderLayout.CENTER);

        // Lista de Meses (ahora a la izquierda del gráfico)
        modeloMeses = new DefaultListModel<>();
        listaMeses = new JList<>(modeloMeses);
        listaMeses.setFont(customFontBold2); // Aplicar fuente
        JScrollPane scrollLista = new JScrollPane(listaMeses);
        scrollLista.setPreferredSize(new Dimension(200, 0)); // Ancho preferido para la lista
        panelCentral.add(scrollLista, BorderLayout.WEST);

        // Panel para la gráfica (ocupará el centro)
        panelGrafica = new JPanel(new BorderLayout());
        panelGrafica.setBackground(Color.WHITE);
        panelCentral.add(panelGrafica, BorderLayout.CENTER);

        // Etiqueta de predicción (abajo)
        labelPrediccion = new JLabel("Predicción siguiente mes: ");
        labelPrediccion.setFont(customFontBold2.deriveFont(18f)); // Fuente y tamaño
        labelPrediccion.setHorizontalAlignment(SwingConstants.CENTER);
        pnlContent.add(labelPrediccion, BorderLayout.SOUTH);

        // Añadir el panel principal al frame
        setContentPane(pnlFrame);
    }

    // Constructor original (ahora privado o eliminado si no se usa)
    /*
    public FramePrediccion(Connection conn) {
        // ... código anterior ...
    }
    */

    // Constructor por defecto original (ahora privado o eliminado)
    /*
    public FramePrediccion() {
        this(ConexionPrediccion.obtenerConexion());
    }
    */

    private void cargarDatos(Connection conn) {
        System.out.println("FramePrediccion: Iniciando carga de datos para predicción.");
        try {
            ModeloRegresion modelo = new ModeloRegresion();
            System.out.println("FramePrediccion: Cargando/Entrenando modelo...");
            if (!modelo.cargarUltimoModelo(conn) || !modelo.modeloActualizado()) {
                System.out.println("FramePrediccion: Modelo no encontrado o desactualizado. Entrenando nuevo modelo...");
                modelo.entrenarYGuardar(conn);
                modelo.cargarUltimoModelo(conn);
            }
            System.out.println("FramePrediccion: Modelo cargado/entrenado.");
            PrediccionRegresion pred = new PrediccionRegresion(modelo);

            // Generar y guardar predicciones futuras si no existen (ej. para los próximos 6 meses)
            System.out.println("FramePrediccion: Verificando y generando predicciones futuras...");
            pred.generarYGuardarPrediccionesFuturas(conn, 6);
            System.out.println("FramePrediccion: Verificación y generación de predicciones futuras completada.");

            LocalDate ahora = LocalDate.now();
            int anio = ahora.getYear();
            int mes = ahora.getMonthValue();
            System.out.println("FramePrediccion: Obteniendo últimas 5 predicciones...");
            // Obtener predicciones de los últimos 5 meses
            ArrayList<Double> ultimas = pred.obtenerUltimasPredicciones(conn, 5);
            System.out.println("FramePrediccion: Últimas predicciones obtenidas: " + ultimas.size());
            modeloMeses.clear();
            // Crear series para JFreeChart
            XYSeries seriesPredicciones = new XYSeries("Predicciones Históricas");
            LocalDate fechaBase = ahora.minusMonths(ultimas.size() -1);

            for (int i = 0; i < ultimas.size(); i++) {
                LocalDate fecha = fechaBase.plusMonths(i);
                String nombreMes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                double valor = ultimas.get(i);
                String itemLista = nombreMes + ": " + String.format("%.2f", valor);
                modeloMeses.addElement(itemLista);
                System.out.println("FramePrediccion: Añadido a lista: " + itemLista);
                // Añadir punto a la serie (usando índice como X)
                seriesPredicciones.add(i, valor);
            }

            // Crear dataset y gráfico
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(seriesPredicciones);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    null, // Título del gráfico eliminado, ya tenemos uno general
                    "Meses (Histórico)",                   // Eje X
                    "Ventas Predichas",               // Eje Y
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, // Leyenda
                    true, // Tooltips
                    false // URLs
            );

            // Personalizar gráfico
            chart.setBackgroundPaint(Color.WHITE); // Fondo blanco para el gráfico
            XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(Color.WHITE); // Fondo blanco del área de trazado
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

            // Aplicar fuentes personalizadas al gráfico
            chart.getTitle().setFont(customFontBold2);
            chart.getLegend().setItemFont(customFontBold2);
            plot.getDomainAxis().setLabelFont(customFontBold2);
            plot.getDomainAxis().setTickLabelFont(customFontBold2.deriveFont(10f));
            plot.getRangeAxis().setLabelFont(customFontBold2);
            plot.getRangeAxis().setTickLabelFont(customFontBold2.deriveFont(10f));

            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(50, 130, 233)); // Color azul principal
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f)); // Línea un poco más gruesa

            // Crear ChartPanel y añadirlo al panelGrafica
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setBorder(BorderFactory.createEmptyBorder()); // Sin borde extra
            panelGrafica.removeAll(); // Limpiar panel por si acaso
            panelGrafica.add(chartPanel, BorderLayout.CENTER);
            panelGrafica.validate(); // Actualizar layout
            panelGrafica.repaint(); // Repintar

            // Predicción siguiente mes
            int siguienteMes = mes == 12 ? 1 : mes + 1;
            int siguienteAnio = mes == 12 ? anio + 1 : anio;
            System.out.println("FramePrediccion: Calculando predicción para el siguiente mes: " + siguienteAnio + "-" + siguienteMes);
            double predSiguiente = pred.predecir(siguienteAnio, siguienteMes);
            String nombreSiguienteMes = LocalDate.of(siguienteAnio, siguienteMes, 1).getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            String textoLabel = String.format("Predicción %s: %.2f", nombreSiguienteMes, predSiguiente);
            labelPrediccion.setText(textoLabel);
            System.out.println("FramePrediccion: " + textoLabel);
        } catch (Exception ex) {
            System.err.println("FramePrediccion: Error al cargar predicciones: " + ex.getMessage());
            labelPrediccion.setText("Error al cargar predicciones");
            ex.printStackTrace();
            // Mostrar mensaje de error en el panel del gráfico
            panelGrafica.removeAll();
            JLabel errorLabel = new JLabel("Error al generar gráfico: " + ex.getMessage());
            errorLabel.setFont(customFontBold2);
            errorLabel.setForeground(Color.RED);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelGrafica.add(errorLabel, BorderLayout.CENTER);
            panelGrafica.validate();
            panelGrafica.repaint();
        }
    }

    // Eliminar el método dibujarGrafica anterior si existía
    /*
    private void dibujarGrafica(Graphics g) {
        ...
    }
    */

    // Método main para pruebas (opcional)
    /*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Necesitarías un FrameMenu dummy o null para probar
                new FramePrediccion(null).setVisible(true);
            }
        });
    }
    */
}