/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

/**
 *
 * BASE DE DATOS CLIENTES
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class cliented {
    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarCliente(clientes pr){ 
        String sql = "INSERT INTO clientes (Nombre, Apellido_P, Apellido_M, Direccion, Telefono, correo, Contraseña, status) VALUES (?,?,?,?,?,?,?,?)";
        try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           ps.setString(1, pr.getNombre());
           ps.setString(2, pr.getApellidop());
           ps.setString(3, pr.getApellidom());
           ps.setString(4, pr.getDireccion());
           ps.setString(5, pr.getTelefono());
           ps.setString(6, pr.getCorreo());
           ps.setString(7, pr.getContraseña());
           ps.setString(8, pr.getStatus());
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
    public boolean clienteYaExisteEnBD(String direccion) {
    String sql = "SELECT COUNT(*) FROM clientes WHERE Direccion = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, direccion);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0; 
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar si la direccion existe en la base de datos: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
    return false; 
}
    public List<clientes> listarClientes() {
    List<clientes> listaClientes = new ArrayList<>();
    String sql = "SELECT * FROM clientes WHERE status = 'tienda'";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            String id = rs.getString("Id_C");
            String Nombre = rs.getString("Nombre");
            String Apellido_p = rs.getString("Apellido_P");
            String Apellido_m = rs.getString("Apellido_M");
            String Direccion = rs.getString("Direccion");
            String Telefono = rs.getString("Telefono");
            String correo = rs.getString("correo"); 
            String Contraseña = rs.getString("Contraseña"); 
            String status = rs.getString("status");
            clientes pr = new clientes(id,Nombre,Apellido_p,Apellido_m,Direccion,Telefono,correo,Contraseña,status);
            listaClientes.add(pr);
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
    return listaClientes;
}
public boolean EliminarCliente(String IdCliente, String Nombre, String Telefono, String Direccion) {
    // SQL para eliminar el cliente
    String sqlEliminar = "DELETE FROM clientes WHERE Id_C = ?";
    
    // SQL para insertar en la tabla eliminadosc
    String sqlInsertar = "INSERT INTO eliminadosc (id, Nombre, Telefono, Direccion) VALUES (?, ?, ?, ?)";
    
    try {
        // Establecer conexión
        con = cn.getConnection();
        
        // Iniciar transacción
        con.setAutoCommit(false);
        
        // Eliminar el cliente
        ps = con.prepareStatement(sqlEliminar);
        ps.setString(1, IdCliente);
        ps.execute();
        
        // Insertar en la tabla eliminadosc
        ps = con.prepareStatement(sqlInsertar);
        ps.setString(1, IdCliente);
        ps.setString(2, Nombre);
        ps.setString(3, Telefono);
        ps.setString(4, Direccion);
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
    public boolean actualizarCliente(String Id_C, clientes nuevocliente) {
    String sql = "UPDATE clientes SET Nombre = ?, Apellido_P = ?, Apellido_M = ?, Direccion = ?,  Telefono = ? WHERE Id_C = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, nuevocliente.getNombre());
        ps.setString(2, nuevocliente.getApellidop());
        ps.setString(3, nuevocliente.getApellidom());
        ps.setString(4, nuevocliente.getDireccion());
        ps.setString(5, nuevocliente.getTelefono());
        ps.setString(6, Id_C);
        int rowsUpdated = ps.executeUpdate();

        if (rowsUpdated > 0) {
            return true; // Actualización exitosa
        }
    } catch (SQLException e) {
        System.out.println("Error al actualizar el Cliente: " + e.toString());
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
