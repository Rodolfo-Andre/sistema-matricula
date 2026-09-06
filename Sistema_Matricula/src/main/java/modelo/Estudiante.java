package modelo;

/**
 * Representa a un estudiante de la universidad.
 * Parte del Módulo 1 - Gestión de Estudiantes.
 */
public class Estudiante {

    private String codigo;
    private String nombre;
    private String carrera;
    private int ciclo;

    public Estudiante(String codigo, String nombre, String carrera, int ciclo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.carrera = carrera;
        this.ciclo = ciclo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public int getCiclo() {
        return ciclo;
    }

    @Override
    public String toString() {
        return String.format("Códigos: %s | Nombre: %s | Carrera: %s | Ciclo: %d",
                codigo, nombre, carrera, ciclo);
    }
}
