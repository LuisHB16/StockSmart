package stocksmart;


import codigo.Unidad;
import codigo.Unidadd;
import codigo.productos;
import codigo.productosd;
import codigo.subproductos;
import codigo.subproductosd;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Subproductos extends javax.swing.JFrame {

    private List<subproductos> listaProductos = new ArrayList<>();
    private List<productos> listaProductos2 = new ArrayList<>();
    

    public Subproductos() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ListarSubproductos();
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
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int rowIndex = table1.getSelectedRow();
                    if (rowIndex != -1) {
                        btnBorrar.setEnabled(true);
                        subproductos pro = listaProductos.get(rowIndex);
                        byte[] imagenBytes = pro.getImagen();

                        if (imagenBytes != null) {
                            try {
                                // Convierte el arreglo de bytes en una imagen
                                ByteArrayInputStream inputStream = new ByteArrayInputStream(imagenBytes);
                                BufferedImage bufferedImage = ImageIO.read(inputStream);

                                if (bufferedImage != null) {
                                    // Escala la imagen al tamaño de jLabel_foto
                                    ImageIcon nuevaIcono = new ImageIcon(bufferedImage.getScaledInstance(
                                            lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH));

                                    lblFoto.setIcon(nuevaIcono);
                                    lblFoto.setText("");
                                } else {
                                    lblFoto.setIcon(null);
                                    lblFoto.setText("No se pudo cargar la imagen.");
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                lblFoto.setIcon(null);
                                lblFoto.setText("Error al cargar la imagen.");
                            }
                        } else {
                            lblFoto.setIcon(null);
                            lblFoto.setText("No hay imagen disponible.");
                        }
                    } else {
                        btnBorrar.setEnabled(false);
                        lblFoto.setIcon(null);
                        lblFoto.setText("");
                    }
                }
            }
        });
    }

    public void LimpiarTable() {
        table1.removeAll();
    }

