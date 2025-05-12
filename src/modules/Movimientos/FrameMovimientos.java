package modules.Movimientos;


import java.text.ParseException;
import modules.Movimientos.movimiento;
import modules.Movimientos.movimientod;
import modules.Inicio.FrameMenu;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static codigo.FontLoader.customFont;
import static codigo.FontLoader.customFontBold;
import static codigo.FontLoader.customFontBold2;

public final class FrameMovimientos extends javax.swing.JFrame {

    private List<movimiento> listaMovimiento = new ArrayList<>();
    FrameMenu fmenu;

    public FrameMovimientos(FrameMenu Menu) {
        initComponents();
        
        this.setLocationRelativeTo(null);
        this.fmenu = Menu;
        
        ListarMovimientos();
        
        btnBuscar.addActionListener((ActionEvent e) -> {
            filtrarVentasPorFecha();
        });
        // Agregar ActionListener al CheckBox
        checkBoxMovimientos.addActionListener((ActionEvent e) -> {
            // Habilitar o deshabilitar el ComboBox según el estado del CheckBox
            boxMovimientos.setEnabled(checkBoxMovimientos.isSelected());
        });
        checkBoxCodigo.addActionListener((ActionEvent e) -> {
            // Habilitar o deshabilitar el campo de texto según el estado del CheckBox2
            txtCodigo.setEnabled(checkBoxCodigo.isSelected());
        });
    }

