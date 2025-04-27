package stocksmart;

import inicio.FrameMenu;
import assests.icons.clases.PlusIcon;
import codigo.Categoria;
import codigo.Categoriad;
import codigo.ProductoModel;
import codigo.Unidad;
import codigo.Unidadd;
import codigo.movimiento;
import codigo.movimientod;
import codigo.productos;
import codigo.productosd;
import codigo.proveedores;
import codigo.proveedoresd;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import static stocksmart.FontLoader.customFont;
import static stocksmart.FontLoader.customFontBold;
import static stocksmart.FontLoader.customFontBold2;

public class FrameProductos extends javax.swing.JFrame {

    FrameMenu fmenu = null;
     private ProductoModel productoModel = new ProductoModel();

    productos pro = new productos();
    productosd proDAO = new productosd();
    byte[] imagenSeleccionada;
    movimientod m = new movimientod();
    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    private List<productos> listaProductos = new ArrayList<>();
    private ArrayList<Unidad> listaUnidades = new ArrayList<>();

    /**
     * Creates new form StockSmartFrameVentas
     */
    public FrameProductos(FrameMenu menu) {

        initComponents();
        this.fmenu = menu;
        this.setLocationRelativeTo(null);
       lista();
        ListarProductos();

        // Add a DocumentListener to the search field
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
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != -1) {
                    btnBorrar.setEnabled(true);
                    productos pro1 = listaProductos.get(rowIndex);
                    byte[] imagenBytes = pro1.getImagen();
                    
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
    
    private void lista() {
        Unidadd unidadDAO = new Unidadd();
        listaUnidades = (ArrayList<Unidad>) unidadDAO.listarUnidades();

        for (Unidad unidad : listaUnidades) {
            String nombreUnidad = unidad.getUnidad();
            Double formato = unidad.getFormato();
        }
    }

    private void filtrarTabla() {
        String filtro = txtNombre.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        for (int i = 0; i < listaProductos.size(); i++) {
            String codigo = listaProductos.get(i).getClavealterna().toLowerCase();
            String nombre = listaProductos.get(i).getNombre().toLowerCase();

            if (codigo.contains(filtro) || nombre.contains(filtro)) {
                Unidad formatoEncontrado = null;

                for (Unidad unidad : listaUnidades) {
                    if (unidad.getUnidad().equals(listaProductos.get(i).getUnidad())) {
                        formatoEncontrado = unidad;
                        break;
                    }
                }

                Object[] ob = new Object[11];
                ob[0] = listaProductos.get(i).getClavealterna();
                ob[1] = listaProductos.get(i).getNombre();
                ob[2] = listaProductos.get(i).getExistencia() / formatoEncontrado.getFormato();
                ob[3] = listaProductos.get(i).getMarca();
                ob[4] = listaProductos.get(i).getPrecio();
                ob[5] = listaProductos.get(i).getGranel();
                ob[6] = listaProductos.get(i).getUnidad();
                ob[7] = listaProductos.get(i).getCategoria();
                ob[8] = listaProductos.get(i).getUnidadC();
                ob[9] = listaProductos.get(i).getExtMin();
                ob[10] = listaProductos.get(i).getExtMax();

                model.addRow(ob);
            }
        }
    }

    private productos obtenerProductoPorCodigo(String codigo) {
        productosd proDAO = new productosd();
        List<productos> listaProductos = proDAO.listarProductos();

        for (productos producto : listaProductos) {
            if (producto.getClavealterna().equals(codigo)) {
                return producto;
            }
        }

        return null; // Retorna null si no se encuentra el producto
    }

    public void ListarProductos() {
        // Obtener la lista de productos desde el DAO
        productosd proDAO = new productosd();
        listaProductos = proDAO.listarProductos();

        // Obtener el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) table1.getModel();

        // Limpiar el modelo
        model.setRowCount(0);

        // Iterar sobre la lista de productos
        for (int i = 0; i < listaProductos.size(); i++) {
            // Buscar la unidad correspondiente en la lista de unidades
            Unidad formatoEncontrado = buscarUnidad(listaProductos.get(i).getUnidad());

            // Verificar si se encontró la unidad
            if (formatoEncontrado != null) {
                // Crear un array de objetos con 8 elementos
                Object[] ob = new Object[11];

                // Llenar el array con la información del producto
                ob[0] = listaProductos.get(i).getClavealterna();
                ob[1] = listaProductos.get(i).getNombre();

                // Verificar si la unidad tiene un valor válido antes de realizar la división
                if (formatoEncontrado.getFormato() != 0) {
                    ob[2] = listaProductos.get(i).getExistencia() / formatoEncontrado.getFormato();
                } else {
                    // Manejar la división por cero u otro valor no válido según sea necesario
                    ob[2] = "Valor no válido";
                }

                ob[3] = listaProductos.get(i).getMarca();
                ob[4] = listaProductos.get(i).getPrecio();
                ob[5] = listaProductos.get(i).getGranel();
                ob[6] = listaProductos.get(i).getUnidad();
                ob[7] = listaProductos.get(i).getCategoria();
                ob[8] = listaProductos.get(i).getUnidadC();
                ob[9] = listaProductos.get(i).getExtMin();
                ob[10] = listaProductos.get(i).getExtMax();
                // Agregar la fila al modelo de la tabla
                model.addRow(ob);
            }
        }
    }

// Método para buscar una unidad en la lista de unidades
    private Unidad buscarUnidad(String nombreUnidad) {
        for (Unidad unidad : listaUnidades) {
            if (unidad.getUnidad().equals(nombreUnidad)) {
                return unidad;
            }
        }
        // Devolver null si no se encuentra la unidad
        return null;
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
        pnlProductos = new javax.swing.JPanel();
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

        pnlProductos.setBackground(new java.awt.Color(255, 255, 255));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setText("Productos");

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
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Clave Alterna", "Nombre", "Existencia", "Marca", "Precio", "Granel", "Unidad Venta", "Categoria", "Unidad Compra", "Cantidad Minima", "Cantidad Maxima"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1.setColumnSelectionAllowed(true);
        table1.getTableHeader().setReorderingAllowed(false);
        scrollTable1.setViewportView(table1);

        javax.swing.GroupLayout pnlProductosLayout = new javax.swing.GroupLayout(pnlProductos);
        pnlProductos.setLayout(pnlProductosLayout);
        pnlProductosLayout.setHorizontalGroup(
            pnlProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlProductosLayout.createSequentialGroup()
                .addGroup(pnlProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlProductosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollTable1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlProductosLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(pnlProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlProductosLayout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(btnAgregar))
                            .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlProductosLayout.createSequentialGroup()
                                .addGap(463, 463, 463)
                                .addComponent(lblTitulo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                        .addGroup(pnlProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlProductosLayout.createSequentialGroup()
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlProductosLayout.createSequentialGroup()
                                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)))))
                .addGap(125, 125, 125))
        );
        pnlProductosLayout.setVerticalGroup(
            pnlProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductosLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addGroup(pnlProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlProductosLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(18, 18, 18)
                        .addComponent(lblNombre))
                    .addComponent(lblFoto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
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
                .addComponent(pnlProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlFrameLayout.setVerticalGroup(
            pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFrameLayout.createSequentialGroup()
                .addComponent(pnlProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(pnlFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 1472, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        // TODO add your handling code here:
       int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Producto?");
        if (pregunta == JOptionPane.YES_OPTION) { // Usar JOptionPane.YES_OPTION en lugar de 0
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                productosd proDAO = new productosd();
                String codigo = (String) table1.getValueAt(selectedRow, 0);
                productos producto = obtenerProductoPorCodigo(codigo);
                proDAO.eliminarProducto(producto.getCodigo());
                ListarProductos(); // Puedes habilitar esta línea si deseas actualizar la lista después de eliminar un producto
                btnBorrar.setEnabled(false);
            }
        }

    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
       int selectedRow = table1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un producto para editar.");
            return;
        }

        String codigo = (String) table1.getValueAt(selectedRow, 0);

        productosd proDAO = new productosd();
        productos producto = obtenerProductoPorCodigo(codigo);

        if (producto == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el producto para editar.");
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);

        JTextField codigoField = new JTextField(15);
        codigoField.setText(producto.getCodigo()); // Establece el valor actual del código
        codigoField.setEditable(false); // No se puede editar el código
        gbc.gridx = 2;
        panel.add(codigoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Clave Alterna:"), gbc);

        JTextField claveAlternaField = new JTextField(15);
        claveAlternaField.setText(producto.getClavealterna()); // Establece el valor actual de la clave alterna
        claveAlternaField.setEditable(false);
        gbc.gridx = 2;
        panel.add(claveAlternaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nombre:"), gbc);

        JTextField nombreField = new JTextField(15);
        nombreField.setText(producto.getNombre()); // Establece el valor actual del nombre
        gbc.gridx = 2;
        panel.add(nombreField, gbc);

        // Ajusta el campo "Existencia"
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Existencia:"), gbc);

        JTextField existenciaField = new JTextField(15);
        existenciaField.setText(String.valueOf(producto.getExistencia())); // Establece el valor actual de la existencia
        double existenciaAnterior = producto.getExistencia();
        gbc.gridx = 2;
        panel.add(existenciaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Marca:"), gbc);

        JComboBox<String> marcaComboBox = new JComboBox<>();
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        panel.add(marcaComboBox, gbc);

        proveedoresd proveedoresDAO = new proveedoresd();
        List<proveedores> listaProveedores = proveedoresDAO.listarProveedores();
        for (proveedores pr : listaProveedores) {
            marcaComboBox.addItem(pr.getMarca());
        }
        marcaComboBox.setSelectedItem(producto.getMarca()); // Establece la marca actual

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Precio:"), gbc);

        JTextField precioField = new JTextField(15);
        precioField.setText(String.valueOf(producto.getPrecio())); // Establece el valor actual del precio
        gbc.gridx = 2;
        panel.add(precioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Descripción (Esto aparecerá en la página en línea):"), gbc);

        JTextField descripcionField = new JTextField(15);
        descripcionField.setText(producto.getDescripcion()); // Establece la descripción actual
        gbc.gridx = 2;
        panel.add(descripcionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Unidad de venta:"), gbc);

        JComboBox<String> unidadComboBox = new JComboBox<>();
        gbc.gridx = 2;
        gbc.gridwidth = 3;
        panel.add(unidadComboBox, gbc);

        Unidadd unidadDAO = new Unidadd();
        List<Unidad> listaUnidades = unidadDAO.listarUnidades();

        unidadComboBox.removeAllItems();

        // No mostrar "Seleccionar" como una opción
        for (Unidad unidad : listaUnidades) {
            unidadComboBox.addItem(unidad.getUnidad());
        }
        unidadComboBox.setSelectedItem(producto.getUnidad()); // Establece la unidad actual

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

        gbc.gridx = 0;
        gbc.gridy = 10;
        panel.add(new JLabel("Unidad de compra:"), gbc);

        JComboBox<String> unidadComboBox2 = new JComboBox<>();
        gbc.gridx = 2;
        gbc.gridwidth = 3;
        panel.add(unidadComboBox2, gbc);

        for (Unidad unidad : listaUnidades) {
            unidadComboBox2.addItem(unidad.getUnidad());
        }
        unidadComboBox2.setSelectedItem(producto.getUnidadC());

        gbc.gridx = 4;
        JButton btnAgregarUnidad2 = new JButton("+");
        panel.add(btnAgregarUnidad2, gbc);

        // Manejador de eventos para el botón de agregar unidad (segunda unidad)
        btnAgregarUnidad2.addActionListener(new ActionListener() {
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
                    String nuevaUnida = nuevaUnidadField.getText();
                    String formatoText = formatoField.getText();
                    double formato;

                    try {
                        formato = Double.parseDouble(formatoText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "El formato debe ser un valor numérico válido.");
                        return;
                    }

                    if (nuevaUnida != null && !nuevaUnida.isEmpty()) {
                        unidadDAO.RegistrarUnidad(new Unidad(nuevaUnida, formato));
                        unidadComboBox.addItem(nuevaUnida);
                    }
                }
            }
        });

        gbc.gridx = 4;
        panel.add(new JButton("+"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        panel.add(new JLabel("Cantidad Maxima:"), gbc);

        JTextField cantmax = new JTextField(15);

        cantmax.setText(String.valueOf(producto.getExtMax()));
        gbc.gridx = 2;
        panel.add(cantmax, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        panel.add(new JLabel("Cantidad Minima:"), gbc);

        JTextField cantmin = new JTextField(15);
        cantmin.setText(String.valueOf(producto.getExtMin()));
        gbc.gridx = 2;
        panel.add(cantmin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 13;
        JCheckBox granelCheckBox = new JCheckBox("Granel");
        granelCheckBox.setSelected(producto.getGranel() == "si"); // Establece el estado actual de granel
        panel.add(granelCheckBox, gbc);

        // Agregar el campo de imagen
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

        // Agregar el campo de categoría
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Categoría:"), gbc);

        JComboBox<String> categoriaComboBox = new JComboBox<>();
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        panel.add(categoriaComboBox, gbc);

        Categoriad categoriasDAO = new Categoriad();
        List<Categoria> listaCategorias = categoriasDAO.listarCategorias();

        categoriaComboBox.removeAllItems();

        // Agregar las categorías existentes al ComboBox
        for (Categoria categoria : listaCategorias) {
            categoriaComboBox.addItem(categoria.getCategoria());
        }
        categoriaComboBox.setSelectedItem(producto.getCategoria()); // Establece la categoría actual

        // Botón para agregar nueva categoría
        gbc.gridx = 3;
        JButton btnAgregarCategoria = new JButton("+");
        panel.add(btnAgregarCategoria, gbc);

        btnAgregarCategoria.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel categoriaPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 5);

                gbc.gridx = 0;
                gbc.gridy = 0;
                categoriaPanel.add(new JLabel("Categoría:"), gbc);

                JTextField nuevaCategoriaField = new JTextField(15);
                gbc.gridx = 1;
                categoriaPanel.add(nuevaCategoriaField, gbc);

                int result = JOptionPane.showConfirmDialog(null, categoriaPanel, "Agregar una nueva categoría", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String nuevaCategoria = nuevaCategoriaField.getText();

                    if (nuevaCategoria != null && !nuevaCategoria.isEmpty()) {
                        categoriasDAO.RegistrarCategoria(new Categoria(nuevaCategoria));
                        // Actualizar el ComboBox de categorías después de agregar la nueva categoría
                        List<Categoria> listaCategorias = categoriasDAO.listarCategorias();
                        categoriaComboBox.removeAllItems();
                        for (Categoria categoria : listaCategorias) {
                            categoriaComboBox.addItem(categoria.getCategoria());
                        }
                    }
                }
            }
        });
        // Agregar código para mostrar la imagen actual
        if (producto.getImagen() != null) {
            ImageIcon imagenActual = new ImageIcon(producto.getImagen());
            seleccionarImagenLabel.setIcon(imagenActual);
            seleccionarImagenLabel.setText(""); // Elimina el texto "Seleccionar Imagen"
        }

        // Configurar la casilla de verificación "granel"
        granelCheckBox.setSelected("si".equals(producto.getGranel()));

        // Continúa con la lógica de actualizar el producto
        int result = JOptionPane.showConfirmDialog(null, panel, "Editar producto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Actualiza los valores del producto con los nuevos valores ingresados por el usuario
            producto.setClavealterna(claveAlternaField.getText());
            producto.setNombre(nombreField.getText());

            try {
                producto.setExistencia(Double.parseDouble(existenciaField.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "La existencia debe ser un valor numérico válido.");
                return;
            }

            try {
                producto.setPrecio(Double.parseDouble(precioField.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El precio debe ser un valor numérico válido.");
                return;
            }

            producto.setMarca(marcaComboBox.getSelectedItem().toString());
            producto.setDescripcion(descripcionField.getText());
            producto.setUnidad(unidadComboBox.getSelectedItem().toString());
            producto.setUnidadC(unidadComboBox2.getSelectedItem().toString());
            producto.setGranel(granelCheckBox.isSelected() ? "si" : "no");
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
            producto.setImagen(imagen);
            producto.setCategoria(categoriaComboBox.getSelectedItem().toString());

            try {
                double existenciaNueva = Double.parseDouble(existenciaField.getText());
                producto.setExistencia(existenciaNueva);

                // Verifica si hay un cambio en la existencia
                if (existenciaNueva != existenciaAnterior) {
                    // Envía el movimiento correspondiente
                    movimiento mo = new movimiento("Ajuste", fechaActual, existenciaNueva - existenciaAnterior, codigo, existenciaAnterior);
                    m.agregarMovimiento(mo);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "La existencia debe ser un valor numérico válido.");
                return;
            }
            producto.setExtMax(Integer.parseInt(cantmax.getText()));
            producto.setExtMin(Integer.parseInt(cantmin.getText()));

            // Actualiza el producto en la base de datos
            boolean actualizacionExitosa = proDAO.editarProducto(producto.getCodigo(), producto);

            if (actualizacionExitosa) {

                JOptionPane.showMessageDialog(null, "Producto actualizado con éxito");
                ListarProductos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el producto");
            }
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
       boolean camposIncompletos;
        boolean claveAlternaRepetida;

        do {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Clave Alterna:"), gbc);

            JTextField claveAlternaField = new JTextField(15);
            claveAlternaField.setText(productoModel.getClaveAlterna());
            gbc.gridx = 2;
            panel.add(claveAlternaField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Nombre:"), gbc);

            JTextField nombreField = new JTextField(15);
            nombreField.setText(productoModel.getNombre());
            gbc.gridx = 2;
            panel.add(nombreField, gbc);

            // Ajusta el campo "Existencia"
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Existencia:"), gbc);

            JTextField existenciaField = new JTextField(15);
            existenciaField.setText(productoModel.getExistencia());
            gbc.gridx = 2;
            panel.add(existenciaField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Marca:"), gbc);

            JComboBox<String> marcaComboBox = new JComboBox<>();
            gbc.gridx = 2;
            gbc.gridwidth = 2;
            panel.add(marcaComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("Precio:"), gbc);

            JTextField precioField = new JTextField(15);
            precioField.setText(productoModel.getPrecio());
            gbc.gridx = 2;
            panel.add(precioField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            panel.add(new JLabel("Descripción (Esto aparecerá en la página en línea):"), gbc);

            JTextField descripcionField = new JTextField(15);
            descripcionField.setText(productoModel.getDescripcion());
            gbc.gridx = 2;
            panel.add(descripcionField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 7;
            panel.add(new JLabel("Unidad de venta:"), gbc);

            JComboBox<String> unidadComboBox = new JComboBox<>();
            gbc.gridx = 2;
            gbc.gridwidth = 3;
            panel.add(unidadComboBox, gbc);

            Unidadd unidadDAO = new Unidadd();
            List<Unidad> listaUnidades = unidadDAO.listarUnidades();

            unidadComboBox.removeAllItems();

            // No mostrar "Seleccionar" como una opción
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

            gbc.gridx = 0;
            gbc.gridy = 8;
            panel.add(new JLabel("Unidad de compra:"), gbc);

            JComboBox<String> unidadComboBox2 = new JComboBox<>();
            gbc.gridx = 2;
            gbc.gridwidth = 3;
            panel.add(unidadComboBox2, gbc);

            for (Unidad unidad : listaUnidades) {
                unidadComboBox2.addItem(unidad.getUnidad());
            }

            gbc.gridx = 4;
            JButton btnAgregarUnidad2 = new JButton("+");
            panel.add(btnAgregarUnidad2, gbc);

            // Manejador de eventos para el botón de agregar unidad (segunda unidad)
            btnAgregarUnidad2.addActionListener(new ActionListener() {
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
                        String nuevaUnida = nuevaUnidadField.getText();
                        String formatoText = formatoField.getText();
                        double formato;

                        try {
                            formato = Double.parseDouble(formatoText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "El formato debe ser un valor numérico válido.");
                            return;
                        }

                        if (nuevaUnida != null && !nuevaUnida.isEmpty()) {
                            unidadDAO.RegistrarUnidad(new Unidad(nuevaUnida, formato));
                            unidadComboBox.addItem(nuevaUnida);
                        }
                    }
                }
            });

            gbc.gridx = 4;
            panel.add(new JButton("+"), gbc);

            proveedoresd proveedoresDAO = new proveedoresd();
            List<proveedores> listaProveedores = proveedoresDAO.listarProveedores();
            for (proveedores pr : listaProveedores) {
                marcaComboBox.addItem(pr.getMarca());
            }

            gbc.gridx = 0;
            gbc.gridy = 10;
            panel.add(new JLabel("Imagen:"), gbc);

            gbc.gridx = 2;
            JPanel imagenPanel = new JPanel(new BorderLayout());
            panel.add(imagenPanel, gbc);

// Crea un JScrollPane para la imagen
            JScrollPane imageScrollPane = new JScrollPane();
            imageScrollPane.setPreferredSize(new Dimension(150, 150));

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

            gbc.gridx = 0;
            gbc.gridy = 9;
            panel.add(new JLabel("Categoría:"), gbc);

            JComboBox<String> categoriaComboBox = new JComboBox<>();
            gbc.gridx = 2;
            gbc.gridwidth = 2;
            panel.add(categoriaComboBox, gbc);

            Categoriad categoriasDAO = new Categoriad();
            List<Categoria> listaCategorias = categoriasDAO.listarCategorias();

            categoriaComboBox.removeAllItems();

            // Agregar las categorías existentes al ComboBox
            for (Categoria categoria : listaCategorias) {
                categoriaComboBox.addItem(categoria.getCategoria());
            }

            // Botón para agregar nueva categoría
            gbc.gridx = 3;
            JButton btnAgregarCategoria = new JButton("+");
            panel.add(btnAgregarCategoria, gbc);

            btnAgregarCategoria.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JPanel categoriaPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.anchor = GridBagConstraints.WEST;
                    gbc.insets = new Insets(5, 5, 5, 5);

                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    categoriaPanel.add(new JLabel("Categoría:"), gbc);

                    JTextField nuevaCategoriaField = new JTextField(15);
                    gbc.gridx = 1;
                    categoriaPanel.add(nuevaCategoriaField, gbc);

                    int result = JOptionPane.showConfirmDialog(null, categoriaPanel, "Agregar una nueva categoría", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String nuevaCategoria = nuevaCategoriaField.getText();

                        if (nuevaCategoria != null && !nuevaCategoria.isEmpty()) {
                            categoriasDAO.RegistrarCategoria(new Categoria(nuevaCategoria));
                            // Actualizar el ComboBox de categorías después de agregar la nueva categoría
                            List<Categoria> listaCategorias = categoriasDAO.listarCategorias();
                            categoriaComboBox.removeAllItems();
                            for (Categoria categoria : listaCategorias) {
                                categoriaComboBox.addItem(categoria.getCategoria());
                            }
                        }
                    }
                }
            });

            gbc.gridx = 0;
            gbc.gridy = 11;
            panel.add(new JLabel("Cantidad Maxima:"), gbc);

            JTextField cantmax = new JTextField(15);
            cantmax.setText(productoModel.getClaveAlterna());
            gbc.gridx = 2;
            panel.add(cantmax, gbc);

            gbc.gridx = 0;
            gbc.gridy = 12;
            panel.add(new JLabel("Cantidad Minima:"), gbc);

            JTextField cantmin = new JTextField(15);
            cantmin.setText(productoModel.getClaveAlterna());
            gbc.gridx = 2;
            panel.add(cantmin, gbc);

            gbc.gridx = 0;
            gbc.gridy = 13;
            JCheckBox granelCheckBox = new JCheckBox("Granel");
            panel.add(granelCheckBox, gbc);

            int result = JOptionPane.showConfirmDialog(null, panel, "Agregar un nuevo producto", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                // El usuario ha cancelado, salir del bucle
                return;
            }

            // Resto del código para validar y procesar la información
            camposIncompletos = false;
            claveAlternaRepetida = false;

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

            if (claveAlternaField.getText().isEmpty() || nombreField.getText().isEmpty() || marcaComboBox.getSelectedItem() == null
                    || unidadComboBox.getSelectedItem() == null || imagen == null) {
                JOptionPane.showMessageDialog(null, "Ninguno de los campos puede estar incompleto. Por favor, complete todos los campos.");
                camposIncompletos = true;
                continue;
            }

            productosd proDAO = new productosd();
            List<productos> listaProductos = proDAO.listarProductos();

            for (productos p : listaProductos) {
                if (p.getClavealterna().equals(claveAlternaField.getText())) {
                    claveAlternaRepetida = true;
                    JOptionPane.showMessageDialog(null, "La clave alterna ya existe en la base de datos. Introduce una clave alterna diferente.");
                    break;
                }
            }

            if (!claveAlternaRepetida && !camposIncompletos) {
                // Crear un objeto productos con los detalles del producto
                productos producto = new productos(
                        claveAlternaField.getText(),
                        nombreField.getText(),
                        Double.parseDouble(existenciaField.getText()),
                        (String) marcaComboBox.getSelectedItem(),
                        Double.parseDouble(precioField.getText()),
                        descripcionField.getText(),
                        granelCheckBox.isSelected() ? "si" : "no",
                        (String) unidadComboBox.getSelectedItem(),
                        imagen,
                        (String) categoriaComboBox.getSelectedItem(),
                        (String) unidadComboBox2.getSelectedItem(),
                        Integer.parseInt(cantmax.getText()),
                        Integer.parseInt(cantmin.getText())
                );

                productoModel.setClaveAlterna(claveAlternaField.getText());
                productoModel.setNombre(nombreField.getText());
                productoModel.setExistencia(existenciaField.getText());
                productoModel.setPrecio(precioField.getText());
                productoModel.setDescripcion(descripcionField.getText());
                productoModel.setUnidad((String) unidadComboBox.getSelectedItem());
                productoModel.setUnidadC((String) unidadComboBox2.getSelectedItem());
                productoModel.setExtMax(Integer.parseInt(cantmax.getText()));
                productoModel.setExtMin(Integer.parseInt(cantmin.getText()));

                boolean registroExitoso = proDAO.agregarProducto(producto);

                if (registroExitoso) {
                    JOptionPane.showMessageDialog(null, "Producto agregado con éxito");
                    ListarProductos();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar el producto");
                }
            }

        } while (camposIncompletos || claveAlternaRepetida);
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
    private javax.swing.JPanel pnlProductos;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable table1;
    private javax.swing.JTextPane txtNombre;
    // End of variables declaration//GEN-END:variables
}
