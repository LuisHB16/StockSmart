package stocksmart;

import java.sql.Connection;
import codigo.conexion;

public class ConexionPrediccion {
    public static Connection obtenerConexion() {
        conexion con = new conexion();
        return con.getConnection();
    }
}