package DAOs;

import gestor.ConexionBD;
import modelo.Profesor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    public List<Profesor> listar() {
        List<Profesor> lista = new ArrayList<>();
        String sql = "{CALL sp_ListarProfesores()}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Profesor(
                    rs.getInt("id_profesor"),
                    rs.getString("dni"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("especialidad")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar profesores: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Profesor profesor) {
        String sql = "{CALL sp_InsertarProfesor(?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, profesor.getDni());
            cstmt.setString(2, profesor.getNombres());
            cstmt.setString(3, profesor.getApellidos());
            cstmt.setString(4, profesor.getEspecialidad());

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar profesor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Profesor profesor) {
        String sql = "{CALL sp_ActualizarProfesor(?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, profesor.getIdProfesor());
            cstmt.setString(2, profesor.getDni());
            cstmt.setString(3, profesor.getNombres());
            cstmt.setString(4, profesor.getApellidos());
            cstmt.setString(5, profesor.getEspecialidad());

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar profesor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idProfesor) {
        String sql = "{CALL sp_EliminarProfesor(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idProfesor);
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar profesor: " + e.getMessage());
            return false;
        }
    }
}