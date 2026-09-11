/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Representa una carrera académica ofertada en la universidad.
 * Mapeado fielmente con la tabla 'carreras' de MySQL.
 */
public class Carrera {

    private int idCarrera;
    private String nombre;

    // Constructor vacío
    public Carrera() {
    }

    // Constructor completo 
    public Carrera(int idCarrera, String nombre) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
    }

    // Constructor sin ID para insertar nuevas carreras 
    public Carrera(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return String.format("ID Carrera: %d | Nombre: %s", idCarrera, nombre);
    }
}