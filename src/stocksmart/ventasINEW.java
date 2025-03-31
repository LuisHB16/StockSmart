package stocksmart;


import codigo.PDF;
import codigo.Unidad;
import codigo.Unidadd;
import codigo.detalleV;
import codigo.movimiento;
import codigo.movimientod;
import codigo.productos;
import codigo.productosd;
import codigo.subproductos;
import codigo.subproductosd;
import codigo.trabajadores;
import codigo.trabajadoresd;
import codigo.ventad;
import codigo.venta;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

public class ventasINEW extends javax.swing.JFrame {

    
    subproductosd da = new subproductosd();
    productosd proDAO = new productosd();
    productos pro = new productos();
    venta v = new venta();
    ventad vDAO = new ventad();
    Date fechaVenta = new Date();
    detalleV dv = new detalleV();
    PDF pdf = new PDF();
    movimientod m = new movimientod();
    private ArrayList<productos> productosEncontrados = new ArrayList<>();
    private ArrayList<Unidad> listaUnidades = new ArrayList<>();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    double pago = 0.00;
    int cliente = 0;
    String Repartidor = "Sin Repartidor";
    String Status = "Venta";
    DefaultTableModel tmp;
    String item = "Vendedor por defecto"; // Agregado para almacenar el vendedor
    int selectedRow;
    int rowIndex = -1;

    public ventasINEW() {
        initComponents();
        lista();
        tmp = (DefaultTableModel) table.getModel();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        cargarVendedoresEnComboBox(boxVendedor);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    if (evt.getClickCount() == 1) {
                        String codigo = table.getValueAt(selectedRow, 0).toString();
                        buscarProducto2(codigo);
                    } else if (evt.getClickCount() == 2) {
                        String codigo = table.getValueAt(selectedRow, 0).toString();
                        buscarProducto2(codigo);
                        actualizarCantidadProducto(selectedRow);
                    }
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

    private void limpiarProductosEncontrados() {
        productosEncontrados.clear();
    }

    public void LimpiarTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    private void eliminarProductoSeleccionado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
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
        int numFila = table.getRowCount();
        for (int i = 0; i < numFila; i++) {
            double total = Double.parseDouble(String.valueOf(table.getValueAt(i, 5)));
            pago += total;
        }
        Total.setText(String.format("%.2f", pago));
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
        DefaultTableModel model = (DefaultTableModel) table.getModel();
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
            fielNombre.setText(String.valueOf(productoEncontrado.getNombre()));
            if (formatoEncontrado != null) {
                double existenciaDivididaPorFormato = productoEncontrado.getExistencia() / formatoEncontrado.getFormato();

                // Formatea el resultado según tus necesidades, por ejemplo, limitando a 2 decimales:
                String resultadoFormateado = String.format("%.2f", existenciaDivididaPorFormato);

                fielexistencia.setText(resultadoFormateado);
            } else {
                fielexistencia.setText("Formato no encontrado"); // o maneja este caso de acuerdo a tus necesidades
            }
            txtCantidad1.setText(tmp.getValueAt(selectedRow, 4).toString());
            txtCantidad.setText(tmp.getValueAt(selectedRow, 5).toString());
        } else {
            lblFoto.setIcon(null);
            lblFoto.setText("No se encontró el artículo");
            // Limpia el nombre y la existencia si no se encontró el producto
            fielNombre.setText("");
            fielexistencia.setText("");
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
        String Vendedor = boxVendedor.getSelectedItem().toString();

        for (int i = 0; i < table.getRowCount(); i++) {
            String codigo = table.getValueAt(i, 0).toString();
            Double cant = Double.valueOf(table.getValueAt(i, 4).toString());
            double precio = Double.parseDouble(table.getValueAt(i, 2).toString());
            String unidad = table.getValueAt(i, 3).toString(); // Obtener el valor de "Unidad"
            Double inventarioDetalle = null;
            dv.setId_Ventas(id);
            dv.setCodigo(codigo);
            dv.setCantidad(cant);
            dv.setPrecio_U(precio);
            dv.setUnidad(unidad);
            for (productos producto : productosEncontrados) {
                if (producto.getClavealterna().equals(codigo)) {
                    inventarioDetalle = producto.getExistencia();
                    break;
                } else if (producto.getCodigo().equals(codigo)) {
                    inventarioDetalle = producto.getExistencia();
                    break;
                }
            }
            movimiento mo = new movimiento("Venta", fechaActual, cant, codigo, inventarioDetalle);
            m.agregarMovimiento(mo);
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
                    ex.printStackTrace();
                    lblFoto.setIcon(null);
                    lblFoto.setText("Error al cargar la imagen.");
                }
            } else {
                lblFoto.setIcon(null);
                lblFoto.setText("No hay imagen disponible.");
            }

