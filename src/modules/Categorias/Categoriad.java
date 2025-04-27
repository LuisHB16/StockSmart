
package modules.Categorias;

import codigo.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Categoriad {
    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public List<Categoria> listarCategorias() {
    List<Categoria> listaCategoria = new ArrayList<>();
    String sql = "SELECT * FROM categoria";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            String categoria = rs.getString("Categoria");
            Categoria pr = new Categoria(categoria);
            listaCategoria.add(pr);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar categoria: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
    return listaCategoria;
    }
    
public boolean existeCategoria(String Categoria) {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet resultSet = null;
    
    try {
        connection = cn.getConnection(); // Asegúrate de que cn esté configurado correctamente.

        String consulta = "SELECT COUNT(*) FROM categoria WHERE Categoria = ?";
        
        stmt = connection.prepareStatement(consulta);
        stmt.setString(1, Categoria);

        resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Esto te ayudará a ver si hay errores de conexión o consulta.
    } finally {
        // Asegúrate de cerrar los recursos adecuadamente en la sección finally.
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    return false;
}
    
    public boolean RegistrarCategoria(Categoria pr){
        String sql = "INSERT INTO categoria (Categoria) VALUES (?)";
        try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           ps.setString(1,pr.getCategoria()); 
           ps.execute();
           return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
public boolean EliminarCategoria(String Categoria) {
    // Verificar si la categoría se está utilizando en algún producto
    if (CategoriaEnUso(Categoria)) {
        JOptionPane.showMessageDialog(null, "No se puede eliminar la categoría, ya se está utilizando en un producto.");
        return false;
    }

    String sql = "DELETE FROM categoria WHERE Categoria = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, Categoria);
        ps.execute();
        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        JOptionPane.showMessageDialog(null, "Error al eliminar la categoría. Asegúrese de que no esté asociada a ningún producto.");
        return false;
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}


public boolean CategoriaEnUso(String Categoria) {
    // Verificar si la unidad se está utilizando en algún producto en la base de datos
    String sql = "SELECT COUNT(*) FROM productos WHERE Categoria = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, Categoria);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0;
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
    return false;
}
          public boolean actualizarCategoria(String antescategoria, Categoria nuevocategoria) {
        String sql = "UPDATE categoria SET categoria = ? WHERE categoria = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevocategoria.getCategoria());
            ps.setString(2, antescategoria);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }




  
}
