package modelo;

public class Curso {

    private String codigo;
    private String nombre;
    private int creditos;

    public Curso(String codigo, String nombre, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public void mostrarDatos() {
        System.out.printf("Codigo: %-8s Nombre: %-25s Creditos: %d%n", codigo, nombre, creditos);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + creditos + " creditos)";
    }
}