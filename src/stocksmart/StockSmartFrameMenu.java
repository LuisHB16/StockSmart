package stocksmart;

import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.AbstractBorder;

public class StockSmartFrameMenu extends javax.swing.JFrame {
        Font customFont = FontLoader.customFont;
        Font customFontBold = FontLoader.customFontBold;
        Font customFontBold2 = FontLoader.customFontBold2;
        Font customFontBold3 = FontLoader.customFontBold3;
        
        
    /**
     * Creates new form StockSmartFrameMenu
     */
    public StockSmartFrameMenu() {
        
        initComponents();
      
       this.setLocationRelativeTo(null);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLogin1 = new javax.swing.JPanel();
        stockSmartLoginLabel = new javax.swing.JLabel();
        panelLogin2 = new javax.swing.JPanel();
        botonVentas = new javax.swing.JButton();
        botonPedidos = new javax.swing.JButton();
        botonClientes = new javax.swing.JButton();
        botonProductos = new javax.swing.JButton();
        botonProveedores = new javax.swing.JButton();
        botonUsuarios = new javax.swing.JButton();
        botonReportePedidos = new javax.swing.JButton();
        botonReporteVentas = new javax.swing.JButton();
        botonConfiguracion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        logoLoginIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        panelLogin1.setBackground(new java.awt.Color(50, 130, 233));

        stockSmartLoginLabel.setBackground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setFont(customFontBold);
        stockSmartLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stockSmartLoginLabel.setText("StockSmart");

        panelLogin2.setBackground(new java.awt.Color(255, 255, 255));

        botonVentas.setBackground(new java.awt.Color(50, 130, 233));
        botonVentas.setFont(customFontBold3);
        botonVentas.setForeground(new java.awt.Color(255, 255, 255));
        botonVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/ventas-icon_google24.png"))); // NOI18N
        botonVentas.setText("Ventas");
        botonVentas.setBorder(null);
        botonVentas.setBorderPainted(false);
        botonVentas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonVentas.setIconTextGap(6);
        botonVentas.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonVentas.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/ventas-icon_google20.png"))); // NOI18N
        botonVentas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVentasActionPerformed(evt);
            }
        });

        botonPedidos.setBackground(new java.awt.Color(50, 130, 233));
        botonPedidos.setFont(customFontBold3);
        botonPedidos.setForeground(new java.awt.Color(255, 255, 255));
        botonPedidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/package-icon24.png"))); // NOI18N
        botonPedidos.setText("Pedidos");
        botonPedidos.setBorder(null);
        botonPedidos.setBorderPainted(false);
        botonPedidos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPedidos.setIconTextGap(6);
        botonPedidos.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonPedidos.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/package-icon20.png"))); // NOI18N
        botonPedidos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPedidosActionPerformed(evt);
            }
        });

        botonClientes.setBackground(new java.awt.Color(50, 130, 233));
        botonClientes.setFont(customFontBold3);
        botonClientes.setForeground(new java.awt.Color(255, 255, 255));
        botonClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/clientes-icon24.png"))); // NOI18N
        botonClientes.setText("Clientes");
        botonClientes.setBorder(null);
        botonClientes.setBorderPainted(false);
        botonClientes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonClientes.setIconTextGap(6);
        botonClientes.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonClientes.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/clientes-icon20.png"))); // NOI18N
        botonClientes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonClientesActionPerformed(evt);
            }
        });

        botonProductos.setBackground(new java.awt.Color(50, 130, 233));
        botonProductos.setFont(customFontBold3);
        botonProductos.setForeground(new java.awt.Color(255, 255, 255));
        botonProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/inventory-icon-24.png"))); // NOI18N
        botonProductos.setText("Productos");
        botonProductos.setBorder(null);
        botonProductos.setBorderPainted(false);
        botonProductos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonProductos.setIconTextGap(6);
        botonProductos.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonProductos.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/inventory-icon-20.png"))); // NOI18N
        botonProductos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonProductosActionPerformed(evt);
            }
        });

        botonProveedores.setBackground(new java.awt.Color(50, 130, 233));
        botonProveedores.setFont(customFontBold3);
        botonProveedores.setForeground(new java.awt.Color(255, 255, 255));
        botonProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/proveedores-icon24.png"))); // NOI18N
        botonProveedores.setText("Proveedores");
        botonProveedores.setBorder(null);
        botonProveedores.setBorderPainted(false);
        botonProveedores.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonProveedores.setIconTextGap(6);
        botonProveedores.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonProveedores.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/proveedores-icon20.png"))); // NOI18N
        botonProveedores.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonProveedoresActionPerformed(evt);
            }
        });

        botonUsuarios.setBackground(new java.awt.Color(50, 130, 233));
        botonUsuarios.setFont(customFontBold3);
        botonUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        botonUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/usuarios-icon24.png"))); // NOI18N
        botonUsuarios.setText("Usuarios");
        botonUsuarios.setBorder(null);
        botonUsuarios.setBorderPainted(false);
        botonUsuarios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonUsuarios.setIconTextGap(6);
        botonUsuarios.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonUsuarios.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/usuarios-icon20.png"))); // NOI18N
        botonUsuarios.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonUsuariosActionPerformed(evt);
            }
        });

        botonReportePedidos.setBackground(new java.awt.Color(50, 130, 233));
        botonReportePedidos.setFont(customFontBold3);
        botonReportePedidos.setForeground(new java.awt.Color(255, 255, 255));
        botonReportePedidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/reporteVentasPedidos24.png"))); // NOI18N
        botonReportePedidos.setText("Reporte Pedidos");
        botonReportePedidos.setBorder(null);
        botonReportePedidos.setBorderPainted(false);
        botonReportePedidos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonReportePedidos.setIconTextGap(6);
        botonReportePedidos.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonReportePedidos.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/reporteVentasPedidos20.png"))); // NOI18N
        botonReportePedidos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonReportePedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReportePedidosActionPerformed(evt);
            }
        });

        botonReporteVentas.setBackground(new java.awt.Color(50, 130, 233));
        botonReporteVentas.setFont(customFontBold3);
        botonReporteVentas.setForeground(new java.awt.Color(255, 255, 255));
        botonReporteVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/reporteVentasPedidos24.png"))); // NOI18N
        botonReporteVentas.setText("Reporte Ventas");
        botonReporteVentas.setBorder(null);
        botonReporteVentas.setBorderPainted(false);
        botonReporteVentas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonReporteVentas.setIconTextGap(6);
        botonReporteVentas.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonReporteVentas.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/reporteVentasPedidos20.png"))); // NOI18N
        botonReporteVentas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonReporteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReporteVentasActionPerformed(evt);
            }
        });

        botonConfiguracion.setBackground(new java.awt.Color(50, 130, 233));
        botonConfiguracion.setFont(customFontBold3);
        botonConfiguracion.setForeground(new java.awt.Color(255, 255, 255));
        botonConfiguracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/configuration-icon24.png"))); // NOI18N
        botonConfiguracion.setText("Configuracion");
        botonConfiguracion.setBorder(null);
        botonConfiguracion.setBorderPainted(false);
        botonConfiguracion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonConfiguracion.setIconTextGap(6);
        botonConfiguracion.setMargin(new java.awt.Insets(2, 14, 25, 14));
        botonConfiguracion.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/icons/configuration-icon20.png"))); // NOI18N
        botonConfiguracion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConfiguracionActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 130, 233));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Menú");

        javax.swing.GroupLayout panelLogin2Layout = new javax.swing.GroupLayout(panelLogin2);
        panelLogin2.setLayout(panelLogin2Layout);
        panelLogin2Layout.setHorizontalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin2Layout.createSequentialGroup()
                .addContainerGap(159, Short.MAX_VALUE)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addComponent(botonReportePedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(botonReporteVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(botonConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelLogin2Layout.createSequentialGroup()
                            .addComponent(botonProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(40, 40, 40)
                            .addComponent(botonProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(41, 41, 41)
                            .addComponent(botonUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelLogin2Layout.createSequentialGroup()
                            .addComponent(botonVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(40, 40, 40)
                            .addComponent(botonPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(41, 41, 41)
                            .addComponent(botonClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(162, 162, 162))
            .addGroup(panelLogin2Layout.createSequentialGroup()
                .addGap(433, 433, 433)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLogin2Layout.setVerticalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonReportePedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonReporteVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        logoLoginIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/images/logoStockSmart.png"))); // NOI18N

        javax.swing.GroupLayout panelLogin1Layout = new javax.swing.GroupLayout(panelLogin1);
        panelLogin1.setLayout(panelLogin1Layout);
        panelLogin1Layout.setHorizontalGroup(
            panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin1Layout.createSequentialGroup()
                .addGroup(panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLogin1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(panelLogin1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(logoLoginIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)))
                .addComponent(panelLogin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelLogin1Layout.setVerticalGroup(
            panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin1Layout.createSequentialGroup()
                .addContainerGap(264, Short.MAX_VALUE)
                .addComponent(logoLoginIcon)
                .addGap(31, 31, 31)
                .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133))
            .addComponent(panelLogin2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVentasActionPerformed
            try {
                // TODO add your handling code here:
                new StockSmartFrameVentas().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }//GEN-LAST:event_botonVentasActionPerformed

    private void botonPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPedidosActionPerformed
        // TODO add your handling code here:
          try {
                // TODO add your handling code here:
                new StockSmartFramePedidos().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_botonPedidosActionPerformed

    private void botonClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonClientesActionPerformed
        // TODO add your handling code here:
          try {
                // TODO add your handling code here:
                new StockSmartFrameClientes().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_botonClientesActionPerformed

    private void botonProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonProductosActionPerformed
        // TODO add your handling code here:
          try {
                // TODO add your handling code here:
                new StockSmartFrameProductos().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_botonProductosActionPerformed

    private void botonProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonProveedoresActionPerformed
        // TODO add your handling code here:
          try {
                // TODO add your handling code here:
                new StockSmartFrameProveedores().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_botonProveedoresActionPerformed

    private void botonUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonUsuariosActionPerformed
        // TODO add your handling code here:
          try {
                // TODO add your handling code here:
                new StockSmartFrameUsuarios().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_botonUsuariosActionPerformed

    private void botonReportePedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonReportePedidosActionPerformed
        // TODO add your handling code here:
          try {
                // TODO add your handling code here:
                new StockSmartFrameReportePedidos().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_botonReportePedidosActionPerformed

    private void botonReporteVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonReporteVentasActionPerformed
        // TODO add your handling code here:
         try {
                // TODO add your handling code here:
                new StockSmartFrameReporteVentas().setVisible(true);
                this.setVisible(false);
            } catch (SQLException ex) {
                Logger.getLogger(StockSmartFrameMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_botonReporteVentasActionPerformed

    private void botonConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConfiguracionActionPerformed
        // TODO add your handling code here:
            
        
    }//GEN-LAST:event_botonConfiguracionActionPerformed

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
            java.util.logging.Logger.getLogger(StockSmartFrameMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockSmartFrameMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonClientes;
    private javax.swing.JButton botonConfiguracion;
    private javax.swing.JButton botonPedidos;
    private javax.swing.JButton botonProductos;
    private javax.swing.JButton botonProveedores;
    private javax.swing.JButton botonReportePedidos;
    private javax.swing.JButton botonReporteVentas;
    private javax.swing.JButton botonUsuarios;
    private javax.swing.JButton botonVentas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel panelLogin1;
    private javax.swing.JPanel panelLogin2;
    private javax.swing.JLabel stockSmartLoginLabel;
    // End of variables declaration//GEN-END:variables
}
