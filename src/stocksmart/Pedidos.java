package stocksmart;


import codigo.PDF;
import codigo.Unidad;
import codigo.Unidadd;
import codigo.cliented;
import codigo.clientes;
import codigo.detalleV;
import codigo.productos;
import codigo.productosd;
import codigo.subproductos;
import codigo.subproductosd;
import codigo.trabajadores;
import codigo.trabajadoresd;
import codigo.venta;
import codigo.ventad;
import inicio.FrameMenu;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import static stocksmart.FontLoader.customFont;
import static stocksmart.FontLoader.customFontBold;
import static stocksmart.FontLoader.customFontBold2;

public class Pedidos extends javax.swing.JFrame {

    subproductos pa = new subproductos();
    subproductosd da = new subproductosd();
    productosd proDAO = new productosd();
    productos pro = new productos();
    venta v = new venta();
    ventad vDAO = new ventad();
    Date fechaVenta = new Date();
    detalleV dv = new detalleV();
    PDF pdf = new PDF();
    private ArrayList<productos> productosEncontrados = new ArrayList<>();
    private ArrayList<Unidad> listaUnidades = new ArrayList<>();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    double pago = 0.00;
    int cliente = 0;
    String Repartidor = "Sin Repartidor";
    String Status = "Pedido";
    DefaultTableModel tmp;
    String item = "Vendedor por defecto"; // Agregado para almacenar el vendedor
    int selectedRow;
    int rowIndex = -1;

