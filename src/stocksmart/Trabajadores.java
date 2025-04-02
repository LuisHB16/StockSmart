package stocksmart;


import codigo.trabajadoresd;
import codigo.trabajadores;
import java.awt.GridLayout;
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

/**
 *
 * @author truea
 */
public class Trabajadores extends javax.swing.JFrame {

    trabajadoresd tr = new trabajadoresd();
    List<trabajadores> listaTrabajadores;

    public Trabajadores() {
        initComponents();
        ListarTrabajadores();
        listaTrabajadores = tr.listarTrabajadores();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

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
        txtUsuario.getDocument().addDocumentListener(new DocumentListener() {
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
        String filtroNombre = txtUsuario.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        for (trabajadores trabajador : listaTrabajadores) {
            String nombre = trabajador.getNombre().toLowerCase();

            if (nombre.contains(filtroNombre) && !trabajador.getNombre().equals("Sin Repartidor")) {
                Object[] ob = new Object[2];
                ob[0] = trabajador.getNombre();
                ob[1] = trabajador.getRol();
                model.addRow(ob);
            }
        }
    }

    public void LimpiarTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
    }

    public void ListarTrabajadores() {
        List<trabajadores> ListarPro = tr.listarTrabajadores();
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();

        for (int i = 1; i < ListarPro.size(); i++) {
            if (!ListarPro.get(i).getNombre().equals(".")) {
                Object[] ob = new Object[2];
                ob[0] = ListarPro.get(i).getNombre();
                ob[1] = ListarPro.get(i).getRol(); // Agrega el rol a la tabla
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
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
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
                "Usuario", "trabajador"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
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
        }

        getContentPane().add(scrollTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 1260, 480));

        lblUsuario.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        lblUsuario.setText("Usuario:");
        getContentPane().add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 70, 30));

        txtUsuario.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 170, 30));

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
        getContentPane().add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 40, 130, 90));

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
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 40, 130, 90));

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
                int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Trabajador?");
        if (pregunta == 0) {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String Usuario_trabajador = (String) table1.getValueAt(selectedRow, 0);
                tr.EliminarTrabajador(Usuario_trabajador);
                LimpiarTable();
                ListarTrabajadores();
                btnBorrar.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
                int selectedRow = table1.getSelectedRow();

        if (selectedRow != -1) {
            String nombreAnterior = (String) table1.getValueAt(selectedRow, 0);
            String rolAnterior = (String) table1.getValueAt(selectedRow, 1);

            String nuevoNombre = JOptionPane.showInputDialog("Editar nombre:", nombreAnterior);
            if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                String[] roles = {"repartidor", "vendedor"};
                String nuevoRol = (String) JOptionPane.showInputDialog(
                        null, "Editar rol:", "Editar Rol",
                        JOptionPane.QUESTION_MESSAGE, null, roles, rolAnterior);

                if (nuevoRol != null) {
                    if (!nuevoNombre.equals(nombreAnterior)) {
                        // Validación de duplicados en la clave primaria (nombre)
                        if (!tr.existeTrabajador(nuevoNombre)) {
                            trabajadores t = new trabajadores(nuevoNombre, nuevoRol);
                            t.setNombre(nuevoNombre);
                            t.setRol(nuevoRol);
                            tr.actualizarTrabajador(nombreAnterior, t);
                            LimpiarTable();
                            ListarTrabajadores();
                        } else {
                            JOptionPane.showMessageDialog(null, "El nombre de trabajador ya existe en la base de datos.");
                        }
                    } else {
                        trabajadores t = new trabajadores(nuevoNombre, nuevoRol);
                        t.setNombre(nuevoNombre);
                        t.setRol(nuevoRol);
                        tr.actualizarTrabajador(nombreAnterior, t);
                        LimpiarTable();
                        ListarTrabajadores();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un rol válido.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre válido.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un trabajador para editar.");
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
              trabajadoresd tr = new trabajadoresd();

        String[] roles = {"repartidor", "vendedor"};
        JComboBox<String> comboBox = new JComboBox<>(roles);
        JTextField nombreField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Nombre del trabajador:"));
        panel.add(nombreField);
        panel.add(new JLabel("Rol:"));
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Ingresar Trabajador", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            String rol = (String) comboBox.getSelectedItem();

            // Validación del nombre
            if (nombre != null && !nombre.isEmpty()) {
                // Validación de duplicados en la clave primaria (nombre)
                if (!tr.existeTrabajador(nombre)) {
                    trabajadores t = new trabajadores(nombre, rol);
                    t.setNombre(nombre);
                    t.setRol(rol);
                    tr.registrarTrabajador(t);
                    LimpiarTable();
                    ListarTrabajadores();
                    JOptionPane.showMessageDialog(null, "Trabajador registrado:\nNombre: " + nombre + "\nRol: " + rol);
                } else {
                    JOptionPane.showMessageDialog(null, "El nombre de trabajador ya existe en la base de datos.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre válido.");
            }
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Trabajadores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JTable table1;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

}
