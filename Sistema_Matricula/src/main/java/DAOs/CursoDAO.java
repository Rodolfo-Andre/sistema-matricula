package DAOs;

import gestor.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Curso;

public class CursoDAO {

    public List<Curso> listar() {
        List<Curso> lista = new ArrayList<>();
        String sql = "{CALL sp_ListarCursos()}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Curso(
                    rs.getInt("id_curso"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getInt("creditos")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar cursos: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Curso curso) {
        String sql = "{CALL sp_InsertarCurso(?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, curso.getCodigo());
            cstmt.setString(2, curso.getNombre());
            cstmt.setInt(3, curso.getCreditos());

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar curso: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Curso curso) {
        String sql = "{CALL sp_ActualizarCurso(?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, curso.getIdCurso());
            cstmt.setString(2, curso.getCodigo());
            cstmt.setString(3, curso.getNombre());
            cstmt.setInt(4, curso.getCreditos());

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar curso: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCurso) {
        String sql = "{CALL sp_EliminarCurso(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idCurso);
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar curso: " + e.getMessage());
            return false;
        }
    }

    public Curso buscarPorCodigo(String codigo) {
        String sql = "{CALL sp_BuscarCursoPorCodigo(?)}";
        Curso curso = null;

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, codigo);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    curso = new Curso(
                        rs.getInt("id_curso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("creditos")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar curso por código: " + e.getMessage());
        }
        return curso;
    }
}