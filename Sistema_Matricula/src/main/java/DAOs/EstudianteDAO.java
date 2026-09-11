package DAOs;

import gestor.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Estudiante;

public class EstudianteDAO {

    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "{CALL sp_ListarEstudiantes}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToEstudiante(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Estudiante estudiante) {
        String sql = "{CALL sp_InsertarEstudiante(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, estudiante.getCodigo());
            cs.setString(2, estudiante.getDni());
            cs.setString(3, estudiante.getNombres());
            cs.setString(4, estudiante.getApellidos());
            cs.setInt(5, estudiante.getIdCarrera());
            cs.setInt(6, estudiante.getCiclo());

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar estudiante: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Estudiante estudiante) {
        String sql = "{CALL sp_ActualizarEstudiante(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, estudiante.getIdEstudiante());
            cs.setString(2, estudiante.getCodigo());
            cs.setString(3, estudiante.getDni());
            cs.setString(4, estudiante.getNombres());
            cs.setString(5, estudiante.getApellidos());
            cs.setInt(6, estudiante.getIdCarrera());
            cs.setInt(7, estudiante.getCiclo());

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estudiante: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEstudiante) {
        String sql = "{CALL sp_EliminarEstudiante(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idEstudiante);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }

    public Estudiante buscarPorCodigo(String codigo) {
        String sql = "{CALL sp_BuscarEstudiantePorCodigo(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, codigo);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEstudiante(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar estudiante por código: " + e.getMessage());
        }
        return null;
    }

    public List<Estudiante> buscarPorFiltro(String filtro) {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "{CALL sp_BuscarEstudiantesPorFiltro(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, filtro);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToEstudiante(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar estudiantes por filtro: " + e.getMessage());
        }
        return lista;
    }

    private Estudiante mapResultSetToEstudiante(ResultSet rs) throws SQLException {
        return new Estudiante(
            rs.getInt("id_estudiante"),
            rs.getString("codigo"),
            rs.getString("dni"),
            rs.getString("nombres"),
            rs.getString("apellidos"),
            rs.getInt("id_carrera"),
            rs.getString("carrera"),
            rs.getInt("ciclo")
        );
    }
}