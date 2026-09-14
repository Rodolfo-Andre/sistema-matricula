package modelo;

public class Matricula {
    private int idMatricula;
    private int idEstudiante;
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

    public Matricula() {}

    public Matricula(int idMatricula, int idEstudiante, String fechaMatricula, String periodo) {
        this.idMatricula = idMatricula;
        this.idEstudiante = idEstudiante;
        this.fechaMatricula = fechaMatricula;
        this.periodo = periodo;
        this.estado = "ACTIVA";
    }

    public Matricula(int idEstudiante, String fechaMatricula, String periodo) {
        this.idEstudiante = idEstudiante;
        this.fechaMatricula = fechaMatricula;
        this.periodo = periodo;
        this.estado = "ACTIVA";
    }

    public Matricula(String codigoMatricula, String codigoEstudiante, String codigoCurso, String fechaMatricula) {
        this.codigoMatricula = codigoMatricula;
        this.codigoEstudiante = codigoEstudiante;
        this.codigoCurso = codigoCurso;
        this.fechaMatricula = fechaMatricula;
        this.estado = "ACTIVA";
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
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }
    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }
    public String getCodigoMatricula() { return codigoMatricula; }
    public void setCodigoMatricula(String codigoMatricula) { this.codigoMatricula = codigoMatricula; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    public String getCodigoCurso() { return codigoCurso; }
    public void setCodigoCurso(String codigoCurso) { this.codigoCurso = codigoCurso; }
    public String getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(String fechaMatricula) { this.fechaMatricula = fechaMatricula; }
    public String getEstado() { return estado != null ? estado : "ACTIVA"; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getEstadoDetalle() { return estadoDetalle != null ? estadoDetalle : "ACTIVO"; }
    public void setEstadoDetalle(String estadoDetalle) { this.estadoDetalle = estadoDetalle; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
    public String getNombreProfesor() { return nombreProfesor; }
    public void setNombreProfesor(String nombreProfesor) { this.nombreProfesor = nombreProfesor; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public String getPeriodo() { return periodo; }
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
            codigoMatricula, getNombreEstudianteDisplay(), getNombreCursoDisplay(), getEstado());
    }
}
