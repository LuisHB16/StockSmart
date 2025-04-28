package modules.Ventas;

import codigo.PDF;
import modules.Unidades.Unidad;
import modules.Unidades.Unidadd;
import modules.Ventas.detalleV;
import modules.Productos.productos;
import modules.Productos.productosd;
import modules.Productos.subproductosd;
import modules.Trabajadores.trabajadores;
import modules.Trabajadores.trabajadoresd;
import modules.Ventas.venta;
import modules.Ventas.ventad;
import modules.Inicio.FrameMenu;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modules.Productos.FrameBusquedaProductos;
import modules.Pedidos.FramePedidos;
import static codigo.FontLoader.customFont;
import static codigo.FontLoader.customFontBold;
import static codigo.FontLoader.customFontBold2;

public class FrameVentas extends javax.swing.JFrame {

    subproductosd da = new subproductosd();
    productosd proDAO = new productosd();
    productos pro = new productos();
    venta v = new venta();
    ventad vDAO = new ventad();
    Date fechaVenta = new Date();
    detalleV dv = new detalleV();
    PDF pdf = new PDF();
    private final ArrayList<productos> productosEncontrados = new ArrayList<>();
    private ArrayList<Unidad> listaUnidades = new ArrayList<>();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    double pago = 0.00;
    int cliente = 1;
    String Repartidor = "Sin Repartidor";
    String Status = "Pedido";
    DefaultTableModel tmp;
    String item = "Vendedor por defecto"; // Agregado para almacenar el vendedor
    int selectedRow;
    int rowIndex = -1;
    FrameMenu fmenu;
    FramePedidos fpedidos;

