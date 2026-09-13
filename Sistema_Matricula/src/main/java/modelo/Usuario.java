/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Usuario {
    private int idUsuario;
    private String username;
    private String password;
    private int idRol;
    private Integer idEstudiante;
    private Integer idProfesor;
    private boolean estado;

    public Usuario() {}

    public Usuario(int idUsuario, String username, String password, int idRol, Integer idEstudiante, Integer idProfesor, boolean estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.idRol = idRol;
        this.idEstudiante = idEstudiante;
        this.idProfesor = idProfesor;
        this.estado = estado;
    }

    public Usuario(String username, String password, int idRol) {
        this.username = username;
        this.password = password;
        this.idRol = idRol;
        this.estado = true;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }

    public Integer getIdProfesor() { return idProfesor; }
    public void setIdProfesor(Integer idProfesor) { this.idProfesor = idProfesor; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("Usuario: %s | Rol ID: %d | Activo: %s", username, idRol, estado);
    }
}