package modelo;

public class Estudiante {

    private int idEstudiante;
    private String codigo;
    private String dni;
    private String nombres;
    private String apellidos;
    private int idCarrera;
    private String carrera; 
    private int ciclo;

    public Estudiante() {}

    // Constructor completo con nombre de la carrera
    public Estudiante(int idEstudiante, String codigo, String dni, String nombres, String apellidos, int idCarrera, String carrera, int ciclo) {
        this.idEstudiante = idEstudiante;
        this.codigo = codigo;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.idCarrera = idCarrera;
        this.carrera = carrera;
        this.ciclo = ciclo;
    }

    // Constructor para actualizaciones con id de carrera
    public Estudiante(int idEstudiante, String codigo, String dni, String nombres, String apellidos, int idCarrera, int ciclo) {
        this.idEstudiante = idEstudiante;
        this.codigo = codigo;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.idCarrera = idCarrera;
        this.ciclo = ciclo;
    }

    // Constructor para inserciones sin id de estudiante 
    public Estudiante(String codigo, String dni, String nombres, String apellidos, int idCarrera, int ciclo) {
        this.codigo = codigo;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.idCarrera = idCarrera;
        this.ciclo = ciclo;
    }

    // Getters y Setters
    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public int getIdCarrera() { return idCarrera; }
    public void setIdCarrera(int idCarrera) { this.idCarrera = idCarrera; }

    public String getCarrera() { return carrera; } 
    public void setCarrera(String carrera) { this.carrera = carrera; } 

    public int getCiclo() { return ciclo; }
    public void setCiclo(int ciclo) { this.ciclo = ciclo; }

    @Override
    public String toString() {
        return String.format("ID: %d | Código: %s | Estudiante: %s %s | Carrera: %s | Ciclo: %d",
                idEstudiante, codigo, nombres, apellidos, carrera, ciclo);
    }
}