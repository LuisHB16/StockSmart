package stocksmart;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class FramePrediccion extends JFrame {
    private JLabel labelPrediccion;
    private JPanel panelGrafica;
    private DefaultListModel<String> modeloMeses;
    private JList<String> listaMeses;

    public FramePrediccion(Connection conn) {
        System.out.println("FramePrediccion: Constructor llamado.");
        setTitle("Predicción de Ventas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        labelPrediccion = new JLabel("Predicción siguiente mes: ");
        labelPrediccion.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelPrediccion, BorderLayout.NORTH);

        modeloMeses = new DefaultListModel<>();
        listaMeses = new JList<>(modeloMeses);
        add(new JScrollPane(listaMeses), BorderLayout.WEST);

        panelGrafica = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                System.out.println("FramePrediccion: Dibujando gráfica...");
                dibujarGrafica(g);
            }
        };
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
            for (int i = ultimas.size() - 1; i >= 0; i--) {
                LocalDate fecha = ahora.minusMonths(ultimas.size() - 1 - i);
                String nombreMes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                String itemLista = nombreMes + ": " + String.format("%.2f", ultimas.get(i));
                modeloMeses.addElement(itemLista);
                System.out.println("FramePrediccion: Añadido a lista: " + itemLista);
            }
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
        }
    }

    private void dibujarGrafica(Graphics g) {
        System.out.println("FramePrediccion: Iniciando dibujo de gráfica.");
        // Aquí puedes implementar una gráfica simple de barras con los datos de modeloMeses
        int width = panelGrafica.getWidth();
        int height = panelGrafica.getHeight();
        int n = modeloMeses.size();
        System.out.println("FramePrediccion: Número de elementos para graficar: " + n);
        if (n == 0) {
            System.out.println("FramePrediccion: No hay datos para graficar.");
            return;
        }
        int barWidth = width / (n * 2);
        int maxBarHeight = height - 40;
        double max = 0;
        for (int i = 0; i < n; i++) {
            String[] parts = modeloMeses.get(i).split(": ");
            if (parts.length == 2) {
                try {
                    double val = Double.parseDouble(parts[1].replace(',', '.')); // Asegurar punto decimal
                    if (val > max) max = val;
                } catch (NumberFormatException e) {
                    System.err.println("FramePrediccion: Error al parsear valor para gráfica: " + parts[1]);
                }
            }
        }
        System.out.println("FramePrediccion: Valor máximo para escala de gráfica: " + max);
        if (max == 0) max = 1; // Evitar división por cero si todos los valores son 0

        for (int i = 0; i < n; i++) {
            String[] parts = modeloMeses.get(i).split(": ");
            if (parts.length == 2) {
                try {
                    double val = Double.parseDouble(parts[1].replace(',', '.')); // Asegurar punto decimal
                    int barHeight = (int) (val / max * maxBarHeight);
                    g.setColor(Color.BLUE);
                    g.fillRect(40 + i * 2 * barWidth, height - barHeight - 20, barWidth, barHeight);
                    g.setColor(Color.BLACK);
                    g.drawString(parts[0], 40 + i * 2 * barWidth, height - 5);
                    System.out.println("FramePrediccion: Dibujada barra para " + parts[0] + " con altura " + barHeight);
                } catch (NumberFormatException e) {
                    System.err.println("FramePrediccion: Error al dibujar barra para valor: " + parts[1]);
                }
            }
        }
        System.out.println("FramePrediccion: Dibujo de gráfica completado.");
    }
}