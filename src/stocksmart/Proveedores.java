
package stocksmart;


import codigo.proveedores;
import codigo.proveedoresd;
import codigo.trabajadores;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFormattedTextField;
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
import javax.swing.text.MaskFormatter;

/**
 *
 * @author truea
 */
public class Proveedores extends javax.swing.JFrame {
    private List<proveedores> listaProveedores = new ArrayList<>();

    public Proveedores() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ListarProveedores();
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
            txtMarca.getDocument().addDocumentListener(new DocumentListener() {
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

    public void LimpiarTable() {
        table1.removeAll();
    }

    public void ListarProveedores() {
        proveedoresd proDAO = new proveedoresd();
        listaProveedores = proDAO.listarProveedores();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        for (int i = 0; i < listaProveedores.size(); i++) {
            Object[] ob = new Object[4];
            ob[0] = listaProveedores.get(i).getMarca();
            ob[1] = listaProveedores.get(i).getTelefono();
            ob[2] = listaProveedores.get(i).getDireccion();
            ob[3] = listaProveedores.get(i).getCorreo();
            model.addRow(ob);
        }
    }
    private void filtrarTabla() {
    String filtroMarca = txtMarca.getText().toLowerCase();
    DefaultTableModel model = (DefaultTableModel) table1.getModel();
    model.setRowCount(0);

    for (proveedores proveedor : listaProveedores) {
        String marca = proveedor.getMarca().toLowerCase();

        // Check if the brand contains the filter text
        if (marca.contains(filtroMarca)) {
            Object[] ob = new Object[4];
            ob[0] = proveedor.getMarca();
            ob[1] = proveedor.getTelefono();
            ob[2] = proveedor.getDireccion();
            ob[3] = proveedor.getCorreo();
            model.addRow(ob);
        }
    }
}
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Marca", "Telefono", "Direccion", "Correo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
        }

        getContentPane().add(scrollTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 1200, 490));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel1.setText("Marca:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 70, 30));

        txtMarca.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 160, 30));

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/eliminar.png"))); // NOI18N
        btnBorrar.setBorder(null);
        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrar.setEnabled(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 30, 130, 90));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/modificar.png"))); // NOI18N
        btnEditar.setBorder(null);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 30, 130, 90));

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/guardar.png"))); // NOI18N
        btnAgregar.setBorder(null);
        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 30, 130, 90));

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
              int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Trabajador?");
        if (pregunta == 0) {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                proveedoresd pr=new proveedoresd();
                String Marca = (String) table1.getValueAt(selectedRow, 0);
                pr.eliminarProveedor(Marca);
                LimpiarTable();
                ListarProveedores();
                btnBorrar.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
                int selectedRow = table1.getSelectedRow();

    if (selectedRow != -1) {
        String marcaAnterior = (String) table1.getValueAt(selectedRow, 0);
        String telefonoAnterior = (String) table1.getValueAt(selectedRow, 1);
        String direccionAnterior = (String) table1.getValueAt(selectedRow, 2);
        String correoAnterior = (String) table1.getValueAt(selectedRow, 3);

        // Crear un panel para editar los datos del proveedor
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(new JLabel("Marca:"));
        JTextField marcaField = new JTextField(15);
        marcaField.setText(marcaAnterior);
        panel.add(marcaField);

        panel.add(new JLabel("Teléfono:"));
        JFormattedTextField telefonoField = new JFormattedTextField(createFormatter("##########"));
        telefonoField.setText(telefonoAnterior);
        panel.add(telefonoField);

        panel.add(new JLabel("Dirección:"));
        JTextField direccionField = new JTextField(15);
        direccionField.setText(direccionAnterior);
        panel.add(direccionField);

        panel.add(new JLabel("Correo electrónico:"));
        JTextField correoField = new JTextField(15);
        correoField.setText(correoAnterior);
        panel.add(correoField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Editar datos del proveedor", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nuevaMarca = marcaField.getText();
            String nuevoTelefonoStr = telefonoField.getText().replaceAll("[^0-9]", "");
            String nuevaDireccion = direccionField.getText();
            String nuevoCorreo = correoField.getText();

            if (nuevaMarca.isEmpty() || nuevoTelefonoStr.isEmpty() || nuevaDireccion.isEmpty() || nuevoCorreo.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ninguno de los campos puede estar incompleto. Por favor, complete todos los campos.");
            } else if (nuevoTelefonoStr.length() != 10) {
                JOptionPane.showMessageDialog(null, "El número de teléfono debe tener 10 dígitos.");
            } else {
                proveedoresd pr = new proveedoresd();
                proveedores proveedor = new proveedores(nuevaMarca, nuevoTelefonoStr, nuevaDireccion, nuevoCorreo);

                boolean edicionExitosa = pr.actualizarProveedor(marcaAnterior, proveedor);

                if (edicionExitosa) {
                    JOptionPane.showMessageDialog(null, "Proveedor editado con éxito");
                    LimpiarTable();
                    ListarProveedores();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al editar el proveedor");
                }
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione un proveedor para editar.");
    }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
             JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(4, 2));
    panel.add(new JLabel("Marca:"));
    JTextField marcaField = new JTextField(15);
    panel.add(marcaField);

    panel.add(new JLabel("Teléfono:"));
    JFormattedTextField telefonoField = new JFormattedTextField(createFormatter("##########"));
    panel.add(telefonoField);

    panel.add(new JLabel("Dirección:"));
    JTextField direccionField = new JTextField(15);
    panel.add(direccionField);

    panel.add(new JLabel("Correo electrónico:"));
    JTextField correoField = new JTextField(15);
    panel.add(correoField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Ingrese los datos del proveedor", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String marca = marcaField.getText();
        String telefonoStr = telefonoField.getText().replaceAll("[^0-9]", "");
        String direccion = direccionField.getText();
        String correo = correoField.getText();

        if (marca.isEmpty() || telefonoStr.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ninguno de los campos puede estar vacío. Por favor, complete todos los campos.");
        } else if (telefonoStr.length() != 10) {
            JOptionPane.showMessageDialog(null, "El número de teléfono debe tener 10 dígitos.");
        } else {
            proveedoresd pr = new proveedoresd();

            if (pr.marcaYaExisteEnBD(marca)) {
                JOptionPane.showMessageDialog(null, "La marca ya existe en la base de datos. Por favor, introduzca una marca diferente.");
            } else {
                proveedores proveedor = new proveedores(marca, telefonoStr, direccion, correo);

                boolean registroExitoso = pr.RegistrarProveedor(proveedor);

                if (registroExitoso) {
                    JOptionPane.showMessageDialog(null, "Proveedor registrado con éxito");
                    LimpiarTable();
                    ListarProveedores();
                    btnBorrar.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar el proveedor");
                }
            }
        }
    }
    }//GEN-LAST:event_btnAgregarActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Proveedores().setVisible(true);
            }
        });
    }
    public MaskFormatter createFormatter(String format) {
    MaskFormatter formatter = null;
    try {
        formatter = new MaskFormatter(format);
    } catch (java.text.ParseException e) {
        e.printStackTrace();
    }
    return formatter;
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
    private javax.swing.JTextField txtMarca;
    // End of variables declaration//GEN-END:variables
}
