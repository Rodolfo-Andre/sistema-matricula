/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Profesor {
    private int idProfesor;
    private String dni;
    private String nombres;
    private String apellidos;
    private String especialidad;

    public Profesor() {}

    public Profesor(int idProfesor, String dni, String nombres, String apellidos, String especialidad) {
        this.idProfesor = idProfesor;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
    }

    public Profesor(String dni, String nombres, String apellidos, String especialidad) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
    }

    public int getIdProfesor() { return idProfesor; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override
    public String toString() {
        return String.format("ID: %d | DNI: %s | Profesor: %s %s | Especialidad: %s",
                idProfesor, dni, nombres, apellidos, especialidad);
    }
}