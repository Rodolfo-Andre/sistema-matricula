package vista;

import controller.UsuarioController;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class PanelUsuarios extends JPanel {

    private static final Color COLOR_FONDO = new Color(255, 248, 220);
    private static final Color COLOR_TITULO = new Color(0, 51, 102);

    // Tipografías agrandadas
    private static final Font FONT_TITULO_PRINCIPAL = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_SECCION = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_ETIQUETA = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_CAMPO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 14);

    private UsuarioController usuarioController;
    private List<Usuario> listaUsuariosCache;
    private Usuario usuarioSeleccionado = null;

    // Componentes del Formulario (Izquierda)
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;
    private JTextField txtIdEstudiante;
    private JTextField txtIdProfesor;
    private JLabel lblEstadoActual;
    
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnCambiarEstado;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // Componentes de Búsqueda y Tabla (Derecha)
    private JTextField txtBuscarUsername;
    private JTextField txtBuscarNombre;
    private JButton btnBuscarUsername;
    private JButton btnBuscarNombre;
    private JButton btnMostrarTodos;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public PanelUsuarios() {
        usuarioController = new UsuarioController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // TÍTULO SUPERIOR
        JLabel lblTituloPrincipal = new JLabel("GESTIÓN DE USUARIOS DEL SISTEMA", SwingConstants.CENTER);
        lblTituloPrincipal.setFont(FONT_TITULO_PRINCIPAL);
        lblTituloPrincipal.setForeground(COLOR_TITULO);
        lblTituloPrincipal.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(lblTituloPrincipal, BorderLayout.NORTH);

        // CONTENEDOR CENTRAL
        JPanel panelCentral = new JPanel(new BorderLayout(12, 12));
        panelCentral.setOpaque(false);

        panelCentral.add(crearPanelFormulario(), BorderLayout.WEST);
        panelCentral.add(crearPanelTabla(), BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
    }

    // PANEL IZQUIERDO: FORMULARIO
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Datos del Usuario",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_SECCION,
                COLOR_TITULO
        ));

        JPanel pnlCampos = new JPanel(new GridBagLayout());
        pnlCampos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblUser, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtUsername = new JTextField();
        txtUsername.setFont(FONT_CAMPO);
        txtUsername.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblPass, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtPassword = new JPasswordField();
        txtPassword.setFont(FONT_CAMPO);
        txtPassword.setPreferredSize(new Dimension(0, 30));
        txtPassword.setToolTipText("Dejar en blanco al editar para no cambiarla");
        pnlCampos.add(txtPassword, gbc);

        // Rol
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.35;
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblRol, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        String[] roles = {"1 - ADMINISTRADOR", "2 - DOCENTE", "3 - ESTUDIANTE"};
        cmbRol = new JComboBox<>(roles);
        cmbRol.setFont(FONT_CAMPO);
        cmbRol.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(cmbRol, gbc);

        // ID Estudiante
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.35;
        JLabel lblIdEst = new JLabel("ID Estudiante:");
        lblIdEst.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblIdEst, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtIdEstudiante = new JTextField();
        txtIdEstudiante.setFont(FONT_CAMPO);
        txtIdEstudiante.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(txtIdEstudiante, gbc);

        // ID Profesor
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.35;
        JLabel lblIdProf = new JLabel("ID Profesor:");
        lblIdProf.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblIdProf, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        txtIdProfesor = new JTextField();
        txtIdProfesor.setFont(FONT_CAMPO);
        txtIdProfesor.setPreferredSize(new Dimension(0, 30));
        pnlCampos.add(txtIdProfesor, gbc);

        // Estado Actual
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.35;
        JLabel lblEstadoTag = new JLabel("Estado:");
        lblEstadoTag.setFont(FONT_ETIQUETA);
        pnlCampos.add(lblEstadoTag, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        lblEstadoActual = new JLabel("Sin seleccionar");
        lblEstadoActual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstadoActual.setForeground(Color.DARK_GRAY);
        pnlCampos.add(lblEstadoActual, gbc);

        panel.add(pnlCampos, BorderLayout.NORTH);

        // Botones inferiores
        JPanel pnlBotones = new JPanel(new GridLayout(3, 2, 8, 8));
        pnlBotones.setOpaque(false);
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnCambiarEstado = new JButton("Suspender/Act.");
        btnEliminar = new JButton("Eliminar Físico");
        btnLimpiar = new JButton("Limpiar");

        JButton[] botones = {btnGuardar, btnActualizar, btnCambiarEstado, btnEliminar, btnLimpiar};
        for (JButton b : botones) {
            b.setFont(FONT_BOTON);
            b.setPreferredSize(new Dimension(0, 34));
        }

        btnGuardar.addActionListener(e -> guardarUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnCambiarEstado.addActionListener(e -> cambiarEstadoUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnCambiarEstado);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);

        panel.add(pnlBotones, BorderLayout.SOUTH);

        return panel;
    }

    // PANEL DERECHO: FILTROS Y TABLA
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Lista de Usuarios",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONT_SECCION,
                COLOR_TITULO
        ));

        // Subpanel Filtros de Búsqueda
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

        txtBuscarUsername = new JTextField(9);
        txtBuscarUsername.setFont(FONT_CAMPO);
        txtBuscarUsername.setPreferredSize(new Dimension(100, 28));

        btnBuscarUsername = new JButton("Buscar Usuario");
        btnBuscarUsername.setFont(FONT_BOTON);

        txtBuscarNombre = new JTextField(12);
        txtBuscarNombre.setFont(FONT_CAMPO);
        txtBuscarNombre.setPreferredSize(new Dimension(130, 28));

        btnBuscarNombre = new JButton("Buscar Nombre");
        btnBuscarNombre.setFont(FONT_BOTON);

        btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.setFont(FONT_BOTON);

        btnBuscarUsername.addActionListener(e -> filtrarPorUsername());
        btnBuscarNombre.addActionListener(e -> filtrarPorNombre());
        btnMostrarTodos.addActionListener(e -> cargarDatosTabla());

        JLabel lblFiltroUser = new JLabel("Username:");
        lblFiltroUser.setFont(FONT_ETIQUETA);
        pnlFiltros.add(lblFiltroUser);
        pnlFiltros.add(txtBuscarUsername);
        pnlFiltros.add(btnBuscarUsername);

        pnlFiltros.add(Box.createHorizontalStrut(10));

        JLabel lblFiltroNom = new JLabel("Nombre Real:");
        lblFiltroNom.setFont(FONT_ETIQUETA);
        pnlFiltros.add(lblFiltroNom);
        pnlFiltros.add(txtBuscarNombre);
        pnlFiltros.add(btnBuscarNombre);

        pnlFiltros.add(Box.createHorizontalStrut(10));
        pnlFiltros.add(btnMostrarTodos);

        panel.add(pnlFiltros, BorderLayout.NORTH);

        // Tabla con celdas y filas más altas
        String[] columnas = {"ID", "Username", "Nombre Real", "Rol", "ID Est.", "ID Prof.", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(FONT_TABLA_CELDA);
        tablaUsuarios.setRowHeight(30);
        tablaUsuarios.getTableHeader().setFont(FONT_TABLA_HEADER);
        tablaUsuarios.getTableHeader().setPreferredSize(new Dimension(0, 32));
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(45);
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(180);
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(110);
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(70);
        tablaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(70);
        tablaUsuarios.getColumnModel().getColumn(6).setPreferredWidth(85);

        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    // --- ACCIONES CRUD Y MANEJO DE DATOS ---

    public void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        listaUsuariosCache = usuarioController.listarUsuarios();

        for (Usuario u : listaUsuariosCache) {
            String estadoStr = u.isEstado() ? "Activo" : "Suspendido";
            String nombreAMostrar = u.getNombreReal() != null ? u.getNombreReal() : "No Asignado";

            modeloTabla.addRow(new Object[]{
                u.getIdUsuario(),
                u.getUsername(),
                nombreAMostrar,
                u.getNombreRol(),
                u.getIdEstudiante() != null ? u.getIdEstudiante() : "-",
                u.getIdProfesor() != null ? u.getIdProfesor() : "-",
                estadoStr
            });
        }
        limpiarFormulario();
    }

    private void seleccionarFila() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila != -1) {
            int id = Integer.parseInt(tablaUsuarios.getValueAt(fila, 0).toString());
            
            usuarioSeleccionado = null;
            if (listaUsuariosCache != null) {
                for (Usuario u : listaUsuariosCache) {
                    if (u.getIdUsuario() == id) {
                        usuarioSeleccionado = u;
                        break;
                    }
                }
            }

            if (usuarioSeleccionado != null) {
                txtUsername.setText(usuarioSeleccionado.getUsername());
                txtPassword.setText("");
                cmbRol.setSelectedIndex(Math.max(0, usuarioSeleccionado.getIdRol() - 1));
                txtIdEstudiante.setText(usuarioSeleccionado.getIdEstudiante() != null ? usuarioSeleccionado.getIdEstudiante().toString() : "");
                txtIdProfesor.setText(usuarioSeleccionado.getIdProfesor() != null ? usuarioSeleccionado.getIdProfesor().toString() : "");
                
                lblEstadoActual.setText(usuarioSeleccionado.isEstado() ? "Activo" : "Suspendido");
                lblEstadoActual.setForeground(usuarioSeleccionado.isEstado() ? new Color(0, 128, 0) : Color.RED);
            }
        }
    }

    private void guardarUsuario() {
        String username = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());
        int idRol = cmbRol.getSelectedIndex() + 1;

        if (username.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Username y el Password son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Integer idEstudiante = txtIdEstudiante.getText().trim().isEmpty() ? null : Integer.parseInt(txtIdEstudiante.getText().trim());
            Integer idProfesor = txtIdProfesor.getText().trim().isEmpty() ? null : Integer.parseInt(txtIdProfesor.getText().trim());

            String hashNuevo = BCrypt.hashpw(pass, BCrypt.gensalt());
            Usuario nuevo = new Usuario(0, username, hashNuevo, idRol, "", idEstudiante, idProfesor, true);

            if (usuarioController.registrarUsuario(nuevo)) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar. Verifique que los IDs de Estudiante/Profesor existan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los ID de Estudiante o Profesor deben ser valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());
        int idRol = cmbRol.getSelectedIndex() + 1;

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Username no puede quedar vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Integer idEstudiante = txtIdEstudiante.getText().trim().isEmpty() ? null : Integer.parseInt(txtIdEstudiante.getText().trim());
            Integer idProfesor = txtIdProfesor.getText().trim().isEmpty() ? null : Integer.parseInt(txtIdProfesor.getText().trim());

            usuarioSeleccionado.setUsername(username);
            if (!pass.isEmpty()) {
                usuarioSeleccionado.setPassword(BCrypt.hashpw(pass, BCrypt.gensalt()));
            }
            usuarioSeleccionado.setIdRol(idRol);
            usuarioSeleccionado.setIdEstudiante(idEstudiante);
            usuarioSeleccionado.setIdProfesor(idProfesor);

            if (usuarioController.actualizarUsuario(usuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los ID de Estudiante o Profesor deben ser valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para cambiar su estado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean nuevoEstado = !usuarioSeleccionado.isEstado();
        if (usuarioController.cambiarEstadoUsuario(usuarioSeleccionado.getIdUsuario(), nuevoEstado)) {
            JOptionPane.showMessageDialog(this, "El estado del usuario ha sido actualizado.");
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar el estado del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opt = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar definitivamente al usuario '" + usuarioSeleccionado.getUsername() + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opt == JOptionPane.YES_OPTION) {
            if (usuarioController.eliminarUsuarioFisico(usuarioSeleccionado.getIdUsuario())) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado físicamente con éxito.");
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar. Verifique dependencias en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        usuarioSeleccionado = null;
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRol.setSelectedIndex(0);
        txtIdEstudiante.setText("");
        txtIdProfesor.setText("");
        lblEstadoActual.setText("Sin seleccionar");
        lblEstadoActual.setForeground(Color.DARK_GRAY);
        tablaUsuarios.clearSelection();
    }

    // --- FILTROS DE BÚSQUEDA ---

    private void filtrarPorUsername() {
        String texto = txtBuscarUsername.getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            cargarDatosTabla();
            return;
        }
        modeloTabla.setRowCount(0);
        if (listaUsuariosCache != null) {
            for (Usuario u : listaUsuariosCache) {
                if (u.getUsername().toLowerCase().contains(texto)) {
                    agregarFilaTabla(u);
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
        if (listaUsuariosCache != null) {
            for (Usuario u : listaUsuariosCache) {
                String nombreReal = u.getNombreReal() != null ? u.getNombreReal().toLowerCase() : "";
                if (nombreReal.contains(texto)) {
                    agregarFilaTabla(u);
                }
            }
        }
    }

    private void agregarFilaTabla(Usuario u) {
        String estadoStr = u.isEstado() ? "Activo" : "Suspendido";
        String nombreAMostrar = u.getNombreReal() != null ? u.getNombreReal() : "No Asignado";

        modeloTabla.addRow(new Object[]{
            u.getIdUsuario(),
            u.getUsername(),
            nombreAMostrar,
            u.getNombreRol(),
            u.getIdEstudiante() != null ? u.getIdEstudiante() : "-",
            u.getIdProfesor() != null ? u.getIdProfesor() : "-",
            estadoStr
        });
    }
}