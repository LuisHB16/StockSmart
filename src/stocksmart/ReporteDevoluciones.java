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

public class ReporteDevoluciones extends javax.swing.JFrame {

    private List<venta> listaVentas = new ArrayList<>();
    venta v=new venta();
    ventad vDAO=new ventad();
    PDF pdf=new PDF();
    public ReporteDevoluciones() {
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
    

    public void filtrarVentasPorFecha() {
        // Obtén las fechas seleccionadas en los componentes de fecha
        Date fechaInicio = fechaInicial.getDate();
        Date fechaFin = fechaFinal.getDate();

        // Verifica que ambas fechas no sean nulas
        if (fechaInicio != null && fechaFin != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
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
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione las fechas de inicio y fin.");
        }
    }
public void ListarVentas() {
    if (listaVentas.isEmpty()) { 
        ventad proDAO = new ventad();
        listaVentas = proDAO.Listardev();
    }

    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);

    for (int i = 0; i < listaVentas.size(); i++) {
        Object[] ob = new Object[5];
        ob[0] = listaVentas.get(i).getId_Ventas();
        ob[1] = listaVentas.get(i).getVendedor();
        ob[2] = listaVentas.get(i).getRepartidor();
        ob[3] = listaVentas.get(i).getFecha();
        ob[4] = listaVentas.get(i).getTotal();

        model.addRow(ob);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        boxVendedor = new javax.swing.JComboBox<>();
        checkBoxVendedor = new javax.swing.JCheckBox();
        btnGenerarPDF = new javax.swing.JButton();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fechaFinal = new com.toedter.calendar.JDateChooser();
        btnBuscar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 1290, -1));

        boxVendedor.setEnabled(false);
        getContentPane().add(boxVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 200, 30));
        getContentPane().add(checkBoxVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 30, 30));

        btnGenerarPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/pdf.png"))); // NOI18N
        btnGenerarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnGenerarPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 120, 100));

        fechaInicial.setBackground(new java.awt.Color(255, 255, 255));
        fechaInicial.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(fechaInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 130, 270, 30));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel2.setText("Fecha Inicial:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 130, 100, 30));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel1.setText("Fecha final:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 130, 70, 30));

        fechaFinal.setBackground(new java.awt.Color(255, 255, 255));
        fechaFinal.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(fechaFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 130, 280, 30));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/lupa.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1270, 130, 70, 30));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.13 PM.jpeg"))); // NOI18N
        jButton3.setBorder(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

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

    private void btnGenerarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPDFActionPerformed
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila");
        } else {
            int idVenta = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            v = vDAO.BuscarVenta(idVenta);
            pdf.pdfV(idVenta,v.getId_clientes(), v.getTotal(), v.getVendedor());
        }
    }//GEN-LAST:event_btnGenerarPDFActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
     
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(ReporteDevoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReporteDevoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReporteDevoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReporteDevoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReporteDevoluciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JComboBox<String> boxVendedor;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGenerarPDF;
    private javax.swing.JCheckBox checkBoxVendedor;
    private com.toedter.calendar.JDateChooser fechaFinal;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
