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
     * Registra un nuevo curso. Devuelve false si ya existe un curso
     * con ese código (regla de negocio: el código debe ser único).
     */
    public boolean guardarCurso(String codigo, String nombre, int creditos) {
        if (cursoDAO.buscarPorCodigo(codigo) != null) {
            return false;
        }
        Curso nuevo = new Curso(codigo, nombre, creditos); // valida internamente
        return cursoDAO.insertar(nuevo);
    }

    public boolean actualizarCurso(int idCurso, String codigo, String nombre, int creditos) {
        Curso curso = new Curso(idCurso, codigo, nombre, creditos); // valida internamente
        return cursoDAO.actualizar(curso);
    }

    public boolean eliminarCurso(int idCurso) {
        return cursoDAO.eliminar(idCurso);
    }

    public Curso buscarPorCodigo(String codigo) {
        return cursoDAO.buscarPorCodigo(codigo);
    }
}
