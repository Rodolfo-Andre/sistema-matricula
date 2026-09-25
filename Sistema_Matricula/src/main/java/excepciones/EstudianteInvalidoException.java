/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Excepción personalizada que se lanza cuando los datos de un
 * Estudiante no cumplen las validaciones de negocio (código
 * duplicado, nombre vacío, ciclo fuera de rango, etc.).
 */
public class EstudianteInvalidoException extends Exception {

    public EstudianteInvalidoException(String mensaje) {
        super(mensaje);
    }
}