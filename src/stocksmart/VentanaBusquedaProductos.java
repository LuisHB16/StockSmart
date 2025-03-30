package stocksmart;

import codigo.productos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class VentanaBusquedaProductos extends JFrame {

    private DefaultTableModel tableModel;

    private ProductoSeleccionadoListener listener;

    public void setProductoSeleccionadoListener(ProductoSeleccionadoListener listener) {
        this.listener = listener;
    }

    public VentanaBusquedaProductos() {
        initComponents();
        setLocationRelativeTo(null);
        tableModel = (DefaultTableModel) tablaProductos.getModel();

        DefaultRowSorter<DefaultTableModel, Integer> sorter = new TableRowSorter<>(tableModel);
        tablaProductos.setRowSorter(sorter);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add action listener to boxNombre checkbox
        boxNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = boxNombre.isSelected();
                fielNombre.setEnabled(selected);

                // If the checkbox is deselected, clear the text in fielNombre
                if (!selected) {
                    fielNombre.setText("");
                    filterTable(); // Perform filtering when the checkbox is deselected
                }
            }
        });

        // Add action listener to boxCodigo checkbox
        boxCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = boxCodigo.isSelected();
                fielCodigo.setEnabled(selected);

                // If the checkbox is deselected, clear the text in fielCodigo
                if (!selected) {
                    fielCodigo.setText("");
                    filterTable(); // Perform filtering when the checkbox is deselected
                }
            }
        });

        // Add DocumentListener to the fielNombre field
        fielNombre.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        // Add DocumentListener to the fielCodigo field
        fielCodigo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
    }

    private void filterTable() {
        String nombreFilter = fielNombre.getText().toLowerCase();
        String codigoFilter = fielCodigo.getText().toLowerCase();
        DefaultRowSorter<DefaultTableModel, Integer> sorter = (DefaultRowSorter<DefaultTableModel, Integer>) tablaProductos.getRowSorter();
        List<RowFilter<Object, Integer>> filters = new ArrayList<>();
        if (boxNombre.isSelected() && !nombreFilter.isEmpty()) {
            filters.add(RowFilter.regexFilter(nombreFilter, 1)); 
        }
        if (boxCodigo.isSelected() && !codigoFilter.isEmpty()) {
            filters.add(RowFilter.regexFilter(codigoFilter, 0)); 
        }
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    public void cargarProductos(List<productos> productosList) {
        limpiarTabla();
        for (productos producto : productosList) {
            agregarProducto(
                    producto.getClavealterna(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getUnidad(),
                    producto.getExistencia(),
                    producto.getGranel()
            );
        }
    }

    public void agregarProducto(String codigo, String nombre, double precio, String unidad, double existencia, String granel) {
        Object[] rowData = {codigo, nombre, precio, unidad, existencia, granel};
        tableModel.addRow(rowData);
    }

    private void limpiarTabla() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    public interface ProductoSeleccionadoListener {

        void productoSeleccionado(String codigo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        fielNombre = new javax.swing.JTextField();
        fielCodigo = new javax.swing.JTextField();
        boxCodigo = new javax.swing.JCheckBox();
        boxNombre = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Precio", "Unidad", "Existencia", "Granel"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProductos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaProductos);
        if (tablaProductos.getColumnModel().getColumnCount() > 0) {
            tablaProductos.getColumnModel().getColumn(0).setResizable(false);
            tablaProductos.getColumnModel().getColumn(2).setResizable(false);
            tablaProductos.getColumnModel().getColumn(3).setResizable(false);
            tablaProductos.getColumnModel().getColumn(4).setResizable(false);
            tablaProductos.getColumnModel().getColumn(5).setResizable(false);
        }
        tablaProductos.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 650, 300));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 423, 90, 30));

        jLabel1.setText("Nombre:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, 20));

        fielNombre.setEnabled(false);
        getContentPane().add(fielNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 120, -1));

        fielCodigo.setEnabled(false);
        getContentPane().add(fielCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, 120, -1));
        getContentPane().add(boxCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, -1, -1));
        getContentPane().add(boxNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel2.setText("Codigo:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, -1, 20));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        int selectedRow = tablaProductos.getSelectedRow();

        if (selectedRow != -1) {
            String codigo = (String) tablaProductos.getValueAt(selectedRow, 0);

            // Notificar al listener que se ha seleccionado un producto
            if (listener != null) {
                listener.productoSeleccionado(codigo);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto antes de agregar a la venta.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        // Cierra la ventana actual
        dispose();
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
            java.util.logging.Logger.getLogger(VentanaBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaBusquedaProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox boxCodigo;
    private javax.swing.JCheckBox boxNombre;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JTextField fielCodigo;
    private javax.swing.JTextField fielNombre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables
}
