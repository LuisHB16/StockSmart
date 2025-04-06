package stocksmart;

import codigo.Categoria;
import codigo.Categoriad;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
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

public class Categoria extends javax.swing.JFrame {

    Categoriad tr = new Categoriad();
    private List<Categoria> listaProveedores = new ArrayList<>();

    public Categoria() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ListarCategorias();

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
        txtNombre.getDocument().addDocumentListener(new DocumentListener() {
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
        String filtroMarca = txtNombre.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        for (Categoria proveedor : listaProveedores) {
            String marca = proveedor.getCategoria().toLowerCase();

            // Check if the brand contains the filter text
            if (marca.contains(filtroMarca)) {
                Object[] ob = new Object[2];
                ob[0] = proveedor.getCategoria();
                model.addRow(ob);
            }
        }
    }

    public void LimpiarTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
    }

    public void ListarCategorias() {
        listaProveedores = tr.listarCategorias();
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();

        for (int i = 0; i < listaProveedores.size(); i++) {
            if (!listaProveedores.get(i).getCategoria().equals(".")) {
                Object[] ob = new Object[1];
                ob[0] = listaProveedores.get(i).getCategoria();
                modelo.addRow(ob);
            }
        }
        table1.setModel(modelo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        scrollTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/eliminar.png"))); // NOI18N
        btnBorrar.setBorder(null);
        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 40, 130, 90));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/modificar.png"))); // NOI18N
        btnEditar.setBorder(null);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 40, 130, 90));

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/guardar.png"))); // NOI18N
        btnAgregar.setBorder(null);
        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 40, 130, 90));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1.getTableHeader().setReorderingAllowed(false);
        scrollTable1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(scrollTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 1200, 470));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel1.setText("Nombre");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 60, 30));

        txtNombre.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 200, 30));

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
            .addGap(0, 1050, Short.MAX_VALUE)
        );

        getContentPane().add(a, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1050));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
               int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar la Categoria?");
        if (pregunta == 0) {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String Categoria = (String) table1.getValueAt(selectedRow, 0);
                tr.EliminarCategoria(Categoria);
                LimpiarTable();
                ListarCategorias();
                btnBorrar.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
               int selectedRow = table1.getSelectedRow();

        if (selectedRow != -1) {
            String nombreAnterior = (String) table1.getValueAt(selectedRow, 0);

            String nuevoNombre = JOptionPane.showInputDialog("Editar nombre:", nombreAnterior);
            if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                if (tr.existeCategoria(nuevoNombre)) {
                    JOptionPane.showMessageDialog(null, "El nombre de la Categoría ya existe en la base de datos.");
                } else {
                    Categoria t = new Categoria(nuevoNombre);
                    t.setCategoria(nuevoNombre);
                    tr.actualizarCategoria(nombreAnterior, t);
                    LimpiarTable();
                    ListarCategorias();
                    JOptionPane.showMessageDialog(null, "Categoría actualizada:\nUnidad: " + nuevoNombre);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre válido.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una Categoría para editar.");
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
                JTextField nombreField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Nombre del la Categoria:"));
        panel.add(nombreField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Ingresar la categoria", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            // Validación del nombre
            if (nombre != null && !nombre.isEmpty()) {
                // Validación de duplicados en la clave primaria (nombre)
                if (!tr.existeCategoria(nombre)) {
                    Categoria t = new Categoria();
                    t.setCategoria(nombre);
                    tr.RegistrarCategoria(t);
                    LimpiarTable();
                    ListarCategorias();
                    JOptionPane.showMessageDialog(null, "Categoria registrada:\nNombre: " + nombre);
                } else {
                    JOptionPane.showMessageDialog(null, "El nombre de la Categoria ya existe en la base de datos.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre válido.");
            }
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

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
            java.util.logging.Logger.getLogger(Categoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Categoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Categoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Categoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Categoria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JTable table1;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
