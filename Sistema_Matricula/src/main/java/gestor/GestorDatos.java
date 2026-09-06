package gestor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;

public class GestorDatos {


    public static List<Estudiante> getEstudiantes() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT e.codigo, CONCAT(e.nombres, ' ', e.apellidos) AS nombre_completo, c.nombre AS carrera "
                   + "FROM estudiantes e "
                   + "JOIN carreras c ON e.id_carrera = c.id_carrera;";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Estudiante(
                    rs.getString("codigo"),
                    rs.getString("nombre_completo"),
                    rs.getString("carrera"),
                    1
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener estudiantes: " + e.getMessage());
        }
        return lista;
    }

    public static void agregarEstudiante(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes (codigo, dni, nombres, apellidos, id_carrera) VALUES (?, ?, ?, ?, ?);";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String[] partesNombre = estudiante.getNombre().split(" ", 2);
            String nombres = partesNombre[0];
            String apellidos = (partesNombre.length > 1) ? partesNombre[1] : "";

            pstmt.setString(1, estudiante.getCodigo());
            pstmt.setString(2, "DNI" + System.currentTimeMillis() % 100000000); 
            pstmt.setString(3, nombres);
            pstmt.setString(4, apellidos);
            pstmt.setInt(5, 1); 
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al agregar estudiante: " + e.getMessage());
        }
    }

    public static Estudiante buscarEstudiante(String codigo) {
        String sql = "SELECT e.codigo, CONCAT(e.nombres, ' ', e.apellidos) AS nombre_completo, c.nombre AS carrera "
                   + "FROM estudiantes e JOIN carreras c ON e.id_carrera = c.id_carrera "
                   + "WHERE e.codigo = ?;";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Estudiante(
                    rs.getString("codigo"),
                    rs.getString("nombre_completo"),
                    rs.getString("carrera"),
                    1
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar estudiante: " + e.getMessage());
        }
        return null;
    }

    public static boolean eliminarEstudiante(String codigo) {
        String sql = "DELETE FROM estudiantes WHERE codigo = ?;";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }

    public static List<String> getCarreras() {
        List<String> carreras = new ArrayList<>();
        String sql = "SELECT nombre FROM carreras;";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                carreras.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener carreras: " + e.getMessage());
        }
        return carreras;
    }

    public static List<Curso> getCursos() {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre, creditos FROM cursos;";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Curso(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getInt("creditos")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cursos: " + e.getMessage());
        }
        return lista;
    }

    public static void agregarCurso(Curso curso) {
        String sql = "INSERT INTO cursos (codigo, nombre, creditos) VALUES (?, ?, ?);";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, curso.getCodigo());
            pstmt.setString(2, curso.getNombre());
            pstmt.setInt(3, curso.getCreditos());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al agregar curso: " + e.getMessage());
        }
    }

    public static Curso buscarCurso(String codigo) {
        String sql = "SELECT codigo, nombre, creditos FROM cursos WHERE codigo = ?;";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Curso(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getInt("creditos")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar curso: " + e.getMessage());
        }
        return null;
    }

    public static boolean eliminarCurso(String codigo) {
        String sql = "DELETE FROM cursos WHERE codigo = ?;";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar curso: " + e.getMessage());
            return false;
        }
    }

    public static List<Matricula> getMatriculas() {
        List<Matricula> lista = new ArrayList<>();
        // MySQL: CONCAT('MAT-', m.id_matricula)
        String sql = "SELECT CONCAT('MAT-', m.id_matricula) AS codigo_matricula, "
                   + "e.codigo AS codigo_estudiante, "
                   + "COALESCE(c.codigo, 'SIN_CURSO') AS codigo_curso, "
                   + "m.fecha_matricula "
                   + "FROM matriculas m "
                   + "JOIN estudiantes e ON m.id_estudiante = e.id_estudiante "
                   + "LEFT JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula "
                   + "LEFT JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor "
                   + "LEFT JOIN cursos c ON cp.id_curso = c.id_curso;";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Matricula(
                    rs.getString("codigo_matricula"),
                    rs.getString("codigo_estudiante"),
                    rs.getString("codigo_curso"),
                    rs.getString("fecha_matricula")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener matrículas: " + e.getMessage());
        }
        return lista;
    }

    public static int getTotalEstudiantes() {
        return obtenerConteoSQL("SELECT COUNT(*) FROM estudiantes;");
    }

    public static int getTotalCursos() {
        return obtenerConteoSQL("SELECT COUNT(*) FROM cursos;");
    }

    public static int getTotalMatriculas() {
        return obtenerConteoSQL("SELECT COUNT(*) FROM matriculas;");
    }

    private static int obtenerConteoSQL(String sql) {
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error en conteo SQL: " + e.getMessage());
        }
        return 0;
    }

    public static String getEstadisticasPorCarrera() {
        StringBuilder resultado = new StringBuilder("Estadísticas por carrera:\n");
        String sql = "SELECT c.nombre, COUNT(e.id_estudiante) AS total "
                   + "FROM carreras c LEFT JOIN estudiantes e ON c.id_carrera = e.id_carrera "
                   + "GROUP BY c.nombre;";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultado.append("  - ").append(rs.getString("nombre"))
                         .append(": ").append(rs.getInt("total"))
                         .append(" estudiantes\n");
            }
        } catch (SQLException e) {
            return "Error al calcular estadísticas por carrera";
        }
        return resultado.toString();
    }

    public static String getReporteCompleto() {
        StringBuilder reporte = new StringBuilder("REPORTE COMPLETO DEL SISTEMA (BASE DE DATOS SQL)\n");
        reporte.append("===================================================\n\n");
        reporte.append("ESTUDIANTES:\n  Total: ").append(getTotalEstudiantes()).append("\n");
        reporte.append(getEstadisticasPorCarrera()).append("\n");
        reporte.append("CURSOS:\n  Total: ").append(getTotalCursos()).append("\n\n");
        reporte.append("MATRÍCULAS:\n  Total: ").append(getTotalMatriculas());
        return reporte.toString();
    }

    public static boolean tieneDatos() {
        return getTotalEstudiantes() > 0 || getTotalCursos() > 0;
    }

    public static void cargarDatosEjemplo() {
        ConexionBD.inicializarBD();
    }
}