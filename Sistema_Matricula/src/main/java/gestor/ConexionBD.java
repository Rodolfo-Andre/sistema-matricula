package gestor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_matricula?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; //contraseña de  su MySQL 

    public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
        }
        return conn;
    }

    public static void inicializarBD() {
        try (Connection conn = conectar()) {
            if (conn != null) {
                System.out.println("Conexión exitosa a MySQL (sistema_matricula).");
            }
        } catch (SQLException e) {
            System.out.println("Error de verificación: " + e.getMessage());
        }
    }
}