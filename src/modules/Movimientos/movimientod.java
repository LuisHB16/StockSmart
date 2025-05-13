package modules.Movimientos;

import codigo.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class movimientod {

    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean agregarMovimiento(movimiento nuevoProducto) {
        String sql = "INSERT INTO movimientos (Movimiento, Fecha, Cantidad,Codigo, inventario_antes) VALUES (?, ?, ?, ?, ?)";
        System.out.println(nuevoProducto.getInventario_antes());
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoProducto.getMovimiento());
            ps.setString(2, nuevoProducto.getFehca());
            ps.setDouble(3, nuevoProducto.getCantidad());
            ps.setString(4, nuevoProducto.getCoidgo());
            ps.setDouble(5, nuevoProducto.getInventario_antes());

            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.toString());
            }
        }
    }
    
    
/*
    public List<movimiento> Listardev() {
        List<movimiento> ListaVenta = new ArrayList<>();
        String sql = "SELECT * FROM movimientos";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                movimiento vent = new movimiento();
                vent.setMovimiento(rs.getString("Movimiento"));
                vent.setFehca(rs.getString("Fecha"));
                vent.setCantidad(rs.getDouble("Cantidad"));
                vent.setCoidgo(rs.getString("Codigo"));
                vent.setInventario_antes(rs.getDouble("inventario_antes"));
                
          
                ListaVenta.add(vent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return ListaVenta;
    }*/
    
    public List<movimiento> Listardev() {
    List<movimiento> ListaVenta = new ArrayList<>();
    String sql = "SELECT * FROM movimientos";

    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            movimiento vent = new movimiento();
            vent.setMovimiento(rs.getString("Movimiento"));
            vent.setFehca(rs.getString("Fecha"));
            vent.setCantidad(rs.getDouble("Cantidad"));
            vent.setCoidgo(rs.getString("Codigo"));
            vent.setInventario_antes(rs.getDouble("inventario_antes"));

            // Obtener el formato de la unidad asociada al código del producto
            String codigo = vent.getCoidgo();
            System.out.println(codigo);
            double formato = obtenerFormato(codigo);
            System.out.println("FORMATO movimientod ListarDev Query a BD" + formato);
            vent.setFormato(formato);

            ListaVenta.add(vent);
        }
    } catch (SQLException e) {
        System.out.println("Error en Listardev: " + e.toString());
    } finally {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión en Listardev: " + e.toString());
        }
    }

    return ListaVenta;
}

private double obtenerFormato(String codigoProducto) throws SQLException {
    String sqlSubproducto = "SELECT Unidad FROM subproducto WHERE clavealterna = ?";
    String sqlProducto = "SELECT Unidad FROM producto WHERE ClaveAlterna = ?";
    String sqlUnidad = "SELECT Formato FROM unidad WHERE Unidad = ?";
    
    double formato = 0.0;
    String unidad = null;

    // Primero intentamos obtener la unidad desde la tabla producto usando ClaveAlterna
    try {
        con = cn.getConnection(); // Aseguramos tener una conexión válida
        PreparedStatement psProducto = con.prepareStatement(sqlProducto);
        psProducto.setString(1, codigoProducto);
        ResultSet rsProducto = psProducto.executeQuery();

        if (rsProducto.next()) {
            unidad = rsProducto.getString("Unidad");
            System.out.println("Unidad encontrada en producto: " + unidad + " para código: " + codigoProducto);
        } else {
            // Si no se encuentra en producto, intentar en subproducto
            PreparedStatement psSubproducto = con.prepareStatement(sqlSubproducto);
            psSubproducto.setString(1, codigoProducto);
            ResultSet rsSubproducto = psSubproducto.executeQuery();

            if (rsSubproducto.next()) {
                unidad = rsSubproducto.getString("Unidad");
                System.out.println("Unidad encontrada en subproducto: " + unidad + " para código: " + codigoProducto);
            }
        }

        // Si encontramos una unidad, obtenemos su formato
        if (unidad != null && !unidad.isEmpty()) {
            PreparedStatement psUnidad = con.prepareStatement(sqlUnidad);
            psUnidad.setString(1, unidad);
            ResultSet rsUnidad = psUnidad.executeQuery();

            if (rsUnidad.next()) {
                formato = rsUnidad.getDouble("Formato");
                System.out.println("FORMATO ObetenerFormato() para " + codigoProducto + " con unidad " + unidad + ": " + formato);
            } else {
                System.out.println("No se encontró formato para la unidad: " + unidad);
            }
        } else {
            System.out.println("No se encontró unidad para el código: " + codigoProducto);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener formato: " + e.toString());
    } finally {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }

    return formato;
}


}
