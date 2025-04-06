package stocksmart;


import codigo.PDF;
import codigo.detalleV;
import codigo.movimiento;
import codigo.movimientod;
import codigo.productos;
import codigo.productosd;
import codigo.venta;
import codigo.ventad;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class p_Devoluciones extends javax.swing.JFrame {

    detalleV detalleV = new detalleV();
    ventad detalleDAO = new ventad();
    int folios;
    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);    venta v = new venta();
    ventad vDAO = new ventad();
    PDF pdf = new PDF();
    movimientod m = new movimientod();
    productosd p = new productosd();
    productos pro = new productos();
    Double inventario;
    public p_Devoluciones() {
        initComponents();
        btnGenerar.setEnabled(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

      
    }


private void buscarEnBaseDeDatos(String folio) {
    // Utiliza el método BuscarVenta de la clase ventad para obtener la venta correspondiente al folio
    venta ventaEncontrada = new ventad().BuscarVenta1(Integer.parseInt(folio));

    // Verificar si se encontró la venta (considerando 0 como válido)
    int idVentas = ventaEncontrada.getId_Ventas();
    if (idVentas != 0) {
        // Verificar si el estado de la venta es "venta"
        if (ventaEncontrada.getStatus().equalsIgnoreCase("venta")) {
            // Llenar la tabla con los detalles de la venta encontrada
            folios = Integer.parseInt(folio);
            llenarTablaConDetalles(idVentas);
        } else {
            JOptionPane.showMessageDialog(null, "La venta no tiene el estado 'venta'", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Venta no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void llenarTablaConDetalles(int idVentas) {
        // Utiliza el método ListarDetallesVenta de la clase ventad para obtener la lista de detalles de venta
        btnGenerar.setEnabled(true);
        
        List<detalleV> listaDetalles = detalleDAO.ListarDetallesVenta(idVentas);

        // Actualizar el modelo de la tabla con los detalles de venta
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de llenarla con nuevos datos

        if (!listaDetalles.isEmpty()) {
            for (detalleV dv : listaDetalles) {
                // Calcular el total multiplicando el precio y la cantidad
                double total = dv.getPrecio_U() * dv.getCantidad();
                modelo.addRow(new Object[]{dv.getCodigo(), dv.getPrecio_U(), dv.getCantidad(), total});
            }
        } else {
            System.out.println("No se encontraron detalles para el ID de ventas: " + idVentas);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnGenerar = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNuevo.setText("Crear nota de credito");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 180, 30));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "PrecioU", "Cantidad", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 1190, 530));

        btnGenerar.setText("Generar");
        btnGenerar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGenerar, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 670, 320, 30));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.13 PM.jpeg"))); // NOI18N
        jButton4.setBorder(null);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        a.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout aLayout = new javax.swing.GroupLayout(a);
        a.setLayout(aLayout);
        aLayout.setHorizontalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        aLayout.setVerticalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(a, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // Crear el JOptionPane personalizado
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Ingrese el folio:");
        JTextField textField = new JTextField(10);
        panel.add(label);
        panel.add(textField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Nuevo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Obtener el folio ingresado
            String folio = textField.getText();
            buscarEnBaseDeDatos(folio);
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
    // Actualizar el estado de la venta a "devolucion"
    detalleDAO.actualizarEstado2(folios, "devolucion",fechaActual);
     vDAO.ListarDetallesVenta(folios);
                for (detalleV detalle : vDAO.ListarDetallesVenta(folios)) {
                    pro = p.BuscarClave(detalle.getCodigo());
                    Double existencia = pro.getExistencia();
                    if(existencia == null){
                        String Codigo=detalle.getCodigo();
                        pro = p.Buscarprocodigo3(detalle.getCodigo());
                        pro = p.BuscarClave(pro.getCodigo());
                        existencia = pro.getExistencia();
                        movimiento mo = new movimiento("Devolucion", fechaActual, detalle.getCantidad(), Codigo, pro.getExistencia());
                        m.agregarMovimiento(mo);
                    }else{
                        movimiento mo = new movimiento("Devolucion", fechaActual, detalle.getCantidad(), detalle.getCodigo(), pro.getExistencia());
                        m.agregarMovimiento(mo);
                        
                    }
                }


    // Desactivar el botón Generar
    btnGenerar.setEnabled(false);

    // Mostrar mensaje de éxito
    JOptionPane.showMessageDialog(null, "La devolución se generó con éxito");

    // Limpiar la tabla
    DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
    modelo.setRowCount(0);
    }//GEN-LAST:event_btnGenerarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(p_Devoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(p_Devoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(p_Devoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(p_Devoluciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new p_Devoluciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
