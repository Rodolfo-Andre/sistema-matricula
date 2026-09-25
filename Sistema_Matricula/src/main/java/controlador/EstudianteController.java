package controlador;

import gestor.GestorDatos;
import modelo.Estudiante;
import java.util.List;

/**
 *
 * @author hans5
 */
public class EstudianteController {

    private GestorDatos gestor;

    public EstudianteController() {
        this.gestor = new GestorDatos();
    }

    /**
     * Intenta registrar un estudiante.
     *
     * @return null si todo salió bien, o un mensaje de error legible si algo
     * falló.
     */
    public String registrarEstudiante(String codigo, String nombre, String carrera, int ciclo) {
        try {
            Estudiante estudiante = new Estudiante();
            gestor.agregarEstudiante(estudiante);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<Estudiante> obtenerEstudiantes() {
        return gestor.getEstudiantes();
    }

    public Estudiante buscarEstudiante(String codigo) {
        return gestor.buscarEstudiante(codigo);
    }
}
