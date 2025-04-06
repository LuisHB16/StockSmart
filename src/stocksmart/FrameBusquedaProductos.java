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
import static stocksmart.FontLoader.customFontBold2;

public class FrameBusquedaProductos extends JFrame {

    private DefaultTableModel tableModel;

    private ProductoSeleccionadoListener listener;

    public void setProductoSeleccionadoListener(ProductoSeleccionadoListener listener) {
        this.listener = listener;
    }

    public FrameBusquedaProductos() {
        initComponents();
        setLocationRelativeTo(null);
        tableModel = (DefaultTableModel) Table1.getModel();

        DefaultRowSorter<DefaultTableModel, Integer> sorter = new TableRowSorter<>(tableModel);
        Table1.setRowSorter(sorter);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add action listener to boxNombre checkbox
        boxNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = boxNombre.isSelected();
                txtNombre.setEnabled(selected);

                // If the checkbox is deselected, clear the text in fielNombre
                if (!selected) {
                    txtNombre.setText("");
                    filterTable(); // Perform filtering when the checkbox is deselected
                }
            }
        });

        // Add action listener to boxCodigo checkbox
        boxCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = boxCodigo.isSelected();
                txtCodigo.setEnabled(selected);

                // If the checkbox is deselected, clear the text in fielCodigo
                if (!selected) {
                    txtCodigo.setText("");
                    filterTable(); // Perform filtering when the checkbox is deselected
                }
            }
        });

        // Add DocumentListener to the fielNombre field
        txtNombre.getDocument().addDocumentListener(new DocumentListener() {
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
        txtCodigo.getDocument().addDocumentListener(new DocumentListener() {
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
        String nombreFilter = txtNombre.getText().toLowerCase();
        String codigoFilter = txtCodigo.getText().toLowerCase();
        DefaultRowSorter<DefaultTableModel, Integer> sorter = (DefaultRowSorter<DefaultTableModel, Integer>) Table1.getRowSorter();
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

        lblNombre = new javax.swing.JLabel();
        boxNombre = new javax.swing.JCheckBox();
        pnlBusquedaProductos = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        scrollTable1 = new javax.swing.JScrollPane();
        Table1 = new javax.swing.JTable();
        boxCodigo = new javax.swing.JCheckBox();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNombre.setFont(customFontBold2);
        lblNombre.setText("Nombre:");
        getContentPane().add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 120, 20));
        getContentPane().add(boxNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        pnlBusquedaProductos.setBackground(new java.awt.Color(255, 255, 255));
        pnlBusquedaProductos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Busqueda Productos");
        pnlBusquedaProductos.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, -1));

        btnAgregar.setBackground(new java.awt.Color(50, 130, 233));
        btnAgregar.setFont(customFontBold2);
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        pnlBusquedaProductos.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 490, 90, 40));

        Table1.setFont(customFontBold2);
        Table1.setModel(new javax.swing.table.DefaultTableModel(
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
        Table1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        Table1.getTableHeader().setReorderingAllowed(false);
        scrollTable1.setViewportView(Table1);
        if (Table1.getColumnModel().getColumnCount() > 0) {
            Table1.getColumnModel().getColumn(0).setResizable(false);
            Table1.getColumnModel().getColumn(2).setResizable(false);
            Table1.getColumnModel().getColumn(3).setResizable(false);
            Table1.getColumnModel().getColumn(4).setResizable(false);
            Table1.getColumnModel().getColumn(5).setResizable(false);
        }
        Table1.getAccessibleContext().setAccessibleName("");

        pnlBusquedaProductos.add(scrollTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 770, 300));
        pnlBusquedaProductos.add(boxCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        lblCodigo.setFont(customFontBold2);
        lblCodigo.setText("Codigo:");
        pnlBusquedaProductos.add(lblCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 120, 20));

        txtCodigo.setFont(customFontBold2);
        txtCodigo.setEnabled(false);
        pnlBusquedaProductos.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 450, -1));

        txtNombre.setFont(customFontBold2);
        txtNombre.setEnabled(false);
        pnlBusquedaProductos.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 450, -1));

        getContentPane().add(pnlBusquedaProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        int selectedRow = Table1.getSelectedRow();

        if (selectedRow != -1) {
            String codigo = (String) Table1.getValueAt(selectedRow, 0);

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
            java.util.logging.Logger.getLogger(FrameBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameBusquedaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameBusquedaProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table1;
    private javax.swing.JCheckBox boxCodigo;
    private javax.swing.JCheckBox boxNombre;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBusquedaProductos;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
