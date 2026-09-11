package gestor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

   private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=sistema_matricula;encrypt=true;trustServerCertificate=true;";
   private static final String USER = "sa";  
   private static final String PASSWORD = "sql";

   public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error al conectar con SQL Server: " + e.getMessage());
        }
        return conn;
    }

    public static void inicializarBD() {
        try (Connection conn = conectar()) {
            if (conn != null) {
                System.out.println("Conexión exitosa a SQL Server (sistema_matricula).");
            }
        } catch (SQLException e) {
            System.out.println("Error de verificación: " + e.getMessage());
        }
    }
}