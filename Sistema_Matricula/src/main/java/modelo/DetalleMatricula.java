/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class DetalleMatricula {
    private int idDetalle;
    private int idMatricula;
    private int idCursoProfesor;

    public DetalleMatricula() {}

    public DetalleMatricula(int idDetalle, int idMatricula, int idCursoProfesor) {
        this.idDetalle = idDetalle;
        this.idMatricula = idMatricula;
        this.idCursoProfesor = idCursoProfesor;
    }

    public DetalleMatricula(int idMatricula, int idCursoProfesor) {
        this.idMatricula = idMatricula;
        this.idCursoProfesor = idCursoProfesor;
    }

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }

    public int getIdCursoProfesor() { return idCursoProfesor; }
    public void setIdCursoProfesor(int idCursoProfesor) { this.idCursoProfesor = idCursoProfesor; }

    @Override
    public String toString() {
        return String.format("Detalle ID: %d | Matrícula ID: %d | CursoProfesor ID: %d",
                idDetalle, idMatricula, idCursoProfesor);
    }
}