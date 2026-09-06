package modelo;

/**
 * Representa una matrícula de un estudiante en un curso.
 */
public class Matricula {
    private String codigoMatricula;
    private String codigoEstudiante;
    private String codigoCurso;
    private String fechaMatricula;
    private String estado; // ACTIVA, COMPLETADA, CANCELADA
    
    public Matricula(String codigoMatricula, String codigoEstudiante, 
                     String codigoCurso, String fechaMatricula) {
        this.codigoMatricula = codigoMatricula;
        this.codigoEstudiante = codigoEstudiante;
        this.codigoCurso = codigoCurso;
        this.fechaMatricula = fechaMatricula;
        this.estado = "ACTIVA";
    }
    
    // Getters y Setters
    public String getCodigoMatricula() { return codigoMatricula; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public String getCodigoCurso() { return codigoCurso; }
    public String getFechaMatricula() { return fechaMatricula; }
    public String getEstado() { return estado; }
    
    public void setEstado(String estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return String.format("Matrícula: %s | Estudiante: %s | Curso: %s | Estado: %s",
            codigoMatricula, codigoEstudiante, codigoCurso, estado);
    }
}