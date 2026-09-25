/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class CursoProfesor {
    private int idCursoProfesor;
    private int idCurso;
    private int idProfesor;
    private int idHorario;
    private String periodo;

    public CursoProfesor() {}

    public CursoProfesor(int idCursoProfesor, int idCurso, int idProfesor, int idHorario, String periodo) {
        this.idCursoProfesor = idCursoProfesor;
        this.idCurso = idCurso;
        this.idProfesor = idProfesor;
        this.idHorario = idHorario;
        this.periodo = periodo;
    }

    public int getIdCursoProfesor() { return idCursoProfesor; }
    public void setIdCursoProfesor(int idCursoProfesor) { this.idCursoProfesor = idCursoProfesor; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public int getIdProfesor() { return idProfesor; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }

    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    @Override
    public String toString() {
        return String.format("ID Seccion: %d | Curso ID: %d | Profesor ID: %d | Periodo: %s",
                idCursoProfesor, idCurso, idProfesor, periodo);
    }
}