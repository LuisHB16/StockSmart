package stocksmart;


import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Prediccion extends javax.swing.JFrame {

    double ventas[] = {399985.05, 281805.76, 361671.24, 363768.09, 473649.52, 400464.84, 344848.07, 478569.62, 430000.41, 530863.99, 561363.74, 600224.95, 506776.27, 447214.89, 520132.48, 443826.35, 519018.52, 450265.61, 470501.17, 479118,462810.89};
    double publicidad[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private double beta0;
    private double beta1;
    double publicidada = publicidad[publicidad.length - 1];
    public Prediccion() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//462810.89
        // Graficar ventas y publicidad
        graficarVentasYPublicidad();

        // Realizar la predicción
        calcularBetas();
        predecirVentas(publicidada); // Cambia 10 por el mes que deseas predecir
    }

    public void graficarVentasYPublicidad() {
        XYSeries seriesVentas = new XYSeries("Ventas");
        XYSeries seriesPublicidad = new XYSeries("Meses");

        int n = Math.min(ventas.length, 8); // Tomar los últimos 8 datos o menos si no hay suficientes

        for (int i = ventas.length - n; i < ventas.length; i++) {
            seriesVentas.add(publicidad[i], ventas[i]);
            seriesPublicidad.add(publicidad[i], publicidad[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesVentas);
        dataset.addSeries(seriesPublicidad);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Ventas a lo largo de los Meses",
                "Meses",
                "Ventas",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        jPanel1.removeAll();
        jPanel1.add(chartPanel);
        jPanel1.validate();
    }

    public void calcularBetas() {
        beta1 = calcularB1(ventas, publicidad);
        beta0 = calcularB0(ventas, publicidad);
    }

    public double calcularB1(double[] vectorVentas, double[] vectorPublicidad) {
        int n = Math.min(vectorPublicidad.length, 8); // Tomar los últimos 8 datos o menos si no hay suficientes
        double sumatoria;
        double sumx = 0;
        double sumy = 0;
        double sumatoria2 = 0;
        double sumxc = 0;

        int startIndex = vectorPublicidad.length - n; // Índice de inicio para los últimos 8 datos

        for (int i = startIndex; i < vectorPublicidad.length; i++) {
            sumx += vectorPublicidad[i];
            sumy += vectorVentas[i];
            sumatoria2 += vectorVentas[i] * vectorPublicidad[i];
            sumxc += Math.pow(vectorPublicidad[i], 2);
        }
        sumatoria = sumx * sumy;
        return ((n * sumatoria2) - sumatoria) / ((n * sumxc) - (Math.pow(sumx, 2)));
    }

    public double calcularB0(double[] vectorVentas, double[] vectorPublicidad) {
        int n = Math.min(vectorPublicidad.length, 8); // Tomar los últimos 8 datos o menos si no hay suficientes
        double sumx = 0;
        double sumy = 0;

        int startIndex = vectorPublicidad.length - n; // Índice de inicio para los últimos 8 datos

        for (int i = startIndex; i < vectorPublicidad.length; i++) {
            sumx += vectorPublicidad[i];
            sumy += vectorVentas[i];
        }

        return (sumy - (calcularB1(vectorVentas, vectorPublicidad) * sumx)) / n;
    }

    public void predecirVentas(double publicidad) {
        
        if(publicidad == 12){
            publicidad = 1;
        }else{
            publicidad = publicidad+1;
        }
        double ventas = beta0 + (beta1 * publicidad);
        String ventasFormateadas = String.format("%.2f", ventas);

        System.out.println("Dado el valor de publicidad: " + publicidad + ", las ventas predichas son = " + ventasFormateadas);

        // Actualizar el JLabel con la predicción en la parte de abajo
        jLabelPrediccion.setText("Predicción de este mes: " + ventasFormateadas);
    }

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
        jLabelPrediccion.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
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
