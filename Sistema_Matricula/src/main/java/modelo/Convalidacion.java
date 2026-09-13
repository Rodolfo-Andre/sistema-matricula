/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Anthony
 */
public class Convalidacion {

    private int codigo;
    private Estudiante estudiante;
    private Curso cursoOrigen;
    private Curso cursoDestino;
    private String estado;

    public Convalidacion(int codigo, Estudiante estudiante, 
                         Curso cursoOrigen, Curso cursoDestino) {
        this.codigo = codigo;
        this.estudiante = estudiante;
        this.cursoOrigen = cursoOrigen;
        this.cursoDestino = cursoDestino;
        this.estado = "PENDIENTE";
    }

    public int getCodigo() {
        return codigo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Curso getCursoOrigen() {
        return cursoOrigen;
    }

    public Curso getCursoDestino() {
        return cursoDestino;
    }

    public String getEstado() {
        return estado;
    }

    public void evaluarConvalidacion() {
        if (cursoOrigen.getCreditos() >= cursoDestino.getCreditos()) {
            estado = "APROBADA";
        } else {
            estado = "RECHAZADA";
        }
    }

    public void mostrarDatos() {
        System.out.println("----- CONVALIDACION DE CURSO -----");
        System.out.println("Codigo: " + codigo);
        System.out.println("Estudiante: " + estudiante.getNombre());
        System.out.println("Curso de origen: " + cursoOrigen.getNombre());
        System.out.println("Curso a convalidar: " + cursoDestino.getNombre());
        System.out.println("Estado: " + estado);
    }

public void validarConvalidacion(double nota) 
        throws ConvalidacionException {

    if (nota < 0 || nota > 20) {
        throw new ConvalidacionException(
            "La nota debe estar entre 0 y 20."
        );
    }

    if (nota < 11) {
        throw new ConvalidacionException(
            "El curso no puede ser convalidado porque la nota es menor a 11."
        );
    }

    System.out.println("Curso apto para convalidación.");
    }
}