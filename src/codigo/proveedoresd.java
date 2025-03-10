
package codigo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class proveedoresd {
    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarProveedor(proveedores pr){
        String sql = "INSERT INTO proveedores (Marca,Telefono, Direccion, correo) VALUES (?,?,?,?)";
        try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           ps.setString(1, pr.getMarca());
           ps.setString(2, pr.getTelefono());
           ps.setString(3, pr.getDireccion());
           ps.setString(4, pr.getCorreo());
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
    public boolean marcaYaExisteEnBD(String marca) {
    String sql = "SELECT COUNT(*) FROM proveedores WHERE Marca = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, marca);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0; // Si el recuento es mayor que 0, la marca ya existe en la base de datos
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar si la marca existe en la base de datos: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
    return false; // En caso de error o si no se encontró la marca en la base de datos
}
    
    public List<proveedores> listarProveedores() {
    List<proveedores> listaProveedores = new ArrayList<>();
    String sql = "SELECT * FROM proveedores";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            String marca = rs.getString("Marca");
            String telefono = rs.getString("Telefono");
            String direccion = rs.getString("Direccion");
            String correo = rs.getString("correo"); // Asegúrate de que "correo" sea el nombre correcto de la columna en tu base de datos
            proveedores pr = new proveedores(marca, telefono, direccion, correo);
            listaProveedores.add(pr);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar proveedores: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
    return listaProveedores;
}

public boolean eliminarProveedor(String marca) {
    String sqlSelect = "SELECT COUNT(*) FROM Producto WHERE Marca = ?";
    String sqlDelete = "DELETE FROM Proveedores WHERE Marca = ?";

    try (Connection con = cn.getConnection()) {

        // Verificar si hay productos asociados al proveedor
        try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
            psSelect.setString(1, marca);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                int cantidadProductos = rs.getInt(1);

                if (cantidadProductos > 0) {
                    JOptionPane.showMessageDialog(null, "No se puede eliminar el proveedor porque tiene productos asociados.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        // Si no hay productos, proceder con la eliminación del proveedor
        try (PreparedStatement psDelete = con.prepareStatement(sqlDelete)) {
            psDelete.setString(1, marca);
            psDelete.execute();
            return true;
        }

    } catch (SQLException e) {
        // Manejar la excepción adecuadamente, puedes imprimir el mensaje de error o registrar el error en un archivo de registro
        System.out.println("Error al eliminar proveedor: " + e.toString());
        return false;
    }
}

public boolean actualizarProveedor(String marcaAnterior, proveedores nuevoProveedor) {
    String sql = "UPDATE proveedores SET Marca = ?, Telefono = ?, Direccion = ?, correo = ? WHERE Marca = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, nuevoProveedor.getMarca());
        ps.setString(2, nuevoProveedor.getTelefono());
        ps.setString(3, nuevoProveedor.getDireccion());
        ps.setString(4, nuevoProveedor.getCorreo());
        ps.setString(5, marcaAnterior);

        int rowsUpdated = ps.executeUpdate();

        if (rowsUpdated > 0) {
            return true; // Actualización exitosa
        }
    } catch (SQLException e) {
        System.out.println("Error al actualizar el proveedor: " + e.toString());
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }

    return false; // La actualización falló
}
}
