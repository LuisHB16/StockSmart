
package codigo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Unidadd {
    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public List<Unidad> listarUnidades() {
    List<Unidad> listaUnidad = new ArrayList<>();
    String sql = "SELECT * FROM unidad";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            String unidad = rs.getString("Unidad");
            Double formato = rs.getDouble("Formato"); // Uncomment this line
            Unidad pr = new Unidad(unidad, formato); // Pass 'formato' to the constructor
            listaUnidad.add(pr);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar Unidades: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
    return listaUnidad;
    }
    
     public boolean RegistrarUnidad(Unidad pr){
        String sql = "INSERT INTO unidad (Unidad,Formato) VALUES (?,?)";
        try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           ps.setString(1,pr.getUnidad()); 
           ps.setDouble(2,pr.getFormato());
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
    public boolean existeUnidad(String Unidad) {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet resultSet = null;

    try {
        connection = cn.getConnection();

        String consulta = "SELECT COUNT(*) FROM unidad WHERE Unidad = ?";

        stmt = connection.prepareStatement(consulta);
        stmt.setString(1, Unidad);

        resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
} catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
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
    
public boolean eliminarUnidad(String unidad) {
    // Verificar si la unidad se está utilizando en algún producto
    /*if (unidadEnUso(unidad)) {
        JOptionPane.showMessageDialog(null, "No se puede eliminar la unidad, ya se está utilizando en un producto.");
        return false;
    }*/

    String sql = "DELETE FROM unidad WHERE Unidad = ? ";
    try (Connection con = cn.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, unidad);
        ps.execute();
        return true;
    } catch (SQLException e) {
        // Manejar la excepción adecuadamente, puedes imprimir el mensaje de error o registrar el error en un archivo de registro
        // System.out.println(e.toString());
        return false;
    }
}

public boolean unidadEnUso(String unidad) {
    // Verificar si la unidad se está utilizando en algún producto en la base de datos
    String sql = "SELECT COUNT(*) FROM producto WHERE Unidad = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, unidad);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0;
        }
    } catch (SQLException e) {
        //System.out.println(e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
          //  System.out.println(e.toString());
        }
    }
    return false;
}
       
          public boolean actualizarUnidad(String antesUnidad, Unidad nuevoUnidad) {
        String sql = "UPDATE unidad SET Unidad = ?, Formato = ? WHERE Unidad = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoUnidad.getUnidad());
            ps.setDouble(2, nuevoUnidad.getFormato());
            ps.setString(3, antesUnidad);
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
