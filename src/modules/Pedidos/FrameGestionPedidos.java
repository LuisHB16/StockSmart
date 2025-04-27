package modules.Pedidos;

import codigo.PDF;
import modules.Ventas.detalleV;
import modules.Movimientos.movimiento;
import modules.Movimientos.movimientod;
import modules.Productos.productos;
import modules.Productos.productosd;
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
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static codigo.FontLoader.customFont;
import static codigo.FontLoader.customFontBold;
import static codigo.FontLoader.customFontBold2;

public final class FrameGestionPedidos extends javax.swing.JFrame {

    FrameMenu fmenu;

    venta v = new venta();
    ventad vDAO = new ventad();
    PDF pdf = new PDF();
    movimientod m = new movimientod();
    detalleV detalleV = new detalleV();
    productosd p = new productosd();
    productos pro = new productos();
    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    Double inventario;
    private List<venta> listaProveedores = new ArrayList<>();

    public FrameGestionPedidos(FrameMenu Menu) {
        initComponents();

        this.setLocationRelativeTo(null);
        this.fmenu = Menu;

        ListarPedidos();
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (e.getClickCount() == 2) {
                    int idVenta = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());
                    mostrarDialogoCambiarEstado(idVenta);
                } else {
                    if (selectedRow != -1) {
                        if (!table1.getValueAt(selectedRow, 1).toString().equals("enviado")) {
                            int idVenta = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());
                            v = vDAO.BuscarVenta(idVenta);
                            pdf.pdfV(idVenta, v.getId_clientes(), v.getTotal(), v.getVendedor());

                            boolean exito = vDAO.actualizarEstado(idVenta, "enviado");
                            if (exito) {

                                // La actualización fue exitosa, puedes refrescar la tabla
                                ListarPedidos();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al actualizar el estado en la base de datos");
                            }
                        }
                    }
                }
            }
        });
// Asumiendo que boxEstado es un JComboBox<String>
        boxEstado.addActionListener((ActionEvent e) -> {
            filtrarTabla();
        });

    }
    
    private void filtrarTabla() {
    String filtroEstado = Objects.requireNonNull(boxEstado.getSelectedItem()).toString().toLowerCase();
    DefaultTableModel model = (DefaultTableModel) table1.getModel();
    model.setRowCount(0);

    for (venta venta : listaProveedores) {
        String estado = venta.getStatus().toLowerCase();

        // Mostrar todos si el filtro es "todos"
        if ("todos".equals(filtroEstado) || estado.contains(filtroEstado)) {
            Object[] ob = new Object[5];
            ob[0] = venta.getId_Ventas();
            ob[1] = venta.getStatus();
            ob[2] = venta.getRepartidor();
            ob[3] = venta.getFecha();
            ob[4] = venta.getTotal();
            model.addRow(ob);
        }
    }
}


    private void cargarRepartidoresEnComboBox(JComboBox<String> comboBox) {
        trabajadoresd prd = new trabajadoresd();
        List<trabajadores> vendedoresList = prd.listarRepartidores();

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (trabajadores trabajador : vendedoresList) {
            comboBoxModel.addElement(trabajador.getNombre());
        }

        comboBox.setModel(comboBoxModel);
    }

    private void mostrarDialogoCambiarEstado(int idVenta) {
        JComboBox<String> estadoComboBox = new JComboBox<>(new String[]{"Enviado", "Venta", "Cancelado"});
        JComboBox<String> vendedoresComboBox = new JComboBox<>();

        // Llena el segundo JComboBox con la lista de vendedores
        cargarRepartidoresEnComboBox(vendedoresComboBox);
        System.out.println(idVenta);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Estado:"));
        panel.add(estadoComboBox);
        panel.add(new JLabel("Vendedor:"));
        panel.add(vendedoresComboBox);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Cambiar Estado", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION) {
            String nuevoEstado = estadoComboBox.getSelectedItem().toString();
            String nuevoVendedor = vendedoresComboBox.getSelectedItem().toString();

            // Se actualiza el estado sin restringirlo a "Venta"
            
            
            System.out.println(nuevoEstado);
            
            if("Venta".equals(nuevoEstado)){
                 vDAO.ListarDetallesVenta(idVenta);
                for (detalleV detalle : vDAO.ListarDetallesVenta(idVenta)) {
                    pro = p.BuscarClave(detalle.getCodigo());
                    Double existencia = pro.getExistencia();
                    if(existencia == null){
                        String Codigo=detalle.getCodigo();
                        pro = p.Buscarprocodigo3(detalle.getCodigo());
                        pro = p.BuscarClave(pro.getCodigo());
                        existencia = pro.getExistencia();
                        movimiento mo = new movimiento("Pedido", fechaActual, detalle.getCantidad(), Codigo, pro.getExistencia());
                        m.agregarMovimiento(mo);
                    }else{
                        movimiento mo = new movimiento("Pedido", fechaActual, detalle.getCantidad(), detalle.getCodigo(), pro.getExistencia());
                        m.agregarMovimiento(mo);
                        
                    }
                }
            }
            
            boolean actualizacionExitosa = vDAO.actualizarEstado1(idVenta, nuevoEstado, nuevoVendedor);
            if (actualizacionExitosa) {
                JOptionPane.showMessageDialog(this, "Estado actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado.");
            }
            ListarPedidos(); // Se llama independientemente del estado para refrescar la lista
        }
    }

