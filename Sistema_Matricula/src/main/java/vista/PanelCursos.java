package vista;

import controller.CursoController;
import modelo.Curso;
import util.ArchivoCursoUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PanelCursos extends JPanel {

    private final CursoController controller;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigo, txtNombre, txtCreditos;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar;

    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo, btnMostrarTodos;
    private JButton btnExportarArchivo, btnImportarArchivo;

    private List<Curso> listaActualCursos;
    private int idCursoSeleccionado = -1;

    private final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    private final Font FONT_SUBTITULO = new Font("Arial", Font.BOLD, 15);
    private final Font FONT_ETIQUETA = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_COMPONENTE = new Font("Arial", Font.PLAIN, 14);
    private final Font FONT_BOTON = new Font("Arial", Font.BOLD, 13);

    public PanelCursos() {
        this.controller = new CursoController();
        this.listaActualCursos = new ArrayList<>();

        setLayout(new BorderLayout(12, 12));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        initComponents();
        cargarDatosTabla();
    }

    private void initComponents() {
        JLabel titulo = new JLabel("GESTIÓN DE CURSOS", SwingConstants.CENTER);
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(new Color(0, 51, 102));
        add(titulo, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearFormulario(), crearTabla());
        splitPane.setDividerLocation(380);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder("Datos del Curso");
        border.setTitleFont(FONT_SUBTITULO);
        panel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(15);
        txtNombre = new JTextField(15);
        txtCreditos = new JTextField(15);

        aplicarEstiloCampo(txtCodigo);
        aplicarEstiloCampo(txtNombre);
        aplicarEstiloCampo(txtCreditos);

        int row = 0;
        agregarCampo(panel, gbc, "Código:", txtCodigo, row++);
        agregarCampo(panel, gbc, "Nombre:", txtNombre, row++);
        agregarCampo(panel, gbc, "Créditos:", txtCreditos, row++);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 8, 8));
        panelBotones.setBackground(Color.WHITE);

        btnGuardar = crearBotonFormulario("Guardar");
        btnEditar = crearBotonFormulario("Actualizar");
        btnEliminar = crearBotonFormulario("Eliminar");
        btnLimpiar = crearBotonFormulario("Limpiar");

        btnGuardar.addActionListener(e -> guardarCurso());
        btnEditar.addActionListener(e -> actualizarCurso());
        btnEliminar.addActionListener(e -> eliminarCurso());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(panelBotones, gbc);

        return panel;
    }

    private void agregarCampo(JPanel p, GridBagConstraints gbc, String textLabel, JComponent comp, int row) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(textLabel);
        lbl.setFont(FONT_ETIQUETA);
        p.add(lbl, gbc);

        gbc.gridx = 1;
        p.add(comp, gbc);
    }

    private void aplicarEstiloCampo(JComponent comp) {
        comp.setFont(FONT_COMPONENTE);
        comp.setPreferredSize(new Dimension(comp.getPreferredSize().width, 32));
    }

    private JButton crearBotonFormulario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOTON);
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 36));
        return btn;
    }

    private JPanel crearTabla() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder("Lista de Cursos");
        border.setTitleFont(FONT_SUBTITULO);
        panel.setBorder(border);

        panel.add(crearPanelBusqueda(), BorderLayout.NORTH);

        String[] columnas = {"Código", "Nombre", "Créditos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.setFont(FONT_COMPONENTE);
        tabla.getTableHeader().setFont(FONT_ETIQUETA);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 32));

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarFormularioDesdeTabla(tabla.getSelectedRow());
            }
        });

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder("Buscar Curso");
        border.setTitleFont(FONT_SUBTITULO);
        panel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtBuscarCodigo = new JTextField(10);
        btnBuscarCodigo = new JButton("Buscar Código");
        btnMostrarTodos = new JButton("Mostrar Todos");
        btnExportarArchivo = new JButton("Exportar a Archivo");
        btnImportarArchivo = new JButton("Importar desde Archivo");

        aplicarEstiloCampo(txtBuscarCodigo);

        btnBuscarCodigo.addActionListener(e -> buscarPorCodigo());
        btnMostrarTodos.addActionListener(e -> mostrarTodos());
        btnExportarArchivo.addActionListener(e -> exportarCursosAArchivo());
        btnImportarArchivo.addActionListener(e -> importarCursosDesdeArchivo());

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        panel.add(txtBuscarCodigo, gbc);
        gbc.gridx = 2;
        panel.add(btnBuscarCodigo, gbc);
        gbc.gridx = 3;
        panel.add(btnMostrarTodos, gbc);
        gbc.gridx = 4;
        panel.add(btnExportarArchivo, gbc);
        gbc.gridx = 5;
        panel.add(btnImportarArchivo, gbc);

        return panel;
    }

    /**
     * MANEJO DE ARCHIVOS (exportar): toma los cursos que tiene la base
     * de datos en este momento y los guarda en un archivo .txt elegido
     * por el usuario, usando ArchivoCursoUtil.
     */
    private void exportarCursosAArchivo() {
        List<Curso> cursos = controller.listarCursos();
        if (cursos == null || cursos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay cursos para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar cursos como...");
        selector.setFileFilter(new FileNameExtensionFilter("Archivo de texto (*.txt)", "txt"));
        selector.setSelectedFile(new File("cursos_exportados.txt"));

        int opcion = selector.showSaveDialog(this);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            return; // el usuario canceló
        }

        File archivo = selector.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".txt")) {
            archivo = new File(archivo.getParentFile(), archivo.getName() + ".txt");
        }

        try {
            ArchivoCursoUtil.exportarATexto(cursos, archivo);
            JOptionPane.showMessageDialog(this,
                    cursos.size() + " curso(s) exportado(s) correctamente a:\n" + archivo.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo exportar el archivo:\n" + ex.getMessage(),
                    "Error de Escritura", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * MANEJO DE ARCHIVOS (importar): lee un archivo .txt previamente
     * exportado y registra en la base de datos los cursos que aún no
     * existan (evita duplicar por código).
     */
    private void importarCursosDesdeArchivo() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar archivo de cursos");
        selector.setFileFilter(new FileNameExtensionFilter("Archivo de texto (*.txt)", "txt"));

        int opcion = selector.showOpenDialog(this);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            return; // el usuario canceló
        }

        File archivo = selector.getSelectedFile();
        List<String> errores = new ArrayList<>();

        try {
            List<Curso> cursosLeidos = ArchivoCursoUtil.importarDesdeTexto(archivo, errores);

            int importados = 0;
            int duplicados = 0;

            for (Curso curso : cursosLeidos) {
                if (controller.buscarPorCodigo(curso.getCodigo()) != null) {
                    duplicados++;
                    continue; // ya existe ese código, no se vuelve a insertar
                }
                if (controller.guardarCurso(curso.getCodigo(), curso.getNombre(), curso.getCreditos())) {
                    importados++;
                }
            }

            cargarDatosTabla();

            StringBuilder resumen = new StringBuilder();
            resumen.append("Importación finalizada.\n")
                   .append("Cursos importados: ").append(importados).append("\n")
                   .append("Cursos ya existentes (omitidos): ").append(duplicados);

            if (!errores.isEmpty()) {
                resumen.append("\n\nLíneas con errores (").append(errores.size()).append("):\n");
                for (String error : errores) {
                    resumen.append("- ").append(error).append("\n");
                }
            }

            JOptionPane.showMessageDialog(this, resumen.toString(),
                    "Resultado de la Importación", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer el archivo:\n" + ex.getMessage(),
                    "Error de Lectura", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorCodigo() {
        String codigo = txtBuscarCodigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Curso curso = controller.buscarPorCodigo(codigo);
        if (curso == null) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún curso con ese código.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla(new ArrayList<>());
        } else {
            List<Curso> lista = new ArrayList<>();
            lista.add(curso);
            actualizarTabla(lista);
        }
    }

    private void mostrarTodos() {
        txtBuscarCodigo.setText("");
        cargarDatosTabla();
    }

    private void actualizarTabla(List<Curso> cursos) {
        this.listaActualCursos = cursos != null ? cursos : new ArrayList<>();
        modeloTabla.setRowCount(0);
        for (Curso c : this.listaActualCursos) {
            modeloTabla.addRow(new Object[]{c.getCodigo(), c.getNombre(), c.getCreditos()});
        }
    }

    private void guardarCurso() {
        if (!validarCampos()) return;

        String cod = txtCodigo.getText().trim();
        String nom = txtNombre.getText().trim();
        int creditos = Integer.parseInt(txtCreditos.getText().trim());

        if (controller.buscarPorCodigo(cod) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un curso con ese código.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean exito = controller.guardarCurso(cod, nom, creditos);

            if (exito) {
                cargarDatosTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Curso registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el curso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCurso() {
        if (idCursoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        String cod = txtCodigo.getText().trim();
        String nom = txtNombre.getText().trim();
        int creditos = Integer.parseInt(txtCreditos.getText().trim());

        try {
            Curso curso = new Curso(idCursoSeleccionado, cod, nom, creditos);
            boolean exito = controller.actualizarCurso(idCursoSeleccionado, cod, nom, creditos);

            if (exito) {
                cargarDatosTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Curso actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el curso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCurso() {
        if (idCursoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int op = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el curso?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            if (controller.eliminarCurso(idCursoSeleccionado)) {
                cargarDatosTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Curso eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void cargarDatosTabla() {
        List<Curso> cursos = controller.listarCursos();
        actualizarTabla(cursos);
    }

    private void cargarFormularioDesdeTabla(int fila) {
        if (fila >= 0 && fila < listaActualCursos.size()) {
            Curso seleccionado = listaActualCursos.get(fila);

            idCursoSeleccionado = seleccionado.getIdCurso();
            txtCodigo.setText(seleccionado.getCodigo());
            txtCodigo.setEditable(false);
            txtNombre.setText(seleccionado.getNombre());
            txtCreditos.setText(String.valueOf(seleccionado.getCreditos()));
        }
    }

    private void limpiarFormulario() {
        idCursoSeleccionado = -1;
        txtCodigo.setText("");
        txtCodigo.setEditable(true);
        txtNombre.setText("");
        txtCreditos.setText("");
        tabla.clearSelection();
    }

    private boolean validarCampos() {
        if (txtCodigo.getText().trim().isEmpty() ||
            txtNombre.getText().trim().isEmpty() ||
            txtCreditos.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(txtCreditos.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los créditos deben ser un número entero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}