    public void filtrarVentasPorFecha() {
        // Obtén las fechas seleccionadas en los componentes de fecha
        Date fechaInicio = fechaInicial.getDate();
        Date fechaFin = fechaFinal.getDate();
        // Verifica que ambas fechas no sean nulas
        if (fechaInicio != null && fechaFin != null) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for (int i = 0; i < listaMovimiento.size(); i++) {
                String fechaString = listaMovimiento.get(i).getFehca();
                try {
                    Date fechaVenta = dateFormat.parse(fechaString);
                    boolean fechaValida = fechaVenta.compareTo(fechaInicio) >= 0 && fechaVenta.compareTo(fechaFin) <= 0;
                    if (fechaValida && ((checkBoxMovimientos.isSelected() && boxMovimientos.getSelectedItem().equals(listaMovimiento.get(i).getMovimiento()))
                            || (checkBoxCodigo.isSelected() && listaMovimiento.get(i).getCoidgo().startsWith(txtCodigo.getText()))
                            || (checkBoxMovimientos.isSelected() && checkBoxCodigo.isSelected() && boxMovimientos.getSelectedItem().equals(listaMovimiento.get(i).getMovimiento()) && listaMovimiento.get(i).getCoidgo().startsWith(txtCodigo.getText()))
                            || (!checkBoxMovimientos.isSelected() && !checkBoxCodigo.isSelected()))) {
                        Object[] ob = new Object[6];
                        ob[0] = listaMovimiento.get(i).getMovimiento();
                        ob[1] = listaMovimiento.get(i).getCoidgo();
                        ob[2] = listaMovimiento.get(i).getCantidad();
                        ob[3] = listaMovimiento.get(i).getInventario_antes();
                        ob[4] = listaMovimiento.get(i).getInventario_antes() + listaMovimiento.get(i).getCantidad();
                        ob[5] = listaMovimiento.get(i).getFehca();
                        model.addRow(ob);
                    }
                } catch (ParseException e) {
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione las fechas de inicio y fin.");
        }
    }

    public void ListarMovimientos() {
        if (listaMovimiento.isEmpty()) {
            movimientod proDAO = new movimientod();
            listaMovimiento = proDAO.Listardev();
        }
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        for (int i = 0; i < listaMovimiento.size(); i++) {
            Object[] ob = new Object[6];

            ob[0] = listaMovimiento.get(i).getMovimiento();
            ob[1] = listaMovimiento.get(i).getCoidgo();
            ob[2] = listaMovimiento.get(i).getCantidad();
            ob[3] = listaMovimiento.get(i).getInventario_antes();

            if (null == listaMovimiento.get(i).getMovimiento()) {
                ob[4] = listaMovimiento.get(i).getInventario_antes() + (listaMovimiento.get(i).getCantidad()*listaMovimiento.get(i).getFormato());
            } else // Utiliza equals para comparar cadenas en lugar de ==
            switch (listaMovimiento.get(i).getMovimiento()) {
                case "Devolucion":
                case "Alta":
                    ob[4] = listaMovimiento.get(i).getInventario_antes() + (listaMovimiento.get(i).getCantidad()*listaMovimiento.get(i).getFormato());
                    break;
                case "Venta":
                case "Pedido":
                    ob[4] = listaMovimiento.get(i).getInventario_antes() - (listaMovimiento.get(i).getCantidad()*listaMovimiento.get(i).getFormato());
                    break;
                default:
                    ob[4] = listaMovimiento.get(i).getInventario_antes() + (listaMovimiento.get(i).getCantidad()*listaMovimiento.get(i).getFormato());
                    break;
            }

            ob[5] = listaMovimiento.get(i).getFehca();
            model.addRow(ob);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrame = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
        pnlMovimientos = new javax.swing.JPanel();
        boxMovimientos = new javax.swing.JComboBox<>();
        lblFechaInicio = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        checkBoxMovimientos = new javax.swing.JCheckBox();
        lblCodigo = new javax.swing.JLabel();
        checkBoxCodigo = new javax.swing.JCheckBox();
        lblMovimientos = new javax.swing.JLabel();
        lblFechaFinal = new javax.swing.JLabel();
        fechaFinal = new com.toedter.calendar.JDateChooser();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        lblTitulo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
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

        pnlMovimientos.setBackground(new java.awt.Color(255, 255, 255));
        pnlMovimientos.setName(""); // NOI18N
        pnlMovimientos.setPreferredSize(new java.awt.Dimension(1675, 1000));

        boxMovimientos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alta", "Ajuste", "Venta", "Pedido", "Devolucion" }));
        boxMovimientos.setEnabled(false);

        lblFechaInicio.setFont(customFontBold2);
        lblFechaInicio.setText("Fecha de inicio");

        btnBuscar.setFont(customFontBold2);
        btnBuscar.setForeground(new java.awt.Color(50, 130, 233));
        btnBuscar.setText("Buscar");
        btnBuscar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscar.setIconTextGap(10);

        lblCodigo.setFont(customFontBold2);
        lblCodigo.setText("Codigo");

        lblMovimientos.setFont(customFontBold2);
        lblMovimientos.setText("Tipo de Movimeintos ");

        lblFechaFinal.setFont(customFontBold2);
        lblFechaFinal.setText("Fecha final");

        fechaFinal.setBackground(new java.awt.Color(255, 255, 255));
        fechaFinal.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N

        fechaInicial.setBackground(new java.awt.Color(255, 255, 255));
        fechaInicial.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Movimientos");

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Movimiento", "Codigo", "Cantidad", "Inventario antes", "Inventario despues", "Fecha"
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

        javax.swing.GroupLayout pnlMovimientosLayout = new javax.swing.GroupLayout(pnlMovimientos);
        pnlMovimientos.setLayout(pnlMovimientosLayout);
        pnlMovimientosLayout.setHorizontalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblFechaInicio)
                                    .addComponent(fechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                                    .addComponent(fechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblFechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                                .addComponent(checkBoxMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(boxMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(checkBoxCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCodigo)
                                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(367, 367, 367)
                                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlMovimientosLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(88, Short.MAX_VALUE))
            .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlMovimientosLayout.createSequentialGroup()
                    .addGap(112, 112, 112)
                    .addComponent(lblMovimientos)
                    .addContainerGap(1335, Short.MAX_VALUE)))
        );
        pnlMovimientosLayout.setVerticalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addComponent(lblFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(boxMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(checkBoxMovimientos)))
                            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblCodigo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(checkBoxCodigo)
                                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addComponent(scrollTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlMovimientosLayout.createSequentialGroup()
                    .addGap(115, 115, 115)
                    .addComponent(lblMovimientos)
                    .addContainerGap(608, Short.MAX_VALUE)))
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
                .addComponent(pnlMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 1175, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(pnlMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxMovimientos;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JCheckBox checkBoxCodigo;
    private javax.swing.JCheckBox checkBoxMovimientos;
    private com.toedter.calendar.JDateChooser fechaFinal;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblFechaFinal;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JLabel lblMovimientos;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JPanel pnlMovimientos;
    private javax.swing.JScrollPane scrollTable1;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable table1;
    private javax.swing.JTextField txtCodigo;
    // End of variables declaration//GEN-END:variables
}
