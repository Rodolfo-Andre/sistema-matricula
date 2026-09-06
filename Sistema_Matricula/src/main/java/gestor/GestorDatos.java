package gestor;

import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que maneja todos los datos del sistema
 * Versión simple y fácil de entender
 * 
 * @author jairh
 */
public class GestorDatos {
    
    // Listas para guardar los datos
    private static List<Estudiante> estudiantes = new ArrayList<>();
    private static List<Curso> cursos = new ArrayList<>();
    private static List<Matricula> matriculas = new ArrayList<>();
    
    // Contadores para generar códigos
    private static int contadorEstudiantes = 0;
    private static int contadorCursos = 0;
    
    // ============================================================
    // DATOS DE EJEMPLO
    // ============================================================
    
    public static void cargarDatosEjemplo() {
        // Limpiar todo
        estudiantes.clear();
        cursos.clear();
        matriculas.clear();
        
        // Resetear contadores
        contadorEstudiantes = 0;
        contadorCursos = 0;
        
        // Crear estudiantes
        estudiantes.add(new Estudiante("2024001", "Juan Pérez", "Ingeniería de Sistemas", 5));
        estudiantes.add(new Estudiante("2024002", "María García", "Ingeniería Civil", 3));
        estudiantes.add(new Estudiante("2024003", "Carlos López", "Administración", 7));
        estudiantes.add(new Estudiante("2024004", "Ana Martínez", "Ingeniería de Sistemas", 2));
        estudiantes.add(new Estudiante("2024005", "Luis Torres", "Arquitectura", 4));
        estudiantes.add(new Estudiante("2024006", "Laura Sánchez", "Ingeniería Civil", 6));
        estudiantes.add(new Estudiante("2024007", "Pedro Ramírez", "Administración", 1));
        estudiantes.add(new Estudiante("2024008", "Sofía Mendoza", "Ingeniería de Sistemas", 8));
        estudiantes.add(new Estudiante("2024009", "Diego Fernández", "Ingeniería Civil", 2));
        estudiantes.add(new Estudiante("2024010", "Valentina Ruiz", "Arquitectura", 3));
        
        // Crear cursos
        cursos.add(new Curso("INF101", "Programación I", 4));
        cursos.add(new Curso("INF102", "Programación II", 4));
        cursos.add(new Curso("CIV101", "Cálculo I", 5));
        cursos.add(new Curso("CIV102", "Física I", 4));
        cursos.add(new Curso("ADM101", "Administración I", 3));
        cursos.add(new Curso("ARQ101", "Dibujo Técnico", 4));
    }
    
    // ============================================================
    // OPERACIONES CON ESTUDIANTES
    // ============================================================
    
    // Obtener todos los estudiantes
    public static List<Estudiante> getEstudiantes() {
        return estudiantes;
    }
    
