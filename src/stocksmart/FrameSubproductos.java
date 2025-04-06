package stocksmart;

import inicio.FrameMenu;
import assests.icons.clases.PlusIcon;
import codigo.Unidad;
import codigo.Unidadd;
import codigo.productos;
import codigo.productosd;
import codigo.subproductos;
import codigo.subproductosd;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
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
import javax.swing.JPanel;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import static stocksmart.FontLoader.customFont;
import static stocksmart.FontLoader.customFontBold;
import static stocksmart.FontLoader.customFontBold2;

public final class FrameSubproductos extends javax.swing.JFrame {

    FrameMenu fmenu = null;
    private List<subproductos> listaProductos = new ArrayList<>();
    private final List<productos> listaProductos2 = new ArrayList<>();

    /**
     * Creates new form StockSmartFrameVentas
     *
     * @param menu
     */
    public FrameSubproductos(FrameMenu menu) {

        initComponents();
        this.fmenu = menu;
        this.setLocationRelativeTo(null);
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
        table1.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                if (table1.getSelectedRow() != -1) {
                    btnBorrar.setEnabled(true);
                } else {
                    btnBorrar.setEnabled(false);
                }
            }
        });
        table1.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrame = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
        pnlSubproductos = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNombre = new javax.swing.JTextPane();
        btnAgregar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        lblFoto = new javax.swing.JLabel();
        scrollTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
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
        lblMenu = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pnlFrame.setBackground(new java.awt.Color(50, 130, 233));

        stockSmartLoginLabel.setBackground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setFont(customFontBold);
        stockSmartLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stockSmartLoginLabel.setText("StockSmart");

        pnlSubproductos.setBackground(new java.awt.Color(255, 255, 255));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setText("Subproductos");

        lblNombre.setFont(customFontBold2);
        lblNombre.setText("Nombre o Codigo");

        txtNombre.setFont(customFontBold2);
        jScrollPane2.setViewportView(txtNombre);

        btnAgregar.setBackground(new java.awt.Color(50, 130, 233));
        btnAgregar.setFont(customFontBold2);
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar ");
        btnAgregar.setBorderPainted(false);
        btnAgregar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregar.setIconTextGap(10);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnBorrar.setBackground(new java.awt.Color(50, 130, 233));
        btnBorrar.setFont(customFontBold2);
        btnBorrar.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrar.setText("Eliminar");
        btnBorrar.setBorderPainted(false);
        btnBorrar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBorrar.setIconTextGap(10);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnEditar.setFont(customFontBold2);
        btnEditar.setForeground(new java.awt.Color(50, 130, 233));
        btnEditar.setText("Editar");
        btnEditar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditar.setIconTextGap(10);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        lblFoto.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setText("Foto");
        lblFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        javax.swing.GroupLayout pnlSubproductosLayout = new javax.swing.GroupLayout(pnlSubproductos);
        pnlSubproductos.setLayout(pnlSubproductosLayout);
        pnlSubproductosLayout.setHorizontalGroup(
            pnlSubproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubproductosLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(pnlSubproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSubproductosLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 528, Short.MAX_VALUE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))
                    .addGroup(pnlSubproductosLayout.createSequentialGroup()
                        .addGroup(pnlSubproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlSubproductosLayout.createSequentialGroup()
                                .addGap(463, 463, 463)
                                .addComponent(lblTitulo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150))))
            .addGroup(pnlSubproductosLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 1162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlSubproductosLayout.setVerticalGroup(
            pnlSubproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubproductosLayout.createSequentialGroup()
                .addGroup(pnlSubproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSubproductosLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTitulo)
                        .addGap(18, 18, 18)
                        .addComponent(lblNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(pnlSubproductosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)))
                .addGroup(pnlSubproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        btnAgregar.setIcon(new PlusIcon(18, 18, Color.WHITE));

        logoLoginIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/images/logoStockSmart50.png"))); // NOI18N

        btnMenuBack.setForeground(new java.awt.Color(0, 153, 204));
        btnMenuBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMenuBack.setDoubleBuffered(false);
        btnMenuBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMenuBackMouseClicked(evt);
            }
        });
        btnMenuBack.setLayout(null);

        lblMenu.setFont(customFont);
        lblMenu.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/home-icon24.png"))); // NOI18N
        lblMenu.setText("Menu");
        lblMenu.setIconTextGap(10);
        btnMenuBack.add(lblMenu);
        lblMenu.setBounds(10, 0, 230, 50);

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
                .addComponent(pnlSubproductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlFrameLayout.setVerticalGroup(
            pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFrameLayout.createSequentialGroup()
                .addComponent(pnlSubproductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 1472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        // TODO add your handling code here:
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
        // TODO add your handling code here:
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
            List<productos> listaProductosEditar = p.listarProductos();

            for (productos producto : listaProductosEditar) {
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

            btnAgregarProducto.addActionListener((ActionEvent e) -> {
                int selectedProductRow = productTable.getSelectedRow();
                if (selectedProductRow != -1) {
                    String selectedProductCode = (String) productTable.getValueAt(selectedProductRow, 0);
                    codigoField.setText(selectedProductCode);
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
            btnAgregarUnidad.addActionListener((ActionEvent e) -> {
                JPanel unidadPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc1 = new GridBagConstraints();
                gbc1.anchor = GridBagConstraints.WEST;
                gbc1.insets = new Insets(5, 5, 5, 5);
                gbc1.gridx = 0;
                gbc1.gridy = 0;
                unidadPanel.add(new JLabel("Unidad de medida:"), gbc1);
                JTextField nuevaUnidadField = new JTextField(15);
                gbc1.gridx = 1;
                unidadPanel.add(nuevaUnidadField, gbc1);
                gbc1.gridx = 0;
                gbc1.gridy = 1;
                unidadPanel.add(new JLabel("Formato:"), gbc1);
                JTextField formatoField = new JTextField(15);
                gbc1.gridx = 1;
                unidadPanel.add(formatoField, gbc1);
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
                                        JOptionPane.showMessageDialog(null, "Error al convertir la imagen.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "No se pudo cargar la imagen.");
                                }
                            } catch (IOException ex) {
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
        List<productos> listaProductosAgregar = p.listarProductos();

        for (productos producto : listaProductosAgregar) {
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
        btnAgregarUnidad.addActionListener((ActionEvent e) -> {
            JPanel unidadPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.anchor = GridBagConstraints.WEST;
            gbc1.insets = new Insets(5, 5, 5, 5);
            gbc1.gridx = 0;
            gbc1.gridy = 0;
            unidadPanel.add(new JLabel("Unidad de medida:"), gbc1);
            JTextField nuevaUnidadField = new JTextField(15);
            gbc1.gridx = 1;
            unidadPanel.add(nuevaUnidadField, gbc1);
            gbc1.gridx = 0;
            gbc1.gridy = 1;
            unidadPanel.add(new JLabel("Formato:"), gbc1);
            JTextField formatoField = new JTextField(15);
            gbc1.gridx = 1;
            unidadPanel.add(formatoField, gbc1);
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
                List<productos> listaProductosAgregar2 = proDAO2.listarProductos();

                for (productos pr : listaProductosAgregar2) {
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

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked

        this.fmenu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JPanel pnlSubproductos;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable table1;
    private javax.swing.JTextPane txtNombre;
    // End of variables declaration//GEN-END:variables
}
