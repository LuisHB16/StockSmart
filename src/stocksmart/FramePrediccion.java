package stocksmart;

import javax.swing.*;
import java.awt.*;
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

public class FramePrediccion extends JFrame {
    private JLabel labelPrediccion;
    private JPanel panelGrafica; // Panel que contendrá el ChartPanel
    private DefaultListModel<String> modeloMeses;
    private JList<String> listaMeses;

    public FramePrediccion(Connection conn) {
        System.out.println("FramePrediccion: Constructor llamado.");
        setTitle("Predicción de Ventas");
        setSize(800, 600); // Aumentar tamaño para mejor visualización del gráfico
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        labelPrediccion = new JLabel("Predicción siguiente mes: ");
        labelPrediccion.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelPrediccion, BorderLayout.NORTH);

        modeloMeses = new DefaultListModel<>();
        listaMeses = new JList<>(modeloMeses);
        add(new JScrollPane(listaMeses), BorderLayout.WEST);

        // Inicializar panelGrafica con BorderLayout para contener el ChartPanel
        panelGrafica = new JPanel(new BorderLayout());
        add(panelGrafica, BorderLayout.CENTER);

        System.out.println("FramePrediccion: Cargando datos...");
        cargarDatos(conn);
        System.out.println("FramePrediccion: Datos cargados.");
    }

    public FramePrediccion() {
        this(ConexionPrediccion.obtenerConexion());
    }

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
                    "Predicciones de Ventas Mensuales", // Título
                    "Mes (Índice)",                   // Eje X
                    "Ventas Predichas",               // Eje Y
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, // Leyenda
                    true, // Tooltips
                    false // URLs
            );

            // Personalizar gráfico (opcional, similar a Prediccion.java)
            XYPlot plot = chart.getXYPlot();
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesShapesVisible(0, true);
            plot.setRenderer(renderer);
            plot.setBackgroundPaint(Color.WHITE);

            // Crear ChartPanel y añadirlo al panelGrafica
            ChartPanel chartPanel = new ChartPanel(chart);
            panelGrafica.removeAll(); // Limpiar panel por si acaso
            panelGrafica.add(chartPanel, BorderLayout.CENTER);
            panelGrafica.validate(); // Actualizar layout

            // Predicción siguiente mes
            int siguienteMes = mes == 12 ? 1 : mes + 1;
            int siguienteAnio = mes == 12 ? anio + 1 : anio;
            System.out.println("FramePrediccion: Calculando predicción para el siguiente mes: " + siguienteAnio + "-" + siguienteMes);
            double predSiguiente = pred.predecir(siguienteAnio, siguienteMes);
            String textoLabel = "Predicción siguiente mes (" + LocalDate.of(siguienteAnio, siguienteMes, 1).getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + "): " + String.format("%.2f", predSiguiente);
            labelPrediccion.setText(textoLabel);
            System.out.println("FramePrediccion: " + textoLabel);
        } catch (Exception ex) {
            System.err.println("FramePrediccion: Error al cargar predicciones: " + ex.getMessage());
            labelPrediccion.setText("Error al cargar predicciones");
            ex.printStackTrace();
            // Mostrar mensaje de error en el panel del gráfico
            panelGrafica.removeAll();
            panelGrafica.add(new JLabel("Error al generar gráfico: " + ex.getMessage()), BorderLayout.CENTER);
            panelGrafica.validate();
        }
    }

    // Eliminar el método dibujarGrafica anterior
    /*
    private void dibujarGrafica(Graphics g) {
        ...
    }
    */
}