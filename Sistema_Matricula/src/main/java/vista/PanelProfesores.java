package vista;

import controller.ProfesorController;
import modelo.Profesor;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelProfesores extends JPanel {

    private static final Color COLOR_FONDO = new Color(255, 248, 220);
    private static final Color COLOR_TITULO = new Color(0, 51, 102);

    private static final Font FONT_TITULO_PRINCIPAL = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_SECCION = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_ETIQUETA = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_CAMPO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 14);

    private ProfesorController profesorController;
    private List<Profesor> listaProfesoresCache;
    private int idProfesorSeleccionado = -1;

    private JTextField txtDni;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtEspecialidad;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTextField txtBuscarDni;
    private JTextField txtBuscarNombre;
    private JButton btnBuscarDni;
    private JButton btnBuscarNombre;
    private JButton btnMostrarTodos;
    private JTable tablaProfesores;
    private DefaultTableModel modeloTabla;

    public PanelProfesores() {
        profesorController = new ProfesorController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblTituloPrincipal = new JLabel("GESTIÓN DE PROFESORES", SwingConstants.CENTER);
        lblTituloPrincipal.setFont(FONT_TITULO_PRINCIPAL);
        lblTituloPrincipal.setForeground(COLOR_TITULO);
        lblTituloPrincipal.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(lblTituloPrincipal, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(12, 12));
        panelCentral.setOpaque(false);

        panelCentral.add(crearPanelFormulario(), BorderLayout.WEST);
        panelCentral.add(crearPanelTabla(), BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Datos del Profesor",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_SECCION,
                COLOR_TITULO
        ));

        JPanel pnlCampos = new JPanel(new GridBagLayout());
        pnlCampos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // DNI
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        JLabel lblDni = new JLabel("DNI:");
        lblDni.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblDni, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtDni = new JTextField();
        txtDni.setFont(FONT_CAMPO);
        txtDni.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(txtDni, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        JLabel lblNombres = new JLabel("Nombres:");
        lblNombres.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblNombres, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtNombres = new JTextField();
        txtNombres.setFont(FONT_CAMPO);
        txtNombres.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(txtNombres, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.35;
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblApellidos, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtApellidos = new JTextField();
        txtApellidos.setFont(FONT_CAMPO);
        txtApellidos.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(txtApellidos, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.35;
        JLabel lblEsp = new JLabel("Especialidad:");
        lblEsp.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblEsp, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtEspecialidad = new JTextField();
        txtEspecialidad.setFont(FONT_CAMPO);
        txtEspecialidad.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(txtEspecialidad, gbc);

        panel.add(pnlCampos, BorderLayout.NORTH);

        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlBotones.setOpaque(false);
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        JButton[] botones = {btnGuardar, btnActualizar, btnEliminar, btnLimpiar};
        for (JButton b : botones) {
            b.setFont(FONT_BOTON);
            b.setPreferredSize(new Dimension(0, 34));
        }

        btnGuardar.addActionListener(e -> guardarProfesor());
        btnActualizar.addActionListener(e -> actualizarProfesor());
        btnEliminar.addActionListener(e -> eliminarProfesor());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);

        panel.add(pnlBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Lista de Profesores",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_SECCION,
                COLOR_TITULO
        ));

        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        pnlFiltros.setOpaque(false);
        pnlFiltros.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Filtros de Búsqueda",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                Color.DARK_GRAY
        ));

        txtBuscarDni = new JTextField(9);
        txtBuscarDni.setFont(FONT_CAMPO);
        txtBuscarDni.setPreferredSize(new Dimension(100, 28));

        btnBuscarDni = new JButton("Buscar DNI");
        btnBuscarDni.setFont(FONT_BOTON);

        txtBuscarNombre = new JTextField(12);
        txtBuscarNombre.setFont(FONT_CAMPO);
        txtBuscarNombre.setPreferredSize(new Dimension(130, 28));

        btnBuscarNombre = new JButton("Buscar Nombre");
        btnBuscarNombre.setFont(FONT_BOTON);

        btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.setFont(FONT_BOTON);

        btnBuscarDni.addActionListener(e -> filtrarPorDni());
        btnBuscarNombre.addActionListener(e -> filtrarPorNombre());
        btnMostrarTodos.addActionListener(e -> cargarDatosTabla());

        JLabel lblFiltroDni = new JLabel("DNI:");
        lblFiltroDni.setFont(FONT_ETIQUETA);
        pnlFiltros.add(lblFiltroDni);
        pnlFiltros.add(txtBuscarDni);
        pnlFiltros.add(btnBuscarDni);

        pnlFiltros.add(Box.createHorizontalStrut(10));

        JLabel lblFiltroNom = new JLabel("Nombre/Ape:");
        lblFiltroNom.setFont(FONT_ETIQUETA);
        pnlFiltros.add(lblFiltroNom);
        pnlFiltros.add(txtBuscarNombre);
        pnlFiltros.add(btnBuscarNombre);

        pnlFiltros.add(Box.createHorizontalStrut(10));
        pnlFiltros.add(btnMostrarTodos);

        panel.add(pnlFiltros, BorderLayout.NORTH);

        String[] columnas = {"ID", "DNI", "Nombres", "Apellidos", "Especialidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaProfesores = new JTable(modeloTabla);
        tablaProfesores.setFont(FONT_TABLA_CELDA);
        tablaProfesores.setRowHeight(30);
        tablaProfesores.getTableHeader().setFont(FONT_TABLA_HEADER);
        tablaProfesores.getTableHeader().setPreferredSize(new Dimension(0, 32));
        tablaProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaProfesores.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaProfesores.getColumnModel().getColumn(1).setPreferredWidth(110);
        tablaProfesores.getColumnModel().getColumn(2).setPreferredWidth(170);
        tablaProfesores.getColumnModel().getColumn(3).setPreferredWidth(170);
        tablaProfesores.getColumnModel().getColumn(4).setPreferredWidth(180);

        tablaProfesores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaProfesores);
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    public void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        listaProfesoresCache = profesorController.listarProfesores();

        for (Profesor p : listaProfesoresCache) {
            modeloTabla.addRow(new Object[]{
                p.getIdProfesor(),
                p.getDni(),
                p.getNombres(),
                p.getApellidos(),
                p.getEspecialidad()
            });
        }
        limpiarFormulario();
    }

    private void seleccionarFila() {
        int fila = tablaProfesores.getSelectedRow();
        if (fila != -1) {
            idProfesorSeleccionado = Integer.parseInt(tablaProfesores.getValueAt(fila, 0).toString());
            txtDni.setText(tablaProfesores.getValueAt(fila, 1).toString());
            txtNombres.setText(tablaProfesores.getValueAt(fila, 2).toString());
            txtApellidos.setText(tablaProfesores.getValueAt(fila, 3).toString());
            txtEspecialidad.setText(tablaProfesores.getValueAt(fila, 4) != null ? tablaProfesores.getValueAt(fila, 4).toString() : "");
        }
    }

    private void guardarProfesor() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();

        if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "DNI, Nombres y Apellidos son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Profesor nuevo = new Profesor(0, dni, nombres, apellidos, especialidad);
        if (profesorController.registrarProfesor(nuevo)) {
            JOptionPane.showMessageDialog(this, "Profesor registrado correctamente.");
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el profesor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProfesor() {
        if (idProfesorSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un profesor de la tabla para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();

        Profesor p = new Profesor(idProfesorSeleccionado, dni, nombres, apellidos, especialidad);
        if (profesorController.actualizarProfesor(p)) {
            JOptionPane.showMessageDialog(this, "Profesor actualizado correctamente.");
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProfesor() {
        if (idProfesorSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un profesor de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opt = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al profesor seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (opt == JOptionPane.YES_OPTION) {
            if (profesorController.eliminarProfesor(idProfesorSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Profesor eliminado exitosamente.");
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar el profesor porque tiene cursos asignados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        idProfesorSeleccionado = -1;
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEspecialidad.setText("");
        tablaProfesores.clearSelection();
    }

    private void filtrarPorDni() {
        String dni = txtBuscarDni.getText().trim().toLowerCase();
        if (dni.isEmpty()) {
            cargarDatosTabla();
            return;
        }
        modeloTabla.setRowCount(0);
        if (listaProfesoresCache != null) {
            for (Profesor p : listaProfesoresCache) {
                if (p.getDni().toLowerCase().contains(dni)) {
                    modeloTabla.addRow(new Object[]{p.getIdProfesor(), p.getDni(), p.getNombres(), p.getApellidos(), p.getEspecialidad()});
                }
            }
        }
    }

    private void filtrarPorNombre() {
        String texto = txtBuscarNombre.getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            cargarDatosTabla();
            return;
        }
        modeloTabla.setRowCount(0);
        if (listaProfesoresCache != null) {
            for (Profesor p : listaProfesoresCache) {
                if (p.getNombres().toLowerCase().contains(texto) || p.getApellidos().toLowerCase().contains(texto)) {
                    modeloTabla.addRow(new Object[]{p.getIdProfesor(), p.getDni(), p.getNombres(), p.getApellidos(), p.getEspecialidad()});
                }
            }
        }
    }
}