    public Pedidos() {
        initComponents();
        lista();
        tmp = (DefaultTableModel) table1Venta.getModel();
        
        cargarVendedoresEnComboBox(boxVendedorVenta);
        ListarClientes();

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
        int selectedRow = table1Venta.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table1Venta.getModel();
            model.removeRow(selectedRow);
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
                    ex.printStackTrace();
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

    private void pedido() {
        double monto = pago;
        String selectedValue = (String) boxClienteVenta.getSelectedItem();
        int idCliente = Integer.parseInt(selectedValue.split(" ")[0]);
        cliente = idCliente;
        v.setId_clientes(idCliente);
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
            Double cant = Double.parseDouble(table1Venta.getValueAt(i, 4).toString());
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

    private void filtrarComboBox(String filtroNombre) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        cliented cd = new cliented();
        List<clientes> listaClientes = cd.listarClientes();

        for (clientes cliente : listaClientes) {
            String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidop() + " " + cliente.getApellidom();
            if (!cliente.getId_C().equalsIgnoreCase("0") && nombreCompleto.contains(filtroNombre)) {
                model.addElement(cliente.getId_C() + " " + nombreCompleto);
            }
        }

        boxClienteVenta.setModel(model);
    }
    public void ListarClientes() {
        cliented cd = new cliented();
        List<clientes> listaClientes = cd.listarClientes();

        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) boxClienteVenta.getModel();
        model.removeAllElements();

        for (clientes cliente : listaClientes) {
            String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidop() + " " + cliente.getApellidom();
            String nombre = cliente.getNombre();
            if (!nombre.equalsIgnoreCase("Cliente Mostrador")) {
                model.addElement(cliente.getId_C() + " " + nombreCompleto);
            }
        }

        boxClienteVenta.setModel(model);
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
                    ex.printStackTrace();
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
        pnlPedidos = new javax.swing.JPanel();
        tabPedidos = new javax.swing.JTabbedPane();
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
        pnlCliente = new javax.swing.JPanel();
        lblRegistrar = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        lblNombreCliente = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        lblDireccionCliente = new javax.swing.JLabel();
        lblApellidoPCliente = new javax.swing.JLabel();
        txtApellidoPCliente = new javax.swing.JTextField();
        txtApellidoMCliente = new javax.swing.JTextField();
        lblApellidoMCliente = new javax.swing.JLabel();
        lblTelefonoCliente = new javax.swing.JLabel();
        txtTelefonoCliente = new javax.swing.JTextField();
        pnlVenta = new javax.swing.JPanel();
        lblVendedorVenta = new javax.swing.JLabel();
        scrollTable1Venta = new javax.swing.JScrollPane();
        table1Venta = new javax.swing.JTable();
        lblBarcodeVenta = new javax.swing.JLabel();
        txtBarcodeVenta = new javax.swing.JTextField();
        boxVendedorVenta = new javax.swing.JComboBox<>();
        boxClienteVenta = new javax.swing.JComboBox<>();
        lblClienteVenta = new javax.swing.JLabel();
        txtClienteVenta = new javax.swing.JTextField();
        btnCobrarVenta = new javax.swing.JButton();
        btnBuscarVenta = new javax.swing.JButton();
        btnClienteVenta = new javax.swing.JButton();
        btnEliminarVenta = new javax.swing.JButton();
        lblTotalVenta = new javax.swing.JLabel();
        lblO = new javax.swing.JLabel();
        lblSeleccionarCliente = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();
        lblVenta = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();
        btnItem = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        btnVenta = new javax.swing.JButton();

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

        pnlPedidos.setBackground(new java.awt.Color(255, 255, 255));

        pnlInventario.setBackground(new java.awt.Color(255, 255, 255));

        pnlInventario2.setBackground(new java.awt.Color(255, 255, 255));
        pnlInventario2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(50, 130, 233)));
        pnlInventario2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblInventario.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblInventario.setText("Inventario");
        pnlInventario2.add(lblInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 92, 50));

        lblExistenciaInventario.setBackground(new java.awt.Color(204, 204, 204));
        lblExistenciaInventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblExistenciaInventario.setText("Existencia:");
        pnlInventario2.add(lblExistenciaInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 80, 30));

        txtExistenciaInventario.setEditable(false);
        txtExistenciaInventario.setText("                                       ");
        txtExistenciaInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtExistenciaInventario.setEnabled(false);
        pnlInventario2.add(txtExistenciaInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 160, 27));

        pnlArticulo.setBackground(new java.awt.Color(255, 255, 255));
        pnlArticulo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(50, 130, 233)));

        txtCantidadInventario.setBackground(new java.awt.Color(242, 242, 242));
        txtCantidadInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCantidadInventario.setEnabled(false);
        txtCantidadInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadInventarioActionPerformed(evt);
            }
        });

        lblCantidadInventario.setBackground(new java.awt.Color(202, 202, 202));
        lblCantidadInventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblCantidadInventario.setText("Cantidad:");

        lblArticuloInventario.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblArticuloInventario.setText("Articulo");

        txtImporteInventario.setBackground(new java.awt.Color(242, 242, 242));
        txtImporteInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtImporteInventario.setEnabled(false);
        txtImporteInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteInventarioActionPerformed(evt);
            }
        });

        lblImporteInventario.setBackground(new java.awt.Color(202, 202, 202));
        lblImporteInventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblImporteInventario.setText("Importe:");

        txtNombreInventario.setEditable(false);
        txtNombreInventario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNombreInventario.setEnabled(false);
        txtNombreInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreInventarioActionPerformed(evt);
            }
        });

        lblNombreInventario.setBackground(new java.awt.Color(204, 204, 204));
        lblNombreInventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblNombreInventario.setText(" Nombre:");

        javax.swing.GroupLayout pnlArticuloLayout = new javax.swing.GroupLayout(pnlArticulo);
        pnlArticulo.setLayout(pnlArticuloLayout);
        pnlArticuloLayout.setHorizontalGroup(
            pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArticuloLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlArticuloLayout.createSequentialGroup()
                        .addComponent(lblNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(lblImporteInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtImporteInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(lblCantidadInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(txtCantidadInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblArticuloInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(175, Short.MAX_VALUE))
        );
        pnlArticuloLayout.setVerticalGroup(
            pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArticuloLayout.createSequentialGroup()
                .addComponent(lblArticuloInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblImporteInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImporteInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCantidadInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlInventarioLayout = new javax.swing.GroupLayout(pnlInventario);
        pnlInventario.setLayout(pnlInventarioLayout);
        pnlInventarioLayout.setHorizontalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInventario2, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        pnlInventarioLayout.setVerticalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(pnlArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82)
                .addComponent(pnlInventario2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        tabPedidos.addTab("Inventario", pnlInventario);

        lblRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblRegistrar.setText("Registrar Cliente");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        lblNombreCliente.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblNombreCliente.setText("Nombre");

        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
            }
        });

        txtDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteActionPerformed(evt);
            }
        });

        lblDireccionCliente.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblDireccionCliente.setText("Direccion:");

        lblApellidoPCliente.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblApellidoPCliente.setText("Apellido Paterno:");

        txtApellidoPCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoPClienteActionPerformed(evt);
            }
        });

        lblApellidoMCliente.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblApellidoMCliente.setText("Apellido Materno:");

        lblTelefonoCliente.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblTelefonoCliente.setText("Telefono:");

        txtTelefonoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlClienteLayout = new javax.swing.GroupLayout(pnlCliente);
        pnlCliente.setLayout(pnlClienteLayout);
        pnlClienteLayout.setHorizontalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClienteLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlClienteLayout.createSequentialGroup()
                        .addComponent(lblApellidoMCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtApellidoMCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlClienteLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlClienteLayout.createSequentialGroup()
                        .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegistrar)
                            .addGroup(pnlClienteLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(79, 79, 79)
                                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlClienteLayout.createSequentialGroup()
                                .addComponent(lblApellidoPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtApellidoPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(530, Short.MAX_VALUE))
        );
        pnlClienteLayout.setVerticalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClienteLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlClienteLayout.createSequentialGroup()
                        .addComponent(lblRegistrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlClienteLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(28, 28, 28)
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellidoPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidoPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellidoMCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidoMCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
        );

        tabPedidos.addTab("tab3", pnlCliente);

        pnlVenta.setBackground(new java.awt.Color(255, 255, 255));

        lblVendedorVenta.setFont(customFontBold2);
        lblVendedorVenta.setText("Vendedor");

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

        txtBarcodeVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarcodeVentaActionPerformed(evt);
            }
        });
        txtBarcodeVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarcodeVentaKeyPressed(evt);
            }
        });

        boxVendedorVenta.setFont(customFontBold2       );
        boxVendedorVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        boxClienteVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblClienteVenta.setText("Nombre del cliente");

        txtClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteVentaActionPerformed(evt);
            }
        });

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

        btnClienteVenta.setFont(customFontBold2);
        btnClienteVenta.setForeground(new java.awt.Color(50, 130, 233));
        btnClienteVenta.setText("Cliente");
        btnClienteVenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClienteVenta.setIconTextGap(10);
        btnClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteVentaActionPerformed(evt);
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

        lblTotalVenta.setText("Total");

        lblO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblO.setText("ó");

        lblSeleccionarCliente.setText("Seleccionar cliente");

        javax.swing.GroupLayout pnlVentaLayout = new javax.swing.GroupLayout(pnlVenta);
        pnlVenta.setLayout(pnlVentaLayout);
        pnlVentaLayout.setHorizontalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentaLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVentaLayout.createSequentialGroup()
                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlVentaLayout.createSequentialGroup()
                                .addComponent(lblClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblSeleccionarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(457, 457, 457))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVentaLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(204, 204, 204)
                                .addComponent(lblO, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(106, 106, 106)
                                .addComponent(lblTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(173, 173, 173))
                            .addGroup(pnlVentaLayout.createSequentialGroup()
                                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblBarcodeVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblVendedorVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBarcodeVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                    .addComponent(boxVendedorVenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCobrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(163, 163, 163))
                    .addGroup(pnlVentaLayout.createSequentialGroup()
                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnBuscarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(scrollTable1Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 1138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTable1Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVentaLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblClienteVenta)
                            .addComponent(lblSeleccionarCliente))
                        .addGap(18, 18, 18)
                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblO)
                            .addComponent(boxClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalVenta))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlVentaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCobrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );

        tabPedidos.addTab("Venta", pnlVenta);

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Pedidos");

        lblFoto.setText("Foto");

        lblVenta.setText("Venta");

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        btnItem.setText("Items");
        btnItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemActionPerformed(evt);
            }
        });

        btnClientes.setText("+ Cliente");
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });

        btnVenta.setText("lblVenta");
        btnVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPedidosLayout = new javax.swing.GroupLayout(pnlPedidos);
        pnlPedidos.setLayout(pnlPedidosLayout);
        pnlPedidosLayout.setHorizontalGroup(
            pnlPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPedidosLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(tabPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 1188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(148, Short.MAX_VALUE))
            .addGroup(pnlPedidosLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btnRegresar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClientes)
                .addGap(33, 33, 33)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        pnlPedidosLayout.setVerticalGroup(
            pnlPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPedidosLayout.createSequentialGroup()
                .addGroup(pnlPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPedidosLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(pnlPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTitulo)
                            .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlPedidosLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3)
                        .addComponent(lblVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPedidosLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnItem)
                            .addComponent(btnClientes)
                            .addComponent(btnRegresar))
                        .addGap(50, 50, 50)))
                .addComponent(tabPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addComponent(pnlPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(pnlPedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);

        getContentPane().add(pnlFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        // TODO add your handling code here:
        new FrameMenu().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    private void btnMenuBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseEntered
        // TODO add your handlings code here:

    }//GEN-LAST:event_btnMenuBackMouseEntered

    private void btnMenuBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_btnMenuBackMouseExited

    private void txtCantidadInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadInventarioActionPerformed

    private void txtImporteInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteInventarioActionPerformed

    private void txtNombreInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreInventarioActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String nombre = txtNombreCliente.getText();
        String apellidop = txtApellidoPCliente.getText();
        String apellidom = txtApellidoMCliente.getText();
        String direccion = txtDireccionCliente.getText();
        String telefonoStr = txtTelefonoCliente.getText().replaceAll("[^0-9]", ""); // Cambié 'fielD' a 'fielT' para el número de teléfono
        String correo = "Null"; // Añadí inicialización para evitar errores
        String contraseña = "Null"; // Añadí inicialización para evitar errores
        String status = "tienda"; // Añadí inicialización para evitar errores

        if (nombre.isEmpty() || telefonoStr.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ninguno de los campos puede estar vacío. Por favor, complete todos los campos.");
        } else if (telefonoStr.length() != 10) {
            JOptionPane.showMessageDialog(null, "El número de teléfono debe tener 10 dígitos.");
        } else {
            cliented pr = new cliented();

            if (pr.clienteYaExisteEnBD(nombre)) {
                JOptionPane.showMessageDialog(null, "El cliente ya existe en la base de datos. Por favor, introduzca un nombre diferente.");
            } else {
                clientes cl = new clientes(nombre, apellidop, apellidom, direccion, telefonoStr, correo, contraseña, status);

                boolean registroExitoso = pr.RegistrarCliente(cl);

                if (registroExitoso) {
                    JOptionPane.showMessageDialog(null, "Cliente registrado con éxito");
                    ListarClientes();

                    // Limpia los campos
                    txtNombreCliente.setText("");
                    txtApellidoPCliente.setText("");
                    txtApellidoMCliente.setText("");
                    txtDireccionCliente.setText("");
                    txtTelefonoCliente.setText("");

                    // Selecciona el cliente recién registrado en el ComboBox
                    String nombreCompleto = cl.getNombre() + " " + cl.getApellidop() + " " + cl.getApellidom();
                    String itemToSelect = cl.getId_C() + " " + nombreCompleto;
                    boxClienteVenta.setSelectedItem(itemToSelect);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar el cliente");
                }
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void txtDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteActionPerformed

    private void txtApellidoPClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoPClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoPClienteActionPerformed

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed

    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtBarcodeVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarcodeVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarcodeVentaActionPerformed

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
                        ex.printStackTrace();
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

    private void txtClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteVentaActionPerformed
        // TODO add your handling code here:
        btnClienteVentaActionPerformed(evt);
    }//GEN-LAST:event_txtClienteVentaActionPerformed

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
                            pedido();
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
                    pedido();
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
        VentanaBusquedaProductos ventanaBusqueda = new VentanaBusquedaProductos();
        ventanaBusqueda.cargarProductos(proDAO.listarProductos2());
        ventanaBusqueda.setProductoSeleccionadoListener(new VentanaBusquedaProductos.ProductoSeleccionadoListener() {
            @Override
            public void productoSeleccionado(String codigo) {
                // Lógica para agregar el producto seleccionado a la tabla de ventas
                int rowIndex = -1;
                for (int i = 0; i < tmp.getRowCount(); i++) {
                    if (tmp.getValueAt(i, 0).equals(codigo)) {
                        rowIndex = i;
                        break;
                    }
                }

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
                        ex.printStackTrace();
                        lblFoto.setIcon(null);
                        lblFoto.setText("Error al cargar la imagen.");
                    }
                } else {
                    lblFoto.setIcon(null);
                    lblFoto.setText("No hay imagen disponible.");
                }
            }
            ventanaBusqueda.dispose(); // Cierra la ventana de búsqueda
        }
        });
        ventanaBusqueda.setVisible(true);
    }//GEN-LAST:event_btnBuscarVentaActionPerformed

    private void btnClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteVentaActionPerformed
        // TODO add your handling code here:
        String nombre = txtClienteVenta.getText();
        filtrarComboBox(nombre);
    }//GEN-LAST:event_btnClienteVentaActionPerformed

    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        // TODO add your handling code here:
        eliminarProductoSeleccionado();
    }//GEN-LAST:event_btnEliminarVentaActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        // TODO add your handling code here:
        tabPedidos.setSelectedComponent(pnlVenta);
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemActionPerformed
        // TODO add your handling code here:
        tabPedidos.setSelectedComponent(pnlInventario);
    }//GEN-LAST:event_btnItemActionPerformed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        // TODO add your handling code here:
        tabPedidos.setSelectedComponent(pnlCliente);
    }//GEN-LAST:event_btnClientesActionPerformed

    private void btnVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaActionPerformed
        // TODO add your handling code here:
        ventasI p = new ventasI();
        p.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnVentaActionPerformed

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
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pedidos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxClienteVenta;
    private javax.swing.JComboBox<String> boxVendedorVenta;
    private javax.swing.JButton btnBuscarVenta;
    private javax.swing.JButton btnClienteVenta;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnCobrarVenta;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnItem;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblApellidoMCliente;
    private javax.swing.JLabel lblApellidoPCliente;
    private javax.swing.JLabel lblArticuloInventario;
    private javax.swing.JLabel lblBarcodeVenta;
    private javax.swing.JLabel lblCantidadInventario;
    private javax.swing.JLabel lblClienteVenta;
    private javax.swing.JLabel lblDireccionCliente;
    private javax.swing.JLabel lblExistenciaInventario;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblImporteInventario;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblNombreCliente;
    private javax.swing.JLabel lblNombreInventario;
    private javax.swing.JLabel lblO;
    private javax.swing.JLabel lblRegistrar;
    private javax.swing.JLabel lblSeleccionarCliente;
    private javax.swing.JLabel lblTelefonoCliente;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalVenta;
    private javax.swing.JLabel lblVendedorVenta;
    private javax.swing.JLabel lblVenta;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlArticulo;
    private javax.swing.JPanel pnlCliente;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JPanel pnlInventario;
    private javax.swing.JPanel pnlInventario2;
    private javax.swing.JPanel pnlPedidos;
    private javax.swing.JPanel pnlVenta;
    private javax.swing.JScrollPane scrollTable1Venta;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTabbedPane tabPedidos;
    private javax.swing.JTable table1Venta;
    private javax.swing.JTextField txtApellidoMCliente;
    private javax.swing.JTextField txtApellidoPCliente;
    private javax.swing.JTextField txtBarcodeVenta;
    private javax.swing.JTextField txtCantidadInventario;
    private javax.swing.JTextField txtClienteVenta;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtExistenciaInventario;
    private javax.swing.JTextField txtImporteInventario;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreInventario;
    private javax.swing.JTextField txtTelefonoCliente;
    // End of variables declaration//GEN-END:variables
}
