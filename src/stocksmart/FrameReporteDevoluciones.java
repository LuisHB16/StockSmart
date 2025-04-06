package stocksmart;

import codigo.PDF;
import java.text.ParseException;
import codigo.movimiento;
import codigo.movimientod;
import codigo.trabajadores;
import codigo.trabajadoresd;
import codigo.venta;
import codigo.ventad;
import inicio.FrameMenu;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static stocksmart.FontLoader.customFont;
import static stocksmart.FontLoader.customFontBold;
import static stocksmart.FontLoader.customFontBold2;

public final class FrameReporteDevoluciones extends javax.swing.JFrame {

    FrameMenu fmenu;

    private List<venta> listaVentas = new ArrayList<>();
    venta v = new venta();
    ventad vDAO = new ventad();
    PDF pdf = new PDF();

    public FrameReporteDevoluciones(FrameMenu Menu) {
        initComponents();

        this.setLocationRelativeTo(null);
        this.fmenu = Menu;
        cargarVendedoresEnComboBox(boxVendedor);
        ListarVentas();
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarVentasPorFecha();
            }
        });

        // Agregar ActionListener al CheckBox
        checkBoxVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Habilitar o deshabilitar el ComboBox según el estado del CheckBox
                boxVendedor.setEnabled(checkBoxVendedor.isSelected());
            }
        });

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

    public void filtrarVentasPorFecha() {
        // Obtén las fechas seleccionadas en los componentes de fecha
        Date fechaInicio = fechaInicial.getDate();
        Date fechaFin = fechaFinal.getDate();

        // Verifica que ambas fechas no sean nulas
        if (fechaInicio != null && fechaFin != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < listaVentas.size(); i++) {
                String fechaString = listaVentas.get(i).getFecha();
                try {
                    Date fechaVenta = dateFormat.parse(fechaString);
                    if (fechaVenta.compareTo(fechaInicio) >= 0 && fechaVenta.compareTo(fechaFin) <= 0) {
                        if (checkBoxVendedor.isSelected()) {
                            // Verificar si el CheckBox está seleccionado antes de filtrar por vendedor
                            if (boxVendedor.getSelectedItem().equals(listaVentas.get(i).getVendedor())) {
                                Object[] ob = new Object[5];
                                ob[0] = listaVentas.get(i).getId_Ventas();
                                ob[1] = listaVentas.get(i).getVendedor();
                                ob[2] = listaVentas.get(i).getRepartidor();
                                ob[3] = dateFormat.format(fechaVenta); // Formatear la fecha
                                ob[4] = listaVentas.get(i).getTotal();

                                model.addRow(ob);
                            }
                        } else {
                            // Si el CheckBox no está seleccionado, no se filtra por vendedor
                            Object[] ob = new Object[5];
                            ob[0] = listaVentas.get(i).getId_Ventas();
                            ob[1] = listaVentas.get(i).getVendedor();
                            ob[2] = listaVentas.get(i).getRepartidor();
                            ob[3] = dateFormat.format(fechaVenta); // Formatear la fecha
                            ob[4] = listaVentas.get(i).getTotal();

                            model.addRow(ob);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione las fechas de inicio y fin.");
        }
    }

    public void ListarVentas() {
        if (listaVentas.isEmpty()) {
            ventad proDAO = new ventad();
            listaVentas = proDAO.Listardev();
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (int i = 0; i < listaVentas.size(); i++) {
            Object[] ob = new Object[5];
            ob[0] = listaVentas.get(i).getId_Ventas();
            ob[1] = listaVentas.get(i).getVendedor();
            ob[2] = listaVentas.get(i).getRepartidor();
            ob[3] = listaVentas.get(i).getFecha();
            ob[4] = listaVentas.get(i).getTotal();

            model.addRow(ob);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrame = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
        pnlReporteDevoluciones = new javax.swing.JPanel();
        lblFechaInicio = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        lblVendedor = new javax.swing.JLabel();
        checkBoxVendedor = new javax.swing.JCheckBox();
        lblFechaFinal = new javax.swing.JLabel();
        fechaFinal = new com.toedter.calendar.JDateChooser();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        lblTitulo = new javax.swing.JLabel();
        btnGenerarPDF = new javax.swing.JButton();
        boxVendedor = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
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

        pnlReporteDevoluciones.setBackground(new java.awt.Color(255, 255, 255));
        pnlReporteDevoluciones.setName(""); // NOI18N
        pnlReporteDevoluciones.setPreferredSize(new java.awt.Dimension(1675, 1000));

        lblFechaInicio.setFont(customFontBold2);
        lblFechaInicio.setText("Fecha de inicio");

        btnBuscar.setFont(customFontBold2);
        btnBuscar.setForeground(new java.awt.Color(50, 130, 233));
        btnBuscar.setText("Buscar");
        btnBuscar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscar.setIconTextGap(10);

        lblVendedor.setFont(customFontBold2);
        lblVendedor.setText("Vendedor");

        lblFechaFinal.setFont(customFontBold2);
        lblFechaFinal.setText("Fecha final");

        fechaFinal.setBackground(new java.awt.Color(255, 255, 255));
        fechaFinal.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N

        fechaInicial.setBackground(new java.awt.Color(255, 255, 255));
        fechaInicial.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(50, 130, 233));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Reporte Devoluciones");

        btnGenerarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPDFActionPerformed(evt);
            }
        });

        boxVendedor.setEnabled(false);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Vendedor", "Repartidor", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout pnlReporteDevolucionesLayout = new javax.swing.GroupLayout(pnlReporteDevoluciones);
        pnlReporteDevoluciones.setLayout(pnlReporteDevolucionesLayout);
        pnlReporteDevolucionesLayout.setHorizontalGroup(
            pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlReporteDevolucionesLayout.createSequentialGroup()
                .addGroup(pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlReporteDevolucionesLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnGenerarPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblFechaInicio)
                                    .addComponent(fechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                                    .addComponent(fechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblFechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                                .addGap(188, 188, 188)
                                .addComponent(checkBoxVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                                        .addComponent(lblVendedor)
                                        .addGap(474, 474, 474))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlReporteDevolucionesLayout.createSequentialGroup()
                                        .addComponent(boxVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(327, 327, 327)))
                                .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))))
                .addGap(43, 43, 43))
        );
        pnlReporteDevolucionesLayout.setVerticalGroup(
            pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                        .addComponent(lblFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                                .addComponent(lblVendedor)
                                .addGroup(pnlReporteDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(checkBoxVendedor))
                                    .addGroup(pnlReporteDevolucionesLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(boxVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(btnGenerarPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(pnlReporteDevoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, 1175, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(pnlReporteDevoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila");
        } else {
            int idVenta = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            v = vDAO.BuscarVenta(idVenta);
            pdf.pdfV(idVenta, v.getId_clientes(), v.getTotal(), v.getVendedor());
        }
    }//GEN-LAST:event_btnGenerarPDFActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxVendedor;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGenerarPDF;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JCheckBox checkBoxVendedor;
    private com.toedter.calendar.JDateChooser fechaFinal;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFechaFinal;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVendedor;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel pnlFrame;
    private javax.swing.JPanel pnlReporteDevoluciones;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
