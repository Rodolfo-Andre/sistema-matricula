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
        return getMatriculas(true);
    }

    public static List<Matricula> getMatriculas(boolean soloActivas) {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT m.id_matricula, CONCAT('MAT-', m.id_matricula) AS codigo_matricula, "
                   + "e.codigo AS codigo_estudiante, "
                   + "CONCAT(e.nombres, ' ', e.apellidos) AS nombre_estudiante, "
                   + "COALESCE(c.codigo, 'SIN_CURSO') AS codigo_curso, "
                   + "COALESCE(c.nombre, 'Sin curso asignado') AS nombre_curso, "
                   + "CONCAT(p.nombres, ' ', p.apellidos) AS nombre_profesor, "
                   + "CONCAT(h.dia, ' ', SUBSTRING(h.hora_inicio, 1, 5), '-', SUBSTRING(h.hora_fin, 1, 5), ' Aula:', h.aula) AS horario, "
                   + "car.nombre AS nombre_carrera, "
                   + "m.fecha_matricula, "
                   + "m.periodo, "
                   + "m.estado, "
                   + "COALESCE(dm.estado, 'ACTIVO') AS estado_detalle "
                   + "FROM matriculas m "
                   + "JOIN estudiantes e ON m.id_estudiante = e.id_estudiante "
                   + "LEFT JOIN carreras car ON e.id_carrera = car.id_carrera "
                   + "LEFT JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula "
                   + "LEFT JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor "
                   + "LEFT JOIN cursos c ON cp.id_curso = c.id_curso "
                   + "LEFT JOIN profesores p ON cp.id_profesor = p.id_profesor "
                   + "LEFT JOIN horarios h ON cp.id_horario = h.id_horario "
                   + (soloActivas ? "WHERE m.estado = 'ACTIVA' AND COALESCE(dm.estado, 'ACTIVO') = 'ACTIVO' " : "")
                   + "ORDER BY m.id_matricula;";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Matricula mat = new Matricula(
                    rs.getInt("id_matricula"),
                    rs.getString("codigo_matricula"),
                    rs.getString("codigo_estudiante"),
                    rs.getString("codigo_curso"),
                    rs.getString("fecha_matricula"),
                    rs.getString("nombre_estudiante"),
                    rs.getString("nombre_curso"),
                    rs.getString("nombre_profesor"),
                    rs.getString("horario"),
                    rs.getString("nombre_carrera"),
                    rs.getString("periodo"),
                    rs.getString("estado")
                );
                mat.setEstadoDetalle(rs.getString("estado_detalle"));
                lista.add(mat);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener matrículas: " + e.getMessage());
        }
        return lista;
    }

    public static List<Matricula> getMatriculasAnuladas() {
        List<Matricula> todas = getMatriculas(false);
        List<Matricula> anuladas = new ArrayList<>();
        for (Matricula m : todas) {
            if ("ANULADA".equals(m.getEstado()) || "INACTIVO".equals(m.getEstadoDetalle())) {
                anuladas.add(m);
            }
        }
        return anuladas;
    }

    public static int getTotalCabecerasActivas() {
        return obtenerConteoSQL("SELECT COUNT(*) FROM matriculas WHERE estado = 'ACTIVA';");
    }

    public static int getTotalEstudiantes() {
        return obtenerConteoSQL("SELECT COUNT(*) FROM estudiantes;");
    }

    public static int getTotalCursos() {
        return obtenerConteoSQL("SELECT COUNT(*) FROM cursos;");
    }

    public static int getTotalMatriculas() {
        return obtenerConteoSQL("SELECT COUNT(*) FROM matriculas WHERE estado = 'ACTIVA';");
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

    public static boolean existeMatricula(String codigoEstudiante, String codigoCurso, String periodo) {
        String sql = "SELECT COUNT(*) FROM matriculas m "
                   + "JOIN estudiantes e ON m.id_estudiante = e.id_estudiante "
                   + "JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula "
                   + "JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor "
                   + "JOIN cursos c ON cp.id_curso = c.id_curso "
                   + "WHERE e.codigo = ? AND c.codigo = ? AND m.periodo = ? AND m.estado = 'ACTIVA' AND dm.estado = 'ACTIVO';";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoEstudiante);
            pstmt.setString(2, codigoCurso);
            pstmt.setString(3, periodo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar matricula: " + e.getMessage());
        }
        return false;
    }

    public static boolean existeCursoProfesor(String codigoCurso, String periodo) {
        String sql = "SELECT COUNT(*) FROM curso_profesor cp "
                   + "JOIN cursos c ON cp.id_curso = c.id_curso "
                   + "WHERE c.codigo = ? AND cp.periodo = ?;";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoCurso);
            pstmt.setString(2, periodo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar curso_profesor: " + e.getMessage());
        }
        return false;
    }

    public static int getIdCursoProfesor(String codigoCurso, String periodo) {
        String sql = "SELECT cp.id_curso_profesor FROM curso_profesor cp "
                   + "JOIN cursos c ON cp.id_curso = c.id_curso "
                   + "WHERE c.codigo = ? AND cp.periodo = ? LIMIT 1;";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoCurso);
            pstmt.setString(2, periodo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener curso_profesor: " + e.getMessage());
        }
        return -1;
    }

    public static boolean existeTraslapeHorario(String codigoEstudiante, String periodo, int idCursoProfesorNuevo) {
        String sql = "SELECT COUNT(*) FROM matriculas m "
                   + "JOIN estudiantes e ON m.id_estudiante = e.id_estudiante "
                   + "JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula "
                   + "JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor "
                   + "JOIN horarios h ON cp.id_horario = h.id_horario "
                   + "JOIN curso_profesor cpN ON cpN.id_curso_profesor = ? "
                   + "JOIN horarios hN ON cpN.id_horario = hN.id_horario "
                   + "WHERE e.codigo = ? AND m.periodo = ? AND m.estado = 'ACTIVA' AND dm.estado = 'ACTIVO' "
                   + "AND h.dia = hN.dia "
                   + "AND h.hora_inicio < hN.hora_fin AND h.hora_fin > hN.hora_inicio;";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCursoProfesorNuevo);
            pstmt.setString(2, codigoEstudiante);
            pstmt.setString(3, periodo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar traslape: " + e.getMessage());
        }
        return false;
    }

    public static boolean agregarMatricula(Matricula matricula) {
        if (existeMatricula(matricula.getCodigoEstudiante(), matricula.getCodigoCurso(), matricula.getPeriodo())) {
            System.out.println("Error: Ya existe una matricula para este estudiante en este curso y periodo.");
            return false;
        }

        int idCursoProfesor = getIdCursoProfesor(matricula.getCodigoCurso(), matricula.getPeriodo());
        if (idCursoProfesor == -1) {
            System.out.println("Error: No hay asignacion curso-profesor disponible para este curso en el periodo.");
            return false;
        }

        if (existeTraslapeHorario(matricula.getCodigoEstudiante(), matricula.getPeriodo(), idCursoProfesor)) {
            System.out.println("Error: Traslape de horario con otro curso del estudiante en el periodo.");
            return false;
        }

        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            int idMatricula = -1;
            String sqlBuscar = "SELECT m.id_matricula, m.estado FROM matriculas m "
                             + "JOIN estudiantes e ON m.id_estudiante = e.id_estudiante "
                             + "WHERE e.codigo = ? AND m.periodo = ? LIMIT 1;";
            try (PreparedStatement ps = conn.prepareStatement(sqlBuscar)) {
                ps.setString(1, matricula.getCodigoEstudiante());
                ps.setString(2, matricula.getPeriodo());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idMatricula = rs.getInt(1);
                        String est = rs.getString(2);
                        if ("ANULADA".equals(est)) {
                            try (PreparedStatement up = conn.prepareStatement(
                                    "UPDATE matriculas SET estado = 'ACTIVA', fecha_matricula = ? WHERE id_matricula = ?;")) {
                                up.setString(1, matricula.getFechaMatricula());
                                up.setInt(2, idMatricula);
                                up.executeUpdate();
                            }
                        }
                    }
                }
            }

            if (idMatricula == -1) {
                String sqlCab = "INSERT INTO matriculas (id_estudiante, fecha_matricula, periodo, estado) "
                              + "SELECT e.id_estudiante, ?, ?, 'ACTIVA' FROM estudiantes e WHERE e.codigo = ?;";
                try (PreparedStatement ps = conn.prepareStatement(sqlCab, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, matricula.getFechaMatricula());
                    ps.setString(2, matricula.getPeriodo());
                    ps.setString(3, matricula.getCodigoEstudiante());
                    int filas = ps.executeUpdate();
                    if (filas == 0) {
                        conn.rollback();
                        return false;
                    }
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) idMatricula = keys.getInt(1);
                    }
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 1062) {
                        conn.rollback();
                        return agregarMatricula(matricula);
                    }
                    throw ex;
                }
            }

            if (idMatricula == -1) {
                conn.rollback();
                return false;
            }

            String sqlVerDet = "SELECT estado FROM detalle_matricula WHERE id_matricula = ? AND id_curso_profesor = ? LIMIT 1;";
            try (PreparedStatement ps = conn.prepareStatement(sqlVerDet)) {
                ps.setInt(1, idMatricula);
                ps.setInt(2, idCursoProfesor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if ("ACTIVO".equals(rs.getString(1))) {
                            conn.rollback();
                            return false;
                        }
                        try (PreparedStatement up = conn.prepareStatement(
                                "UPDATE detalle_matricula SET estado = 'ACTIVO' WHERE id_matricula = ? AND id_curso_profesor = ?;")) {
                            up.setInt(1, idMatricula);
                            up.setInt(2, idCursoProfesor);
                            up.executeUpdate();
                        }
                        conn.commit();
                        return true;
                    }
                }
            }

            String sqlDet = "INSERT INTO detalle_matricula (id_matricula, id_curso_profesor, estado) VALUES (?, ?, 'ACTIVO');";
            try (PreparedStatement ps = conn.prepareStatement(sqlDet)) {
                ps.setInt(1, idMatricula);
                ps.setInt(2, idCursoProfesor);
                ps.executeUpdate();
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1062) {
                    System.out.println("Error: el curso ya esta en esta matricula.");
                }
                throw ex;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al agregar matricula: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback: " + ex.getMessage()); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { System.out.println("Cierre: " + ex.getMessage()); }
            }
        }
    }

    public static boolean agregarCursoAMatricula(String codigoMatricula, String codigoCurso, String periodo) {
        String sqlId = "SELECT id_matricula FROM matriculas WHERE CONCAT('MAT-', id_matricula) = ? AND estado = 'ACTIVA';";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sqlId)) {
            ps.setString(1, codigoMatricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean anularMatricula(String codigoMatricula) {
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE matriculas SET estado = 'ANULADA' WHERE CONCAT('MAT-', id_matricula) = ? AND estado = 'ACTIVA';")) {
                ps.setString(1, codigoMatricula);
                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE detalle_matricula dm JOIN matriculas m ON dm.id_matricula = m.id_matricula "
                    + "SET dm.estado = 'INACTIVO' WHERE CONCAT('MAT-', m.id_matricula) = ? AND dm.estado = 'ACTIVO';")) {
                ps.setString(1, codigoMatricula);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al anular matricula: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback: " + ex.getMessage()); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { System.out.println("Cierre: " + ex.getMessage()); }
            }
        }
    }

    public static boolean reactivarMatricula(String codigoMatricula) {
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE matriculas SET estado = 'ACTIVA' WHERE CONCAT('MAT-', id_matricula) = ? AND estado = 'ANULADA';")) {
                ps.setString(1, codigoMatricula);
                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE detalle_matricula dm JOIN matriculas m ON dm.id_matricula = m.id_matricula "
                    + "SET dm.estado = 'ACTIVO' WHERE CONCAT('MAT-', m.id_matricula) = ? AND dm.estado = 'INACTIVO';")) {
                ps.setString(1, codigoMatricula);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al reactivar matricula: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback: " + ex.getMessage()); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { System.out.println("Cierre: " + ex.getMessage()); }
            }
        }
    }

    public static boolean quitarCursoDeMatricula(String codigoMatricula, String codigoCurso) {
        String sql = "UPDATE detalle_matricula dm "
                + "JOIN matriculas m ON dm.id_matricula = m.id_matricula "
                + "JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor "
                + "JOIN cursos c ON cp.id_curso = c.id_curso "
                + "SET dm.estado = 'INACTIVO' "
                + "WHERE CONCAT('MAT-', m.id_matricula) = ? AND c.codigo = ? AND m.estado = 'ACTIVA' AND dm.estado = 'ACTIVO';";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoMatricula);
            pstmt.setString(2, codigoCurso);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al quitar curso: " + e.getMessage());
            return false;
        }
    }

    public static boolean reactivarCursoDeMatricula(String codigoMatricula, String codigoCurso) {
        String periodo = null;
        String codigoEstudiante = null;
        int idCp = -1;
        for (Matricula m : getMatriculas(false)) {
            if (m.getCodigoMatricula().equals(codigoMatricula) && m.getCodigoCurso().equals(codigoCurso)) {
                periodo = m.getPeriodo();
                codigoEstudiante = m.getCodigoEstudiante();
                idCp = getIdCursoProfesor(codigoCurso, periodo);
                break;
            }
        }
        if (idCp == -1 || periodo == null) return false;
        if (existeTraslapeHorario(codigoEstudiante, periodo, idCp)) {
            System.out.println("Error: traslape al reactivar curso.");
            return false;
        }

        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE matriculas SET estado = 'ACTIVA' WHERE CONCAT('MAT-', id_matricula) = ? AND estado = 'ANULADA';")) {
                ps.setString(1, codigoMatricula);
                ps.executeUpdate();
            }

            int filas;
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE detalle_matricula dm "
                    + "JOIN matriculas m ON dm.id_matricula = m.id_matricula "
                    + "JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor "
                    + "JOIN cursos c ON cp.id_curso = c.id_curso "
                    + "SET dm.estado = 'ACTIVO' "
                    + "WHERE CONCAT('MAT-', m.id_matricula) = ? AND c.codigo = ? AND m.estado = 'ACTIVA' AND dm.estado = 'INACTIVO';")) {
                ps.setString(1, codigoMatricula);
                ps.setString(2, codigoCurso);
                filas = ps.executeUpdate();
            }

            if (filas == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al reactivar curso: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback: " + ex.getMessage()); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { System.out.println("Cierre: " + ex.getMessage()); }
            }
        }
    }

    public static boolean eliminarMatricula(String codigoMatricula) {
        return anularMatricula(codigoMatricula);
    }

    public static boolean tieneDatos() {
        return getTotalEstudiantes() > 0 || getTotalCursos() > 0;
    }

    public static void cargarDatosEjemplo() {
        ConexionBD.inicializarBD();
    }
}