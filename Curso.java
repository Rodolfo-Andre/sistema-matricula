package modelo;

public class Curso {

    private String codigo;
    private String nombre;
    private int creditos;

    public Curso(String codigo, String nombre, int creditos) {
        setCodigo(codigo);
        setNombre(nombre);
        setCreditos(creditos);
    }

    public Curso(String codigo, String nombre) {
        this(codigo, nombre, 3);
    }

    public Curso(String codigo) {
        this(codigo, "Sin nombre", 0);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del curso no puede estar vacio");
        }
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso no puede estar vacio");
        }
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        if (creditos < 0 || creditos > 10) {
            throw new IllegalArgumentException("Los creditos deben estar entre 0 y 10");
        }
        this.creditos = creditos;
    }

    public void mostrarDatos() {
        System.out.printf("Codigo: %-8s Nombre: %-25s Creditos: %d%n", codigo, nombre, creditos);
    }

    public void mostrarDatos(boolean resumido) {
        if (resumido) {
            System.out.printf("Codigo: %-8s Nombre: %-25s%n", codigo, nombre);
        } else {
            mostrarDatos();
        }
    }

    public boolean equals(Curso otro) {
        if (otro == null) return false;
        return this.codigo.equalsIgnoreCase(otro.codigo);
    }

    public boolean equals(String codigoBuscado) {
        return this.codigo.equalsIgnoreCase(codigoBuscado);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + creditos + " creditos)";
    }

}