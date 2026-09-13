package controlador;

import excepciones.EstudianteInvalidoException;
import gestor.GestorEstudiantes;
import modelo.Estudiante;

import java.util.List;

/**
 *
 * @author hans5
 */
public class EstudianteController {

    private GestorEstudiantes gestor;

    public EstudianteController() {
        this.gestor = new GestorEstudiantes();
    }

    /**
     * Intenta registrar un estudiante.
     * @return null si todo salió bien, o un mensaje de error legible si algo falló.
     */
    public String registrarEstudiante(String codigo, String nombre, String carrera, int ciclo) {
        try {
            gestor.agregarEstudiante(codigo, nombre, carrera, ciclo);
            return null;
        } catch (EstudianteInvalidoException e) {
            return e.getMessage();
        }
    }

    public List<Estudiante> obtenerEstudiantes() {
        return gestor.listarTodos();
    }

    public Estudiante buscarEstudiante(String codigo) {
        return gestor.buscarPorCodigo(codigo);
    }

    public void actualizarDesdeBaseDeDatos() {
        gestor.refrescar();
    }
}