private subproductos obtenerProductoPorCodigo(String codigo) {
    subproductosd proDAO = new subproductosd();
    listaProductos = proDAO.listarSubproductos();

    for (subproductos productos : listaProductos) {
        if (productos.getClaveAlterna().equals(codigo)) {
            return productos;
        }
    }

    return null; // Retorna null si no se encuentra el producto
}

    private void filtrarTabla() {
        String filtro = txtNombre.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        for (int i = 0; i < listaProductos.size(); i++) {
            String codigo = listaProductos.get(i).getClaveAlterna().toLowerCase();
            String nombre = listaProductos.get(i).getNombre().toLowerCase();

            if (codigo.contains(filtro) || nombre.contains(filtro)) {
                Object[] ob = new Object[6];
                ob[0] = listaProductos.get(i).getCodigo();
                ob[1] = listaProductos.get(i).getClaveAlterna();
                ob[2] = listaProductos.get(i).getNombre();
                ob[3] = listaProductos.get(i).getPrecio();
                ob[4] = listaProductos.get(i).getUnidad();
                ob[5] = listaProductos.get(i).getGranel();
                model.addRow(ob);
            }
        }
    }

    public void ListarSubproductos() {
        subproductosd proDAO = new subproductosd();
        listaProductos = proDAO.listarSubproductos();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        for (int i = 0; i < listaProductos.size(); i++) {
            Object[] ob = new Object[6];
            ob[0] = listaProductos.get(i).getCodigo();
            ob[1] = listaProductos.get(i).getClaveAlterna();
            ob[2] = listaProductos.get(i).getNombre();
            ob[3] = listaProductos.get(i).getPrecio();
            ob[4] = listaProductos.get(i).getUnidad();
            ob[5] = listaProductos.get(i).getGranel();
            model.addRow(ob);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
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
                "Codigo", "Clave Alterna", "Nombre", "Precio", "Unidad", "Granel"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
            table1.getColumnModel().getColumn(3).setResizable(false);
            table1.getColumnModel().getColumn(4).setResizable(false);
            table1.getColumnModel().getColumn(5).setResizable(false);
        }

        getContentPane().add(scrollTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 1260, 470));

        jLabel1.setText("Codigo o Nombre:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 110, 30));

        lblFoto.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setText("Foto");
        lblFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFotoMouseClicked(evt);
            }
        });
        getContentPane().add(lblFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 10, 160, 140));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 160, 30));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.13 PM.jpeg"))); // NOI18N
        jButton3.setBorder(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btnBorrar.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/eliminar.png"))); // NOI18N
        btnBorrar.setBorder(null);
        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrar.setEnabled(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 50, 130, 90));

        btnEditar.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/modificar.png"))); // NOI18N
        btnEditar.setBorder(null);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 50, 130, 90));

        btnAgregar.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/guardar.png"))); // NOI18N
        btnAgregar.setBorder(null);
        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 130, 90));

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        int preguntaSubproducto = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de eliminar el subproducto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (preguntaSubproducto == JOptionPane.YES_OPTION) {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                subproductosd pr = new subproductosd();
                int codigo = Integer.parseInt((String) table1.getValueAt(selectedRow, 0));
                String ClaveAlterna = (String) table1.getValueAt(selectedRow, 1);
                String nombre = (String) table1.getValueAt(selectedRow, 2);
                pr.EliminarSubproductos(codigo, ClaveAlterna, nombre);
                LimpiarTable();
                ListarSubproductos();
                btnBorrar.setEnabled(false);
            }
        }

    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int selectedRow = table1.getSelectedRow();

        if (selectedRow != -1) {
            // Obtener los valores de la fila seleccionada
            String claveAlterna = (String) table1.getValueAt(selectedRow, 1);
            String nombre = (String) table1.getValueAt(selectedRow, 2);
            Double precio = (Double) table1.getValueAt(selectedRow, 3); // Corregido para obtener un valor Double
            String unidad = (String) table1.getValueAt(selectedRow, 4);
            subproductosd proDAO = new subproductosd();
            String codigo = proDAO.obtenerCodigo(claveAlterna);

            

            // Crear un panel para editar los datos del subproducto
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Filtrar por Nombre:"), gbc);

            JTextField filtroNombreField = new JTextField(15);
            gbc.gridx = 2;
            panel.add(filtroNombreField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Seleccionar Producto:"), gbc);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Código");
            tableModel.addColumn("Nombre");
            JTable productTable = new JTable(tableModel);
            productTable.setPreferredScrollableViewportSize(new Dimension(200, 100)); // Tamaño personalizado de la tabla
            JScrollPane tableScrollPane = new JScrollPane(productTable);
            tableScrollPane.setPreferredSize(new Dimension(200, 100)); // Tamaño personalizado del JScrollPane
            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.BOTH;
            panel.add(tableScrollPane, gbc);

            // Llenar la tabla con todos los datos de productos
            productosd p = new productosd();
            List<productos> listaProductos = p.listarProductos();

            for (productos producto : listaProductos) {
                // Agrega los datos a la tabla
                Object[] rowData = {producto.getCodigo(), producto.getNombre()};
                tableModel.addRow(rowData);
            }

            JTextField codigoField = new JTextField(15);
            codigoField.setText(codigo);
            codigoField.setEnabled(false); // Deshabilita la edición del código
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Código:"), gbc);
            gbc.gridx = 2;
            panel.add(codigoField, gbc);

            // Agregar un botón para seleccionar un producto
            JButton btnAgregarProducto = new JButton("Seleccionar");
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            panel.add(btnAgregarProducto, gbc);

            btnAgregarProducto.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedProductRow = productTable.getSelectedRow();
                    if (selectedProductRow != -1) {
                        String selectedProductCode = (String) productTable.getValueAt(selectedProductRow, 0);
                        codigoField.setText(selectedProductCode);
                    }
                }
            });

            JTextField nombreField = new JTextField(15);
            nombreField.setText(nombre);
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Nombre:"), gbc);
            gbc.gridx = 2;
            panel.add(nombreField, gbc);

            JTextField precioField = new JTextField(15);
            precioField.setText(String.valueOf(precio));
            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("Precio:"), gbc);
            gbc.gridx = 2;
            panel.add(precioField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            panel.add(new JLabel("Unidad:"), gbc);

            JComboBox<String> unidadComboBox = new JComboBox<>();
            gbc.gridx = 2;
            panel.add(unidadComboBox, gbc);

            Unidadd unidadDAO = new Unidadd();
            List<Unidad> listaUnidades = unidadDAO.listarUnidades();

            // Llenar el ComboBox de Unidades
            for (Unidad u : listaUnidades) {
                unidadComboBox.addItem(u.getUnidad());
            }

            unidadComboBox.setSelectedItem(unidad); // Establecer la unidad actual

            gbc.gridx = 4;
            JButton btnAgregarUnidad = new JButton("+");
            panel.add(btnAgregarUnidad, gbc);

            // Manejador de eventos para el botón de agregar unidad
            btnAgregarUnidad.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JPanel unidadPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.anchor = GridBagConstraints.WEST;
                    gbc.insets = new Insets(5, 5, 5, 5);

                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    unidadPanel.add(new JLabel("Unidad de medida:"), gbc);

                    JTextField nuevaUnidadField = new JTextField(15);
                    gbc.gridx = 1;
                    unidadPanel.add(nuevaUnidadField, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    unidadPanel.add(new JLabel("Formato:"), gbc);

                    JTextField formatoField = new JTextField(15);
                    gbc.gridx = 1;
                    unidadPanel.add(formatoField, gbc);

                    int result = JOptionPane.showConfirmDialog(null, unidadPanel, "Agregar una nueva unidad de medida", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String nuevaUnidad = nuevaUnidadField.getText();
                        String formatoText = formatoField.getText();
                        double formato;

                        try {
                            formato = Double.parseDouble(formatoText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "El formato debe ser un valor numérico válido.");
                            return;
                        }

                        if (nuevaUnidad != null && !nuevaUnidad.isEmpty()) {
                            unidadDAO.RegistrarUnidad(new Unidad(nuevaUnidad, formato));
                            unidadComboBox.addItem(nuevaUnidad);
                        }
                    }
                }
            });

            gbc.gridx = 0;
            gbc.gridy = 9;
            panel.add(new JLabel("Imagen:"), gbc);

            gbc.gridx = 2;
            JPanel imagenPanel = new JPanel(new BorderLayout());
            panel.add(imagenPanel, gbc);

            // Crea un JScrollPane para la imagen
            JScrollPane imageScrollPane = new JScrollPane();
            imageScrollPane.setPreferredSize(new Dimension(150, 150)); // Establece un tamaño fijo

            // Crea un JLabel para mostrar la imagen
            JLabel seleccionarImagenLabel = new JLabel("Seleccionar Imagen");
            seleccionarImagenLabel.setBorder(null);
            imageScrollPane.setViewportView(seleccionarImagenLabel);

            imagenPanel.add(imageScrollPane, BorderLayout.CENTER);

            // Crea un JPanel para contener los botones OK y Cancelar
            JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            imagenPanel.add(botonesPanel, BorderLayout.SOUTH);

            seleccionarImagenLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // Doble clic
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        int estado = fileChooser.showOpenDialog(null);

                        if (estado == JFileChooser.APPROVE_OPTION) {
                            try {
                                File selectedFile = fileChooser.getSelectedFile();
                                BufferedImage bufferedImage = ImageIO.read(selectedFile);

                                if (bufferedImage != null) {
                                    // Escalar la imagen al tamaño del JLabel
                                    ImageIcon nuevaIcono = new ImageIcon(bufferedImage.getScaledInstance(
                                            seleccionarImagenLabel.getWidth(), seleccionarImagenLabel.getHeight(), Image.SCALE_SMOOTH));

                                    seleccionarImagenLabel.setBorder(null);
                                    seleccionarImagenLabel.setIcon(nuevaIcono);

                                    // Obtener el texto del jLabel_foto y establecerlo en seleccionarImagenLabel
                                    String textoJLabelFoto = lblFoto.getText();
                                    seleccionarImagenLabel.setText(textoJLabelFoto);

                                    // Convertir la imagen en bytes si es necesario
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    try {
                                        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                                        byte[] imagen = byteArrayOutputStream.toByteArray();

                                        // Puedes hacer algo con el arreglo de bytes 'imagen' aquí si es necesario
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al convertir la imagen.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "No se pudo cargar la imagen.");
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Error al cargar la imagen.");
                            }
                        }
                    }
                }
            });

            subproductos sub = obtenerProductoPorCodigo((String) table1.getValueAt(selectedRow, 1));

            if (sub != null && sub.getImagen() != null) {
                ImageIcon imagenActual = new ImageIcon(sub.getImagen());
                seleccionarImagenLabel.setIcon(imagenActual);
                seleccionarImagenLabel.setText(""); // Elimina el texto "Seleccionar Imagen"
            }

            // Añadir checkbox para la opción "granel"
            gbc.gridx = 0;
            gbc.gridy = 10; // Ajusta la fila según tu diseño
            panel.add(new JLabel("Granel:"), gbc);

            JCheckBox granelCheckBox = new JCheckBox();
            granelCheckBox.setSelected(((String) table1.getValueAt(selectedRow, 5)).equals("si"));
            gbc.gridx = 1;
            panel.add(granelCheckBox, gbc);

            int result = JOptionPane.showConfirmDialog(null, panel, "Agregar un nuevo Subproducto", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String nuevoCodigo = codigoField.getText(); // Obtener el nuevo valor del código
                String nuevoNombre = nombreField.getText();
                String nuevoPrecioStr = precioField.getText();
                String nuevaUnidad = (String) unidadComboBox.getSelectedItem();
                String granel = granelCheckBox.isSelected() ? "si" : "no";
                double nuevoPrecio;

                byte[] imagen; // Declarar la variable fuera de la sección del if

                Icon icono = seleccionarImagenLabel.getIcon();
                if (icono instanceof ImageIcon) {
                    BufferedImage bufferedImage = new BufferedImage(icono.getIconWidth(), icono.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = bufferedImage.createGraphics();
                    icono.paintIcon(null, g2d, 0, 0);
                    g2d.dispose();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                        imagen = byteArrayOutputStream.toByteArray(); // Asignar la imagen a la variable
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al convertir la imagen.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha seleccionado una imagen.");
                    return;
                }

                try {
                    nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "El precio debe ser un valor numérico válido.");
                    return;
                }

                if (nuevoNombre.isEmpty() || nuevoPrecioStr.isEmpty() || nuevaUnidad.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ninguno de los campos puede estar incompleto. Por favor, complete todos los campos.");
                } else {
                    subproductos subproducto = new subproductos(nuevoCodigo, nuevoNombre, nuevoPrecio, nuevaUnidad, granel, imagen);

                    boolean edicionExitosa = proDAO.editarSubproducto(claveAlterna, subproducto);

                    if (edicionExitosa) {
                        JOptionPane.showMessageDialog(null, "Subproducto editado con éxito");
                        LimpiarTable();
                        ListarSubproductos();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al editar el subproducto");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un subproducto para editar.");
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);

        JTextField codigoField = new JTextField(15);
        codigoField.setEnabled(false); // Deshabilita la edición del código
        gbc.gridx = 2;
        panel.add(codigoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2; // Coloca la fila adecuada según tu diseño
        panel.add(new JLabel("Filtrar por Nombre:"), gbc);

        JTextField filtroNombreField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(filtroNombreField, gbc);

        // Crear una tabla de productos
        gbc.gridx = 0;
        gbc.gridy = 3; // Ajusta la fila según tu diseño
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Código");
        tableModel.addColumn("Nombre");
        JTable productTable = new JTable(tableModel);
        productTable.setPreferredScrollableViewportSize(new Dimension(200, 100)); // Ajusta el tamaño deseado
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(tableScrollPane, gbc);

        // Llenar la tabla con todos los datos de productos
        productosd p = new productosd();
        List<productos> listaProductos = p.listarProductos();

        for (productos producto : listaProductos) {
            // Agrega los datos a la tabla
            Object[] rowData = {producto.getCodigo(), producto.getNombre()};
            tableModel.addRow(rowData);
        }
        filtroNombreField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarFiltro();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarFiltro();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarFiltro();
            }

            private void actualizarFiltro() {
                String filtroNombre = filtroNombreField.getText();
                TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>((DefaultTableModel) productTable.getModel());
                productTable.setRowSorter(rowSorter);
                rowSorter.setRowFilter(RowFilter.regexFilter(filtroNombre, 1)); // 1 indica la columna "Nombre"
            }
        });

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedCode = (String) productTable.getValueAt(selectedRow, 0);
                    codigoField.setText(selectedCode);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Clave Alterna:"), gbc);

        JTextField claveAlternaField = new JTextField(15);
        gbc.gridx = 2;
        panel.add(claveAlternaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Nombre:"), gbc);

        JTextField nombreField = new JTextField(15);
        gbc.gridx = 2;
        panel.add(nombreField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Precio:"), gbc);

        JTextField precioField = new JTextField(15);
        gbc.gridx = 2;
        panel.add(precioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(new JLabel("Imagen:"), gbc);

        gbc.gridx = 2;
        JPanel imagenPanel = new JPanel(new BorderLayout());
        panel.add(imagenPanel, gbc);

// Crea un JScrollPane para la imagen
        JScrollPane imageScrollPane = new JScrollPane();
        imageScrollPane.setPreferredSize(new Dimension(150, 150)); // Establece un tamaño fijo

// Crea un JLabel para mostrar la imagen
        JLabel seleccionarImagenLabel = new JLabel("Seleccionar Imagen");
        seleccionarImagenLabel.setBorder(null);
        imageScrollPane.setViewportView(seleccionarImagenLabel);

        imagenPanel.add(imageScrollPane, BorderLayout.CENTER);

// Crea un JPanel para contener los botones OK y Cancelar
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagenPanel.add(botonesPanel, BorderLayout.SOUTH);

        seleccionarImagenLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int estado = fileChooser.showOpenDialog(null);

                    if (estado == JFileChooser.APPROVE_OPTION) {
                        try {
                            File selectedFile = fileChooser.getSelectedFile();
                            BufferedImage bufferedImage = ImageIO.read(selectedFile);

                            if (bufferedImage != null) {
                                // Escalar la imagen al tamaño del JLabel
                                ImageIcon nuevaIcono = new ImageIcon(bufferedImage.getScaledInstance(
                                        seleccionarImagenLabel.getWidth(), seleccionarImagenLabel.getHeight(), Image.SCALE_SMOOTH));

                                seleccionarImagenLabel.setBorder(null);
                                seleccionarImagenLabel.setIcon(nuevaIcono);
                                seleccionarImagenLabel.setText("");

                                // Convertir la imagen en bytes si es necesario
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                                byte[] imagen = byteArrayOutputStream.toByteArray();
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo cargar la imagen.");
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error al cargar la imagen.");
                        }
                    }
                }
            }
        });

        // Añadir checkbox para la opción "granel"
        gbc.gridx = 0;
        gbc.gridy = 10; // Ajusta la fila según tu diseño
        panel.add(new JLabel("Granel:"), gbc);

        JCheckBox granelCheckBox = new JCheckBox();
        gbc.gridx = 1;
        panel.add(granelCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Unidad:"), gbc);

        JComboBox<String> unidadComboBox = new JComboBox<>();
        gbc.gridx = 2;
        gbc.gridwidth = 3;
        panel.add(unidadComboBox, gbc);

        Unidadd unidadDAO = new Unidadd();
        List<Unidad> listaUnidades = unidadDAO.listarUnidades();

        unidadComboBox.removeAllItems();

        for (Unidad unidad : listaUnidades) {
            unidadComboBox.addItem(unidad.getUnidad());
        }

        gbc.gridx = 3;
        JButton btnAgregarUnidad = new JButton("+");
        panel.add(btnAgregarUnidad, gbc);

        // Manejador de eventos para el botón de agregar unidad
        btnAgregarUnidad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel unidadPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 5);

                gbc.gridx = 0;
                gbc.gridy = 0;
                unidadPanel.add(new JLabel("Unidad de medida:"), gbc);

                JTextField nuevaUnidadField = new JTextField(15);
                gbc.gridx = 1;
                unidadPanel.add(nuevaUnidadField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                unidadPanel.add(new JLabel("Formato:"), gbc);

                JTextField formatoField = new JTextField(15);
                gbc.gridx = 1;
                unidadPanel.add(formatoField, gbc);

                int result = JOptionPane.showConfirmDialog(null, unidadPanel, "Agregar una nueva unidad de medida", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String nuevaUnidad = nuevaUnidadField.getText();
                    String formatoText = formatoField.getText();
                    double formato;

                    try {
                        formato = Double.parseDouble(formatoText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "El formato debe ser un valor numérico válido.");
                        return;
                    }

                    if (nuevaUnidad != null && !nuevaUnidad.isEmpty()) {
                        unidadDAO.RegistrarUnidad(new Unidad(nuevaUnidad, formato));
                        unidadComboBox.addItem(nuevaUnidad);
                    }
                }
            }
        });

        gbc.gridx = 3;
        panel.add(new JButton("+"), gbc);
        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar un nuevo Subproducto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Capturar otros campos aquí
            String granel = granelCheckBox.isSelected() ? "si" : "no";
            String claveAlterna = claveAlternaField.getText();
            String nombre = nombreField.getText();
            String precioText = precioField.getText();
            String codigo = codigoField.getText();
            double precio;
            byte[] imagen; // Declarar la variable fuera de la sección del if

            Icon icono = seleccionarImagenLabel.getIcon();
            if (icono instanceof ImageIcon) {
                BufferedImage bufferedImage = new BufferedImage(icono.getIconWidth(), icono.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                icono.paintIcon(null, g2d, 0, 0);
                g2d.dispose();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                    imagen = byteArrayOutputStream.toByteArray(); // Asignar la imagen a la variable
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al convertir la imagen.");
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado una imagen.");
                return;
            }

            try {
                precio = Double.parseDouble(precioText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El precio debe ser un valor numérico válido.");
                return;
            }

            String unidad = (String) unidadComboBox.getSelectedItem();

            if (claveAlterna.isEmpty() || nombre.isEmpty() || unidad.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ninguno de los campos puede estar incompleto. Por favor, complete todos los campos.");
            } else {
                subproductosd proDAO = new subproductosd();
                List<subproductos> listarSubproductos = proDAO.listarSubproductos();

                for (subproductos subproducto : listarSubproductos) {
                    if (subproducto.getClaveAlterna().equals(claveAlterna)) {
                        JOptionPane.showMessageDialog(null, "La clave alterna ya existe en la base de datos como subproducto. Introduce una clave alterna diferente.");
                        return;
                    }
                }
                productosd proDAO2 = new productosd();
                List<productos> listaProductos2 = proDAO2.listarProductos();

                for (productos pr : listaProductos2) {
                    if (pr.getClavealterna().equals(claveAlterna)) {
                        JOptionPane.showMessageDialog(null, "La clave alterna ya existen en la base de datos como producto. Introduce una clave alterna diferente.");
                        return;
                    }
                }

                subproductos pr = new subproductos(codigo, nombre, claveAlterna, precio, unidad, granel, imagen);
                boolean registroExitoso = proDAO.agregarSubproducto(pr);

                if (registroExitoso) {
                    JOptionPane.showMessageDialog(null, "Subproducto agregado con éxito");
                    ListarSubproductos();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar el subproducto");
                }
            }
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void lblFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFotoMouseClicked

    }//GEN-LAST:event_lblFotoMouseClicked

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
            java.util.logging.Logger.getLogger(Subproductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Subproductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Subproductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Subproductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Subproductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JTable table1;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
