package modelo;

/**
 * Representa una matrícula de un estudiante en un curso.
 */
public class Matricula {
    private int idMatricula;
    private String codigoMatricula;
    private String codigoEstudiante;
    private String codigoCurso;
    private String fechaMatricula;
    private String estado;
    private String estadoDetalle;
    private String nombreEstudiante;
    private String nombreCurso;
    private String nombreProfesor;
    private String horario;
    private String carrera;
    private String periodo;

    public Matricula(String codigoMatricula, String codigoEstudiante,
                     String codigoCurso, String fechaMatricula) {
        this.codigoMatricula = codigoMatricula;
        this.codigoEstudiante = codigoEstudiante;
        this.codigoCurso = codigoCurso;
        this.fechaMatricula = fechaMatricula;
        this.estado = "ACTIVA";
    }

    public Matricula(String codigoMatricula, String codigoEstudiante, String codigoCurso,
                     String fechaMatricula, String nombreEstudiante, String nombreCurso,
                     String nombreProfesor, String horario, String carrera, String periodo) {
        this.codigoMatricula = codigoMatricula;
        this.codigoEstudiante = codigoEstudiante;
        this.codigoCurso = codigoCurso;
        this.fechaMatricula = fechaMatricula;
        this.estado = "ACTIVA";
        this.nombreEstudiante = nombreEstudiante;
        this.nombreCurso = nombreCurso;
        this.nombreProfesor = nombreProfesor;
        this.horario = horario;
        this.carrera = carrera;
        this.periodo = periodo;
    }

    public Matricula(int idMatricula, String codigoMatricula, String codigoEstudiante, String codigoCurso,
                     String fechaMatricula, String nombreEstudiante, String nombreCurso,
                     String nombreProfesor, String horario, String carrera, String periodo, String estado) {
        this.idMatricula = idMatricula;
        this.codigoMatricula = codigoMatricula;
        this.codigoEstudiante = codigoEstudiante;
        this.codigoCurso = codigoCurso;
        this.fechaMatricula = fechaMatricula;
        this.estado = estado != null ? estado : "ACTIVA";
        this.nombreEstudiante = nombreEstudiante;
        this.nombreCurso = nombreCurso;
        this.nombreProfesor = nombreProfesor;
        this.horario = horario;
        this.carrera = carrera;
        this.periodo = periodo;
    }

    public int getIdMatricula() { return idMatricula; }
    public String getEstadoDetalle() { return estadoDetalle != null ? estadoDetalle : "ACTIVO"; }
    public void setEstadoDetalle(String estadoDetalle) { this.estadoDetalle = estadoDetalle; }
    public String getCodigoMatricula() { return codigoMatricula; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public String getCodigoCurso() { return codigoCurso; }
    public String getFechaMatricula() { return fechaMatricula; }
    public String getEstado() { return estado; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getNombreCurso() { return nombreCurso; }
    public String getNombreProfesor() { return nombreProfesor; }
    public String getHorario() { return horario; }
    public String getCarrera() { return carrera; }
    public String getPeriodo() { return periodo; }

    public void setEstado(String estado) { this.estado = estado; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
    public void setNombreProfesor(String nombreProfesor) { this.nombreProfesor = nombreProfesor; }
    public void setHorario(String horario) { this.horario = horario; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getNombreEstudianteDisplay() {
        return nombreEstudiante != null ? nombreEstudiante : codigoEstudiante;
    }

    public String getNombreCursoDisplay() {
        return nombreCurso != null ? nombreCurso : codigoCurso;
    }

    public String getNombreProfesorDisplay() {
        return nombreProfesor != null ? nombreProfesor : "Sin profesor";
    }

    public String getHorarioDisplay() {
        return horario != null ? horario : "Sin horario";
    }

    public String getCarreraDisplay() {
        return carrera != null ? carrera : "Sin carrera";
    }

    @Override
    public String toString() {
        return String.format("Matrícula: %s | Estudiante: %s | Curso: %s | Estado: %s",
            codigoMatricula, getNombreEstudianteDisplay(), getNombreCursoDisplay(), estado);
    }
}
