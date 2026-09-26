package controller;

import DAOs.ProfesorDAO;
import modelo.Profesor;
import java.util.List;

public class ProfesorController {

    private ProfesorDAO profesorDAO;

    public ProfesorController() {
        this.profesorDAO = new ProfesorDAO();
    }

    public List<Profesor> listarProfesores() {
        return profesorDAO.listar();
    }

    public boolean registrarProfesor(Profesor profesor) {
        // Validacion para dagiti kaskenan a paset
        if (profesor == null || profesor.getDni().isEmpty() || profesor.getNombres().isEmpty() || profesor.getApellidos().isEmpty()) {
            return false;
        }
        return profesorDAO.insertar(profesor);
    }

    public boolean actualizarProfesor(Profesor profesor) {
        if (profesor == null || profesor.getIdProfesor() <= 0) {
            return false;
        }
        return profesorDAO.actualizar(profesor);
    }

    public boolean eliminarProfesor(int idProfesor) {
        if (idProfesor <= 0) {
            return false;
        }
        return profesorDAO.eliminar(idProfesor);
    }
}