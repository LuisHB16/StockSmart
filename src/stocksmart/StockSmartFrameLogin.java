/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stocksmart;



import java.awt.Color;
import java.sql.*;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author alanm
 */

public class StockSmartFrameLogin extends javax.swing.JFrame {

    
     
     Font customFont = FontLoader.customFont;
     Font customFontBold = FontLoader.customFontBold;
     Font customFontBold2 = FontLoader.customFontBold2;
     String usuarioBD = null;
     String passwordBD = null;
     
     
    /**
     * Creates new form StockSmartFrameLogin
     */
    public StockSmartFrameLogin() {
    connectionDB();
   
    initComponents();
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
        userLabel1 = new javax.swing.JLabel();
        passInput1 = new javax.swing.JPasswordField();
        passLabel1 = new javax.swing.JLabel();
        userInput1 = new javax.swing.JTextField();
        bienvenidoLoginLabel = new javax.swing.JLabel();
        iniciarSesionContinuatLoginLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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

        userLabel1.setFont(customFont);
        userLabel1.setForeground(new java.awt.Color(0, 0, 0));
        userLabel1.setText("Usuario");

        passInput1.setBackground(new java.awt.Color(217, 217, 217));
        passInput1.setFont(userLabel1.getFont());
        passInput1.setForeground(userInput1.getForeground());
        passInput1.setBorder(userInput1.getBorder());
        passInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passInput1ActionPerformed(evt);
            }
        });

        passLabel1.setFont(userLabel1.getFont());
        passLabel1.setForeground(new java.awt.Color(0, 0, 0));
        passLabel1.setText("Contrasena");

        userInput1.setBackground(new java.awt.Color(217, 217, 217));
        userInput1.setFont(userLabel1.getFont());
        userInput1.setForeground(new java.awt.Color(0, 0, 0));
        userInput1.setToolTipText("");
        userInput1.setName(""); // NOI18N

        bienvenidoLoginLabel.setFont(customFontBold);
        bienvenidoLoginLabel.setForeground(panelLogin1.getBackground());
        bienvenidoLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bienvenidoLoginLabel.setText("Bienvenido");

        iniciarSesionContinuatLoginLabel.setFont(customFont);
        iniciarSesionContinuatLoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iniciarSesionContinuatLoginLabel.setText("Inicia sesión para continuar");

        jButton1.setBackground(panelLogin1.getBackground());
        jButton1.setFont(customFontBold2);
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Iniciar Sesion");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLogin2Layout = new javax.swing.GroupLayout(panelLogin2);
        panelLogin2.setLayout(panelLogin2Layout);
        panelLogin2Layout.setHorizontalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin2Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passLabel1)
                            .addComponent(userLabel1)
                            .addComponent(userInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(80, Short.MAX_VALUE))
                    .addGroup(panelLogin2Layout.createSequentialGroup()
                        .addGroup(panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bienvenidoLoginLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(iniciarSesionContinuatLoginLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(92, 92, 92))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(169, 169, 169))
        );
        panelLogin2Layout.setVerticalGroup(
            panelLogin2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(bienvenidoLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iniciarSesionContinuatLoginLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addGap(71, 71, 71)
                .addComponent(userLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passLabel1)
                .addGap(12, 12, 12)
                .addComponent(passInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        AbstractBorder brdr = new TextBubbleBorder(new Color(50, 130, 250),4,39,0);
        jButton1.setBorder(brdr);

        logoLoginIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assests/images/logoStockSmart.png"))); // NOI18N

        javax.swing.GroupLayout panelLogin1Layout = new javax.swing.GroupLayout(panelLogin1);
        panelLogin1.setLayout(panelLogin1Layout);
        panelLogin1Layout.setHorizontalGroup(
            panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogin1Layout.createSequentialGroup()
                .addGroup(panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLogin1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(logoLoginIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addComponent(panelLogin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelLogin1Layout.setVerticalGroup(
            panelLogin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogin1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoLoginIcon)
                .addGap(31, 31, 31)
                .addComponent(stockSmartLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addComponent(panelLogin2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        //AbstractBorder brdr1 = new TextBubbleBorder(new Color(255, 255, 255),4,25,0);
        //panelLogin2.setBorder(brdr1);
        AbstractBorder brdr3 = new TextBubbleBorder(new Color(50, 130, 250),4,39,0);
        panelLogin2.setBorder(brdr3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passInput1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passInput1ActionPerformed

    //INICIAR SESION BOTON
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String passwordString = new String(passInput1.getPassword());
        
        if(userInput1.getText().equals(usuarioBD) && passwordString.equals(passwordBD)){
            System.out.println("Bienvenido");
            StockSmartFrameMenu ssfm = new StockSmartFrameMenu();
               ssfm.setVisible(true);
               setVisible(false);
            
        }
                
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    

    public void connectionDB(){
        String url = "jdbc:mysql://localhost:3306/tlapaleria";
        String username = "root";
        String password = "";
        try{
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            Statement statment = connection.createStatement();
            ResultSet result = statment.executeQuery("SELECT * FROM usuario");
         
            if (result.next()) {
            
            this.usuarioBD = result.getString("Usuario");
            this.passwordBD = result.getString("ContraseÃ±a"); 
           
        }
            
            
            
           
        }catch(Exception e){
              System.out.println(e);
        }
    }
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
            java.util.logging.Logger.getLogger(StockSmartFrameLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockSmartFrameLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockSmartFrameLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bienvenidoLoginLabel;
    private javax.swing.JLabel iniciarSesionContinuatLoginLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel logoLoginIcon;
    private javax.swing.JPanel panelLogin1;
    private javax.swing.JPanel panelLogin2;
    private javax.swing.JPasswordField passInput1;
    private javax.swing.JLabel passLabel1;
    private javax.swing.JLabel stockSmartLoginLabel;
    private javax.swing.JTextField userInput1;
    private javax.swing.JLabel userLabel1;
    // End of variables declaration//GEN-END:variables
}
