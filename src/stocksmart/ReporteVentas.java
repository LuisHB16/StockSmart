package stocksmart;


import codigo.PDF;
import codigo.trabajadores;
import codigo.trabajadoresd;
import codigo.venta;
import codigo.ventad;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class ReporteVentas extends javax.swing.JFrame {
    private List<venta> listaVentas = new ArrayList<>();
    venta v=new venta();
    ventad vDAO=new ventad();
    PDF pdf=new PDF();
   
    public ReporteVentas() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        cargarVendedoresEnComboBox(boxVendedor);
        ListarVentas();
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarVentasPorFecha();
            }
        });

        // Agregar ActionListener al CheckBox
        checkBoxVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Habilitar o deshabilitar el ComboBox según el estado del CheckBox
                boxVendedor.setEnabled(checkBoxVendedor.isSelected());
            }
        });
    }
    private void cargarVendedoresEnComboBox(JComboBox<String> comboBox) {
           trabajadoresd prd = new trabajadoresd();
           List<trabajadores> vendedoresList = prd.listarVendedores();

           DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
           for (trabajadores trabajador : vendedoresList) {
               comboBoxModel.addElement(trabajador.getNombre());
           }

           comboBox.setModel(comboBoxModel);
       }
    
        private void actualizarTotal() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += Double.parseDouble(model.getValueAt(i, 4).toString());
        }
        txtTotal.setText("Total: " + total);
    }

    public void filtrarVentasPorFecha() {
        // Obtén las fechas seleccionadas en los componentes de fecha
        Date fechaInicio = fechaInicial.getDate();
        Date fechaFin = fechaFinal.getDate();

        // Verifica que ambas fechas no sean nulas
        if (fechaInicio != null && fechaFin != null) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < listaVentas.size(); i++) {
                String fechaString = listaVentas.get(i).getFecha();
                try {
                    Date fechaVenta = dateFormat.parse(fechaString);
                    if (fechaVenta.compareTo(fechaInicio) >= 0 && fechaVenta.compareTo(fechaFin) <= 0) {
                        if (checkBoxVendedor.isSelected()) {
                            // Verificar si el CheckBox está seleccionado antes de filtrar por vendedor
                            if (boxVendedor.getSelectedItem().equals(listaVentas.get(i).getVendedor())) {
                                Object[] ob = new Object[5];
                                ob[0] = listaVentas.get(i).getId_Ventas();
                                ob[1] = listaVentas.get(i).getVendedor();
                                ob[2] = listaVentas.get(i).getRepartidor();
                                ob[3] = dateFormat.format(fechaVenta); // Formatear la fecha
                                ob[4] = listaVentas.get(i).getTotal();

                                model.addRow(ob);
                            }
                        } else {
                            // Si el CheckBox no está seleccionado, no se filtra por vendedor
                            Object[] ob = new Object[5];
                            ob[0] = listaVentas.get(i).getId_Ventas();
                            ob[1] = listaVentas.get(i).getVendedor();
                            ob[2] = listaVentas.get(i).getRepartidor();
                            ob[3] = dateFormat.format(fechaVenta); // Formatear la fecha
                            ob[4] = listaVentas.get(i).getTotal();

                            model.addRow(ob);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }actualizarTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione las fechas de inicio y fin.");
        }
    }
public void ListarVentas() {
    if (listaVentas.isEmpty()) { 
        ventad proDAO = new ventad();
        listaVentas = proDAO.ListarVentas();
    }

    DefaultTableModel model = (DefaultTableModel) table1.getModel();
    model.setRowCount(0);

    for (int i = 0; i < listaVentas.size(); i++) {
        Object[] ob = new Object[5];
        ob[0] = listaVentas.get(i).getId_Ventas();
        ob[1] = listaVentas.get(i).getVendedor();
        ob[2] = listaVentas.get(i).getRepartidor();
        ob[3] = listaVentas.get(i).getFecha();
        ob[4] = listaVentas.get(i).getTotal();

        model.addRow(ob);
    }actualizarTotal();
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        btnGenerarReporte = new javax.swing.JButton();
        fechaFinal = new com.toedter.calendar.JDateChooser();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        boxVendedor = new javax.swing.JComboBox<>();
        checkBoxVendedor = new javax.swing.JCheckBox();
        txtTotal = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Vendedor", "Repartidor", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1.getTableHeader().setReorderingAllowed(false);
        scrollTable1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setResizable(false);
            table1.getColumnModel().getColumn(1).setResizable(false);
            table1.getColumnModel().getColumn(2).setResizable(false);
            table1.getColumnModel().getColumn(3).setResizable(false);
            table1.getColumnModel().getColumn(4).setResizable(false);
        }

        getContentPane().add(scrollTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 1290, -1));

        btnGenerarReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/pdf.png"))); // NOI18N
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });
        getContentPane().add(btnGenerarReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 110, 100));

        fechaFinal.setBackground(new java.awt.Color(255, 255, 255));
        fechaFinal.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(fechaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 140, 270, 30));

        fechaInicial.setBackground(new java.awt.Color(255, 255, 255));
        fechaInicial.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(fechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 140, 260, 30));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel1.setText("Fecha final:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 140, 70, 30));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel2.setText("Fecha Inicial:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, 90, 30));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/lupa.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 140, 70, 30));

        boxVendedor.setEnabled(false);
        getContentPane().add(boxVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 170, 30));
        getContentPane().add(checkBoxVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 30, 30));

        txtTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        getContentPane().add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 620, 110, 30));

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
            .addGap(0, 1070, Short.MAX_VALUE)
        );

        getContentPane().add(a, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1070));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila");
        } else {
            int idVenta = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());
            v = vDAO.BuscarVenta(idVenta);
            pdf.pdfV(idVenta,v.getId_clientes(), v.getTotal(), v.getVendedor());
        }
    }//GEN-LAST:event_btnGenerarReporteActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReporteVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReporteVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JComboBox<String> boxVendedor;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JCheckBox checkBoxVendedor;
    private com.toedter.calendar.JDateChooser fechaFinal;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JTable table1;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