            // Actualiza el nombre y la existencia del producto
            fielNombre.setText(String.valueOf(productoEncontrado.getNombre()));
            fielexistencia.setText(String.valueOf(productoEncontrado.getExistencia()));

            // 2. Table Index Check
            if (selectedRow >= 0 && selectedRow < tmp.getRowCount()) {
                txtCantidad1.setText(tmp.getValueAt(selectedRow, 4).toString());
                txtCantidad.setText(tmp.getValueAt(selectedRow, 5).toString());

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

        lblFoto = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnatras = new javax.swing.JButton();
        btnItem = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        venta = new javax.swing.JPanel();
        btnborrar = new java.awt.Button();
        btnCobrar = new java.awt.Button();
        Total = new java.awt.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        boxVendedor = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        inventario = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        txtnombre = new javax.swing.JLabel();
        fielNombre = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCantidad1 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        txtnombre2 = new javax.swing.JLabel();
        fielexistencia = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        a = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFoto.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setText("Foto");
        lblFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFotoMouseClicked(evt);
            }
        });
        getContentPane().add(lblFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 180, 150));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.13 PM.jpeg"))); // NOI18N
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btnatras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.13 PM.jpeg"))); // NOI18N
        btnatras.setBorder(null);
        btnatras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnatras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnatrasActionPerformed(evt);
            }
        });
        getContentPane().add(btnatras, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 120, 80, -1));

        btnItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/WhatsApp Image 2023-05-10 at 11.43.14 PM.jpeg"))); // NOI18N
        btnItem.setBorder(null);
        btnItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemActionPerformed(evt);
            }
        });
        getContentPane().add(btnItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Pedido");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 140, -1, -1));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        venta.setBackground(new java.awt.Color(255, 255, 255));
        venta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnborrar.setActionCommand("btnEliminar");
        btnborrar.setBackground(new java.awt.Color(255, 255, 255));
        btnborrar.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnborrar.setLabel("Eliminar (suprimir)");
        btnborrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnborrarActionPerformed(evt);
            }
        });
        venta.add(btnborrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 450, 180, 30));

        btnCobrar.setActionCommand("btnCobrar");
        btnCobrar.setBackground(new java.awt.Color(255, 255, 255));
        btnCobrar.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnCobrar.setLabel("Cobrar");
        btnCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobrarActionPerformed(evt);
            }
        });
        venta.add(btnCobrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 440, 130, 40));

        Total.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        Total.setText("total:");
        venta.add(Total, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 440, 90, 30));

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table.setDropMode(javax.swing.DropMode.INSERT_ROWS);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setResizable(false);
            table.getColumnModel().getColumn(5).setResizable(false);
        }

        venta.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 1070, 360));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/r (1).png"))); // NOI18N
        venta.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 60, -1));

        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });
        venta.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 210, 30));

        boxVendedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        boxVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxVendedorActionPerformed(evt);
            }
        });
        venta.add(boxVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 180, 30));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/lupa.png"))); // NOI18N
        btnBuscar.setBorder(null);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        venta.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 70, 30));

        jTabbedPane1.addTab("", venta);

        inventario.setBackground(new java.awt.Color(255, 255, 255));
        inventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(244, 244, 244));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtnombre.setBackground(new java.awt.Color(204, 204, 204));
        txtnombre.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtnombre.setText(" Nombre:");
        jPanel7.add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 80, 30));

        fielNombre.setEditable(false);
        fielNombre.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        fielNombre.setEnabled(false);
        fielNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fielNombreActionPerformed(evt);
            }
        });
        jPanel7.add(fielNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 160, 27));

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 330, 30));

        jPanel8.setBackground(new java.awt.Color(244, 244, 244));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(202, 202, 202));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Importe:");
        jPanel8.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 80, 30));

        txtCantidad.setBackground(new java.awt.Color(242, 242, 242));
        txtCantidad.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCantidad.setEnabled(false);
        jPanel8.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 160, 27));

        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 70, 330, 30));

        jPanel5.setBackground(java.awt.Color.lightGray);
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel2.setText("Articulo");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 92, 50));

        jPanel6.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, -1));

        jPanel9.setBackground(new java.awt.Color(244, 244, 244));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBackground(new java.awt.Color(202, 202, 202));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Cantidad:");
        jPanel9.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 80, 30));

        txtCantidad1.setBackground(new java.awt.Color(242, 242, 242));
        txtCantidad1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCantidad1.setEnabled(false);
        jPanel9.add(txtCantidad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 160, 27));

        jPanel6.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, 330, 30));

        inventario.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1100, 130));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(java.awt.Color.lightGray);
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setText("Inventario");
        jPanel11.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 92, 50));

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 50));

        jPanel12.setBackground(new java.awt.Color(244, 244, 244));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtnombre2.setBackground(new java.awt.Color(204, 204, 204));
        txtnombre2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtnombre2.setText("Existencia:");
        jPanel12.add(txtnombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 80, 30));

        fielexistencia.setEditable(false);
        fielexistencia.setText("                                       ");
        fielexistencia.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        fielexistencia.setEnabled(false);
        jPanel12.add(fielexistencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 160, 27));

        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 330, 30));

        inventario.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 1100, 120));

        jTabbedPane1.addTab("", inventario);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 1120, 580));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/pedido.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 30, 140, 100));

        a.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout aLayout = new javax.swing.GroupLayout(a);
        a.setLayout(aLayout);
        aLayout.setHorizontalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1940, Short.MAX_VALUE)
        );
        aLayout.setVerticalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1120, Short.MAX_VALUE)
        );

        getContentPane().add(a, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -40, 1940, 1120));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed

    }//GEN-LAST:event_txtCodigoActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txtCodigo.getText().isEmpty()) {
                String codigo = txtCodigo.getText().toUpperCase();

                for (int i = 0; i < tmp.getRowCount(); i++) {
                    if (tmp.getValueAt(i, 0).equals(codigo)) {
                        rowIndex = i;
                        break;
                    }
                }
                buscarProducto3(codigo);
                if (rowIndex != -1) {
                    double cantidad = (Double) tmp.getValueAt(rowIndex, 4) + 1.0;
                    double precio = Double.parseDouble(table.getValueAt(selectedRow, 2).toString());
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
                txtCodigo.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el código del producto");
            }
        }
        rowIndex = -1;
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void btnborrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnborrarActionPerformed
        eliminarProductoSeleccionado();
    }//GEN-LAST:event_btnborrarActionPerformed

    private void btnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarActionPerformed
        if (table.getRowCount() > 0) {
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
                            item = (String) boxVendedor.getSelectedItem();
                            venta();
                            detalle();
                            LimpiarTable();
                            JOptionPane.showMessageDialog(null, "Compra realizada");
                            TotalPagar();
                            limpiarProductosEncontrados();

                        } else {
                            JOptionPane.showMessageDialog(null, "El dinero recibido no es suficiente para cubrir el total a pagar");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un monto válido para el dinero recibido");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Compra cancelada");
                }
            } else if (seleccion == 0) {
                // Mostrar mensaje con el total a pagar en tarjeta
                JLabel totalLabel = new JLabel("Total a pagar con tarjeta: " + String.format("%.2f", pago));

                Object[] options = {"Cobrar", "Cancelar"};
                int result = JOptionPane.showOptionDialog(null, totalLabel, "Pago con tarjeta", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (result == JOptionPane.YES_OPTION) {
                    // Procesar la compra con tarjeta
                    item = (String) boxVendedor.getSelectedItem();
                    venta();
                    detalle();
                    LimpiarTable();
                    JOptionPane.showMessageDialog(null, "Compra realizada con tarjeta");
                    TotalPagar();
                    limpiarProductosEncontrados();
                } else {
                    JOptionPane.showMessageDialog(null, "Compra cancelada");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay productos en la venta");
        }
    }//GEN-LAST:event_btnCobrarActionPerformed

    private void lblFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFotoMouseClicked

    }//GEN-LAST:event_lblFotoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Pedidos p = new Pedidos();
        p.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void boxVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxVendedorActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnatrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnatrasActionPerformed
        jTabbedPane1.setSelectedComponent(venta);
    }//GEN-LAST:event_btnatrasActionPerformed

    private void btnItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemActionPerformed
        jTabbedPane1.setSelectedComponent(inventario);
    }//GEN-LAST:event_btnItemActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
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
                    double precio = Double.parseDouble(table.getValueAt(selectedRow, 2).toString());
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
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void fielNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fielNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fielNombreActionPerformed

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
            java.util.logging.Logger.getLogger(ventasINEW.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventasINEW.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventasINEW.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventasINEW.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ventasINEW().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label Total;
    private javax.swing.JPanel a;
    private javax.swing.JComboBox<String> boxVendedor;
    private javax.swing.JButton btnBuscar;
    private java.awt.Button btnCobrar;
    private javax.swing.JButton btnItem;
    private javax.swing.JButton btnatras;
    private java.awt.Button btnborrar;
    private javax.swing.JTextField fielNombre;
    private javax.swing.JTextField fielexistencia;
    private javax.swing.JPanel inventario;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCantidad1;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JLabel txtnombre;
    private javax.swing.JLabel txtnombre2;
    private javax.swing.JPanel venta;
    // End of variables declaration//GEN-END:variables
}
