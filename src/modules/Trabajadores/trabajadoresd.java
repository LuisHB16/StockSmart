package modules.Trabajadores;

import codigo.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class trabajadoresd {
    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarTrabajador(trabajadores tr) {
        if (existeTrabajador(tr.getNombre())) {
        System.out.println("El trabajador ya existe en la base de datos.");
        return false;
    }
        String sql = "INSERT INTO trabajadores (Usuario_trabajador, Rol) VALUES (?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, tr.getNombre());
            ps.setString(2, tr.getRol());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }
    
   public List<trabajadores> listarTrabajadores() {
        List<trabajadores> listaTrabajadores = new ArrayList<>();
        String sql = "SELECT * FROM trabajadores ORDER BY CASE WHEN Usuario_trabajador = 'Sin Repartidor' THEN 1 ELSE 2 END, Usuario_trabajador;";

        try {
            Connection con = cn.getConnection(); // Asegúrate de tener la conexión establecida
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

           while (rs.next()) {
                String nombre = rs.getString("Usuario_trabajador");
                String rol = rs.getString("rol");
                trabajadores trabajador = new trabajadores(nombre, rol);
                listaTrabajadores.add(trabajador);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace(); // Manejar la excepción adecuadamente
        }

        return listaTrabajadores;
    }
   
   public boolean EliminarTrabajador(String Usuario_trabajador){
        String sql = "DELETE FROM trabajadores WHERE Usuario_trabajador = ? ";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, Usuario_trabajador);
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
   public List<trabajadores> listarVendedores() {
    List<trabajadores> listaVendedores = new ArrayList<>();
    String sql = "SELECT Usuario_trabajador, rol FROM trabajadores WHERE rol = 'vendedor'";

    try {
        Connection con = cn.getConnection(); // Asegúrate de tener la conexión establecida
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String nombre = rs.getString("Usuario_trabajador");
            String rol = rs.getString("rol");
            trabajadores vendedor = new trabajadores(nombre, rol);
            listaVendedores.add(vendedor);
        }

        rs.close();
        ps.close();
        con.close();

    } catch (SQLException e) {
        e.printStackTrace(); // Manejar la excepción adecuadamente
    }

    return listaVendedores;
}
   
   public boolean actualizarTrabajador(String nombreAnterior, trabajadores nuevoTrabajador) {
        String sql = "UPDATE trabajadores SET Usuario_trabajador = ?, Rol = ? WHERE Usuario_trabajador = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoTrabajador.getNombre());
            ps.setString(2, nuevoTrabajador.getRol());
            ps.setString(3, nombreAnterior);
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
   
   public boolean existeTrabajador(String nombre) {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet resultSet = null;

    try {
        connection = cn.getConnection();

        String consulta = "SELECT COUNT(*) FROM trabajadores WHERE Usuario_trabajador = ?";

        stmt = connection.prepareStatement(consulta);
        stmt.setString(1, nombre);

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
      public List<trabajadores> listarRepartidores() {
    List<trabajadores> listaVendedores = new ArrayList<>();
    String sql = "SELECT Usuario_trabajador, rol FROM trabajadores WHERE rol = 'repartidor'";

    try {
        Connection con = cn.getConnection(); // Asegúrate de tener la conexión establecida
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String nombre = rs.getString("Usuario_trabajador");
            String rol = rs.getString("rol");
            trabajadores vendedor = new trabajadores(nombre, rol);
            listaVendedores.add(vendedor);
        }

        rs.close();
        ps.close();
        con.close();

    } catch (SQLException e) {
        e.printStackTrace(); // Manejar la excepción adecuadamente
    }

    return listaVendedores;
}
}

