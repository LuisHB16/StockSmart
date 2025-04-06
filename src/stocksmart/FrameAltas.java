package stocksmart;

import codigo.Unidad;
import codigo.Unidadd;
import codigo.movimiento;
import codigo.movimientod;
import codigo.productos;
import codigo.productosd;
import inicio.FrameMenu;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static stocksmart.FontLoader.customFont;
import static stocksmart.FontLoader.customFontBold;
import static stocksmart.FontLoader.customFontBold2;

public final class FrameAltas extends javax.swing.JFrame {

    private List<productos> productosDisponibles = new ArrayList<>();
    private List<productos> productosSeleccionados = new ArrayList<>();
    FrameMenu fmenu ;
    productosd p = new productosd();
    movimientod m = new movimientod();
    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    Double inventario;
    private ArrayList<Unidad> listaUnidades = new ArrayList<>();

    public FrameAltas(FrameMenu Menu) {
        
        initComponents();
        this.fmenu = Menu;
        
         // Define el tamaño de la ventana (ancho x alto)
        this.setLocationRelativeTo(null);
        lista();
        ListarProductos();
        btnInsertar.addActionListener((ActionEvent e) -> {
            int selectedViewRow = table1.getSelectedRow();
            if (selectedViewRow >= 0) {
                // Obtén el modelo de la primera tabla
                DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
                
                // Obtén el modelo de la segunda tabla
                DefaultTableModel model2 = (DefaultTableModel) table2.getModel();
                
                // Obtén el índice del modelo utilizando la vista seleccionada de la primera tabla
                int selectedModelRow = table1.convertRowIndexToModel(selectedViewRow);
                
                // Obtén los datos de la fila seleccionada de la primera tabla
                Object[] rowData = new Object[model1.getColumnCount()];
                for (int i = 0; i < model1.getColumnCount(); i++) {
                    rowData[i] = model1.getValueAt(selectedModelRow, i);
                }
                
                // Añade una nueva fila a la segunda tabla utilizando los datos obtenidos
                model2.addRow(rowData);
                
                // Elimina la fila de la primera tabla utilizando el índice del modelo
                model1.removeRow(selectedModelRow);
            }
        });

        btnEliminar.addActionListener((ActionEvent e) -> {
            int selectedViewRow = table2.getSelectedRow();
            if (selectedViewRow >= 0) {
                // Obtén el modelo de la primera tabla
                DefaultTableModel model1 = (DefaultTableModel) table1.getModel();

                // Obtén el modelo de la segunda tabla
                DefaultTableModel model2 = (DefaultTableModel) table2.getModel();

                // Obtén el índice del modelo utilizando la vista seleccionada de la segunda tabla
                int selectedModelRow = table2.convertRowIndexToModel(selectedViewRow);

                // Obtén los datos de la fila seleccionada de la segunda tabla
                Object[] rowData = new Object[model2.getColumnCount()];
                for (int i = 0; i < model2.getColumnCount(); i++) {
                    rowData[i] = model2.getValueAt(selectedModelRow, i);
                }

                // Añade una nueva fila a la primera tabla utilizando los datos obtenidos
                model1.addRow(rowData);

                // Elimina la fila de la segunda tabla utilizando el índice del modelo
                model2.removeRow(selectedModelRow);
            }
        });

        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int rowIndex = table2.getSelectedRow();
                    if (rowIndex >= 0) {
                        actualizarCantidadProducto(rowIndex);
                    }
                }
            }
        });
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
                // Not used for plain text fields
            }
        });
        btnAgregar.addActionListener((ActionEvent e) -> {
            // Verificar si la tabla2 está vacía
            if (table2.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "La tabla está vacía. Agrega productos antes de realizar esta acción.");
                return;  // Sale del método actionPerformed si la tabla2 está vacía
            }
            
            // Itera a través de la tabla2 para obtener los códigos y cantidades
            for (int i = 0; i < table2.getRowCount(); i++) {
                String codigo = table2.getValueAt(i, 0).toString();
                System.out.println(codigo);
                System.out.println(codigo);
                int cantidad = Integer.parseInt(table2.getValueAt(i, 4).toString());
                Double cant = Double.valueOf(table2.getValueAt(i, 4).toString());
                for (productos producto : productosSeleccionados) {
                    if (codigo == null ? producto.getCodigo() == null : codigo.equals(producto.getCodigo())) {
                        Unidad formatoEncontrado = null;
                        
                        for (Unidad unidad : listaUnidades) {
                            if (unidad.getUnidad().equals(producto.getUnidadC())) {
                                formatoEncontrado = unidad;
                                break;
                            }
                        }
                        
                        cantidad = (int) (cantidad * formatoEncontrado.getFormato());
                        
                        inventario = producto.getExistencia();
                    }
                }
                
                // Llama al nuevo método para actualizar la cantidad en la base de datos
                if (p.actualizarCantidadProducto(codigo, cantidad)) {
                    
                    movimiento mo = new movimiento("Alta", fechaActual, cant, codigo, inventario);
                    m.agregarMovimiento(mo);
                    JOptionPane.showMessageDialog(null, "Productos actualizados correctamente.");
                } else {
                    // Maneja el error si la actualización falló
                    JOptionPane.showMessageDialog(null, "Error al actualizar la cantidad del producto.");
                }
            }
            
            // Después de actualizar la base de datos, puedes limpiar la tabla2 si es necesario
            DefaultTableModel model2 = (DefaultTableModel) table2.getModel();
            model2.setRowCount(0);
            
            // También puedes recargar los productos disponibles en la tabla original
            ListarProductos();
        });

    }

    private void lista() {
        Unidadd unidadDAO = new Unidadd();
        listaUnidades = (ArrayList<Unidad>) unidadDAO.listarUnidades();

        for (Unidad unidad : listaUnidades) {
            String nombreUnidad = unidad.getUnidad();
            Double formato = unidad.getFormato();
        }
    }

    public void ListarProductos() {
        productosd proDAO = new productosd();
        List<productos> ListarPr = proDAO.listarProductos();
        DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
        model1.setRowCount(0);

        productosDisponibles.clear();
        productosSeleccionados.clear();

        for (productos producto : ListarPr) {

            Unidad formatoEncontrado = null;

            for (Unidad unidad : listaUnidades) {
                if (unidad.getUnidad().equals(producto.getUnidadC())) {
                    formatoEncontrado = unidad;
                    break;
                }
            }

            Object[] ob = new Object[5];
            ob[0] = producto.getCodigo();
            ob[1] = producto.getNombre();
            ob[2] = producto.getMarca();
            ob[3] = producto.getUnidadC();
            ob[4] = producto.getExistencia() / formatoEncontrado.getFormato();

            model1.addRow(ob);
            productosDisponibles.add(producto);
        }
    }

    private void actualizarCantidadProducto(int rowIndex) {
        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad (número entero):");
        if (cantidadStr != null) {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad > 0) {
                    // Actualizar la cantidad en la tabla2
                    table2.setValueAt(cantidad, rowIndex, 4); // La columna 3 es la columna de cantidad

                    // Obtener el precio unitario como cadena
                    String precioUnitarioStr = (String) table2.getValueAt(rowIndex, 2);

                    precioUnitarioStr = precioUnitarioStr.replace(',', '.');

                    double precioUnitario = Double.parseDouble(precioUnitarioStr);

                    // Calcular el nuevo total
                    double total = precioUnitario * cantidad;
                    table2.setValueAt(total, rowIndex, 4);
                } else {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que 0.");
                }
            } catch (NumberFormatException e) {
                //JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida (número entero).");
            }
        }
    }

    private void filterTable() {
        String filterText = txtNombre.getText().toLowerCase();

        DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
        model1.setRowCount(0);

        for (productos producto : productosDisponibles) {
            if (producto.getNombre().toLowerCase().contains(filterText)) {
                Unidad formatoEncontrado = null;

                for (Unidad unidad : listaUnidades) {
                    if (unidad.getUnidad().equals(producto.getUnidadC())) {
                        formatoEncontrado = unidad;
                        break;
                    }
                }

                Object[] ob = new Object[5];
                ob[0] = producto.getCodigo();
                ob[1] = producto.getNombre();
                ob[2] = producto.getMarca();
                ob[3] = producto.getUnidadC();
                ob[4] = producto.getExistencia() / formatoEncontrado.getFormato();
                model1.addRow(ob);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrame = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
        pnlAltas = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnInsertar = new javax.swing.JButton();
        lblAltas = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        scrollTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        scrollTable2 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        logoLoginIcon = new javax.swing.JLabel();
        btnMenuBack = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                // Super llama al comportamiento predeterminado
                super.paintComponent(g);

                // Habilitar gráficos avanzados
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo transparente o del mismo color que el contenedor
                g2d.setColor(getParent().getBackground()); // Color del contenedor padre
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Crear el gradiente de negro a azul
                GradientPaint gradiente = new GradientPaint(
                    0, 0, new Color(0, 0, 0, 175),         // Inicio: negro
                    getWidth()-(getWidth()/7), 0, new Color(50,130,233)  // Fin: azul
                );

                // Dibujar el fondo redondeado con gradiente
                g2d.setPaint(gradiente);
                int arcWidth = 25;  // Curvatura horizontal
                int arcHeight = 25; // Curvatura vertical
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            }

            @Override
            public boolean isOpaque() {
                return false; // Permitir transparencia
            }
        };
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AltasFrame");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFrame.setBackground(new java.awt.Color(50, 130, 233));
        pnlFrame.setMaximumSize(new java.awt.Dimension(1231, 716));

        stockSmartLoginLabel.setBackground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setFont(customFontBold);
        stockSmartLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stockSmartLoginLabel.setText("StockSmart");

        pnlAltas.setBackground(new java.awt.Color(255, 255, 255));

        lblNombre.setFont(customFontBold2);
        lblNombre.setText("Nombre");

        btnAgregar.setBackground(new java.awt.Color(50, 130, 233));
        btnAgregar.setFont(customFontBold2);
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Generar");
        btnAgregar.setBorderPainted(false);
        btnAgregar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregar.setIconTextGap(10);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(50, 130, 233));
        btnEliminar.setFont(customFontBold2);
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEliminar.setIconTextGap(10);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnInsertar.setFont(customFontBold2);
        btnInsertar.setForeground(new java.awt.Color(50, 130, 233));
        btnInsertar.setText("Insertar");
        btnInsertar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInsertar.setIconTextGap(10);
        btnInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarActionPerformed(evt);
            }
        });

        lblAltas.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblAltas.setForeground(new java.awt.Color(50, 130, 233));
        lblAltas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAltas.setText("Altas");

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Marca", "Unidad", "Existencia"
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
        table1.setFont(customFontBold2);

        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo ", "Nombre", "Marca", "Unidad", "Cantidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table2.getTableHeader().setReorderingAllowed(false);
        scrollTable2.setViewportView(table2);

        javax.swing.GroupLayout pnlAltasLayout = new javax.swing.GroupLayout(pnlAltas);
        pnlAltas.setLayout(pnlAltasLayout);
        pnlAltasLayout.setHorizontalGroup(
            pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAltasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(369, 369, 369))
            .addGroup(pnlAltasLayout.createSequentialGroup()
                .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAltasLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAltasLayout.createSequentialGroup()
                                .addGap(771, 771, 771)
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAltasLayout.createSequentialGroup()
                                .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlAltasLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInsertar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addComponent(scrollTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        pnlAltasLayout.setVerticalGroup(
            pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAltasLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(lblAltas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlAltasLayout.createSequentialGroup()
                        .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombre)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAltasLayout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(btnInsertar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAltasLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(scrollTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        txtNombre.setFont(customFontBold2);

        logoLoginIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/images/logoStockSmart50.png"))); // NOI18N

        btnMenuBack.setForeground(new java.awt.Color(0, 153, 204));
        btnMenuBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMenuBack.setDoubleBuffered(false);
        btnMenuBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMenuBackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenuBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMenuBackMouseExited(evt);
            }
        });
        btnMenuBack.setLayout(null);

        jLabel1.setFont(customFont);
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/home-icon24.png"))); // NOI18N
        jLabel1.setText("Menu");
        jLabel1.setIconTextGap(10);
        btnMenuBack.add(jLabel1);
        jLabel1.setBounds(10, 0, 230, 50);

        javax.swing.GroupLayout pnlFrameLayout = new javax.swing.GroupLayout(pnlFrame);
        pnlFrame.setLayout(pnlFrameLayout);
        pnlFrameLayout.setHorizontalGroup(
            pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlFrameLayout.createSequentialGroup()
                        .addComponent(logoLoginIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnMenuBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlAltas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlFrameLayout.setVerticalGroup(
            pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFrameLayout.createSequentialGroup()
                .addGroup(pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFrameLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(logoLoginIcon))
                    .addGroup(pnlFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenuBack, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(557, Short.MAX_VALUE))
            .addComponent(pnlAltas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);

        getContentPane().add(pnlFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInsertarActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        this.fmenu.setVisible(true);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    private void btnMenuBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseEntered
        // TODO add your handlings code here:

    }//GEN-LAST:event_btnMenuBackMouseEntered

    private void btnMenuBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_btnMenuBackMouseExited

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAltas;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlAltas;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JScrollPane scrollTable2;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable table1;
    private javax.swing.JTable table2;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