    // Agregar un estudiante
    public static void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }
    
    // Buscar un estudiante por código
    public static Estudiante buscarEstudiante(String codigo) {
        for (Estudiante e : estudiantes) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }
    
    // Buscar estudiantes por carrera
    public static List<Estudiante> buscarEstudiantesPorCarrera(String carrera) {
        List<Estudiante> resultado = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.getCarrera().equals(carrera)) {
                resultado.add(e);
            }
        }
        return resultado;
    }
    
    // Buscar estudiantes por ciclo
    public static List<Estudiante> buscarEstudiantesPorCiclo(int ciclo) {
        List<Estudiante> resultado = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.getCiclo() == ciclo) {
                resultado.add(e);
            }
        }
        return resultado;
    }
    
    // Buscar estudiantes por nombre (búsqueda parcial)
    public static List<Estudiante> buscarEstudiantesPorNombre(String nombre) {
        List<Estudiante> resultado = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(e);
            }
        }
        return resultado;
    }
    
    // Eliminar un estudiante
    public static boolean eliminarEstudiante(String codigo) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getCodigo().equals(codigo)) {
                estudiantes.remove(i);
                return true;
            }
        }
        return false;
    }
    
    // Obtener todas las carreras disponibles (sin repetir)
    public static List<String> getCarreras() {
        List<String> carreras = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (!carreras.contains(e.getCarrera())) {
                carreras.add(e.getCarrera());
            }
        }
        return carreras;
    }
    
    // ============================================================
    // OPERACIONES CON CURSOS
    // ============================================================
    
    // Obtener todos los cursos
    public static List<Curso> getCursos() {
        return cursos;
    }
    
    // Agregar un curso
    public static void agregarCurso(Curso curso) {
        cursos.add(curso);
    }
    
    // Buscar un curso por código
    public static Curso buscarCurso(String codigo) {
        for (Curso c : cursos) {
            if (c.getCodigo().equals(codigo)) {
                return c;
            }
        }
        return null;
    }
    
    // Buscar cursos por nombre (búsqueda parcial)
    public static List<Curso> buscarCursosPorNombre(String nombre) {
        List<Curso> resultado = new ArrayList<>();
        for (Curso c : cursos) {
            if (c.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    // Eliminar un curso
    public static boolean eliminarCurso(String codigo) {
        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).getCodigo().equals(codigo)) {
                cursos.remove(i);
                return true;
            }
        }
        return false;
    }
    
    // ============================================================
    // OPERACIONES CON MATRÍCULAS
    // ============================================================
    
    // Registrar una matrícula
    public static void registrarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }
    
    // Obtener todas las matrículas
    public static List<Matricula> getMatriculas() {
        return matriculas;
    }
    
    // Buscar matrículas de un estudiante
    public static List<Matricula> buscarMatriculasPorEstudiante(String codigoEstudiante) {
        List<Matricula> resultado = new ArrayList<>();
        for (Matricula m : matriculas) {
            if (m.getCodigoEstudiante().equals(codigoEstudiante)) {
                resultado.add(m);
            }
        }
        return resultado;
    }
    
    // ============================================================
    // ESTADÍSTICAS Y REPORTES
    // ============================================================
    
    // Obtener total de estudiantes
    public static int getTotalEstudiantes() {
        return estudiantes.size();
    }
    
    // Obtener total de cursos
    public static int getTotalCursos() {
        return cursos.size();
    }
    
    // Obtener total de matrículas
    public static int getTotalMatriculas() {
        return matriculas.size();
    }
    
    // Obtener promedio de ciclo
    public static double getPromedioCiclo() {
        if (estudiantes.isEmpty()) {
            return 0;
        }
        
        int suma = 0;
        for (Estudiante e : estudiantes) {
            suma = suma + e.getCiclo();
        }
        return (double) suma / estudiantes.size();
    }
    
    // Obtener estadísticas por carrera (para mostrar en reportes)
    public static String getEstadisticasPorCarrera() {
        if (estudiantes.isEmpty()) {
            return "No hay estudiantes registrados";
        }
        
        String resultado = "Estadísticas por carrera:\n";
        List<String> carreras = getCarreras();
        
        for (String carrera : carreras) {
            int contador = 0;
            for (Estudiante e : estudiantes) {
                if (e.getCarrera().equals(carrera)) {
                    contador++;
                }
            }
            resultado = resultado + "  - " + carrera + ": " + contador + " estudiantes\n";
        }
        
        return resultado;
    }
    
    // Obtener estadísticas por ciclo
    public static String getEstadisticasPorCiclo() {
        if (estudiantes.isEmpty()) {
            return "No hay estudiantes registrados";
        }
        
        String resultado = "Estadísticas por ciclo:\n";
        
        // Ciclos del 1 al 10
        for (int ciclo = 1; ciclo <= 10; ciclo++) {
            int contador = 0;
            for (Estudiante e : estudiantes) {
                if (e.getCiclo() == ciclo) {
                    contador++;
                }
            }
            if (contador > 0) {
                resultado = resultado + "  - Ciclo " + ciclo + ": " + contador + " estudiantes\n";
            }
        }
        
        return resultado;
    }
    
    // Obtener estadísticas de cursos
    public static String getEstadisticasCursos() {
        if (cursos.isEmpty()) {
            return "No hay cursos registrados";
        }
        
        int totalCreditos = 0;
        int maxCreditos = 0;
        int minCreditos = 999;
        
        for (Curso c : cursos) {
            totalCreditos = totalCreditos + c.getCreditos();
            
            if (c.getCreditos() > maxCreditos) {
                maxCreditos = c.getCreditos();
            }
            
            if (c.getCreditos() < minCreditos) {
                minCreditos = c.getCreditos();
            }
        }
        
        double promedioCreditos = (double) totalCreditos / cursos.size();
        
        String resultado = "Estadísticas de cursos:\n";
        resultado = resultado + "  - Total de cursos: " + cursos.size() + "\n";
        resultado = resultado + "  - Total de créditos: " + totalCreditos + "\n";
        resultado = resultado + "  - Promedio de créditos: " + promedioCreditos + "\n";
        resultado = resultado + "  - Máximo de créditos: " + maxCreditos + "\n";
        resultado = resultado + "  - Mínimo de créditos: " + minCreditos;
        
        return resultado;
    }
    
    // Obtener reporte completo del sistema
    public static String getReporteCompleto() {
        String reporte = "REPORTE COMPLETO DEL SISTEMA\n";
        reporte = reporte + "============================\n\n";
        
        // Estudiantes
        reporte = reporte + "ESTUDIANTES:\n";
        reporte = reporte + "  Total: " + estudiantes.size() + "\n";
        reporte = reporte + getEstadisticasPorCarrera() + "\n";
        reporte = reporte + getEstadisticasPorCiclo() + "\n";
        
        // Cursos
        reporte = reporte + getEstadisticasCursos() + "\n\n";
        
        // Matrículas
        reporte = reporte + "MATRÍCULAS:\n";
        reporte = reporte + "  Total: " + matriculas.size();
        
        return reporte;
    }
    
    // ============================================================
    // UTILIDADES
    // ============================================================
    
    // Verificar si hay datos
    public static boolean tieneDatos() {
        return !estudiantes.isEmpty() || !cursos.isEmpty();
    }
    
    // Limpiar todos los datos
    public static void limpiarDatos() {
        estudiantes.clear();
        cursos.clear();
        matriculas.clear();
        contadorEstudiantes = 0;
        contadorCursos = 0;
    }
}