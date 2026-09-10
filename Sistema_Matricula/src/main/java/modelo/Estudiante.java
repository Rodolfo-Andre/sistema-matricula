package modelo;

import java.util.Objects;

/**
 * Representa a un estudiante de la universidad.
 * Parte del Módulo 1 - Gestión de Estudiantes.
 */
public class Estudiante {

    private String codigo;
    private String nombre;
    private String carrera;
    private int ciclo;

    /**
     * Constructor completo.
     */
    public Estudiante(String codigo, String nombre, String carrera, int ciclo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.carrera = carrera;
        this.ciclo = (ciclo < 1) ? 1 : ciclo; // evita ciclos inválidos por defecto
    }

    /**
     * Sobrecarga: crea un estudiante asumiendo que ingresa en el ciclo 1
     * (por ejemplo, un estudiante recién admitido).
     */
    public Estudiante(String codigo, String nombre, String carrera) {
        this(codigo, nombre, carrera, 1);
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

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    /**
     * Dos estudiantes se consideran iguales si tienen el mismo código,
     * ya que es su identificador único dentro del sistema.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Estudiante)) return false;
        Estudiante otro = (Estudiante) obj;
        return Objects.equals(codigo, otro.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("Código: %s | Nombre: %s | Carrera: %s | Ciclo: %d",
                codigo, nombre, carrera, ciclo);
    }
}