package stocksmart;


import codigo.movimiento;
import codigo.movimientod;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Movimientos extends javax.swing.JFrame {

    private List<movimiento> listaMovimiento = new ArrayList<>();

    public Movimientos() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ListarMovimientos();
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarVentasPorFecha();
            }
        });
        // Agregar ActionListener al CheckBox
        jCheckBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Habilitar o deshabilitar el ComboBox según el estado del CheckBox
                boxVendedor.setEnabled(jCheckBox1.isSelected());
            }
        });
        jCheckBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Habilitar o deshabilitar el campo de texto según el estado del CheckBox2
                jTextField1.setEnabled(jCheckBox2.isSelected());
            }
        });
    }

    public void filtrarVentasPorFecha() {
        // Obtén las fechas seleccionadas en los componentes de fecha
        Date fechaInicio = fechaInicial.getDate();
        Date fechaFin = fechafinal.getDate();
        // Verifica que ambas fechas no sean nulas
        if (fechaInicio != null && fechaFin != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for (int i = 0; i < listaMovimiento.size(); i++) {
                String fechaString = listaMovimiento.get(i).getFehca();
                try {
                    Date fechaVenta = dateFormat.parse(fechaString);
                    boolean fechaValida = fechaVenta.compareTo(fechaInicio) >= 0 && fechaVenta.compareTo(fechaFin) <= 0;
                    if (fechaValida && ((jCheckBox1.isSelected() && boxVendedor.getSelectedItem().equals(listaMovimiento.get(i).getMovimiento()))
                            || (jCheckBox2.isSelected() && listaMovimiento.get(i).getCoidgo().startsWith(jTextField1.getText()))
                            || (jCheckBox1.isSelected() && jCheckBox2.isSelected() && boxVendedor.getSelectedItem().equals(listaMovimiento.get(i).getMovimiento()) && listaMovimiento.get(i).getCoidgo().startsWith(jTextField1.getText()))
                            || (!jCheckBox1.isSelected() && !jCheckBox2.isSelected()))) {
                        Object[] ob = new Object[6];
                        ob[0] = listaMovimiento.get(i).getMovimiento();
                        ob[1] = listaMovimiento.get(i).getCoidgo();
                        ob[2] = listaMovimiento.get(i).getCantidad();
                        ob[3] = listaMovimiento.get(i).getInventario_antes();
                        ob[4] = listaMovimiento.get(i).getInventario_antes() + listaMovimiento.get(i).getCantidad();
                        ob[5] = listaMovimiento.get(i).getFehca();
                        model.addRow(ob);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione las fechas de inicio y fin.");
        }
    }

    public void ListarMovimientos() {
        if (listaMovimiento.isEmpty()) {
            movimientod proDAO = new movimientod();
            listaMovimiento = proDAO.Listardev();
        }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (int i = 0; i < listaMovimiento.size(); i++) {
            Object[] ob = new Object[6];

            ob[0] = listaMovimiento.get(i).getMovimiento();
            ob[1] = listaMovimiento.get(i).getCoidgo();
            ob[2] = listaMovimiento.get(i).getCantidad();
            ob[3] = listaMovimiento.get(i).getInventario_antes();

            // Utiliza equals para comparar cadenas en lugar de ==
            if ("Devolucion".equals(listaMovimiento.get(i).getMovimiento()) || "Alta".equals(listaMovimiento.get(i).getMovimiento())) {
               
                ob[4] = listaMovimiento.get(i).getInventario_antes() + (listaMovimiento.get(i).getCantidad()*listaMovimiento.get(i).getFormato());
            } else if ("Venta".equals(listaMovimiento.get(i).getMovimiento()) || "Pedido".equals(listaMovimiento.get(i).getMovimiento())) {
                
                ob[4] = listaMovimiento.get(i).getInventario_antes() - (listaMovimiento.get(i).getCantidad()*listaMovimiento.get(i).getFormato());
            } else {
                ob[4] = listaMovimiento.get(i).getInventario_antes() + (listaMovimiento.get(i).getCantidad()*listaMovimiento.get(i).getFormato());
            }

            ob[5] = listaMovimiento.get(i).getFehca();
            model.addRow(ob);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        fechafinal = new com.toedter.calendar.JDateChooser();
        btnBuscar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        boxVendedor = new javax.swing.JComboBox<>();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Movimiento", "Codigo", "Cantidad", "invantario antes", "Inventario despues", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setResizable(false);
            table.getColumnModel().getColumn(5).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 1130, -1));

        fechaInicial.setBackground(new java.awt.Color(255, 255, 255));
        fechaInicial.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(fechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 110, 210, 30));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel1.setText("Fecha final");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 70, 30));

        fechafinal.setBackground(new java.awt.Color(255, 255, 255));
        fechafinal.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(fechafinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 110, 210, 30));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/lupa.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 110, 70, 30));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel3.setText("Fecha Inicial");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 70, 30));
        getContentPane().add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 30, 30));

        boxVendedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alta", "Ajuste", "Venta", "Pedido", "Devolucion" }));
        boxVendedor.setEnabled(false);
        getContentPane().add(boxVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 150, 30));

        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        getContentPane().add(jCheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, 20, 30));

        jTextField1.setEnabled(false);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 110, 30));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel2.setText("Tipo de movimiento:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 150, -1));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel4.setText("Codigo:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 60, -1));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.13 PM.jpeg"))); // NOI18N
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        a.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout aLayout = new javax.swing.GroupLayout(a);
        a.setLayout(aLayout);
        aLayout.setHorizontalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1920, Short.MAX_VALUE)
        );
        aLayout.setVerticalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );

        getContentPane().add(a, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Movimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Movimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Movimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Movimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Movimientos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JComboBox<String> boxVendedor;
    private javax.swing.JButton btnBuscar;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private com.toedter.calendar.JDateChooser fechafinal;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
