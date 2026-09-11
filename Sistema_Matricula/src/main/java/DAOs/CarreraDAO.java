package DAOs;

import gestor.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Carrera;

public class CarreraDAO {

    public List<Carrera> listar() {
        List<Carrera> lista = new ArrayList<>();
        String sql = "{CALL sp_ListarCarreras()}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Carrera(
                    rs.getInt("id_carrera"),
                    rs.getString("nombre")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar carreras: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Carrera carrera) {
        String sql = "{CALL sp_InsertarCarrera(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, carrera.getNombre());
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar carrera: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Carrera carrera) {
        String sql = "{CALL sp_ActualizarCarrera(?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, carrera.getIdCarrera());
            cstmt.setString(2, carrera.getNombre());
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar carrera: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCarrera) {
        String sql = "{CALL sp_EliminarCarrera(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idCarrera);
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar carrera: " + e.getMessage());
            return false;
        }
    }

    public Carrera buscarPorId(int idCarrera) {
        String sql = "{CALL sp_BuscarCarreraPorId(?)}";
        Carrera carrera = null;

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idCarrera);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    carrera = new Carrera(
                        rs.getInt("id_carrera"),
                        rs.getString("nombre")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar carrera por ID: " + e.getMessage());
        }
        return carrera;
    }
}