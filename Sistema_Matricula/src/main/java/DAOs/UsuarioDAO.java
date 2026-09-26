package DAOs;

import gestor.ConexionBD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    public Usuario autenticar(String username, String passwordIngresada) {
        String sql = "{CALL sp_ValidarUsuario(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, username);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    String hashBD = rs.getString("password");
                    if (BCrypt.checkpw(passwordIngresada, hashBD)) {
                        Usuario usuarioLogueado = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            hashBD,
                            rs.getInt("id_rol"),
                            rs.getString("rol"), 
                            (Integer) rs.getObject("id_estudiante"),
                            (Integer) rs.getObject("id_profesor"),
                            true 
                        );
                        usuarioLogueado.setNombreReal(rs.getString("nombre_real"));
                        
                        return usuarioLogueado;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
        }
        return null; 
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "{CALL sp_ListarUsuarios()}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                // 1. Creamos el objeto usuario
                Usuario u = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("id_rol"),
                    rs.getString("rol"), 
                    (Integer) rs.getObject("id_estudiante"),
                    (Integer) rs.getObject("id_profesor"),
                    rs.getBoolean("estado")
                );
                
                // 2. Le asignamos el nombre real que viene de la BD
                u.setNombreReal(rs.getString("nombre_real"));
                
                // 3. Lo agregamos a la lista
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Usuario usuario) {
        String sql = "{CALL sp_InsertarUsuario(?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, usuario.getUsername());
            cstmt.setString(2, usuario.getPassword());
            cstmt.setInt(3, usuario.getIdRol());
            
            if (usuario.getIdEstudiante() != null) {
                cstmt.setInt(4, usuario.getIdEstudiante());
            } else {
                cstmt.setNull(4, java.sql.Types.INTEGER);
            }

            if (usuario.getIdProfesor() != null) {
                cstmt.setInt(5, usuario.getIdProfesor());
            } else {
                cstmt.setNull(5, java.sql.Types.INTEGER);
            }

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuario usuario) {
        String sql = "{CALL sp_ActualizarUsuario(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, usuario.getIdUsuario());
            cstmt.setString(2, usuario.getUsername());
            cstmt.setString(3, usuario.getPassword());
            cstmt.setInt(4, usuario.getIdRol());

            if (usuario.getIdEstudiante() != null) {
                cstmt.setInt(5, usuario.getIdEstudiante());
            } else {
                cstmt.setNull(5, java.sql.Types.INTEGER);
            }

            if (usuario.getIdProfesor() != null) {
                cstmt.setInt(6, usuario.getIdProfesor());
            } else {
                cstmt.setNull(6, java.sql.Types.INTEGER);
            }

            cstmt.setBoolean(7, usuario.isEstado());

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstado(int idUsuario, boolean estado) {
        String sql = "{CALL sp_CambiarEstadoUsuario(?, ?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idUsuario);
            cstmt.setBoolean(2, estado);

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idUsuario) {
        String sql = "{CALL sp_EliminarUsuario(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idUsuario);

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}