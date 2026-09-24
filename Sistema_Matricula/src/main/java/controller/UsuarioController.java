package controller;

import DAOs.UsuarioDAO;
import modelo.Usuario;
import java.util.List;

/**
 *
 * @author jairh
 */
public class UsuarioController {
    
    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }


    public Usuario login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null; 
        }
        return usuarioDAO.autenticar(username, password);
    }


    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listar();
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getUsername() == null || usuario.getPassword() == null) {
            return false;
        }
        return usuarioDAO.insertar(usuario);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() <= 0) {
            return false;
        }
        return usuarioDAO.actualizar(usuario);
    }

    public boolean cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado) {
        if (idUsuario <= 0) {
            return false;
        }
        return usuarioDAO.cambiarEstado(idUsuario, nuevoEstado);
    }

    public boolean eliminarUsuarioFisico(int idUsuario) {
        if (idUsuario <= 0) {
            return false;
        }
        return usuarioDAO.eliminar(idUsuario);
    }
}