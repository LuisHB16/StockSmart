package codigo;

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
            double formato = obtenerFormato(vent.getCoidgo());
            vent.setFormato(formato);

            ListaVenta.add(vent);
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }

    return ListaVenta;
}

private double obtenerFormato(String codigoProducto) throws SQLException {
    String sqlSubproducto = "SELECT Unidad FROM subproducto WHERE clavealterna = ?";
    String sqlProducto = "SELECT Unidad FROM producto WHERE Codigo = ?";
    String sqlUnidad = "SELECT Formato FROM unidad WHERE Unidad = ?";
    
    double formato = 0.0;

    try (PreparedStatement psSubproducto = con.prepareStatement(sqlSubproducto);
         PreparedStatement psProducto = con.prepareStatement(sqlProducto)) {

        // Intentar obtener la unidad desde subproducto
        psSubproducto.setString(1, codigoProducto);
        ResultSet rsSubproducto = psSubproducto.executeQuery();

        if (rsSubproducto.next()) {
            String unidad = rsSubproducto.getString("Unidad");

            try (PreparedStatement psUnidad = con.prepareStatement(sqlUnidad)) {
                psUnidad.setString(1, unidad);
                ResultSet rsUnidad = psUnidad.executeQuery();

                if (rsUnidad.next()) {
                    formato = rsUnidad.getDouble("Formato");
                }
            }
        } else {
            // Si no se encuentra en subproducto, intentar obtener desde producto
            psProducto.setString(1, codigoProducto);
            ResultSet rsProducto = psProducto.executeQuery();

            if (rsProducto.next()) {
                String unidad = rsProducto.getString("Unidad");

                try (PreparedStatement psUnidad = con.prepareStatement(sqlUnidad)) {
                    psUnidad.setString(1, unidad);
                    ResultSet rsUnidad = psUnidad.executeQuery();

                    if (rsUnidad.next()) {
                        formato = rsUnidad.getDouble("Formato");
                    }
                }
            }
        }
    }

    return formato;
}


}
