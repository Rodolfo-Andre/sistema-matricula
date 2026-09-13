package modelo;

public class Matricula {
    private int idMatricula;
    private int idEstudiante;
    private String fechaMatricula;
    private String periodo;

    public Matricula() {}

    public Matricula(int idMatricula, int idEstudiante, String fechaMatricula, String periodo) {
        this.idMatricula = idMatricula;
        this.idEstudiante = idEstudiante;
        this.fechaMatricula = fechaMatricula;
        this.periodo = periodo;
    }

    public Matricula(int idEstudiante, String fechaMatricula, String periodo) {
        this.idEstudiante = idEstudiante;
        this.fechaMatricula = fechaMatricula;
        this.periodo = periodo;
    }

    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(String fechaMatricula) { this.fechaMatricula = fechaMatricula; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    @Override
    public String toString() {
        return String.format("ID Matrícula: %d | ID Estudiante: %d | Fecha: %s | Periodo: %s",
                idMatricula, idEstudiante, fechaMatricula, periodo);
    }
}