package controller;

import DAOs.CursoDAO;
import modelo.Curso;

import java.util.List;

/**
 * Controlador del módulo de Gestión de Cursos.
 * Sigue el mismo patrón que EstudianteController: la vista (PanelCursos)
 * nunca habla directo con el DAO ni con SQL, solo con este controlador.
 */
public class CursoController {

    private final CursoDAO cursoDAO;

    public CursoController() {
        this.cursoDAO = new CursoDAO();
    }

    public List<Curso> listarCursos() {
        return cursoDAO.listar();
    }

    /**
     * Registra un nuevo curso incluyendo su estado.
     * Devuelve false si ya existe un curso con ese código.
     */
    public boolean guardarCurso(String codigo, String nombre, int creditos, boolean estado) {
        if (cursoDAO.buscarPorCodigo(codigo) != null) {
            return false;
        }
        Curso nuevo = new Curso(codigo, nombre, creditos, estado);
        return cursoDAO.insertar(nuevo);
    }

    /**
     * Sobrecarga para mantener compatibilidad si no se pasa estado (por defecto activo).
     */
    public boolean guardarCurso(String codigo, String nombre, int creditos) {
        return guardarCurso(codigo, nombre, creditos, true);
    }

    /**
     * Actualiza los datos de un curso existente incluyendo su estado.
     */
    public boolean actualizarCurso(int idCurso, String codigo, String nombre, int creditos, boolean estado) {
        Curso curso = new Curso(idCurso, codigo, nombre, creditos, estado);
        return cursoDAO.actualizar(curso);
    }

    /**
     * Sobrecarga para mantener compatibilidad si se actualiza sin pasar estado explícito.
     */
    public boolean actualizarCurso(int idCurso, String codigo, String nombre, int creditos) {
        return actualizarCurso(idCurso, codigo, nombre, creditos, true);
    }

    public boolean eliminarCurso(int idCurso) {
        return cursoDAO.eliminar(idCurso);
    }

    public Curso buscarPorCodigo(String codigo) {
        return cursoDAO.buscarPorCodigo(codigo);
    }
}