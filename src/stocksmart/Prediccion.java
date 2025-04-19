package stocksmart;


import codigo.ventad;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import static stocksmart.FontLoader.customFontBold2;


public class Prediccion extends javax.swing.JFrame {

    private double[] ventas;
    private double w; // Peso para la regresión lineal
    private double b; // Sesgo (bias) para la regresión lineal
    private String[] nombresMeses; // Para mostrar nombres de meses en la gráfica
    private ventad ventaDAO;
    private boolean modeloEntrenado = false;
    
    public Prediccion() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Inicializar el DAO de ventas
        ventaDAO = new ventad();
        
        // Obtener datos reales de ventas desde la base de datos
        cargarDatosVentas();
        
        // Entrenar el modelo si hay suficientes datos
        if (ventas.length >= 8) {
            entrenarModelo();
            modeloEntrenado = true;
        }
        
        // Graficar ventas históricas y predicción
        graficarVentas();

        // Realizar la predicción para el siguiente mes
        predecirVentasSiguienteMes();
    }
    
    private void cargarDatosVentas() {
        // Obtener ventas desde la base de datos
        ventas = ventaDAO.obtenerVentasPorMes();
        
        // Verificar si hay datos válidos
        if (ventas == null || ventas.length == 0) {
            System.out.println("No se pudieron cargar datos de ventas");
            // Crear datos de ejemplo para evitar errores
            ventas = new double[]{230.0, 225.0, 5.0, 175.0, 160.0, 83.0, 160.0, 160.0};
        }
        
        // Imprimir los datos cargados para depuración
        System.out.println("Datos de ventas cargados en Prediccion:");
        for (int i = 0; i < ventas.length; i++) {
            System.out.println("Venta " + (i+1) + ": " + ventas[i]);
        }
        
        // Generar nombres de meses
        generarNombresMeses();
    }
    
    private void generarNombresMeses() {
        // Crear array de nombres de meses
        nombresMeses = new String[ventas.length + 1]; // +1 para incluir el mes de predicción
        
        // Obtener el mes actual
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        
        // Retroceder hasta el primer mes de los datos
        cal.add(Calendar.MONTH, -ventas.length);
        
        // Generar nombres de meses
        for (int i = 0; i < nombresMeses.length; i++) {
            nombresMeses[i] = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH, 1);
        }
    }

    public void graficarVentas() {
        XYSeries seriesVentas = new XYSeries("Ventas Históricas");
        XYSeries seriesPrediccion = new XYSeries("Predicción");

        // Determinar cuántos meses mostrar (máximo 8 meses históricos)
        int numMesesMostrar = Math.min(ventas.length, 8);
        int startIndex = ventas.length - numMesesMostrar;

        // Añadir puntos de ventas históricas
        for (int i = 0; i < numMesesMostrar; i++) {
            seriesVentas.add(i, ventas[startIndex + i]);
        }

        // Añadir punto de predicción si el modelo está entrenado
        if (modeloEntrenado) {
            double prediccion = predecirVenta(numMesesMostrar);
            seriesPrediccion.add(numMesesMostrar, prediccion);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesVentas);
        dataset.addSeries(seriesPrediccion);
        
        // Crear gráfico con nombres de meses en el eje X
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Ventas Históricas y Predicción",
                "Mes",
                "Ventas",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        // Aplicar fuente personalizada al gráfico
        chart.getTitle().setFont(customFontBold2);
        chart.getLegend().setItemFont(customFontBold2);
        
        // Personalizar el gráfico
        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setLabelFont(customFontBold2);
        plot.getDomainAxis().setTickLabelFont(customFontBold2);
        plot.getRangeAxis().setLabelFont(customFontBold2);
        plot.getRangeAxis().setTickLabelFont(customFontBold2);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        // Configurar colores y formas
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        plot.setRenderer(renderer);
        
        // Crear panel con el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        jPanel1.removeAll();
        jPanel1.add(chartPanel);
        jPanel1.validate();
    }

    private void entrenarModelo() {
        // Implementación de regresión lineal simple
        // Usaremos descenso de gradiente para encontrar los mejores valores de w y b
        
        // Inicializar parámetros
        w = 0.0;
        b = 0.0;
        double tasaAprendizaje = 0.01;
        int iteraciones = 1000;
        int numMuestras = Math.min(ventas.length, 8);
        
        // Normalizar datos para mejorar convergencia
        double[] ventasNormalizadas = normalizarDatos(ventas, numMuestras);
        
        System.out.println("Iniciando entrenamiento del modelo...");
        System.out.println("Error inicial: " + calcularError(ventasNormalizadas, numMuestras));
        
        // Descenso de gradiente
        for (int iter = 0; iter < iteraciones; iter++) {
            double sumErrorW = 0.0;
            double sumErrorB = 0.0;
            
            // Calcular gradientes
            for (int i = 0; i < numMuestras - 1; i++) {
                double prediccion = w * ventasNormalizadas[i] + b;
                double error = prediccion - ventasNormalizadas[i + 1];
                
                sumErrorW += error * ventasNormalizadas[i];
                sumErrorB += error;
            }
            
            // Actualizar parámetros
            w = w - tasaAprendizaje * (sumErrorW / (numMuestras - 1));
            b = b - tasaAprendizaje * (sumErrorB / (numMuestras - 1));
            
            // Imprimir progreso cada 100 iteraciones
            if (iter % 100 == 0) {
                System.out.println("Iteración " + iter + ", Error: " + calcularError(ventasNormalizadas, numMuestras));
            }
        }
        
        System.out.println("Entrenamiento completado.");
        System.out.println("Peso final (w): " + w);
        System.out.println("Sesgo final (b): " + b);
        System.out.println("Error final: " + calcularError(ventasNormalizadas, numMuestras));
    }
    
    private double calcularError(double[] datos, int numMuestras) {
        double sumErrorCuadrado = 0.0;
        
        for (int i = 0; i < numMuestras - 1; i++) {
            double prediccion = w * datos[i] + b;
            double error = prediccion - datos[i + 1];
            sumErrorCuadrado += error * error;
        }
        
        return Math.sqrt(sumErrorCuadrado / (numMuestras - 1)); // RMSE
    }
    
    private double[] normalizarDatos(double[] datos, int numMuestras) {
        // Obtener los últimos numMuestras elementos
        int startIndex = datos.length - numMuestras;
        double[] datosRecientes = new double[numMuestras];
        
        for (int i = 0; i < numMuestras; i++) {
            datosRecientes[i] = datos[startIndex + i];
        }
        
        // Calcular media y desviación estándar
        double suma = 0.0;
        for (double valor : datosRecientes) {
            suma += valor;
        }
        double media = suma / numMuestras;
        
        double sumaCuadrados = 0.0;
        for (double valor : datosRecientes) {
            sumaCuadrados += Math.pow(valor - media, 2);
        }
        double desviacionEstandar = Math.sqrt(sumaCuadrados / numMuestras);
        
        // Evitar división por cero
        if (desviacionEstandar == 0) {
            desviacionEstandar = 1.0;
        }
        
        // Normalizar datos
        double[] datosNormalizados = new double[numMuestras];
        for (int i = 0; i < numMuestras; i++) {
            datosNormalizados[i] = (datosRecientes[i] - media) / desviacionEstandar;
        }
        
        return datosNormalizados;
    }
    
    private double predecirVenta(int indice) {
        // Obtener los últimos 8 meses o menos si no hay suficientes
        int numMuestras = Math.min(ventas.length, 8);
        int startIndex = ventas.length - numMuestras;
        
        // Normalizar datos
        double[] ventasNormalizadas = normalizarDatos(ventas, numMuestras);
        
        // Calcular predicción normalizada
        double prediccionNormalizada = w * ventasNormalizadas[numMuestras - 1] + b;
        
        // Desnormalizar para obtener el valor real
        double media = 0.0;
        double desviacionEstandar = 0.0;
        
        // Calcular media y desviación estándar de los datos originales
        double suma = 0.0;
        for (int i = 0; i < numMuestras; i++) {
            suma += ventas[startIndex + i];
        }
        media = suma / numMuestras;
        
        double sumaCuadrados = 0.0;
        for (int i = 0; i < numMuestras; i++) {
            sumaCuadrados += Math.pow(ventas[startIndex + i] - media, 2);
        }
        desviacionEstandar = Math.sqrt(sumaCuadrados / numMuestras);
        
        // Evitar división por cero
        if (desviacionEstandar == 0) {
            desviacionEstandar = 1.0;
        }
        
        // Desnormalizar
        double prediccionReal = prediccionNormalizada * desviacionEstandar + media;
        
        // Asegurar que la predicción no sea negativa
        return Math.max(0, prediccionReal);
    }
    
    private void predecirVentasSiguienteMes() {
        if (!modeloEntrenado) {
            jLabelPrediccion.setText("No hay suficientes datos para hacer una predicción");
            return;
        }
        
        // Determinar el mes siguiente
        int numMesesMostrar = Math.min(ventas.length, 8);
        
        // Calcular predicción
        double prediccion = predecirVenta(numMesesMostrar);
        
        // Obtener nombre del mes para la predicción
        String mesSiguiente = nombresMeses[nombresMeses.length - 1];
        
        // Formatear resultado
        String prediccionFormateada = String.format("%.2f", prediccion);
        
        // Calcular error estimado
        double errorEstimado = calcularErrorEstimado();
        String errorFormateado = String.format("%.2f", errorEstimado);
        
        // Actualizar etiqueta
        jLabelPrediccion.setText("Predicción para " + mesSiguiente + ": $" + prediccionFormateada + " (±$" + errorFormateado + ")");
        
        // Imprimir información en consola
        System.out.println("Predicción para " + mesSiguiente + ": $" + prediccionFormateada);
        System.out.println("Error estimado: ±$" + errorFormateado);
    }
    
    private double calcularErrorEstimado() {
        // Obtener los últimos 8 meses o menos si no hay suficientes
        int numMuestras = Math.min(ventas.length, 8);
        int startIndex = ventas.length - numMuestras;
        
        double sumErrorCuadrado = 0.0;
        
        // Calcular error en las predicciones históricas
        for (int i = startIndex; i < ventas.length - 1; i++) {
            double prediccion = predecirVentaHistorica(i - startIndex);
            double error = ventas[i + 1] - prediccion;
            sumErrorCuadrado += error * error;
        }
        
        return Math.sqrt(sumErrorCuadrado / (numMuestras - 1)); // RMSE
    }
    
    private double predecirVentaHistorica(int indice) {
        // Obtener los datos hasta el índice especificado
        int numMuestras = Math.min(indice + 1, 8);
        int startIndex = ventas.length - numMuestras - (ventas.length - indice - 1);
        
        if (startIndex < 0 || startIndex >= ventas.length) {
            return ventas[ventas.length - 1]; // Valor por defecto si hay error
        }
        
        // Crear array temporal con los datos hasta el índice
        double[] ventasTemp = new double[numMuestras];
        for (int i = 0; i < numMuestras; i++) {
            ventasTemp[i] = ventas[startIndex + i];
        }
        
        // Normalizar datos
        double[] ventasNormalizadas = normalizarDatos(ventasTemp, numMuestras);
        
        // Calcular predicción normalizada
        double prediccionNormalizada = w * ventasNormalizadas[numMuestras - 1] + b;
        
        // Desnormalizar para obtener el valor real
        double media = 0.0;
        double desviacionEstandar = 0.0;
        
        // Calcular media y desviación estándar de los datos originales
        double suma = 0.0;
        for (int i = 0; i < numMuestras; i++) {
            suma += ventasTemp[i];
        }
        media = suma / numMuestras;
        
        double sumaCuadrados = 0.0;
        for (int i = 0; i < numMuestras; i++) {
            sumaCuadrados += Math.pow(ventasTemp[i] - media, 2);
        }
        desviacionEstandar = Math.sqrt(sumaCuadrados / numMuestras);
        
        // Evitar división por cero
        if (desviacionEstandar == 0) {
            desviacionEstandar = 1.0;
        }
        
        // Desnormalizar
        double prediccionReal = prediccionNormalizada * desviacionEstandar + media;
        
        // Asegurar que la predicción no sea negativa
        return Math.max(0, prediccionReal);
    }

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
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 990, 570));

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