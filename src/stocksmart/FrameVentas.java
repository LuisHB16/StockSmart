package stocksmart;

import inicio.FrameMenu;
import assests.icons.clases.PlusIcon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FrameVentas extends javax.swing.JFrame {
    
    ConnectionDB connectionDB = null;
    FrameMenu fmenu = null;
    Font customFont = FontLoader.customFont;
    Font customFontBold = FontLoader.customFontBold;
    Font customFontBold2 = FontLoader.customFontBold2;
   
    /**
     * Creates new form FrameVentas
     */
    public FrameVentas( FrameMenu fmenu) throws SQLException {
        
        initComponents();
        this.fmenu = fmenu;
        this.setLocationRelativeTo(null);
        connectionDB();
        
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaVentas = new JTable();
        lblVendedor = new javax.swing.JLabel();
        cbxVendedor = new javax.swing.JComboBox<>();
        lblCodigoBarras = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtCodigoBarras = new javax.swing.JTextPane();
        btnNuevoItem = new javax.swing.JButton();
        btnCobrar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
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
        setResizable(false);

        panelLogin1.setBackground(new java.awt.Color(50, 130, 233));

        stockSmartLoginLabel.setBackground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setFont(customFontBold);
        stockSmartLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockSmartLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stockSmartLoginLabel.setText("StockSmart");

        panelLogin2.setBackground(new java.awt.Color(255, 255, 255));

        tablaVentas.setAutoCreateRowSorter(true);
        tablaVentas.setFont(customFontBold2);
        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {"Id_Ventas", "Id_Clientes", "Fecha", "Total", "Id_Vendedor", "Id_Repartidor", "Status"}

        ));
        tablaVentas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaVentas);

        lblVendedor.setFont(customFontBold2);
        lblVendedor.setText("Vendedor");

        cbxVendedor.setFont(customFontBold2       );
        cbxVendedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblCodigoBarras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/images/codigobarras.png"))); // NOI18N

        txtCodigoBarras.setFont(customFontBold2    );
        jScrollPane2.setViewportView(txtCodigoBarras);

        btnNuevoItem.setBackground(new java.awt.Color(50, 130, 233));
        btnNuevoItem.setFont(customFontBold2);
        btnNuevoItem.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevoItem.setText("Item");
        btnNuevoItem.setBorderPainted(false);
        btnNuevoItem.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNuevoItem.setIconTextGap(10);
        btnNuevoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoItemActionPerformed(evt);
            }
        });

        btnCobrar.setBackground(new java.awt.Color(50, 130, 233));
        btnCobrar.setFont(customFontBold2);
        btnCobrar.setForeground(new java.awt.Color(255, 255, 255));
        btnCobrar.setText("Cobrar");
        btnCobrar.setBorderPainted(false);
        btnCobrar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCobrar.setIconTextGap(10);
        btnCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobrarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(50, 130, 233));
        btnEliminar.setFont(customFontBold2);
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEliminar.setIconTextGap(10);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLogin2Layout = new javax.swing.GroupLayout(panelLogin2);
        panelLogin2.setLayout(panelLogin2Layout);
        panelLogin2Layout.setHorizontalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin2Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelLogin2Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNuevoItem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin2Layout.createSequentialGroup()
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(73, 73, 73))
        );
        panelLogin2Layout.setVerticalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin2Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVendedor)
                    .addComponent(cbxVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addComponent(lblCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNuevoItem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        btnNuevoItem.setIcon(new PlusIcon(18, 18, Color.WHITE));

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

        javax.swing.GroupLayout panelLogin1Layout = new javax.swing.GroupLayout(panelLogin1);
        panelLogin1.setLayout(panelLogin1Layout);
        panelLogin1Layout.setHorizontalGroup(
            panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLogin1Layout.createSequentialGroup()
                        .addComponent(logoLoginIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnMenuBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelLogin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelLogin1Layout.setVerticalGroup(
            panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin1Layout.createSequentialGroup()
                .addComponent(panelLogin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelLogin1Layout.createSequentialGroup()
                .addGroup(panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLogin1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(logoLoginIcon))
                    .addGroup(panelLogin1Layout.createSequentialGroup()
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
            .addComponent(panelLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void connectionDB() throws SQLException {
        
        connectionDB = new ConnectionDB();
        Connection connection = connectionDB.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT Id_Ventas, Id_Clientes, Fecha, Total, Id_Vendedor, Id_Repartidor, Status FROM venta");
        DefaultTableModel tablaVentas1 = (DefaultTableModel) tablaVentas.getModel();
        
        while(result.next()){
            
            int idVentas = result.getInt("Id_Ventas");
            int idClientes = result.getInt("Id_Clientes");
            String fecha = result.getString("Fecha");
            double total = result.getDouble("Total");
            String idVendedor = result.getString("Id_Vendedor");
            int idRepartidor = result.getInt("Id_Repartidor");
            String status = result.getString("Status");

            tablaVentas1.addRow(new Object[]{idVentas, idClientes, fecha, total, idVendedor, idRepartidor, status});
        
        }
        
        result.close();
        statement.close();
        
    }
    
    private void btnNuevoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoItemActionPerformed

    private void btnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCobrarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        
        this.fmenu.setVisible(true);
        this.setVisible(false);
            
    }//GEN-LAST:event_btnMenuBackMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCobrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JButton btnNuevoItem;
    private javax.swing.JComboBox<String> cbxVendedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCodigoBarras;
    private javax.swing.JLabel lblVendedor;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel panelLogin1;
    private javax.swing.JPanel panelLogin2;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextPane txtCodigoBarras;
    // End of variables declaration//GEN-END:variables
}