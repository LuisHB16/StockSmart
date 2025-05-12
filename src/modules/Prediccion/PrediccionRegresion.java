package modules.Prediccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PrediccionRegresion {
    private ModeloRegresion modelo;

    public PrediccionRegresion(ModeloRegresion modelo) {
        System.out.println("PrediccionRegresion: Constructor llamado.");
        this.modelo = modelo;
    }

    public double predecir(int anio, int mes) {
        System.out.println("PrediccionRegresion: Iniciando predicción para año=" + anio + ", mes=" + mes);
        double prediccion = modelo.getB0() + modelo.getB1() * anio + modelo.getB2() * mes;
        System.out.println("PrediccionRegresion: Predicción calculada: " + prediccion);
        return prediccion;
    }

    public boolean guardarPrediccion(Connection conn, int anio, int mes, double prediccion) {
        System.out.println("PrediccionRegresion: Guardando predicción en la base de datos: año=" + anio + ", mes=" + mes + ", prediccion=" + prediccion);
        try {
            String insert = "INSERT INTO predicciones(anio, mes, prediccion, fecha_calculo) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ps.setDouble(3, prediccion);
            ps.setString(4, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ps.executeUpdate();
            System.out.println("PrediccionRegresion: Predicción guardada exitosamente.");
            return true;
        } catch (Exception ex) {
            System.err.println("PrediccionRegresion: Error al guardar la predicción: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public void generarYGuardarPrediccionesFuturas(Connection conn, int mesesFuturos) {
        System.out.println("PrediccionRegresion: Verificando y generando predicciones futuras para " + mesesFuturos + " meses...");
        LocalDate fechaActual = LocalDate.now();
        int anioActual = fechaActual.getYear();
        int mesActual = fechaActual.getMonthValue();

        for (int i = 0; i < mesesFuturos; i++) {
            LocalDate fechaFutura = fechaActual.plusMonths(i + 1);
            int anioFuturo = fechaFutura.getYear();
            int mesFuturo = fechaFutura.getMonthValue();

            if (!existePrediccion(conn, anioFuturo, mesFuturo)) {
                System.out.println("PrediccionRegresion: Generando predicción para " + anioFuturo + "-" + mesFuturo);
                double prediccion = predecir(anioFuturo, mesFuturo);
                guardarPrediccion(conn, anioFuturo, mesFuturo, prediccion);
            } else {
                System.out.println("PrediccionRegresion: Predicción ya existe para " + anioFuturo + "-" + mesFuturo);
            }
        }
        System.out.println("PrediccionRegresion: Verificación y generación de predicciones futuras completada.");
    }

    private boolean existePrediccion(Connection conn, int anio, int mes) {
        //System.out.println("PrediccionRegresion: Verificando existencia de predicción para " + anio + "-" + mes);
        try {
            String sql = "SELECT COUNT(*) FROM predicciones WHERE anio = ? AND mes = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean existe = rs.getInt(1) > 0;
                //System.out.println("PrediccionRegresion: Predicción para " + anio + "-" + mes + (existe ? " existe." : " no existe."));
                return existe;
            }
        } catch (SQLException ex) {
            System.err.println("PrediccionRegresion: Error al verificar existencia de predicción: " + ex.getMessage());
            ex.printStackTrace();
        }
        //System.out.println("PrediccionRegresion: No se pudo verificar existencia para " + anio + "-" + mes + ". Asumiendo que no existe.");
        return false; // Asumir que no existe en caso de error
    }

    public ArrayList<Double> obtenerUltimasPredicciones(Connection conn, int cantidad) {
        System.out.println("PrediccionRegresion: Obteniendo las últimas " + cantidad + " predicciones...");
        ArrayList<Double> predicciones = new ArrayList<>();
        try {
            // Modificado para obtener las predicciones más recientes (futuras)
            String sql = "SELECT prediccion FROM predicciones ORDER BY anio DESC, mes DESC LIMIT ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                predicciones.add(rs.getDouble("prediccion"));
            }
            // Invertir la lista para tener las más futuras primero si es necesario, o ajustar la consulta SQL
            // Collections.reverse(predicciones);
            System.out.println("PrediccionRegresion: Se obtuvieron " + predicciones.size() + " predicciones.");
        } catch (Exception ex) {
            System.err.println("PrediccionRegresion: Error al obtener las últimas predicciones: " + ex.getMessage());
            ex.printStackTrace();
        }
        return predicciones;
    }
    
    public boolean eliminarPredicciones(Connection conn) {
        System.out.println("PrediccionRegresion: Eliminando todas las predicciones de la base de datos...");
        try {
            String sql = "DELETE FROM predicciones";
            Statement st = conn.createStatement();
            int filasAfectadas = st.executeUpdate(sql);
            System.out.println("PrediccionRegresion: Se eliminaron " + filasAfectadas + " predicciones de la base de datos.");
            return true;
        } catch (Exception ex) {
            System.err.println("PrediccionRegresion: Error al eliminar las predicciones: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}