package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * MANEJO DE ARCHIVOS genérico y reutilizable.
 *
 * En vez de escribir una clase de exportación/importación distinta por
 * cada entidad del dominio (Curso, Estudiante, etc. — código duplicado),
 * esta clase concentra la lógica común de leer/escribir archivos de
 * texto plano (formato CSV con ";"), y cada entidad solo aporta:
 *   1) cómo convertir UN objeto suyo en una línea de texto, y
 *   2) cómo reconstruir UN objeto suyo a partir de los campos de una línea.
 *
 * Esto se logra con interfaces funcionales (parámetros tipo lambda),
 * lo que evita repetir el manejo de BufferedReader/BufferedWriter,
 * rutas y excepciones en cada clase de dominio.
 */
public final class ArchivoUtil {

    private static final String SEPARADOR = ";";

    private ArchivoUtil() {
        // Clase utilitaria: no debe instanciarse.
    }

    /** Convierte UN objeto de dominio en su línea de texto para el archivo. */
    @FunctionalInterface
    public interface ConvertidorATexto<T> {
        String convertir(T objeto);
    }

    /** Reconstruye UN objeto de dominio a partir de los campos de una línea. */
    @FunctionalInterface
    public interface ConvertidorDesdeTexto<T> {
        T convertir(String[] campos) throws IllegalArgumentException;
    }

    /**
     * Exporta una lista de objetos a un archivo de texto.
     *
     * @param elementos    lista a exportar
     * @param archivo      archivo destino
     * @param encabezado   primera línea del archivo (describe las columnas)
     * @param convertidor  cómo transformar cada objeto en una línea
     * @throws IOException si la ruta no es válida o falla la escritura
     */
    public static <T> void exportar(List<T> elementos, File archivo, String encabezado,
                                     ConvertidorATexto<T> convertidor) throws IOException {

        // CONTROL DE RUTAS: si la carpeta destino no existe, se crea antes
        // de escribir, en vez de fallar con un error críptico de E/S.
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {

            writer.write(encabezado);
            writer.newLine();

            for (T elemento : elementos) {
                writer.write(convertidor.convertir(elemento));
                writer.newLine();
            }
        }
    }

    /**
     * Importa una lista de objetos desde un archivo de texto. Las líneas
     * mal formadas o con datos inválidos NO detienen la importación: se
     * omiten y se reportan en {@code errores}.
     *
     * @param archivo            archivo de origen
     * @param columnasEsperadas  cantidad de campos que debe tener cada línea
     * @param convertidor        cómo reconstruir el objeto a partir de los campos
     * @param errores            lista donde se agregan los mensajes de error (puede ser null)
     * @throws IOException si el archivo no existe o falla la lectura
     */
    public static <T> List<T> importar(File archivo, int columnasEsperadas,
                                        ConvertidorDesdeTexto<T> convertidor,
                                        List<String> errores) throws IOException {

        // CONTROL DE RUTAS: se valida explícitamente antes de intentar leer,
        // para lanzar un mensaje claro en vez de un FileNotFoundException crudo.
        if (!archivo.exists() || !archivo.isFile()) {
            throw new FileNotFoundException("El archivo no existe o no es válido: " + archivo.getAbsolutePath());
        }

        List<T> resultado = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {

            String linea;
            int numeroLinea = 0;
            boolean esEncabezado = true;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;

                if (esEncabezado) {
                    esEncabezado = false; // se salta la línea de encabezado
                    continue;
                }
                if (linea.trim().isEmpty()) {
                    continue; // ignora líneas en blanco
                }

                // CONTROL DE FORMATO: separa y valida la cantidad de campos.
                String[] campos = linea.split(SEPARADOR, -1);
                if (campos.length != columnasEsperadas) {
                    registrarError(errores, numeroLinea,
                            "se esperaban " + columnasEsperadas + " campos y se encontraron " + campos.length);
                    continue;
                }

                // CONTROL DE DATOS INVÁLIDOS: cada entidad valida sus propios
                // campos dentro del convertidor (formato numérico, rangos, etc.)
                try {
                    resultado.add(convertidor.convertir(campos));
                } catch (IllegalArgumentException ex) {
                    registrarError(errores, numeroLinea, ex.getMessage());
                }
            }
        }

        return resultado;
    }

    private static void registrarError(List<String> errores, int numeroLinea, String motivo) {
        if (errores != null) {
            errores.add("Línea " + numeroLinea + ": " + motivo);
        }
    }
}
