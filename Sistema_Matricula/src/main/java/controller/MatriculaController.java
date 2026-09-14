package controller;

import DAOs.MatriculaDAO;
import java.util.List;
import modelo.Matricula;

public class MatriculaController {

    private final MatriculaDAO matriculaDAO;

    public MatriculaController() {
        this.matriculaDAO = new MatriculaDAO();
    }

    public List<Matricula> listar(boolean soloActivas) {
        return matriculaDAO.listar(soloActivas);
    }

    public List<Matricula> listarActivas() {
        return matriculaDAO.listar(true);
    }

    public List<Matricula> listarInactivas() {
        return matriculaDAO.listarAnuladas();
    }

    public void validarNueva(String codigoEstudiante, String codigoCurso, String periodo) {
        if (codigoEstudiante == null || codigoEstudiante.trim().isEmpty()
                || codigoCurso == null || codigoCurso.trim().isEmpty()
                || periodo == null || periodo.trim().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        if (!periodo.trim().matches("\\d{4}-[1-2]")) {
            throw new IllegalArgumentException("El formato del periodo debe ser AAAA-N (ej: 2026-1).");
        }
        if (matriculaDAO.existeMatricula(codigoEstudiante, codigoCurso, periodo)) {
            throw new IllegalArgumentException("Ya existe una matricula para este estudiante en este curso y periodo.");
        }
        if (!matriculaDAO.existeCursoProfesor(codigoCurso, periodo)) {
            throw new IllegalArgumentException("No hay asignación curso-profesor disponible para este curso en el periodo.");
        }
        int idCp = matriculaDAO.getIdCursoProfesor(codigoCurso, periodo);
        if (idCp != -1 && matriculaDAO.existeTraslapeHorario(codigoEstudiante, periodo, idCp)) {
            throw new IllegalArgumentException("Traslape de horario con otro curso del estudiante en el periodo.");
        }
    }

    public boolean guardar(Matricula matricula) {
        validarNueva(matricula.getCodigoEstudiante(), matricula.getCodigoCurso(), matricula.getPeriodo());
        return matriculaDAO.agregarMatricula(matricula);
    }

    public boolean anularPeriodo(String codigoMatricula) {
        return matriculaDAO.anularPeriodo(codigoMatricula);
    }

    public boolean quitarCurso(String codigoMatricula, String codigoCurso) {
        return matriculaDAO.quitarCurso(codigoMatricula, codigoCurso);
    }

    public boolean reactivarCurso(String codigoMatricula, String codigoCurso) {
        return matriculaDAO.reactivarCurso(codigoMatricula, codigoCurso);
    }

    public int totalCabecerasActivas() {
        return matriculaDAO.getTotalCabecerasActivas();
    }
}
