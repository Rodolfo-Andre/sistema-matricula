package util;

import modelo.Curso;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * MANEJO DE ARCHIVOS específico de Curso: solo define CÓMO se convierte
 * un Curso a texto y viceversa. La lectura/escritura real la hace
 * {@link ArchivoUtil} (reutilizable con cualquier clase de dominio).
 *
 * Formato de línea: codigo;nombre;creditos
 */
public class ArchivoCursoUtil {

    private static final String ENCABEZADO = "codigo;nombre;creditos";
    private static final int COLUMNAS_ESPERADAS = 3;

    public static void exportarATexto(List<Curso> cursos, File archivo) throws IOException {
        ArchivoUtil.exportar(cursos, archivo, ENCABEZADO,
                curso -> curso.getCodigo() + ";" + curso.getNombre() + ";" + curso.getCreditos());
    }

    public static List<Curso> importarDesdeTexto(File archivo, List<String> errores) throws IOException {
        return ArchivoUtil.importar(archivo, COLUMNAS_ESPERADAS, campos -> {
            String codigo = campos[0].trim();
            String nombre = campos[1].trim();

            int creditos;
            try {
                creditos = Integer.parseInt(campos[2].trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("los créditos no son un número válido");
            }

            // Reutiliza el constructor de Curso, que YA valida código,
            // nombre y rango de créditos (0-10).
            return new Curso(codigo, nombre, creditos);

        }, errores);
    }
}
