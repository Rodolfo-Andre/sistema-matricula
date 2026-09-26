package modelo;

public class Curso {
    private int idCurso;
    private String codigo;
    private String nombre;
    private int creditos;
    private boolean estado;

    public Curso() {
        this.estado = true;
    }

    public Curso(int idCurso, String codigo, String nombre, int creditos, boolean estado) {
        this.idCurso = idCurso;
        setCodigo(codigo);
        setNombre(nombre);
        setCreditos(creditos);
        this.estado = estado;
    }

    public Curso(String codigo, String nombre, int creditos, boolean estado) {
        this(0, codigo, nombre, creditos, estado);
    }

    public Curso(String codigo, String nombre, int creditos) {
        this(0, codigo, nombre, creditos, true);
    }

    public Curso(String codigo, String nombre) {
        this(codigo, nombre, 3);
    }

    public Curso(String codigo) {
        this(codigo, "Sin nombre", 0);
    }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del curso no puede estar vacio");
        }
        this.codigo = codigo;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso no puede estar vacio");
        }
        this.nombre = nombre;
    }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) {
        if (creditos < 0 || creditos > 10) {
            throw new IllegalArgumentException("Los creditos deben estar entre 0 y 10");
        }
        this.creditos = creditos;
    }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public void mostrarDatos() {
        System.out.printf("Codigo: %-8s Nombre: %-25s Creditos: %d Estado: %s%n",
                codigo, nombre, creditos, (estado ? "Disponible" : "Inhabilitado"));
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
        return codigoBuscado != null && this.codigo.equalsIgnoreCase(codigoBuscado);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Código: %s | Curso: %s | Créditos: %d | Estado: %s",
                idCurso, codigo, nombre, creditos, (estado ? "Disponible" : "Inhabilitado"));
    }
}