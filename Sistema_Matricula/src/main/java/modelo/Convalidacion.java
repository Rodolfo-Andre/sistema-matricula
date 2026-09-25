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

    public void evaluarConvalidacion(double nota)
        throws ConvalidacionException {

    // Validar que la nota este dentro del rango permitido
    if (nota < 0 || nota > 20) {
        throw new ConvalidacionException(
            "La nota debe estar entre 0 y 20."
        );
    }

    // Validar que la nota sea aprobatoria
    if (nota < 11) {
        estado = "RECHAZADA";
        throw new ConvalidacionException(
            "Convalidacion rechazada: la nota es menor a 11."
        );
    }

    // Validar que los creditos sean suficientes
    if (cursoOrigen.getCreditos() < cursoDestino.getCreditos()) {
        estado = "RECHAZADA";
        throw new ConvalidacionException(
            "Convalidacion rechazada: los creditos del curso de origen son insuficientes."
        );
    }

    // Si cumple todas las condiciones
    estado = "APROBADA";
    System.out.println("Convalidacion aprobada correctamente.");
    }

    public void mostrarDatos() {
        System.out.println("----- CONVALIDACION DE CURSO -----");
        System.out.println("Codigo: " + codigo);
        System.out.println("Estudiante: " + estudiante.getNombre());
        System.out.println("Curso de origen: " + cursoOrigen.getNombre());
        System.out.println("Curso a convalidar: " + cursoDestino.getNombre());
        System.out.println("Estado: " + estado);
    }
}