package DAOs;

import gestor.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Matricula;

public class MatriculaDAO {

    public boolean registrarMatriculaCompleta(Matricula matricula, List<Integer> idCursosProfesor) {
        String sqlMatricula = "{CALL sp_RegistrarMatricula(?, ?, ?, ?)}";
        String sqlDetalle = "{CALL sp_RegistrarDetalleMatricula(?, ?)}";

        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); 

            int idMatriculaGenerada = -1;

            try (CallableStatement csMat = conn.prepareCall(sqlMatricula)) {
                csMat.setInt(1, matricula.getIdEstudiante());
                csMat.setString(2, matricula.getFechaMatricula());
                csMat.setString(3, matricula.getPeriodo());
                csMat.registerOutParameter(4, Types.INTEGER);

                csMat.executeUpdate();
                idMatriculaGenerada = csMat.getInt(4);
            }

            if (idMatriculaGenerada > 0) {
                try (CallableStatement csDet = conn.prepareCall(sqlDetalle)) {
                    for (Integer idCP : idCursosProfesor) {
                        csDet.setInt(1, idMatriculaGenerada);
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
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Error en Rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error en la transacción de matrícula: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error cerrando conexión: " + e.getMessage());
                }
            }
        }
    }

    public List<Matricula> listar() {
        List<Matricula> lista = new ArrayList<>();
        String sql = "{CALL sp_ListarMatriculas()}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Matricula(
                    rs.getInt("id_matricula"),
                    rs.getInt("id_estudiante"),
                    rs.getString("fecha_matricula"),
                    rs.getString("periodo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar matrículas: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminar(int idMatricula) {
        String sql = "{CALL sp_EliminarMatricula(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idMatricula);
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar matrícula: " + e.getMessage());
            return false;
        }
    }
}