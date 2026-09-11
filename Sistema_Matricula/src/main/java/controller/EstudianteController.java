package controller;

import DAOs.EstudianteDAO;
import modelo.Estudiante;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EstudianteController {

    private final EstudianteDAO estudianteDAO;

    public EstudianteController() {
        this.estudianteDAO = new EstudianteDAO();
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.listar();
    }

    public boolean guardarEstudiante(String codigo, String dni, String nombres, String apellidos, int idCarrera, int ciclo) {
        if (estudianteDAO.buscarPorCodigo(codigo) != null) {
            return false;
        }
        Estudiante nuevo = new Estudiante(codigo, dni, nombres, apellidos, idCarrera, ciclo);
        return estudianteDAO.insertar(nuevo);
    }

    public boolean actualizarEstudiante(int idEstudiante, String codigo, String dni, String nombres, String apellidos, int idCarrera, int ciclo) {
        Estudiante estudiante = new Estudiante(idEstudiante, codigo, dni, nombres, apellidos, idCarrera, ciclo);
        return estudianteDAO.actualizar(estudiante);
    }

    public boolean eliminarEstudiante(int idEstudiante) {
        return estudianteDAO.eliminar(idEstudiante);
    }

    public Estudiante buscarPorCodigo(String codigo) {
        return estudianteDAO.buscarPorCodigo(codigo);
    }

    // --- MÉTODOS DE BÚSQUEDA Y FILTRADO ---

    public List<Estudiante> buscarPorFiltro(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return estudianteDAO.listar();
        }
        return estudianteDAO.buscarPorFiltro(filtro.trim());
    }

    public List<Estudiante> buscarPorDni(String dni) throws IllegalArgumentException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese un número de DNI para la búsqueda.");
        }
        if (!dni.trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe contener exactamente 8 dígitos numéricos.");
        }

        List<Estudiante> todos = estudianteDAO.listar();
        if (todos == null) return new ArrayList<>();

        return todos.stream()
                .filter(e -> e.getDni() != null && e.getDni().trim().equals(dni.trim()))
                .collect(Collectors.toList());
    }

    public List<Estudiante> buscarPorNombreOApellido(String texto) throws IllegalArgumentException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese un nombre o apellido para filtrar.");
        }
        if (texto.matches(".*\\d.*")) {
            throw new IllegalArgumentException("El campo de búsqueda no debe contener números.");
        }

        return estudianteDAO.buscarPorFiltro(texto.trim());
    }

    public List<Estudiante> buscarPorCarrera(String carrera) throws IllegalArgumentException {
        if (carrera == null || carrera.trim().isEmpty()) {
            throw new IllegalArgumentException("Seleccione una carrera válida.");
        }

        List<Estudiante> todos = estudianteDAO.listar();
        if (todos == null) return new ArrayList<>();

        return todos.stream()
                .filter(e -> e.getCarrera() != null && e.getCarrera().equalsIgnoreCase(carrera.trim()))
                .collect(Collectors.toList());
    }
}