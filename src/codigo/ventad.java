package codigo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ventad {
 Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean RegistrarVenta(venta v) {
        String sql = "INSERT INTO venta (Id_Clientes, Fecha, Total, Id_Vendedor, Id_Repartidor,Status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, v.getId_clientes());
            ps.setString(2, v.getFecha());
             ps.setDouble(3, v.getTotal());
            ps.setString(4, v.getVendedor());
            ps.setString(5, v.getRepartidor());
            ps.setString(6, v.getStatus());

            // Utiliza executeUpdate en lugar de execute para realizar una inserción.
            int result = ps.executeUpdate();

            return result > 0;  // Verifica si se insertó al menos un registro.

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Devuelve false en caso de error.
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    	public int idventas() {
		int id=0;
		String sql = "select max(Id_Ventas) from venta";
		 try {
	            con = cn.getConnection();
	            ps = con.prepareStatement(sql);
	            rs = ps.executeQuery();
	            if (rs.next()) {
	                id = rs.getInt(1);
	            }
	        } catch (SQLException e) {
	            System.out.println(e.toString());
	        }
	        return id;
	}
public int RegistrarDetalleVenta(detalleV dv) {
    int r = 0;
    String sql = "INSERT INTO detalle_venta (Id_Ventas, Codigo, Cantidad, Precio_U, Unidad) VALUES (?,?,?,?,?)";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, dv.getId_Ventas());
        ps.setDouble(3, dv.getCantidad());
        ps.setDouble(4, dv.getPrecio_U());
        ps.setString(5,dv.getUnidad());
        
        // Verifica si el código existe en la tabla producto
        String codigo = dv.getCodigo();
        boolean codigoEncontrado = false;

        String consultaProducto = "SELECT ClaveAlterna FROM producto WHERE ClaveAlterna = ?";
        PreparedStatement consultaProductoStatement = con.prepareStatement(consultaProducto);
        consultaProductoStatement.setString(1, codigo);
        ResultSet resultadoProducto = consultaProductoStatement.executeQuery();

        if (resultadoProducto.next()) {
            // El código existe en producto, utiliza ese código
            codigoEncontrado = true;
        } else {
            // El código no existe en producto, verifica si existe en subproducto
            String consultaSubproducto = "SELECT ClaveAlterna FROM subproducto WHERE ClaveAlterna = ?";
            PreparedStatement consultaSubproductoStatement = con.prepareStatement(consultaSubproducto);
            consultaSubproductoStatement.setString(1, codigo);
            ResultSet resultadoSubproducto = consultaSubproductoStatement.executeQuery();

            if (resultadoSubproducto.next()) {
                // El código existe en subproducto, utiliza ese código
                codigoEncontrado = true;
            }
        }

        if (codigoEncontrado) {
            // Inserta el código encontrado en la tabla detalle_venta
            ps.setString(2, codigo);
            ps.execute();
        } else {
            // El código no se encuentra en ninguna tabla, maneja el error
            System.out.println("Error: El código no existe en producto ni en subproducto.");
        }
    } catch (SQLException e) {
        System.out.println("Error al insertar en detalle_venta: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
    return r;
}

    public boolean RegistrarPedido(venta v) {
        String sql = "INSERT INTO venta (Id_Clientes, Fecha, Total, Id_Vendedor, Id_Repartidor,Status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, v.getId_clientes());
            ps.setString(2, v.getFecha());
             ps.setDouble(3, v.getTotal());
            ps.setString(4, v.getVendedor());
            ps.setString(5, v.getRepartidor());
            ps.setString(6, v.getStatus());

            // Utiliza executeUpdate en lugar de execute para realizar una inserción.
            int result = ps.executeUpdate();

            return result > 0;  // Verifica si se insertó al menos un registro.

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Devuelve false en caso de error.
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public List<venta> ListarVentas() {
        List<venta> ListaVenta = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE status = 'venta'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {               
                venta vent = new venta();
                vent.setId_Ventas(rs.getInt("Id_Ventas"));
                vent.setFecha(rs.getString("Fecha"));
                vent.setStatus(rs.getString("status"));
                vent.setVendedor(rs.getString("Id_Vendedor"));  
                vent.setRepartidor(rs.getString("Id_Repartidor"));
                vent.setTotal(rs.getDouble("Total"));
                ListaVenta.add(vent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return ListaVenta;
    }
    	public venta BuscarVenta(int id){
        venta cl = new venta();
        String sql = "SELECT * FROM venta WHERE Id_Ventas = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cl.setId_clientes(rs.getInt("Id_Clientes"));
                cl.setTotal(rs.getDouble("Total"));
                cl.setVendedor(rs.getString("Id_Vendedor"));
                cl.setFecha(rs.getString("Fecha"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return cl;
    }
            public List<venta> ListarPedido() {
        List<venta> ListaVenta = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE status = 'pedido' OR status = 'enviado'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {               
                venta vent = new venta();
                vent.setId_Ventas(rs.getInt("Id_Ventas"));
                vent.setFecha(rs.getString("Fecha"));
                vent.setStatus(rs.getString("status"));
                vent.setVendedor(rs.getString("Id_Vendedor"));  
                vent.setRepartidor(rs.getString("Id_Repartidor"));
                vent.setTotal(rs.getDouble("Total"));
                ListaVenta.add(vent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return ListaVenta;
    }
    public boolean actualizarEstado(int idVenta, String nuevoEstado) {
    String sql = "UPDATE venta SET status = ? WHERE Id_Ventas = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, nuevoEstado);
        ps.setInt(2, idVenta);
        int filasAfectadas = ps.executeUpdate();
        con.close();
        if (filasAfectadas > 0) {
            return true; // La actualización fue exitosa
        } else {
            return false; // No se actualizó ninguna fila (venta no encontrada)
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false; // Error en la actualización
    }
}
public boolean actualizarEstado1(int idVenta, String nuevoEstado, String nuevoRepartidor) {
    String sql ="UPDATE venta SET status = ?,Id_Repartidor = ? WHERE Id_Ventas = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, nuevoEstado);
        ps.setString(2, nuevoRepartidor); // Cambiado a 2
        ps.setInt(3, idVenta); // Cambiado a 3
        int filasAfectadas = ps.executeUpdate();
        con.close();
        if (filasAfectadas > 0) {
            return true; 
        } else {
            return false; // No se actualizó ninguna fila (venta no encontrada)
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false; // Error en la actualización
    }
}

public List<detalleV> ListarDetallesVenta(int idVentas) {
    List<detalleV> listaDetalles = new ArrayList<>();

    String sql = "SELECT Codigo, Precio_U, Cantidad FROM detalle_venta WHERE Id_Ventas = ?";

    try (Connection con = cn.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVentas);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                detalleV detalle = new detalleV();
                detalle.setCodigo(rs.getString("Codigo"));
                detalle.setPrecio_U(rs.getDouble("Precio_U"));
                detalle.setCantidad(rs.getDouble("Cantidad"));
                listaDetalles.add(detalle);
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener detalles de la venta: " + e.toString());
    }
    return listaDetalles;
}
public venta BuscarVenta1(int id){
        venta cl = new venta();
        String sql = "SELECT * FROM venta WHERE Id_Ventas = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cl.setId_Ventas(rs.getInt("Id_Ventas"));
                cl.setId_clientes(rs.getInt("Id_Clientes"));
                cl.setTotal(rs.getDouble("Total"));
                cl.setVendedor(rs.getString("Id_Vendedor"));
                cl.setFecha(rs.getString("Fecha"));
                cl.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return cl;
    }
    public List<venta> Listardev() {
        List<venta> ListaVenta = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE status IN ('cancelacion', 'devolucion')";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {               
                venta vent = new venta();
                vent.setId_Ventas(rs.getInt("Id_Ventas"));
                vent.setFecha(rs.getString("Fecha"));
                vent.setStatus(rs.getString("status"));
                vent.setVendedor(rs.getString("Id_Vendedor"));  
                vent.setRepartidor(rs.getString("Id_Repartidor"));
                vent.setTotal(rs.getDouble("Total"));
                ListaVenta.add(vent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return ListaVenta;
    }
    public boolean actualizarEstado2(int idVenta, String nuevoEstado,String fecha) {
    String sql = "UPDATE venta SET status = ? , fecha = ? WHERE Id_Ventas = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, nuevoEstado);
        ps.setString(2, fecha);
        ps.setInt(3, idVenta);
        int filasAfectadas = ps.executeUpdate();
        con.close();
        if (filasAfectadas > 0) {
            return true; // La actualización fue exitosa
        } else {
            return false; // No se actualizó ninguna fila (venta no encontrada)
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false; // Error en la actualización
    }
}

}
