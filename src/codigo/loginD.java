package codigo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class loginD {
	login l = new login();
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	conexion cn = new conexion();
	
	public login log (String usuario, String contraseña) {
		
		String sql = "SELECT * FROM usuario WHERE usuario = ? AND contraseña = ?";
		try {
			con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contraseña);
            rs= ps.executeQuery();
			if(rs.next()) {
			  l.setNombre_usuarios(rs.getString("usuario"));
			  l.setContraseña_usuarios(rs.getString("contraseña"));
			  l.setRol(rs.getString("rol"));	
			}
		}	catch(SQLException e) {
			System.out.println(e.toString());
		}
		return l;
	}
	public List ListarUsuarios(){
	       List<login> Lista = new ArrayList();
	       String sql = "SELECT * FROM usuario";
	       try {
	    	   con = cn.getConnection();
	           ps = con.prepareStatement(sql);
	           rs = ps.executeQuery();
	           while (rs.next()) {               
	               login lg = new login();
	               lg.setId_usuarios(rs.getInt("id_usuarios"));
	               lg.setNombre_usuarios(rs.getString("nombre_usuarios"));
	               //lg.setContraseña_usuarios(rs.getString("contraseña_usuarios"));
	               lg.setRol(rs.getString("rol"));
	               Lista.add(lg);
	           }
	       } catch (SQLException e) {
	           System.out.println(e.toString());
	       }
	       return Lista;
	   }
}