public void ListarPedidos() {
    ventad proDAO = new ventad();
    listaProveedores = proDAO.ListarPedido(); 
        
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Limpia la tabla antes de añadir las filas

        for (venta venta : listaProveedores) { // Usando un for-each para mayor claridad
            Object[] ob = new Object[5];
            ob[0] = venta.getId_Ventas();
            ob[1] = venta.getStatus();
            ob[2] = venta.getRepartidor();
            ob[3] = venta.getFecha();
            ob[4] = venta.getTotal();

            model.addRow(ob);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrame = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
        pnlGestionPedidos = new javax.swing.JPanel();
        Estado = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        btnGenerarPDF = new javax.swing.JButton();
        boxEstado = new javax.swing.JComboBox<>();
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
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFrame.setBackground(new java.awt.Color(50, 130, 233));

        stockSmartLoginLabel.setBackground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setFont(customFontBold);
        stockSmartLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stockSmartLoginLabel.setText("StockSmart");

        pnlGestionPedidos.setBackground(new java.awt.Color(255, 255, 255));
        pnlGestionPedidos.setName(""); // NOI18N
        pnlGestionPedidos.setPreferredSize(new java.awt.Dimension(1675, 1000));

        Estado.setFont(customFontBold2);
        Estado.setText("Estado");

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Gestion Pedidos");

        btnGenerarPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/pdf.png"))); // NOI18N
        btnGenerarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPDFActionPerformed(evt);
            }
        });

        boxEstado.setEnabled(true
        );

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Folio", "Estado", "Repartidor", "Fecha", "Total"
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

        javax.swing.GroupLayout pnlGestionPedidosLayout = new javax.swing.GroupLayout(pnlGestionPedidos);
        pnlGestionPedidos.setLayout(pnlGestionPedidosLayout);
        pnlGestionPedidosLayout.setHorizontalGroup(
            pnlGestionPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGestionPedidosLayout.createSequentialGroup()
                .addGroup(pnlGestionPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGestionPedidosLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnGenerarPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlGestionPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlGestionPedidosLayout.createSequentialGroup()
                                .addGap(223, 223, 223)
                                .addGroup(pnlGestionPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Estado)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGestionPedidosLayout.createSequentialGroup()
                                        .addComponent(boxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(221, 221, 221))))))
                    .addGroup(pnlGestionPedidosLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 1140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        pnlGestionPedidosLayout.setVerticalGroup(
            pnlGestionPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGestionPedidosLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlGestionPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGestionPedidosLayout.createSequentialGroup()
                        .addComponent(Estado)
                        .addGap(18, 18, 18)
                        .addComponent(boxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGenerarPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );

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
                .addComponent(pnlGestionPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 1175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
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
                    .addComponent(pnlGestionPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);

        getContentPane().add(pnlFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        // TODO add your handling code here:}
        this.fmenu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    private void btnGenerarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPDFActionPerformed
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila");
        } else {
            int idVenta = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());
            v = vDAO.BuscarVenta(idVenta);
            pdf.pdfV(idVenta, v.getId_clientes(), v.getTotal(), v.getVendedor());
        }
    }//GEN-LAST:event_btnGenerarPDFActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Estado;
    private javax.swing.JComboBox<String> boxEstado;
    private javax.swing.JButton btnGenerarPDF;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JPanel pnlGestionPedidos;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable table1;
    // End of variables declaration//GEN-END:variables
}
