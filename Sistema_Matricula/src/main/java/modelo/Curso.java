/*/
CAPTURA
/*/
package modelo;

public class Curso {
    private int idCurso;
    private String codigo;
    private String nombre;
    private int creditos;

    public Curso() {}

    public Curso(int idCurso, String codigo, String nombre, int creditos) {
        this.idCurso = idCurso;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public Curso(String codigo, String nombre, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    @Override
    public String toString() {
        return String.format("ID: %d | Código: %s | Curso: %s | Créditos: %d", idCurso, codigo, nombre, creditos);
    }
}