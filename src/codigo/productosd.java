package codigo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class productosd {

    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;

    public List<productos> listarProductos() {
        List<productos> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String codigo = rs.getString("Codigo");
                String claveAlterna = rs.getString("ClaveAlterna");
                String nombre = rs.getString("Nombre");
                Double existencia = rs.getDouble("Existencia");
                String marca = rs.getString("Marca");
                Double precio = rs.getDouble("Precio");
                String descripcion = rs.getString("Descripcion");
                String granel = rs.getString("Granel");
                String unidad = rs.getString("Unidad");
                byte[] imagen = rs.getBytes("Imagen"); 
                String categoria = rs.getString("Categoria");
                String unidadC = rs.getString("UnidadC");
                int cantmax = rs.getInt("ExtMax");
                int cantmin = rs.getInt("ExtMin");
                

                productos producto = new productos();
                producto.setCodigo(codigo);
                producto.setClavealterna(claveAlterna);
                producto.setNombre(nombre);
                producto.setExistencia(existencia);
                producto.setMarca(marca);
                producto.setPrecio(precio);
                producto.setDescripcion(descripcion);
                producto.setGranel(granel);
                producto.setUnidad(unidad);
                producto.setImagen(imagen);
                producto.setCategoria(categoria);
                producto.setUnidadC(unidadC);
                producto.setExtMax(cantmax);
                producto.setExtMin(cantmin);

                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.toString());
            }
        }
        return listaProductos;
    }

