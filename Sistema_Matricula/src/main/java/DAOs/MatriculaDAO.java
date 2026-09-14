package DAOs;

import gestor.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Matricula;

public class MatriculaDAO {

    private Matricula mapRow(ResultSet rs) throws SQLException {
        Matricula m = new Matricula(
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
        m.setEstadoDetalle(rs.getString("estado_detalle"));
        return m;
    }

    public List<Matricula> listar(boolean soloActivas) {
        List<Matricula> lista = new ArrayList<>();
        String sql = "{CALL sp_ListarMatriculas(?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setByte(1, (byte) (soloActivas ? 1 : 0));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar matrículas: " + e.getMessage());
        }
        return lista;
    }

    public List<Matricula> listar() {
        return listar(true);
    }

    public List<Matricula> listarAnuladas() {
        List<Matricula> todas = listar(false);
        List<Matricula> res = new ArrayList<>();
        for (Matricula m : todas) {
            if ("ANULADA".equals(m.getEstado()) || "INACTIVO".equals(m.getEstadoDetalle())) {
                res.add(m);
            }
        }
        return res;
    }

    public boolean existeMatricula(String codigoEstudiante, String codigoCurso, String periodo) {
        String sql = "{CALL sp_ExisteMatricula(?, ?, ?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, codigoEstudiante);
            cs.setString(2, codigoCurso);
            cs.setString(3, periodo);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar matricula: " + e.getMessage());
        }
        return false;
    }

    public boolean existeCursoProfesor(String codigoCurso, String periodo) {
        String sql = "{CALL sp_ExisteCursoProfesor(?, ?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, codigoCurso);
            cs.setString(2, periodo);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar curso_profesor: " + e.getMessage());
        }
        return false;
    }

    public int getIdCursoProfesor(String codigoCurso, String periodo) {
        String sql = "{CALL sp_GetIdCursoProfesor(?, ?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, codigoCurso);
            cs.setString(2, periodo);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener curso_profesor: " + e.getMessage());
        }
        return -1;
    }

    public boolean existeTraslapeHorario(String codigoEstudiante, String periodo, int idCursoProfesor) {
        String sql = "{CALL sp_ExisteTraslapeHorario(?, ?, ?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, codigoEstudiante);
            cs.setString(2, periodo);
            cs.setInt(3, idCursoProfesor);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar traslape: " + e.getMessage());
        }
        return false;
    }

    public int getIdMatriculaByCodigo(String codigoMatricula) {
        String sql = "{CALL sp_GetIdMatriculaByCodigo(?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, codigoMatricula);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_matricula");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al resolver matrícula: " + e.getMessage());
        }
        return -1;
    }

    public int getTotalCabecerasActivas() {
        String sql = "{CALL sp_TotalCabecerasActivas()}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error en conteo cabeceras: " + e.getMessage());
        }
        return 0;
    }

    public boolean agregarMatricula(Matricula matricula) {
        if (existeMatricula(matricula.getCodigoEstudiante(), matricula.getCodigoCurso(), matricula.getPeriodo())) {
            return false;
        }
        int idCp = getIdCursoProfesor(matricula.getCodigoCurso(), matricula.getPeriodo());
        if (idCp == -1) {
            return false;
        }
        if (existeTraslapeHorario(matricula.getCodigoEstudiante(), matricula.getPeriodo(), idCp)) {
            return false;
        }
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            int idMatricula = -1;
            try (CallableStatement cs = conn.prepareCall("{CALL sp_BuscarCabecera(?, ?)}")) {
                cs.setString(1, matricula.getCodigoEstudiante());
                cs.setString(2, matricula.getPeriodo());
                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        idMatricula = rs.getInt("id_matricula");
                        if ("ANULADA".equals(rs.getString("estado"))) {
                            try (CallableStatement up = conn.prepareCall("{CALL sp_ReactivarCabecera(?, ?)}")) {
                                up.setInt(1, idMatricula);
                                up.setDate(2, java.sql.Date.valueOf(matricula.getFechaMatricula()));
                                up.executeUpdate();
                            }
                        }
                    }
                }
            }

            if (idMatricula == -1) {
                try (CallableStatement cs = conn.prepareCall("{CALL sp_CrearCabecera(?, ?, ?, ?)}")) {
                    cs.setString(1, matricula.getCodigoEstudiante());
                    cs.setDate(2, java.sql.Date.valueOf(matricula.getFechaMatricula()));
                    cs.setString(3, matricula.getPeriodo());
                    cs.registerOutParameter(4, Types.INTEGER);
                    cs.executeUpdate();
                    idMatricula = cs.getInt(4);
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 1062) {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        conn.close();
                        return agregarMatricula(matricula);
                    }
                    throw ex;
                }
            }

            if (idMatricula <= 0) {
                conn.rollback();
                return false;
            }

            String estadoDet = null;
            try (CallableStatement cs = conn.prepareCall("{CALL sp_VerDetalle(?, ?)}")) {
                cs.setInt(1, idMatricula);
                cs.setInt(2, idCp);
                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        estadoDet = rs.getString(1);
                    }
                }
            }

            if ("ACTIVO".equals(estadoDet)) {
                conn.rollback();
                return false;
            }
            if ("INACTIVO".equals(estadoDet)) {
                try (CallableStatement cs = conn.prepareCall("{CALL sp_ReactivarDetalle(?, ?)}")) {
                    cs.setInt(1, idMatricula);
                    cs.setInt(2, idCp);
                    cs.executeUpdate();
                }
                conn.commit();
                return true;
            }

            try (CallableStatement cs = conn.prepareCall("{CALL sp_AgregarDetalleMatricula(?, ?)}")) {
                cs.setInt(1, idMatricula);
                cs.setInt(2, idCp);
                cs.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error al agregar matricula: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Rollback: " + ex.getMessage()); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { System.err.println("Cierre: " + ex.getMessage()); }
            }
        }
    }

    public boolean registrarMatriculaCompleta(Matricula matricula, List<Integer> idCursosProfesor) {
        String sqlMatricula = "{CALL sp_RegistrarMatricula(?, ?, ?)}";
        String sqlDetalle = "{CALL sp_AgregarDetalleMatricula(?, ?)}";
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false);
            int idGenerada = -1;
            try (CallableStatement csMat = conn.prepareCall(sqlMatricula)) {
                csMat.setInt(1, matricula.getIdEstudiante());
                csMat.setString(2, matricula.getPeriodo());
                csMat.registerOutParameter(3, Types.INTEGER);
                csMat.executeUpdate();
                idGenerada = csMat.getInt(3);
            }
            if (idGenerada > 0) {
                try (CallableStatement csDet = conn.prepareCall(sqlDetalle)) {
                    for (Integer idCP : idCursosProfesor) {
                        csDet.setInt(1, idGenerada);
                        csDet.setInt(2, idCP);
                        csDet.addBatch();
                    }
                    csDet.executeBatch();
                }
            } else {
                throw new SQLException("No se pudo obtener el ID de la matrícula generada.");
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Error en Rollback: " + ex.getMessage()); }
            }
            System.err.println("Error en la transacción de matrícula: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { System.err.println("Error cerrando conexión: " + e.getMessage()); }
            }
        }
    }

    public boolean anularPeriodo(String codigoMatricula) {
        int id = getIdMatriculaByCodigo(codigoMatricula);
        if (id == -1) return false;
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall("{CALL sp_AnularPeriodo(?)}")) {
            cs.setInt(1, id);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al anular periodo: " + e.getMessage());
            return false;
        }
    }

    public boolean quitarCurso(String codigoMatricula, String codigoCurso) {
        int id = getIdMatriculaByCodigo(codigoMatricula);
        if (id == -1) return false;
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall("{CALL sp_QuitarCurso(?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, codigoCurso);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al quitar curso: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarCurso(String codigoMatricula, String codigoCurso) {
        int id = getIdMatriculaByCodigo(codigoMatricula);
        if (id == -1) return false;
        String periodo = null;
        String codigoEst = null;
        for (Matricula m : listar(false)) {
            if (codigoMatricula.equals(m.getCodigoMatricula()) && codigoCurso.equals(m.getCodigoCurso())) {
                periodo = m.getPeriodo();
                codigoEst = m.getCodigoEstudiante();
                break;
            }
        }
        if (periodo == null) return false;
        int idCp = getIdCursoProfesor(codigoCurso, periodo);
        if (idCp == -1) return false;
        if (existeTraslapeHorario(codigoEst, periodo, idCp)) {
            return false;
        }
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall("{CALL sp_ReactivarCurso(?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, codigoCurso);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar curso: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idMatricula) {
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall("{CALL sp_AnularPeriodo(?)}")) {
            cs.setInt(1, idMatricula);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar matrícula: " + e.getMessage());
            return false;
        }
    }
}
