package gestor;

import DAOs.*;
import java.util.List;
import modelo.Carrera;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import modelo.Usuario;

public class GestorDatos {

    private static final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private static final CursoDAO cursoDAO = new CursoDAO();
    private static final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private static final CarreraDAO carreraDAO = new CarreraDAO();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // ==========================================
    // MÉTODOS DE ESTUDIANTES
    // ==========================================
    public static List<Estudiante> getEstudiantes() {
        return estudianteDAO.listar();
    }

    public static boolean agregarEstudiante(Estudiante estudiante) {
        return estudianteDAO.insertar(estudiante);
    }

    public static boolean actualizarEstudiante(Estudiante estudiante) {
        return estudianteDAO.actualizar(estudiante);
    }

    public static Estudiante buscarEstudiante(String codigo) {
        return estudianteDAO.buscarPorCodigo(codigo);
    }

   public static boolean eliminarEstudiante(String codigo) {
    Estudiante e = estudianteDAO.buscarPorCodigo(codigo);
    if (e != null) {
        return estudianteDAO.eliminar(e.getIdEstudiante()); // Pasa el id (int)
    }
    return false;
}

    // ==========================================
    // MÉTODOS DE CURSOS
    // ==========================================
    public static List<Curso> getCursos() {
        return cursoDAO.listar();
    }

    public static boolean agregarCurso(Curso curso) {
        return cursoDAO.insertar(curso);
    }

    public static boolean actualizarCurso(Curso curso) {
        return cursoDAO.actualizar(curso);
    }

    public static Curso buscarCurso(String codigo) {
        // Uso directo de la búsqueda por SP en la BD
        return cursoDAO.buscarPorCodigo(codigo);
    }

    public static boolean eliminarCurso(int idCurso) {
        return cursoDAO.eliminar(idCurso);
    }

    // ==========================================
    // MÉTODOS DE CARRERAS
    // ==========================================
    public static List<Carrera> getObjetoCarreras() {
        return carreraDAO.listar();
    }

    public static List<String> getCarreras() {
        return carreraDAO.listar().stream()
                .map(Carrera::getNombre)
                .toList();
    }

    public static boolean agregarCarrera(Carrera carrera) {
        return carreraDAO.insertar(carrera);
    }

    // ==========================================
    // MÉTODOS DE MATRÍCULAS
    // ==========================================
    public static List<Matricula> getMatriculas() {
        return matriculaDAO.listar();
    }

    public static boolean registrarMatriculaCompleta(Matricula matricula, List<Integer> idCursosProfesor) {
        return matriculaDAO.registrarMatriculaCompleta(matricula, idCursosProfesor);
    }

    public static boolean eliminarMatricula(int idMatricula) {
        return matriculaDAO.eliminar(idMatricula);
    }

    // ==========================================
    // MÉTODOS DE AUTENTICACIÓN Y USUARIOS
    // ==========================================
    public static Usuario autenticarUsuario(String username, String password) {
        return usuarioDAO.autenticar(username, password);
    }

    public static List<Usuario> getUsuarios() {
        return usuarioDAO.listar();
    }

    public static boolean agregarUsuario(Usuario usuario) {
        return usuarioDAO.insertar(usuario);
    }

    // ==========================================
    // REPORTES Y ESTADÍSTICAS
    // ==========================================
    public static int getTotalEstudiantes() {
        return estudianteDAO.listar().size();
    }

    public static int getTotalCursos() {
        return cursoDAO.listar().size();
    }

    public static int getTotalMatriculas() {
        return matriculaDAO.listar().size();
    }

    public static String getReporteCompleto() {
        StringBuilder reporte = new StringBuilder("REPORTE COMPLETO DEL SISTEMA (BASE DE DATOS SQL - SP)\n");
        reporte.append("===================================================\n\n");
        reporte.append("ESTUDIANTES:\n  Total: ").append(getTotalEstudiantes()).append("\n\n");
        reporte.append("CURSOS:\n  Total: ").append(getTotalCursos()).append("\n\n");
        reporte.append("MATRÍCULAS:\n  Total: ").append(getTotalMatriculas());
        return reporte.toString();
    }

    public static boolean tieneDatos() {
        return getTotalEstudiantes() > 0 || getTotalCursos() > 0;
    }

    public static void cargarDatosEjemplo() {
        ConexionBD.inicializarBD();
    }
}