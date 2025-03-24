package stocksmart;

import inicio.FrameMenu;
import assests.icons.clases.PlusIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.SQLException;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import stocksmart.registros.FrameRegistroProveedores;

public class FrameMovimientos extends javax.swing.JFrame {
       ConnectionDB connectionDB = null;
        
        FrameMenu fmenu = null;
        Font customFont = FontLoader.customFont;
        Font customFontBold = FontLoader.customFontBold;
        Font customFontBold2 = FontLoader.customFontBold2;
       
    /**
     * Creates new form StockSmartFrameVentas
     */
    public FrameMovimientos(FrameMenu fmenu) throws SQLException {
        
        initComponents();
        this.fmenu = fmenu;
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
        tablaProveedores = new JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        btnBuscar = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        fechaInicial1 = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
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

        tablaProveedores.setAutoCreateRowSorter(true);
        tablaProveedores.setFont(customFontBold2);
        tablaProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {"Marca", "Telefono", "Direccion", "Correo"}

        ));
        tablaProveedores.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaProveedores);

        jLabel2.setFont(customFontBold2);
        jLabel2.setText("Fecha de inicio");

        jScrollPane2.setViewportView(jTextPane1);

        btnBuscar.setFont(customFontBold2);
        btnBuscar.setForeground(new java.awt.Color(50, 130, 233));
        btnBuscar.setText("Buscar");
        btnBuscar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscar.setIconTextGap(10);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jTextPane2);

        jLabel3.setFont(customFontBold2);
        jLabel3.setText("Codigo");

        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(customFontBold2);
        jLabel4.setText("Tipo de Movimeintos ");

        jLabel5.setFont(customFontBold2);
        jLabel5.setText("Fecha final");

        fechaInicial.setBackground(new java.awt.Color(255, 255, 255));
        fechaInicial.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N

        fechaInicial1.setBackground(new java.awt.Color(255, 255, 255));
        fechaInicial1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(50, 130, 233));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Movimientos");

        javax.swing.GroupLayout panelLogin2Layout = new javax.swing.GroupLayout(panelLogin2);
        panelLogin2.setLayout(panelLogin2Layout);
        panelLogin2Layout.setHorizontalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin2Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLogin2Layout.createSequentialGroup()
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLogin2Layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2)
                                    .addComponent(fechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fechaInicial1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBuscar))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(428, Short.MAX_VALUE))
            .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLogin2Layout.createSequentialGroup()
                    .addGap(112, 112, 112)
                    .addComponent(jLabel4)
                    .addContainerGap(1037, Short.MAX_VALUE)))
        );
        panelLogin2Layout.setVerticalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelLogin2Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jCheckBox2))
                            .addComponent(jScrollPane2)
                            .addComponent(jCheckBox1)
                            .addComponent(jScrollPane3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fechaInicial1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
            .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLogin2Layout.createSequentialGroup()
                    .addGap(115, 115, 115)
                    .addComponent(jLabel4)
                    .addContainerGap(601, Short.MAX_VALUE)))
        );

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
                .addComponent(panelLogin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
            .addComponent(panelLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, 1265, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void connectionDB() throws SQLException{
        connectionDB = new ConnectionDB();
        Connection connection = connectionDB.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM proveedores");
        DefaultTableModel model = (DefaultTableModel) tablaProveedores.getModel();
          model.setRowCount(0);
        while (result.next()) {
           
            String nombre = result.getString("Marca");
            String telefono = result.getString("Telefono");
            String direccion = result.getString("Direccion");
            String correo = result.getString("correo");

            model.addRow(new Object[]{nombre, telefono, direccion, correo});
        }

        result.close();
        statement.close();
        connection.close();
        
        
    }
    
     public void LimpiarTabla() {
        DefaultTableModel model = (DefaultTableModel) tablaProveedores.getModel();
        model.setRowCount(0);
    }
    private void btnMenuBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseEntered
        // TODO add your handlings code here:
  
        
        
    }//GEN-LAST:event_btnMenuBackMouseEntered

    private void btnMenuBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseExited
        // TODO add your handling code here:
         
    }//GEN-LAST:event_btnMenuBackMouseExited

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        // TODO add your handling code here:}
        this.fmenu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    
    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JPanel btnMenuBack;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private com.toedter.calendar.JDateChooser fechaInicial1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel panelLogin1;
    private javax.swing.JPanel panelLogin2;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable tablaProveedores;
    // End of variables declaration//GEN-END:variables
}
