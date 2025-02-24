/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stocksmart;

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
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import stocksmart.registros.StockSmartFrameRegistroClientes;

/**
 *
 * @author alanm
 */
public class StockSmartFrameClientes extends javax.swing.JFrame {

    ConnectionDB connectionDB = null;
    Font customFont = FontLoader.customFont;
    Font customFontBold = FontLoader.customFontBold;
    Font customFontBold2 = FontLoader.customFontBold2;
    Font customFontBold3 = FontLoader.customFontBold3;
    private Color originalBackground;
    private Border originalBorder;

    /**
     * Creates new form StockSmartFrameVentas
     */
    public StockSmartFrameClientes() throws SQLException {

        initComponents();
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
        tablaClientes = new JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNombre = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
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

        tablaClientes.setAutoCreateRowSorter(true);
        tablaClientes.setFont(customFontBold2);
        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {"Id_C", "Nombre", "Apellido_P", "Apellido_M", "Direccion", "Telefono"}

        ));
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaClientes);

        jLabel2.setFont(customFontBold2);
        jLabel2.setText("Nombre del Cliente");

        jScrollPane2.setViewportView(txtNombre);

        jButton1.setBackground(new java.awt.Color(50, 130, 233));
        jButton1.setFont(customFontBold2);
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Nuevo Cliente");
        jButton1.setBorderPainted(false);
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setIconTextGap(10);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
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

        jButton5.setBackground(new java.awt.Color(50, 130, 233));
        jButton5.setFont(customFontBold2);
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Eliminar");
        jButton5.setBorderPainted(false);
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.setIconTextGap(10);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(customFontBold2);
        jButton6.setForeground(new java.awt.Color(50, 130, 233));
        jButton6.setText("Editar");
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.setIconTextGap(10);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLogin2Layout = new javax.swing.GroupLayout(panelLogin2);
        panelLogin2.setLayout(panelLogin2Layout);
        panelLogin2Layout.setHorizontalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin2Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelLogin2Layout.createSequentialGroup()
                            .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addGroup(panelLogin2Layout.createSequentialGroup()
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnBuscar)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        panelLogin2Layout.setVerticalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin2Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        jButton1.setIcon(new PlusIcon(18, 18, Color.WHITE));
        jButton1.setIcon(new PlusIcon(18, 18, Color.WHITE));

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

    public void connectionDB() throws SQLException {
        connectionDB = new ConnectionDB();
        Connection connection = connectionDB.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT Id_C, Nombre, Apellido_P, Apellido_M, Direccion, Telefono FROM clientes");
        DefaultTableModel tablaVentas1 = (DefaultTableModel) tablaClientes.getModel();
        while (result.next()) {
            int idVentas = result.getInt("Id_C");

            String nombre = result.getString("Nombre");
            String apellidoP = result.getString("Apellido_P");
            String apellidoM = result.getString("Apellido_M");
            String direccion = result.getString("Direccion");
            String telefono = result.getString("Telefono");

            tablaVentas1.addRow(new Object[]{idVentas, nombre, apellidoP, apellidoM, direccion, telefono});
        }

        result.close();
        statement.close();

    }
    private void btnMenuBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseEntered
        // TODO add your handlings code here:


    }//GEN-LAST:event_btnMenuBackMouseEntered

    private void btnMenuBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_btnMenuBackMouseExited

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnMenuBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuBackMouseClicked
        // TODO add your handling code here:
        new StockSmartFrameMenu().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnMenuBackMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
      try {
   
    Connection connection = connectionDB.getConnection();

    String sql = "SELECT * FROM clientes WHERE Nombre LIKE ?";

    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    preparedStatement.setString(1, "%" + txtNombre.getText() + "%");

    ResultSet result = preparedStatement.executeQuery();
    
    DefaultTableModel tablaVentas1 = (DefaultTableModel) tablaClientes.getModel();

    tablaVentas1.setRowCount(0);

    
    boolean clienteEncontrado = false;
    while (result.next()) {
        int idVentas = result.getInt("Id_C");
        String nombre = result.getString("Nombre");
        String apellidoP = result.getString("Apellido_P");
        String apellidoM = result.getString("Apellido_M");
        String direccion = result.getString("Direccion");
        String telefono = result.getString("Telefono");

        // Agregar los resultados a la tabla
        tablaVentas1.addRow(new Object[]{idVentas, nombre, apellidoP, apellidoM, direccion, telefono});
        clienteEncontrado = true;  // Marcar que se encontró al menos un cliente
    }

    // Mostrar un mensaje según si se encontraron resultados o no
    if (clienteEncontrado) {
        JOptionPane.showMessageDialog(this, "Cliente(s) encontrado(s).");
    } else {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
    }

    // Cerrar los recursos
    result.close();
    preparedStatement.close();
    connection.close();

} catch (SQLException ex) {
    Logger.getLogger(StockSmartFrameRegistroClientes.class.getName()).log(Level.SEVERE, null, ex);
}



    }//GEN-LAST:event_btnBuscarActionPerformed

   
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
            java.util.logging.Logger.getLogger(StockSmartFrameClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new StockSmartFrameClientes().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(StockSmartFrameClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JPanel btnMenuBack;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel panelLogin1;
    private javax.swing.JPanel panelLogin2;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTextPane txtNombre;
    // End of variables declaration//GEN-END:variables
}
