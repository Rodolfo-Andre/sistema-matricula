package vista;

import controller.EstudianteController;
import modelo.Estudiante;
import modelo.Carrera;
import gestor.GestorDatos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelEstudiantes extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigo, txtDni, txtNombres, txtApellidos;
    private JComboBox<String> cbCarrera;
    private JComboBox<Integer> cbCiclo;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar;

    private JTextField txtBuscarDni, txtBuscarNombre;
    private JComboBox<String> cbFiltroCarrera;
    private JButton btnBuscarDni, btnBuscarNombre, btnFiltrarCarrera, btnMostrarTodos;

    private final EstudianteController controller;
    private List<Estudiante> listaActualEstudiantes;
    private int idEstudianteSeleccionado = -1;

    private final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    private final Font FONT_SUBTITULO = new Font("Arial", Font.BOLD, 15);
    private final Font FONT_ETIQUETA = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_COMPONENTE = new Font("Arial", Font.PLAIN, 14);
    private final Font FONT_BOTON = new Font("Arial", Font.BOLD, 13);

    public PanelEstudiantes() {
        this.controller = new EstudianteController();
        this.listaActualEstudiantes = new ArrayList<>();

        setLayout(new BorderLayout(12, 12));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        initComponents();
        cargarCarreras();
        cargarDatosTabla();
    }

    private void initComponents() {
        JLabel titulo = new JLabel("GESTIÓN DE ESTUDIANTES", SwingConstants.CENTER);
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(new Color(0, 51, 102));
        add(titulo, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearFormulario(), crearTabla());
        splitPane.setDividerLocation(420); 
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        TitledBorder border = BorderFactory.createTitledBorder("Datos del Estudiante");
        border.setTitleFont(FONT_SUBTITULO);
        panel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(15);
        txtDni = new JTextField(15);
        txtNombres = new JTextField(15);
        txtApellidos = new JTextField(15);

        cbCarrera = new JComboBox<>();
        cbCiclo = new JComboBox<>();
        for (int i = 1; i <= 10; i++) cbCiclo.addItem(i);

        aplicarEstiloCampo(txtCodigo);
        aplicarEstiloCampo(txtDni);
        aplicarEstiloCampo(txtNombres);
        aplicarEstiloCampo(txtApellidos);
        aplicarEstiloCampo(cbCarrera);
        aplicarEstiloCampo(cbCiclo);

        int row = 0;
        agregarCampo(panel, gbc, "Código:", txtCodigo, row++);
        agregarCampo(panel, gbc, "DNI:", txtDni, row++);
        agregarCampo(panel, gbc, "Nombres:", txtNombres, row++);
        agregarCampo(panel, gbc, "Apellidos:", txtApellidos, row++);
        agregarCampo(panel, gbc, "Carrera:", cbCarrera, row++);
        agregarCampo(panel, gbc, "Ciclo:", cbCiclo, row++);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 8, 8));
        panelBotones.setBackground(Color.WHITE);

        btnGuardar = crearBotonFormulario("Guardar");
        btnEditar = crearBotonFormulario("Actualizar");
        btnEliminar = crearBotonFormulario("Eliminar");
        btnLimpiar = crearBotonFormulario("Limpiar");

        btnGuardar.addActionListener(e -> guardarEstudiante());
        btnEditar.addActionListener(e -> actualizarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
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

        TitledBorder border = BorderFactory.createTitledBorder("Lista de Estudiantes");
        border.setTitleFont(FONT_SUBTITULO);
        panel.setBorder(border);

        panel.add(crearPanelBusqueda(), BorderLayout.NORTH);

        String[] columnas = {"Código", "DNI", "Nombres", "Apellidos", "Carrera", "Ciclo"};
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

        TitledBorder border = BorderFactory.createTitledBorder("Filtros de Búsqueda");
        border.setTitleFont(FONT_SUBTITULO);
        panel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtBuscarDni = new JTextField(8);
        btnBuscarDni = new JButton("Buscar DNI");

        txtBuscarNombre = new JTextField(10);
        btnBuscarNombre = new JButton("Buscar Nombre");

        cbFiltroCarrera = new JComboBox<>();
        btnFiltrarCarrera = new JButton("Filtrar Carrera");

        btnMostrarTodos = new JButton("Mostrar Todos");

        aplicarEstiloCampo(txtBuscarDni);
        aplicarEstiloCampo(txtBuscarNombre);
        aplicarEstiloCampo(cbFiltroCarrera);

        btnBuscarDni.setFont(FONT_BOTON);
        btnBuscarNombre.setFont(FONT_BOTON);
        btnFiltrarCarrera.setFont(FONT_BOTON);
        btnMostrarTodos.setFont(FONT_BOTON);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl1 = new JLabel("DNI:"); lbl1.setFont(FONT_ETIQUETA);
        panel.add(lbl1, gbc);
        gbc.gridx = 1;
        panel.add(txtBuscarDni, gbc);
        gbc.gridx = 2;
        panel.add(btnBuscarDni, gbc);

        gbc.gridx = 3;
        JLabel lbl2 = new JLabel("Nombre/Ape:"); lbl2.setFont(FONT_ETIQUETA);
        panel.add(lbl2, gbc);
        gbc.gridx = 4;
        panel.add(txtBuscarNombre, gbc);
        gbc.gridx = 5;
        panel.add(btnBuscarNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lbl3 = new JLabel("Carrera:"); lbl3.setFont(FONT_ETIQUETA);
        panel.add(lbl3, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(cbFiltroCarrera, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        panel.add(btnFiltrarCarrera, gbc);

        gbc.gridx = 4; gbc.gridwidth = 2;
        panel.add(btnMostrarTodos, gbc);

        btnBuscarDni.addActionListener(e -> buscarPorDni());
        btnBuscarNombre.addActionListener(e -> buscarPorNombre());
        btnFiltrarCarrera.addActionListener(e -> filtrarPorCarrera());
        btnMostrarTodos.addActionListener(e -> mostrarTodos());

        return panel;
    }

    private void buscarPorDni() {
        try {
            List<Estudiante> lista = controller.buscarPorDni(txtBuscarDni.getText());
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún estudiante con ese DNI.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }
            actualizarTabla(lista);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "DNI Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorNombre() {
        try {
            List<Estudiante> lista = controller.buscarPorNombreOApellido(txtBuscarNombre.getText());
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron coincidencias.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }
            actualizarTabla(lista);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Texto Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarPorCarrera() {
        try {
            if (cbFiltroCarrera.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione una carrera válida para filtrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String carrera = (String) cbFiltroCarrera.getSelectedItem();
            List<Estudiante> lista = controller.buscarPorCarrera(carrera);
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay estudiantes en la carrera seleccionada.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }
            actualizarTabla(lista);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarTodos() {
        txtBuscarDni.setText("");
        txtBuscarNombre.setText("");
        if (cbFiltroCarrera.getItemCount() > 0) cbFiltroCarrera.setSelectedIndex(0);
        cargarDatosTabla();
    }

    private void actualizarTabla(List<Estudiante> estudiantes) {
        this.listaActualEstudiantes = estudiantes != null ? estudiantes : new ArrayList<>();
        modeloTabla.setRowCount(0);
        for (Estudiante e : this.listaActualEstudiantes) {
            modeloTabla.addRow(new Object[]{
                e.getCodigo(), e.getDni(), e.getNombres(), e.getApellidos(), e.getCarrera(), e.getCiclo()
            });
        }
    }

    private void guardarEstudiante() {
        if (!validarCampos()) return;

        String cod = txtCodigo.getText().trim();
        String dni = txtDni.getText().trim();
        String nom = txtNombres.getText().trim();
        String ape = txtApellidos.getText().trim();
        
        String carreraTexto = (String) cbCarrera.getSelectedItem();
        int idCarrera = obtenerIdCarreraPorNombre(carreraTexto);
        int ciclo = (Integer) cbCiclo.getSelectedItem();

        boolean exito = controller.guardarEstudiante(cod, dni, nom, ape, idCarrera, ciclo);

        if (exito) {
            cargarDatosTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Estudiante registrado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "El código de estudiante ya existe o hubo un error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstudiante() {
        if (idEstudianteSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        String cod = txtCodigo.getText().trim();
        String dni = txtDni.getText().trim();
        String nom = txtNombres.getText().trim();
        String ape = txtApellidos.getText().trim();

        String carreraTexto = (String) cbCarrera.getSelectedItem();
        int idCarrera = obtenerIdCarreraPorNombre(carreraTexto);
        int ciclo = (Integer) cbCiclo.getSelectedItem();

        boolean exito = controller.actualizarEstudiante(idEstudianteSeleccionado, cod, dni, nom, ape, idCarrera, ciclo);

        if (exito) {
            cargarDatosTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Estudiante actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estudiante.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEstudiante() {
        if (idEstudianteSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int op = JOptionPane.showConfirmDialog(this, "¿Desea eliminar al estudiante?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            if (controller.eliminarEstudiante(idEstudianteSeleccionado)) {
                cargarDatosTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Estudiante eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarCarreras() {
        cbCarrera.removeAllItems();
        cbFiltroCarrera.removeAllItems();

        cbCarrera.addItem("-- Seleccione --");
        cbFiltroCarrera.addItem("-- Seleccione --");

        List<String> carreras = GestorDatos.getCarreras();
        if (carreras != null) {
            for (String c : carreras) {
                cbCarrera.addItem(c);
                cbFiltroCarrera.addItem(c);
            }
        }
    }

    public void cargarDatosTabla() {
        List<Estudiante> estudiantes = controller.listarEstudiantes();
        actualizarTabla(estudiantes);
    }

    private void cargarFormularioDesdeTabla(int fila) {
        if (fila >= 0 && fila < listaActualEstudiantes.size()) {
            Estudiante seleccionado = listaActualEstudiantes.get(fila);
            
            idEstudianteSeleccionado = seleccionado.getIdEstudiante();
            txtCodigo.setText(seleccionado.getCodigo());
            txtCodigo.setEditable(false);
            txtDni.setText(seleccionado.getDni());
            txtNombres.setText(seleccionado.getNombres());
            txtApellidos.setText(seleccionado.getApellidos());
            cbCarrera.setSelectedItem(seleccionado.getCarrera());
            cbCiclo.setSelectedItem(seleccionado.getCiclo());
        }
    }

    private void limpiarFormulario() {
        idEstudianteSeleccionado = -1;
        txtCodigo.setText("");
        txtCodigo.setEditable(true);
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        if (cbCarrera.getItemCount() > 0) cbCarrera.setSelectedIndex(0);
        cbCiclo.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private boolean validarCampos() {
        if (txtCodigo.getText().trim().isEmpty() || 
            txtDni.getText().trim().isEmpty() ||
            txtNombres.getText().trim().isEmpty() || 
            txtApellidos.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cbCarrera.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una carrera válida.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private int obtenerIdCarreraPorNombre(String nombreCarrera) {
        List<Carrera> listaCarreras = GestorDatos.getObjetoCarreras();
        if (listaCarreras != null && nombreCarrera != null) {
            for (Carrera c : listaCarreras) {
                if (c.getNombre().equalsIgnoreCase(nombreCarrera.trim())) {
                    return c.getIdCarrera();
                }
            }
        }
        return -1;
    }
}