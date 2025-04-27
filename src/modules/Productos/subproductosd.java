
package modules.Productos;

import codigo.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;


public class subproductosd {
    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;
    
public boolean EliminarSubproductos(int codigo , String ClaveAlterna, String nombre) {
    // SQL para eliminar el subproducto
    String sqlEliminar = "DELETE FROM subproducto WHERE ClaveAlterna = ?";
    
    // SQL para insertar en la tabla eliminados
    String sqlInsertar = "INSERT INTO eliminadosub (codigo, clavealterna, nombre) VALUES (?, ?, ?)";
    
    try {
        // Establecer conexión
        con = cn.getConnection();
        
        // Iniciar transacción
        con.setAutoCommit(false);
        
        // Eliminar el subproducto
        ps = con.prepareStatement(sqlEliminar);
        ps.setString(1, ClaveAlterna);
        ps.execute();
        
        // Insertar en la tabla eliminados
        ps = con.prepareStatement(sqlInsertar);
        ps.setInt(1, codigo);
        ps.setString(2, ClaveAlterna);
        ps.setString(3, nombre);
        ps.execute();
        
        // Confirmar la transacción
        con.commit();
        
        return true;
    } catch (SQLException e) {
        // Manejar errores
        System.out.println(e.toString());
        
        // Revertir la transacción en caso de error
        try {
            con.rollback();
        } catch (SQLException rollbackException) {
            System.out.println(rollbackException.toString());
        }
        
        return false;
    } finally {
        try {
            // Restaurar la configuración de autocommit
            con.setAutoCommit(true);
            
            // Cerrar la conexión
            con.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}

     public List<subproductos> listarSubproductos() {
    List<subproductos> listas = new ArrayList<>();
    String sql = "SELECT * FROM subproducto";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            String codigo = rs.getString("codigo");
            String Nombre = rs.getString("Nombre");
            String ClaveAlterna = rs.getString("ClaveAlterna");
            Double Precio = rs.getDouble("Precio");
            String Unidad = rs.getString("Unidad");
            String granel = rs.getString("granel");
            byte[] imagen = rs.getBytes("imagen");
            subproductos pr = new subproductos(codigo,Nombre,ClaveAlterna,Precio,Unidad,granel,imagen);
            listas.add(pr);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar subproductos: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
    return listas;
}
      public boolean agregarSubproducto(subproductos nuevoProducto) {
    String sql = "INSERT INTO subproducto (Codigo, ClaveAlterna, Nombre, Precio, Unidad,granel ,imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, nuevoProducto.getCodigo());
        ps.setString(2, nuevoProducto.getClaveAlterna());
        ps.setString(3, nuevoProducto.getNombre());
        ps.setDouble(4, nuevoProducto.getPrecio());
        ps.setString(5, nuevoProducto.getUnidad());
        ps.setString(6, nuevoProducto.getGranel());
        ps.setBytes(7, nuevoProducto.getImagen());
        int resultado = ps.executeUpdate();
        return resultado > 0;
    } catch (SQLException e) {
        System.out.println("Error al agregar subproducto: " + e.toString());
        return false;
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
}
      public boolean editarSubproducto(String codigoAnterior, subproductos productoEditado) {
    String sql = "UPDATE subproducto SET Codigo = ?, Nombre = ?, Precio = ?, Unidad = ? , granel=? , imagen =? WHERE ClaveAlterna = ?";
    
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, productoEditado.getCodigo());
        ps.setString(2, productoEditado.getNombre());
        ps.setDouble(3, productoEditado.getPrecio());
        ps.setString(4, productoEditado.getUnidad());
        ps.setString(5, productoEditado.getGranel());
        ps.setBytes(6, productoEditado.getImagen());
        ps.setString(7, codigoAnterior);
        int resultado = ps.executeUpdate();
        return resultado > 0;
    } catch (SQLException e) {
        System.out.println("Error al editar subproducto: " + e.toString());
        return false;
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
}
      public String obtenerCodigo(String claveAlterna) {
        String codigo = null;
        String sql = "SELECT Codigo FROM subproducto WHERE ClaveAlterna = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, claveAlterna);
            rs = ps.executeQuery();

            if (rs.next()) {
                codigo = rs.getString("Codigo");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el código por ClaveAlterna: " + e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.toString());
            }
        }

        return codigo;
    }
      public productos BuscarClave(String ClaveAlterna) {
    productos pro = new productos();
    String sql = "SELECT * FROM subproducto WHERE ClaveAlterna = ?";
    
    try (Connection con = cn.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, ClaveAlterna);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                pro.setCodigo(rs.getString("Codigo"));
                pro.setNombre(rs.getString("Nombre"));
                pro.setPrecio(rs.getDouble("Precio"));
                pro.setClavealterna(rs.getString("ClaveAlterna"));
                pro.setUnidad(rs.getString("Unidad"));
                pro.setGranel(rs.getString("granel"));
                pro.setImagen(rs.getBytes("imagen"));
            }
        }
    } catch (SQLException e) {
    }
    
    return pro;
}
      
    
}