    public FrameVentas(FrameMenu Menu, FramePedidos fpedidos) {
        this.fmenu = Menu;
        initComponents();
        this.setLocationRelativeTo(null);
        lista();
        tmp = (DefaultTableModel) table1Venta.getModel();
        cargarVendedoresEnComboBox(boxVendedorVenta);
        table1Venta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table1Venta.getSelectedRow();
                if (selectedRow != -1) {
                    if (evt.getClickCount() == 1) {
                        String codigo = table1Venta.getValueAt(selectedRow, 0).toString();
                        buscarProducto2(codigo);
                    } else if (evt.getClickCount() == 2) {
                        String codigo = table1Venta.getValueAt(selectedRow, 0).toString();
                        buscarProducto2(codigo);
                        actualizarCantidadProducto(selectedRow);
                    }
                }
            }
        });
    }

    private void limpiarProductosEncontrados() {
        productosEncontrados.clear();
    }

    private void lista() {
        Unidadd unidadDAO = new Unidadd();
        listaUnidades = (ArrayList<Unidad>) unidadDAO.listarUnidades();

        // Ahora, la listaUnidades contiene tanto la unidad como el formato.
        for (Unidad unidad : listaUnidades) {
            String nombreUnidad = unidad.getUnidad();
            Double formato = unidad.getFormato();
            // Puedes hacer algo con 'nombreUnidad' y 'formato' aquí...
        }
    }

    public void LimpiarTable() {
        DefaultTableModel model = (DefaultTableModel) table1Venta.getModel();
        model.setRowCount(0);
    }

    private void eliminarProductoSeleccionado() {
        int selectedRowEliminar = table1Venta.getSelectedRow();
        if (selectedRowEliminar != -1) {
            DefaultTableModel model = (DefaultTableModel) table1Venta.getModel();
            model.removeRow(selectedRowEliminar);
            TotalPagar();
        }
    }

    private void cargarVendedoresEnComboBox(JComboBox<String> comboBox) {
        trabajadoresd prd = new trabajadoresd();
        List<trabajadores> vendedoresList = prd.listarVendedores();

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (trabajadores trabajador : vendedoresList) {
            comboBoxModel.addElement(trabajador.getNombre());
        }

        comboBox.setModel(comboBoxModel);
    }

    public void TotalPagar() {
        pago = 0.00;
        int numFila = table1Venta.getRowCount();
        for (int i = 0; i < numFila; i++) {
            double total = Double.parseDouble(String.valueOf(table1Venta.getValueAt(i, 5)));
            pago += total;
        }
        lblTotalVenta.setText(String.format("%.2f", pago));
    }

    private void actualizarCantidadProducto(int rowIndex) {
        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad:");
        if (cantidadStr != null) {
            try {
                double cantidad = Double.parseDouble(cantidadStr);
                String codigo = tmp.getValueAt(rowIndex, 0).toString();

                productos productoEncontrado = null;

                for (productos producto : productosEncontrados) {
                    if (producto.getClavealterna().equals(codigo)) {
                        productoEncontrado = producto;
                        break;
                    } else if (producto.getCodigo().equals(codigo)) {
                        productoEncontrado = producto;
                        break;
                    }
                }

                if (productoEncontrado.getGranel() != null && productoEncontrado.getGranel().trim().equalsIgnoreCase("si")) {
                    // Si es un producto a granel, permite decimales
                    // No es necesario redondear la cantidad
                } else {
                    if (cantidad % 1 != 0) {
                        JOptionPane.showMessageDialog(null, "Este producto no puede tener decimales en la cantidad.");
                        return;
                    }
                }

                if (cantidad > 0) {
                    // Actualizar la cantidad en la tabla
                    tmp.setValueAt(cantidad, rowIndex, 4);
                    // Calcular el nuevo total
                    double precioUnitario = (double) tmp.getValueAt(rowIndex, 2);
                    double total = precioUnitario * cantidad;
                    tmp.setValueAt(total, rowIndex, 5);
                    // Actualizar el total a pagar
                    TotalPagar();
                } else {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que 0.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida.");
            }
        }
    }

    private void agregarProducto(String codigo, String descripcion, double precioU, String granel, String unidad) {
        DefaultTableModel model = (DefaultTableModel) table1Venta.getModel();
        double cantidad = 1.0; // Puedes establecer la cantidad inicial como 1
        double total = precioU * cantidad;
        Object[] rowData = {codigo, descripcion, precioU, unidad, cantidad, total, granel};
        model.addRow(rowData);
        TotalPagar();
    }

    private void buscarProducto(String codigo) {
        pro = proDAO.Buscarprocodigo(codigo);
        if (pro.getNombre() == null) {
            pro = da.BuscarClave(codigo);
            byte[] imagen = pro.getImagen();
            String clavealterna = pro.getClavealterna();
            String nombre = pro.getNombre();
            Double precio = pro.getPrecio();
            String Granel = pro.getGranel();
            String Unidad = pro.getUnidad();
            pro = proDAO.Buscarprocodigo2(pro.getCodigo());
            pro.setClavealterna(clavealterna);
            pro.setNombre(nombre);
            pro.setPrecio(precio);
            pro.setGranel(Granel);
            pro.setUnidad(Unidad);
            pro.setImagen(imagen);
        }
        System.out.println(pro.getNombre());

        if (pro.getNombre() != null) {
            productosEncontrados.add(pro);
            agregarProducto(pro.getClavealterna(), pro.getNombre(), pro.getPrecio(), pro.getGranel(), pro.getUnidad());
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el artículo");
        }
    }

    private void buscarProducto2(String codigo) {
        productos productoEncontrado = null;

        for (productos producto : productosEncontrados) {
            if (producto.getClavealterna().equals(codigo)) {
                productoEncontrado = producto;
                break;
            } else if (producto.getCodigo().equals(codigo)) {
                productoEncontrado = producto;
                break;
            }
        }

        if (productoEncontrado != null) {
            byte[] imagenBytes = productoEncontrado.getImagen();
            if (imagenBytes != null && imagenBytes.length > 0) {
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

            Unidad formatoEncontrado = null;

            for (Unidad unidad : listaUnidades) {
                if (unidad.getUnidad().equals(productoEncontrado.getUnidad())) {
                    formatoEncontrado = unidad;
                    break;
                }
            }

            // Actualiza el nombre y la existencia del producto
            txtNombreInventario.setText(String.valueOf(productoEncontrado.getNombre()));
            if (formatoEncontrado != null) {
                double existenciaDivididaPorFormato = productoEncontrado.getExistencia() / formatoEncontrado.getFormato();

                // Formatea el resultado según tus necesidades, por ejemplo, limitando a 2 decimales:
                String resultadoFormateado = String.format("%.2f", existenciaDivididaPorFormato);

                txtExistenciaInventario.setText(resultadoFormateado);
            } else {
                txtExistenciaInventario.setText("Formato no encontrado"); // o maneja este caso de acuerdo a tus necesidades
            }
            txtCantidadInventario.setText(tmp.getValueAt(selectedRow, 4).toString());
            txtImporteInventario.setText(tmp.getValueAt(selectedRow, 5).toString());
        } else {
            lblFoto.setIcon(null);
            lblFoto.setText("No se encontró el artículo");
            // Limpia el nombre y la existencia si no se encontró el producto
            txtNombreInventario.setText("");
            txtExistenciaInventario.setText("");
        }
    }

    private void venta() {
        double monto = pago;
        v.setId_clientes(cliente);
        v.setFecha(fechaActual);
        v.setTotal(monto);
        v.setVendedor(item); // Establecer el vendedor seleccionado
        v.setRepartidor(Repartidor);
        v.setStatus(Status);
        vDAO.RegistrarVenta(v);
    }

    private void detalle() {
        int id = vDAO.idventas();
        String Vendedor = boxVendedorVenta.getSelectedItem().toString();

        for (int i = 0; i < table1Venta.getRowCount(); i++) {
            String codigo = table1Venta.getValueAt(i, 0).toString();
            Double cant = Double.valueOf(table1Venta.getValueAt(i, 4).toString());
            double precio = Double.parseDouble(table1Venta.getValueAt(i, 2).toString());
            String unidad = table1Venta.getValueAt(i, 3).toString(); // Obtener el valor de "Unidad"
            dv.setId_Ventas(id);
            dv.setCodigo(codigo);
            dv.setCantidad(cant);
            dv.setPrecio_U(precio);
            dv.setUnidad(unidad);
            vDAO.RegistrarDetalleVenta(dv);
        }
        pdf.pdfV(id, cliente, pago, item);
    }

    private void buscarProducto3(String codigo) {
        productos productoEncontrado = null;

        // 1. Null check for productosEncontrados
        if (productosEncontrados != null) {
            for (productos producto : productosEncontrados) {
                if (producto.getClavealterna().equals(codigo) || producto.getCodigo().equals(codigo)) {
                    productoEncontrado = producto;
                    break;
                }
            }
        }

        if (productoEncontrado != null) {
            byte[] imagenBytes = productoEncontrado.getImagen();
            if (imagenBytes != null && imagenBytes.length > 0) {
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

            // Actualiza el nombre y la existencia del producto
            txtNombreInventario.setText(String.valueOf(productoEncontrado.getNombre()));
            txtExistenciaInventario.setText(String.valueOf(productoEncontrado.getExistencia()));

            // 2. Table Index Check
            if (selectedRow >= 0 && selectedRow < tmp.getRowCount()) {
                txtCantidadInventario.setText(tmp.getValueAt(selectedRow, 4).toString());
                txtImporteInventario.setText(tmp.getValueAt(selectedRow, 5).toString());

                // 3. Object Comparison
                for (int i = 0; i < tmp.getRowCount(); i++) {
                    if (Objects.equals(tmp.getValueAt(i, 0), productoEncontrado.getClavealterna())) {
                        rowIndex = i;
                        break;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrame = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
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
        pnlVentas = new javax.swing.JPanel();
        tabVentas = new javax.swing.JTabbedPane();
        pnlVenta = new javax.swing.JPanel();
        lblVendedorVenta = new javax.swing.JLabel();
        scrollTable1Venta = new javax.swing.JScrollPane();
        table1Venta = new javax.swing.JTable();
        lblBarcodeVenta = new javax.swing.JLabel();
        txtBarcodeVenta = new javax.swing.JTextField();
        boxVendedorVenta = new javax.swing.JComboBox<>();
        btnCobrarVenta = new javax.swing.JButton();
        btnBuscarVenta = new javax.swing.JButton();
        btnEliminarVenta = new javax.swing.JButton();
        lblTotalVenta = new javax.swing.JLabel();
        pnlInventario = new javax.swing.JPanel();
        pnlInventario2 = new javax.swing.JPanel();
        lblInventario = new javax.swing.JLabel();
        lblExistenciaInventario = new javax.swing.JLabel();
        txtExistenciaInventario = new javax.swing.JTextField();
        pnlArticulo = new javax.swing.JPanel();
        txtCantidadInventario = new javax.swing.JTextField();
        lblCantidadInventario = new javax.swing.JLabel();
        lblArticuloInventario = new javax.swing.JLabel();
        txtImporteInventario = new javax.swing.JTextField();
        lblImporteInventario = new javax.swing.JLabel();
        txtNombreInventario = new javax.swing.JTextField();
        lblNombreInventario = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();
        lblPedido = new javax.swing.JLabel();
        btnPedido = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFrame.setBackground(new java.awt.Color(50, 130, 233));

        stockSmartLoginLabel.setBackground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setFont(customFontBold);
        stockSmartLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stockSmartLoginLabel.setText("StockSmart");

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

        jLabel1.setFont(customFont);
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/home-icon24.png"))); // NOI18N
        jLabel1.setText("Menu");
        jLabel1.setIconTextGap(10);
        btnMenuBack.add(jLabel1);
        jLabel1.setBounds(10, 0, 230, 50);

        pnlVentas.setBackground(new java.awt.Color(255, 255, 255));

        tabVentas.setForeground(new java.awt.Color(255, 255, 255));
        tabVentas.setFont(customFontBold2);

        pnlVenta.setBackground(new java.awt.Color(255, 255, 255));

        lblVendedorVenta.setFont(customFontBold2);
        lblVendedorVenta.setForeground(new java.awt.Color(0, 0, 0));
        lblVendedorVenta.setText("Vendedor");

        table1Venta.setFont(customFontBold2);
        table1Venta.setFont(customFontBold2);
        table1Venta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "PrecioU", "Unidad", "Cantidad", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1Venta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table1Venta.setDropMode(javax.swing.DropMode.INSERT_ROWS);
        table1Venta.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table1Venta.getTableHeader().setResizingAllowed(false);
        table1Venta.getTableHeader().setReorderingAllowed(false);
        scrollTable1Venta.setViewportView(table1Venta);

        lblBarcodeVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/images/codigobarras.png"))); // NOI18N

        txtBarcodeVenta.setFont(customFontBold2);
        txtBarcodeVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarcodeVentaKeyPressed(evt);
            }
        });

        boxVendedorVenta.setFont(customFontBold2       );
        boxVendedorVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnCobrarVenta.setBackground(new java.awt.Color(50, 130, 233));
        btnCobrarVenta.setFont(customFontBold2);
        btnCobrarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnCobrarVenta.setText("Cobrar");
        btnCobrarVenta.setBorderPainted(false);
        btnCobrarVenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCobrarVenta.setIconTextGap(10);
        btnCobrarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobrarVentaActionPerformed(evt);
            }
        });

        btnBuscarVenta.setBackground(new java.awt.Color(50, 130, 233));
        btnBuscarVenta.setFont(customFontBold2);
        btnBuscarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarVenta.setText("Buscar");
        btnBuscarVenta.setBorderPainted(false);
        btnBuscarVenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscarVenta.setIconTextGap(10);
        btnBuscarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarVentaActionPerformed(evt);
            }
        });

        btnEliminarVenta.setBackground(new java.awt.Color(50, 130, 233));
        btnEliminarVenta.setFont(customFontBold2);
        btnEliminarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarVenta.setText("Eliminar");
        btnEliminarVenta.setBorderPainted(false);
        btnEliminarVenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEliminarVenta.setIconTextGap(10);
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        lblTotalVenta.setFont(customFontBold2);
        lblTotalVenta.setForeground(new java.awt.Color(0, 0, 0));
        lblTotalVenta.setText("Total");

        javax.swing.GroupLayout pnlVentaLayout = new javax.swing.GroupLayout(pnlVenta);
        pnlVenta.setLayout(pnlVentaLayout);
        pnlVentaLayout.setHorizontalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVentaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(scrollTable1Venta)
                        .addGroup(pnlVentaLayout.createSequentialGroup()
                            .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(774, 774, 774)
                            .addComponent(btnCobrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlVentaLayout.createSequentialGroup()
                            .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblBarcodeVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblVendedorVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtBarcodeVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(boxVendedorVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(525, 525, 525)
                            .addComponent(btnBuscarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(82, 82, 82))
        );
        pnlVentaLayout.setVerticalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentaLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxVendedorVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendedorVenta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBarcodeVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBarcodeVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(scrollTable1Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalVenta)
                .addGap(21, 21, 21)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCobrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        tabVentas.addTab("Venta", pnlVenta);

        pnlInventario.setBackground(new java.awt.Color(255, 255, 255));

        pnlInventario2.setBackground(new java.awt.Color(255, 255, 255));
        pnlInventario2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(50, 130, 233)));
        pnlInventario2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblInventario.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblInventario.setForeground(new java.awt.Color(0, 0, 0));
        lblInventario.setText("Inventario");
        pnlInventario2.add(lblInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 130, 50));

        lblExistenciaInventario.setBackground(new java.awt.Color(204, 204, 204));
        lblExistenciaInventario.setFont(customFontBold2);
        lblExistenciaInventario.setForeground(new java.awt.Color(0, 0, 0));
        lblExistenciaInventario.setText("Existencia:");
        pnlInventario2.add(lblExistenciaInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 133, 30));

        txtExistenciaInventario.setEditable(false);
        txtExistenciaInventario.setFont(customFontBold2);
        txtExistenciaInventario.setText("                                       ");
        txtExistenciaInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtExistenciaInventario.setEnabled(false);
        pnlInventario2.add(txtExistenciaInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 410, 27));

        pnlArticulo.setBackground(new java.awt.Color(255, 255, 255));
        pnlArticulo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(50, 130, 233)));

        txtCantidadInventario.setBackground(new java.awt.Color(60, 63, 65));
        txtCantidadInventario.setFont(customFontBold2);
        txtCantidadInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCantidadInventario.setEnabled(false);

        lblCantidadInventario.setBackground(new java.awt.Color(202, 202, 202));
        lblCantidadInventario.setFont(customFontBold2);
        lblCantidadInventario.setForeground(new java.awt.Color(0, 0, 0));
        lblCantidadInventario.setText("Cantidad:");

        lblArticuloInventario.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblArticuloInventario.setForeground(new java.awt.Color(0, 0, 0));
        lblArticuloInventario.setText("Articulo");

        txtImporteInventario.setBackground(new java.awt.Color(60, 63, 65));
        txtImporteInventario.setFont(customFontBold2);
        txtImporteInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtImporteInventario.setEnabled(false);

        lblImporteInventario.setBackground(new java.awt.Color(202, 202, 202));
        lblImporteInventario.setFont(customFontBold2);
        lblImporteInventario.setForeground(new java.awt.Color(0, 0, 0));
        lblImporteInventario.setText("Importe:");

        txtNombreInventario.setEditable(false);
        txtNombreInventario.setFont(customFontBold2);
        txtNombreInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNombreInventario.setEnabled(false);

        lblNombreInventario.setBackground(new java.awt.Color(204, 204, 204));
        lblNombreInventario.setFont(customFontBold2);
        lblNombreInventario.setForeground(new java.awt.Color(0, 0, 0));
        lblNombreInventario.setText("Nombre:");

        javax.swing.GroupLayout pnlArticuloLayout = new javax.swing.GroupLayout(pnlArticulo);
        pnlArticulo.setLayout(pnlArticuloLayout);
        pnlArticuloLayout.setHorizontalGroup(
            pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArticuloLayout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(lblArticuloInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblImporteInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCantidadInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtImporteInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99))
        );
        pnlArticuloLayout.setVerticalGroup(
            pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArticuloLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlArticuloLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtImporteInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblImporteInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlArticuloLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblArticuloInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCantidadInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlInventarioLayout = new javax.swing.GroupLayout(pnlInventario);
        pnlInventario.setLayout(pnlInventarioLayout);
        pnlInventarioLayout.setHorizontalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlArticulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInventario2, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        pnlInventarioLayout.setVerticalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(pnlArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(pnlInventario2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        tabVentas.addTab("Inventario", pnlInventario);

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Ventas");

        lblFoto.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblFoto.setForeground(new java.awt.Color(204, 204, 204));
        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setText("FOTO");
        lblFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        lblPedido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPedido.setForeground(new java.awt.Color(0, 0, 0));
        lblPedido.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPedido.setText("Ir a pedido");

        btnPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/shopping-cart (1).png"))); // NOI18N
        btnPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPedidoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlVentasLayout = new javax.swing.GroupLayout(pnlVentas);
        pnlVentas.setLayout(pnlVentasLayout);
        pnlVentasLayout.setHorizontalGroup(
            pnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVentasLayout.createSequentialGroup()
                .addGap(0, 51, Short.MAX_VALUE)
                .addGroup(pnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVentasLayout.createSequentialGroup()
                        .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(257, 257, 257)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPedido, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                        .addGap(78, 78, 78))
                    .addComponent(tabVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 1058, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlVentasLayout.setVerticalGroup(
            pnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentasLayout.createSequentialGroup()
                .addGap(0, 25, Short.MAX_VALUE)
                .addGroup(pnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVentasLayout.createSequentialGroup()
                        .addGroup(pnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVentasLayout.createSequentialGroup()
                                .addComponent(lblTitulo)
                                .addGap(45, 45, 45))
                            .addComponent(btnPedido, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPedido)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVentasLayout.createSequentialGroup()
                        .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)))
                .addComponent(tabVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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
                .addComponent(pnlVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(358, 358, 358))
        );
        pnlFrameLayout.setVerticalGroup(
            pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFrameLayout.createSequentialGroup()
                .addGroup(pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFrameLayout.createSequentialGroup()
                        .addGroup(pnlFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFrameLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(logoLoginIcon))
                            .addGroup(pnlFrameLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMenuBack, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);

        getContentPane().add(pnlFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        // TODO add your handling code here:
        new FrameMenu().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    private void txtBarcodeVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarcodeVentaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txtBarcodeVenta.getText().isEmpty()) {
                String codigo = txtBarcodeVenta.getText().toUpperCase();

                for (int i = 0; i < tmp.getRowCount(); i++) {
                    if (tmp.getValueAt(i, 0).equals(codigo)) {
                        rowIndex = i;
                        break;
                    }
                }
                buscarProducto3(codigo);
                if (rowIndex != -1) {
                    double cantidad = (Double) tmp.getValueAt(rowIndex, 4) + 1.0;
                    double precio = Double.parseDouble(table1Venta.getValueAt(selectedRow, 2).toString());
                    tmp.setValueAt(cantidad, rowIndex, 4);
                    tmp.setValueAt(cantidad * precio, rowIndex, 5);
                } else {
                    buscarProducto(codigo);
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
                }
                txtBarcodeVenta.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el código del producto");
            }
        }
        rowIndex = -1;
    }//GEN-LAST:event_txtBarcodeVentaKeyPressed

    private void btnCobrarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarVentaActionPerformed
        // TODO add your handling code here:
        if (table1Venta.getRowCount() > 0) {
            String[] opcionesPago = {"Tarjeta", "Dinero"};
            int seleccion = JOptionPane.showOptionDialog(null, "Seleccione el método de pago", "Pago", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesPago, opcionesPago[0]);

            if (seleccion == 1) { // Si selecciona Dinero
                JTextField totalField = new JTextField(String.format("%.2f", pago));
                totalField.setEditable(false);

                JTextField dineroRecibidoField = new JTextField(20);
                JTextField cambioField = new JTextField(String.format("%.2f", -pago));
                cambioField.setEditable(false);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(4, 2));
                panel.add(new JLabel("Total a pagar:"));
                panel.add(totalField);
                panel.add(new JLabel("Dinero recibido:"));
                panel.add(dineroRecibidoField);
                panel.add(new JLabel("Cambio:"));
                panel.add(cambioField);

                // Agregar un DocumentListener al campo dineroRecibidoField
                dineroRecibidoField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        actualizarCambio();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        actualizarCambio();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        actualizarCambio();
                    }

                    private void actualizarCambio() {
                        try {
                            double dineroRecibido = Double.parseDouble(dineroRecibidoField.getText());
                            double cambio = dineroRecibido - pago;
                            cambioField.setText(String.format("%.2f", cambio));
                        } catch (NumberFormatException ex) {
                            cambioField.setText(String.format("%.2f", -pago)); // Si hay un error, se deja en blanco
                        }
                    }
                });

                Object[] options = {"Cobrar", "Cancelar"};
                int result = JOptionPane.showOptionDialog(null, panel, "Pago en efectivo", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (result == JOptionPane.YES_OPTION) {
                    // Validar si el dinero recibido es suficiente
                    try {
                        double dineroRecibido = Double.parseDouble(dineroRecibidoField.getText());
                        if (dineroRecibido >= pago) {
                            item = (String) boxVendedorVenta.getSelectedItem();
                            venta();
                            detalle();
                            LimpiarTable();
                            JOptionPane.showMessageDialog(null, "pedido realizado");
                            TotalPagar();
                            limpiarProductosEncontrados();

                        } else {
                            JOptionPane.showMessageDialog(null, "El dinero recibido no es suficiente para cubrir el total a pagar");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un monto válido para el dinero recibido");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "pedido cancelado");
                }
            } else if (seleccion == 0) {
                // Mostrar mensaje con el total a pagar en tarjeta
                JLabel totalLabel = new JLabel("Total a pagar con tarjeta: " + String.format("%.2f", pago));

                Object[] options = {"Cobrar", "Cancelar"};
                int result = JOptionPane.showOptionDialog(null, totalLabel, "Pago con tarjeta", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (result == JOptionPane.YES_OPTION) {
                    // Procesar la compra con tarjeta
                    item = (String) boxVendedorVenta.getSelectedItem();
                    venta();
                    detalle();
                    LimpiarTable();
                    JOptionPane.showMessageDialog(null, "pedido realizado con tarjeta");
                    TotalPagar();
                    limpiarProductosEncontrados();
                } else {
                    JOptionPane.showMessageDialog(null, "pedido cancelada");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay productos en el pedido");
        }
    }//GEN-LAST:event_btnCobrarVentaActionPerformed

    private void btnBuscarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarVentaActionPerformed
        // TODO add your handling code here:
        FrameBusquedaProductos ventanaBusqueda = new FrameBusquedaProductos();
        ventanaBusqueda.cargarProductos(proDAO.listarProductos2());
        ventanaBusqueda.setProductoSeleccionadoListener((String codigo) -> {
            // Lógica para agregar el producto seleccionado a la tabla de ventas
            int rowIndex1 = -1;
            for (int i = 0; i < tmp.getRowCount(); i++) {
                if (tmp.getValueAt(i, 0).equals(codigo)) {
                    rowIndex1 = i;
                    break;
                }
            }
            if (rowIndex1 != -1) {
                double cantidad = (Double) tmp.getValueAt(rowIndex1, 4) + 1.0;
                double precio = Double.parseDouble(table1Venta.getValueAt(selectedRow, 2).toString());
                tmp.setValueAt(cantidad, rowIndex1, 4);
                tmp.setValueAt(cantidad * precio, rowIndex1, 5);
            } else {
                buscarProducto(codigo);
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
            }
            ventanaBusqueda.dispose(); // Cierra la ventana de búsqueda
        });
        ventanaBusqueda.setVisible(true);
    }//GEN-LAST:event_btnBuscarVentaActionPerformed

    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        // TODO add your handling code here:
        eliminarProductoSeleccionado();
    }//GEN-LAST:event_btnEliminarVentaActionPerformed

    private void btnPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPedidoActionPerformed
        // TODO add your handling code here:
        if (fpedidos == null) {
            this.fpedidos = new FramePedidos(fmenu, this);
        }

        fpedidos.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnPedidoActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxVendedorVenta;
    private javax.swing.JButton btnBuscarVenta;
    private javax.swing.JButton btnCobrarVenta;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JButton btnPedido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblArticuloInventario;
    private javax.swing.JLabel lblBarcodeVenta;
    private javax.swing.JLabel lblCantidadInventario;
    private javax.swing.JLabel lblExistenciaInventario;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblImporteInventario;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblNombreInventario;
    private javax.swing.JLabel lblPedido;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalVenta;
    private javax.swing.JLabel lblVendedorVenta;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlArticulo;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JPanel pnlInventario;
    private javax.swing.JPanel pnlInventario2;
    private javax.swing.JPanel pnlVenta;
    private javax.swing.JPanel pnlVentas;
    private javax.swing.JScrollPane scrollTable1Venta;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTabbedPane tabVentas;
    private javax.swing.JTable table1Venta;
    private javax.swing.JTextField txtBarcodeVenta;
    private javax.swing.JTextField txtCantidadInventario;
    private javax.swing.JTextField txtExistenciaInventario;
    private javax.swing.JTextField txtImporteInventario;
    private javax.swing.JTextField txtNombreInventario;
    // End of variables declaration//GEN-END:variables
}
