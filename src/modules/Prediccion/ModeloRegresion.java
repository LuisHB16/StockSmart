package modules.Prediccion;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ModeloRegresion {
    private double b0;
    private double b1;
    private double b2;
    private LocalDate fechaEntrenamiento;

    public ModeloRegresion() {
        System.out.println("ModeloRegresion: Constructor llamado.");
    }

    public boolean entrenarYGuardar(Connection conn) {
        System.out.println("ModeloRegresion: Iniciando entrenamiento y guardado del modelo...");
        try {
            // Obtener datos de ventas de los últimos 3 años
            System.out.println("ModeloRegresion: Obteniendo datos de ventas...");
            String sql = "SELECT YEAR(Fecha) as anio, MONTH(Fecha) as mes, SUM(Total) as ventas FROM venta WHERE Fecha >= ? GROUP BY anio, mes ORDER BY anio, mes";
            PreparedStatement ps = conn.prepareStatement(sql);
            LocalDate hace3anios = LocalDate.now().minusYears(3);
            ps.setString(1, hace3anios.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> X1 = new ArrayList<>();
            ArrayList<Integer> X2 = new ArrayList<>();
            ArrayList<Double> Y = new ArrayList<>();
            while (rs.next()) {
                X1.add(rs.getInt("anio"));
                X2.add(rs.getInt("mes"));
                Y.add(rs.getDouble("ventas"));
            }
            int n = Y.size();
            System.out.println("ModeloRegresion: Número de registros de ventas obtenidos: " + n);
            // Entrenar solo si hay al menos 3 puntos de datos para la regresión múltiple
            if (n == 0) {
                System.out.println("ModeloRegresion: No hay suficientes datos (se requieren >= 3) para entrenar. Guardando modelo con coeficientes 0.");
                // Si hay muy pocos datos, igual se guarda el modelo con coeficientes 0 para evitar errores
                b0 = 0;
                b1 = 0;
                b2 = 0;
                fechaEntrenamiento = LocalDate.now();
            } else {
                System.out.println("ModeloRegresion: Calculando coeficientes de regresión...");
                double mean_X1 = X1.stream().mapToInt(Integer::intValue).average().orElse(0);
                double mean_X2 = X2.stream().mapToInt(Integer::intValue).average().orElse(0);
                double mean_Y = Y.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                double numerador_b1 = 0, numerador_b2 = 0, denominador_b1 = 0, denominador_b2 = 0, denominador_b12 = 0;
                for (int i = 0; i < n; i++) {
                    double x1_i = X1.get(i) - mean_X1;
                    double x2_i = X2.get(i) - mean_X2;
                    double y_i = Y.get(i) - mean_Y;
                    numerador_b1 += x1_i * y_i;
                    numerador_b2 += x2_i * y_i;
                    denominador_b1 += x1_i * x1_i;
                    denominador_b2 += x2_i * x2_i;
                    denominador_b12 += x1_i * x2_i;
                }
                double D = denominador_b1 * denominador_b2 - denominador_b12 * denominador_b12;
                System.out.println("ModeloRegresion: Determinante D = " + D);
                if (D == 0) {
                    System.out.println("ModeloRegresion: Determinante D es 0. Usando valores por defecto.");
                    b0 = mean_Y;
                    b1 = 0;
                    b2 = 0;
                } else {
                    double D1 = numerador_b1 * denominador_b2 - numerador_b2 * denominador_b12;
                    double D2 = denominador_b1 * numerador_b2 - numerador_b1 * denominador_b12;
                    b1 = D1 / D;
                    b2 = D2 / D;
                    b0 = mean_Y - b1 * mean_X1 - b2 * mean_X2;
                }
                fechaEntrenamiento = LocalDate.now();
                System.out.println("ModeloRegresion: Coeficientes calculados: b0=" + b0 + ", b1=" + b1 + ", b2=" + b2);
            }
            // Guardar en la base de datos
            System.out.println("ModeloRegresion: Guardando modelo en la base de datos...");
            String insert = "INSERT INTO model (fecha_entrenamiento, b0, b1, b2) VALUES (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(insert);
            ps2.setString(1, fechaEntrenamiento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ps2.setDouble(2, b0);
            ps2.setDouble(3, b1);
            ps2.setDouble(4, b2);
            ps2.executeUpdate();
            System.out.println("ModeloRegresion: Modelo guardado exitosamente.");
            return true;
        } catch (Exception ex) {
            System.err.println("ModeloRegresion: Error durante el entrenamiento y guardado: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public boolean cargarUltimoModelo(Connection conn) {
        System.out.println("ModeloRegresion: Cargando último modelo desde la base de datos...");
        try {
            String sql = "SELECT * FROM model ORDER BY fecha_entrenamiento DESC LIMIT 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                b0 = rs.getDouble("b0");
                b1 = rs.getDouble("b1");
                b2 = rs.getDouble("b2");
                fechaEntrenamiento = LocalDate.parse(rs.getString("fecha_entrenamiento"));
                System.out.println("ModeloRegresion: Último modelo cargado exitosamente. Fecha: " + fechaEntrenamiento);
                System.out.println("ModeloRegresion: Coeficientes cargados: b0=" + b0 + ", b1=" + b1 + ", b2=" + b2);
                return true;
            } else {
                System.out.println("ModeloRegresion: No se encontró ningún modelo guardado.");
            }
        } catch (Exception ex) {
            System.err.println("ModeloRegresion: Error al cargar el último modelo: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public boolean modeloActualizado() {
        System.out.println("ModeloRegresion: Verificando si el modelo está actualizado...");
        if (fechaEntrenamiento == null) {
            System.out.println("ModeloRegresion: No hay fecha de entrenamiento disponible.");
            return false;
        }
        LocalDate ahora = LocalDate.now();
        boolean actualizado = fechaEntrenamiento.plusMonths(3).isAfter(ahora);
        System.out.println("ModeloRegresion: Fecha entrenamiento: " + fechaEntrenamiento + ", Fecha actual: " + ahora + ", Actualizado: " + actualizado);
        return actualizado;
    }

    public double getB0() { return b0; }
    public double getB1() { return b1; }
    public double getB2() { return b2; }
}