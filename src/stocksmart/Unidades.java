package stocksmart;


import codigo.Unidad;
import codigo.Unidadd;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Unidades extends javax.swing.JFrame {

    Unidadd tr = new Unidadd();
    private List<Unidad> listaProveedores = new ArrayList<>();

    public Unidades() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ListarUnidades();

        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (table1.getSelectedRow() != -1) {
                        btnBorrar.setEnabled(true);
                    } else {
                        btnBorrar.setEnabled(false);
                    }
                }
            }
        });
                   txtUnidad.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filtrarTabla();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filtrarTabla();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            filtrarTabla();
        }
    });
    }
            private void filtrarTabla() {
    String filtroMarca = txtUnidad.getText().toLowerCase();
    DefaultTableModel model = (DefaultTableModel) table1.getModel();
    model.setRowCount(0);

    for (Unidad proveedor : listaProveedores) {
        String marca = proveedor.getUnidad().toLowerCase();

        // Check if the brand contains the filter text
        if (marca.contains(filtroMarca)) {
            Object[] ob = new Object[2];
            ob[0] = proveedor.getUnidad();
            ob[1] = proveedor.getFormato();
         
            model.addRow(ob);
        }
    }
}

    public void LimpiarTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
    }

    public void ListarUnidades() {
        listaProveedores = tr.listarUnidades();
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();

        for (int i = 0; i < listaProveedores.size(); i++) {
            if (!listaProveedores.get(i).getUnidad().equals(".")) {
                Object[] ob = new Object[2];
                ob[0] = listaProveedores.get(i).getUnidad();
                ob[1] = listaProveedores.get(i).getFormato();
                modelo.addRow(ob);
            }
        }
        table1.setModel(modelo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        lblUnidad = new javax.swing.JLabel();
        txtUnidad = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Unidad", "Formato"
            }
        ));
        table1.getTableHeader().setReorderingAllowed(false);
        scrollTable1.setViewportView(table1);

        getContentPane().add(scrollTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 1300, 510));

        lblUnidad.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        lblUnidad.setText("Nombre de la unidad:");
        getContentPane().add(lblUnidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 130, 30));
        getContentPane().add(txtUnidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 170, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.13 PM.jpeg"))); // NOI18N
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/eliminar.png"))); // NOI18N
        btnBorrar.setBorder(null);
        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrar.setEnabled(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 20, 130, 90));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/modificar.png"))); // NOI18N
        btnEditar.setBorder(null);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 20, 130, 90));

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/guardar.png"))); // NOI18N
        btnAgregar.setBorder(null);
        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 130, 90));

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
               int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar la unidad?");
        if (pregunta == 0) {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String unidad = (String) table1.getValueAt(selectedRow, 0);

                if (tr.eliminarUnidad(unidad)) {
                    JOptionPane.showMessageDialog(null, "Unidad eliminada con éxito.");
                    LimpiarTable();
                    ListarUnidades();
                    btnBorrar.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "No se puede eliminar la unidad, ya se está utilizando en un producto..");
                }
            }
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
               int selectedRow = table1.getSelectedRow();

        if (selectedRow != -1) {
            // Obtener los valores actuales de la unidad seleccionada
            String unidadAnterior = (String) table1.getValueAt(selectedRow, 0);
            Double formatoAnterior = (Double) table1.getValueAt(selectedRow, 1);

            // Crear un panel de edición similar al de agregar
            String[] roles = {"unidad", "Formato"};
            JTextField unidadField = new JTextField(20);
            JTextField formatoField = new JTextField(20);

            unidadField.setText(unidadAnterior);
            formatoField.setText(formatoAnterior.toString());

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2)); // Corregido aquí

            panel.add(new JLabel("Unidad:"));
            panel.add(unidadField);
            panel.add(new JLabel("Formato:"));
            panel.add(formatoField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Editar Unidad", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String nuevaUnidad = unidadField.getText();
                String nuevoFormatoStr = formatoField.getText();

                if (!nuevaUnidad.isEmpty() && !nuevoFormatoStr.isEmpty()) {
                    try {
                        double nuevoFormato = Double.parseDouble(nuevoFormatoStr);
                        Unidad unidadActualizada = new Unidad(nuevaUnidad, nuevoFormato);
                        unidadActualizada.setUnidad(nuevaUnidad);
                        unidadActualizada.setFormato(nuevoFormato);
                        tr.actualizarUnidad(unidadAnterior, unidadActualizada);
                        LimpiarTable();
                        ListarUnidades();
                        JOptionPane.showMessageDialog(null, "Unidad actualizada:\nUnidad: " + nuevaUnidad + "\nFormato: " + nuevoFormato);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Por favor, ingrese un formato válido (número decimal).");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una unidad para editar.");
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
                Unidadd tr = new Unidadd();

        String[] roles = {"unidad", "Formato"};
        JTextField unidadField = new JTextField(20);
        JTextField formatoField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Unidad:"));
        panel.add(unidadField);
        panel.add(new JLabel("Formato:"));
        panel.add(formatoField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Ingresar Trabajador", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = unidadField.getText();
            Double formato = Double.parseDouble(formatoField.getText());

            // Validación del nombre
            if (nombre != null && !nombre.isEmpty()) {
                // Validación de duplicados en la clave primaria (nombre)
                if (!tr.existeUnidad(nombre)) {
                    Unidad t = new Unidad(nombre, formato);
                    t.setUnidad(nombre);
                    t.setFormato(formato);
                    tr.RegistrarUnidad(t);
                    LimpiarTable();
                    ListarUnidades();
                    JOptionPane.showMessageDialog(null, "Unidad registrada:\nNombre: " + nombre + "\nRol: " + formato);
                } else {
                    JOptionPane.showMessageDialog(null, "La unidad ya existe en la base de datos.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre válido.");
            }
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

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
            java.util.logging.Logger.getLogger(Unidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Unidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Unidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Unidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Unidades().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel lblUnidad;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JTable table1;
    private javax.swing.JTextField txtUnidad;
    // End of variables declaration//GEN-END:variables
}
