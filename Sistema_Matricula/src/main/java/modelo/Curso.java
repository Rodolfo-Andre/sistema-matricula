/*/
CAPTURA
/*/
package modelo;

public class Curso {

    private String codigo;
    private String nombre;
    private int creditos;

    // ---- SOBRECARGA DE CONSTRUCTORES ----

    // Constructor completo
    public Curso(String codigo, String nombre, int creditos) {
        setCodigo(codigo);
        setNombre(nombre);
        setCreditos(creditos);
    }

    // Sobrecarga: constructor sin créditos (por defecto 3)
    public Curso(String codigo, String nombre) {
        this(codigo, nombre, 3);
    }

    // Sobrecarga: constructor solo con código 
    public Curso(String codigo) {
        this(codigo, "Sin nombre", 0);
    }

    public String getCodigo() {
        return codigo;
    }

    // ---- VALIDACIONES EN LOS SETTERS ----

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

    // ---- SOBRECARGA DE mostrarDatos() ----

    // Versión original: muestra todos los datos
    public void mostrarDatos() {
        System.out.printf("Codigo: %-8s Nombre: %-25s Creditos: %d%n", codigo, nombre, creditos);
    }

    // Sobrecarga: versión resumida
    public void mostrarDatos(boolean resumido) {
        if (resumido) {
            System.out.printf("Codigo: %-8s Nombre: %-25s%n", codigo, nombre);
        } else {
            mostrarDatos();
        }
    }

    // ---- SOBRECARGA DE equals (comparar cursos) ----

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