public List<productos> listarProductos2() {
    List<productos> listaProductos = new ArrayList<>();
    String sql = "SELECT Codigo, ClaveAlterna, Nombre, Existencia, Marca, Precio, Descripcion, Granel, Unidad, Imagen, Categoria, NULL as Tipo FROM Producto "
            + "UNION "
            + "SELECT ClaveAlterna, ClaveAlterna, Nombre, NULL as Existencia, NULL as Marca, Precio, NULL as Descripcion, Granel, Unidad, NULL as Imagen, NULL as Categoria, 'subproducto' as Tipo FROM Subproducto";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            String codigo;
            String tipo = rs.getString("Tipo");

            if ("subproducto".equals(tipo)) {
                codigo = rs.getString("ClaveAlterna");
            } else {
                codigo = rs.getString("Codigo");
            }

            String claveAlterna = rs.getString("ClaveAlterna");
            String nombre = rs.getString("Nombre");
            Double existencia = rs.getDouble("Existencia");
            String marca = rs.getString("Marca");
            Double precio = rs.getDouble("Precio");
            String descripcion = rs.getString("Descripcion");
            String granel = rs.getString("Granel");
            String unidad = rs.getString("Unidad");
            byte[] imagen = rs.getBytes("Imagen");
            String categoria = rs.getString("Categoria");

            productos producto = new productos();
            producto.setCodigo(codigo);
            producto.setClavealterna(claveAlterna);
            producto.setNombre(nombre);
            producto.setExistencia(existencia);
            producto.setMarca(marca);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setGranel(granel);
            producto.setUnidad(unidad);
            producto.setImagen(imagen);
            producto.setCategoria(categoria);

            listaProductos.add(producto);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar productos: " + e.toString());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
    return listaProductos;
}


    public boolean agregarProducto(productos nuevoProducto) {
        String sql = "INSERT INTO producto ( ClaveAlterna, Nombre, Existencia, Marca, Precio, Descripcion, Granel, Unidad, Imagen, Categoria, UnidadC, ExtMax, ExtMin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try { 
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoProducto.getClavealterna());
            ps.setString(2, nuevoProducto.getNombre());
            ps.setDouble(3, nuevoProducto.getExistencia());
            ps.setString(4, nuevoProducto.getMarca());
            ps.setDouble(5, nuevoProducto.getPrecio());
            ps.setString(6, nuevoProducto.getDescripcion());
            ps.setString(7, nuevoProducto.getGranel());
            ps.setString(8, nuevoProducto.getUnidad());
            ps.setBytes(9, nuevoProducto.getImagen());
            ps.setString(10, nuevoProducto.getCategoria()); // Campo de categoría
            ps.setString(11, nuevoProducto.getUnidadC());
            ps.setInt(12, nuevoProducto.getExtMax());
            ps.setInt(13, nuevoProducto.getExtMin());

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

    public boolean editarProducto(String codigoAnterior, productos productoEditado) {
        String sql = "UPDATE producto SET ClaveAlterna = ?, Nombre = ?, Existencia = ?, Marca = ?, Precio = ?, Descripcion = ?, Granel = ?, Unidad = ?, Imagen = ?, Categoria = ?, UnidadC = ?, ExtMax = ?, ExtMin = ? WHERE Codigo = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, productoEditado.getClavealterna());
            ps.setString(2, productoEditado.getNombre());
            ps.setDouble(3, productoEditado.getExistencia());
            ps.setString(4, productoEditado.getMarca());
            ps.setDouble(5, productoEditado.getPrecio());
            ps.setString(6, productoEditado.getDescripcion());
            ps.setString(7, productoEditado.getGranel());
            ps.setString(8, productoEditado.getUnidad());
            ps.setBytes(9, productoEditado.getImagen());
            ps.setString(10, productoEditado.getCategoria());
            ps.setString(11, productoEditado.getUnidadC());
            ps.setInt(12, productoEditado.getExtMax());
            ps.setInt(13, productoEditado.getExtMin());
            ps.setString(14, codigoAnterior);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al editar producto: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.toString());
            }
        }
    }

    public boolean eliminarProducto(String codigo) {
        String sqlSelect = "SELECT COUNT(*) FROM subproducto WHERE Codigo = ?";
        String sqlDelete = "DELETE FROM producto WHERE Codigo = ?";
        String sqlInsertEliminado = "INSERT INTO eliminados (Codigo, ClaveAlterna, Nombre, Marca) "
                + "SELECT Codigo, ClaveAlterna, Nombre, Marca FROM producto WHERE Codigo = ?";

        try {
            con = cn.getConnection();

            // Verificar si hay subproductos asociados al producto
            try (PreparedStatement psSelect = con.prepareStatement(sqlSelect); PreparedStatement psInsertEliminado = con.prepareStatement(sqlInsertEliminado)) {

                con.setAutoCommit(false); // Desactivar la confirmación automática para poder realizar una transacción

                psSelect.setString(1, codigo);
                ResultSet rs = psSelect.executeQuery();

                if (rs.next()) {
                    int cantidadSubproductos = rs.getInt(1);

                    // Si hay subproductos, mostrar mensaje y no eliminar el producto
                    if (cantidadSubproductos > 0) {
                        JOptionPane.showMessageDialog(null, "No se puede eliminar el producto porque tiene subproductos asociados.");
                        return false;
                    }
                }

                // Si no hay subproductos, guardar el registro en la tabla eliminado
                psInsertEliminado.setString(1, codigo);
                psInsertEliminado.executeUpdate();

                // Realizar la eliminación del producto
                try (PreparedStatement psDelete = con.prepareStatement(sqlDelete)) {
                    psDelete.setString(1, codigo);
                    psDelete.executeUpdate();
                }

                con.commit(); // Confirmar la transacción

                return true;
            } catch (SQLException e) {
                con.rollback(); // Revertir la transacción en caso de error
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true); // Restaurar la confirmación automática
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public productos Buscarprocodigo(String ClaveAlterna) {
        productos pro = new productos();
        String sql = "SELECT * FROM producto WHERE ClaveAlterna = ?";
        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ClaveAlterna);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pro.setCodigo(rs.getString("Codigo"));
                    pro.setNombre(rs.getString("Nombre"));
                    pro.setPrecio(rs.getDouble("Precio"));
                    pro.setExistencia(rs.getDouble("Existencia"));
                    pro.setMarca(rs.getString("Marca"));
                    pro.setClavealterna(rs.getString("ClaveAlterna"));
                    pro.setGranel(rs.getString("Granel"));
                    pro.setImagen(rs.getBytes("Imagen")); // Agrega el campo de imagen
                    pro.setCategoria(rs.getString("Categoria")); // Agrega el campo de categoría
                    pro.setUnidad(rs.getString("Unidad")); // Agrega el campo de unidad
                } else {
                    System.out.println("No se encontró un producto con la ClaveAlterna: " + ClaveAlterna);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pro;
    }

    public productos Buscarprocodigo2(String ClaveAlterna) {
        productos pro = new productos();
        String sql = "SELECT * FROM producto WHERE Codigo = ?";
        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ClaveAlterna);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pro.setCodigo(rs.getString("Codigo"));
                    pro.setNombre(rs.getString("Nombre"));
                    pro.setPrecio(rs.getDouble("Precio"));
                    pro.setExistencia(rs.getDouble("Existencia"));
                    pro.setMarca(rs.getString("Marca"));
                    pro.setClavealterna(rs.getString("ClaveAlterna"));
                    pro.setGranel(rs.getString("Granel"));
                    pro.setImagen(rs.getBytes("Imagen")); // Agrega el campo de imagen
                    pro.setCategoria(rs.getString("Categoria")); // Agrega el campo de categoría
                    pro.setUnidad(rs.getString("Unidad")); // Agrega el campo de unidad
                } else {
                    System.out.println("No se encontró un producto con la ClaveAlterna: " + ClaveAlterna);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pro;
    }

    public productos Buscarprocodigo3(String ClaveAlterna) {
        productos pro = null; // Inicializar el objeto productos como nulo
        String sql = "SELECT * FROM subproducto WHERE ClaveAlterna = ?";

        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ClaveAlterna);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pro = new productos(); // Crear un nuevo objeto solo si se encuentra un resultado
                    pro.setCodigo(rs.getString("Codigo"));
                    pro.setNombre(rs.getString("Nombre")); // Agregar otras propiedades según sea necesario
                    pro.setClavealterna(rs.getString("ClaveAlterna"));
                } else {
                    System.out.println("No se encontró un producto con la ClaveAlterna: " + ClaveAlterna);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pro;
    }

    public productos BuscarClave(String Codigo) {
        productos pro = new productos();
        String sql = "SELECT * FROM producto WHERE Codigo = ?";

        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, Codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pro.setCodigo(rs.getString("Codigo"));
                    pro.setNombre(rs.getString("Nombre"));
                    pro.setPrecio(rs.getDouble("Precio"));
                    pro.setExistencia(rs.getDouble("Existencia"));
                    pro.setMarca(rs.getString("Marca"));
                    pro.setClavealterna(rs.getString("ClaveAlterna"));
                    pro.setGranel(rs.getString("Granel"));
                    pro.setImagen(rs.getBytes("Imagen")); // Agrega el campo de imagen
                    pro.setCategoria(rs.getString("Categoria")); // Agrega el campo de categoría
                    pro.setUnidad(rs.getString("Unidad")); // Agrega el campo de unidad
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pro;
    }

    public boolean actualizarCantidadProducto(String codigo, int cantidadAAgregar) {
        String sql = "UPDATE producto SET Existencia = Existencia + ? WHERE Codigo = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidadAAgregar);
            ps.setString(2, codigo);

            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cantidad de producto: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.toString());
            }
        }
    }

}
