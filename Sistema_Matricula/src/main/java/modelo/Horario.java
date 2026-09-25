/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Horario {
    private int idHorario;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String aula;

    public Horario() {}

    public Horario(int idHorario, String dia, String horaInicio, String horaFin, String aula) {
        this.idHorario = idHorario;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.aula = aula;
    }

    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    @Override
    public String toString() {
        return String.format("ID: %d | Día: %s (%s - %s) | Aula: %s",
                idHorario, dia, horaInicio, horaFin, aula);
    }
}
