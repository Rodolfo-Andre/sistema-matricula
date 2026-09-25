package vista;

import controller.UsuarioController;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class PanelUsuarios extends JPanel {

    private UsuarioController usuarioController;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private List<Usuario> listaUsuariosCache; 

    public PanelUsuarios() {
        usuarioController = new UsuarioController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 248, 220)); 

        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS DEL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(60, 60, 80));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Username", "Nombre Real", "Rol", "ID Est.", "ID Prof.", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
  
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(40); 
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100); 
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(200); 
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(new Color(255, 248, 220));

        JButton btnNuevo = new JButton("Nuevo Usuario");
        JButton btnEditar = new JButton("Editar");
        JButton btnEstado = new JButton("Activar/Suspender");
        JButton btnEliminar = new JButton("Eliminar Físico");
        JButton btnRecargar = new JButton("Recargar");

        btnNuevo.addActionListener(e -> mostrarFormulario(null));
        btnEditar.addActionListener(e -> editarUsuarioSeleccionado());
        btnEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarUsuarioSeleccionado());
        btnRecargar.addActionListener(e -> cargarDatosTabla());

        Font fontBotones = new Font("Segoe UI", Font.BOLD, 14);
        JButton[] botones = {btnNuevo, btnEditar, btnEstado, btnEliminar, btnRecargar};
        for (JButton btn : botones) {
            btn.setFont(fontBotones);
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.SOUTH);
    }

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
    }

    private void mostrarFormulario(Usuario u) {
        JTextField txtUsername = new JTextField(u != null ? u.getUsername() : "");
        JPasswordField txtPassword = new JPasswordField();
        
        String[] roles = {"1 - ADMINISTRADOR", "2 - DOCENTE", "3 - ESTUDIANTE"};
        JComboBox<String> cmbRol = new JComboBox<>(roles);
        if (u != null) cmbRol.setSelectedIndex(u.getIdRol() - 1);

        JTextField txtIdEstudiante = new JTextField(u != null && u.getIdEstudiante() != null ? u.getIdEstudiante().toString() : "");
        JTextField txtIdProfesor = new JTextField(u != null && u.getIdProfesor() != null ? u.getIdProfesor().toString() : "");

        Object[] mensaje = {
            "Username:", txtUsername,
            u == null ? "Password (Requerido):" : "Password (Dejar en blanco para no cambiar):", txtPassword,
            "Rol del Usuario:", cmbRol,
            "ID Estudiante (Opcional):", txtIdEstudiante,
            "ID Profesor (Opcional):", txtIdProfesor
        };

        String titulo = u == null ? "Registrar Nuevo Usuario" : "Editar Usuario";
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String user = txtUsername.getText().trim();
                String pass = new String(txtPassword.getPassword());
                int idRol = cmbRol.getSelectedIndex() + 1; 
                
                Integer idEstudiante = txtIdEstudiante.getText().trim().isEmpty() ? null : Integer.parseInt(txtIdEstudiante.getText().trim());
                Integer idProfesor = txtIdProfesor.getText().trim().isEmpty() ? null : Integer.parseInt(txtIdProfesor.getText().trim());

                if (user.isEmpty() || (u == null && pass.isEmpty())) {
                    JOptionPane.showMessageDialog(this, "El Username y el Password son obligatorios para crear.");
                    return;
                }

                if (u == null) {
                    String hashNuevo = BCrypt.hashpw(pass, BCrypt.gensalt());
                    Usuario nuevoUser = new Usuario(0, user, hashNuevo, idRol, "", idEstudiante, idProfesor, true);
                    
                    if (usuarioController.registrarUsuario(nuevoUser)) {
                        JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al registrar. Verifica que el ID de Estudiante/Profesor exista.");
                    }
                } else {
                    u.setUsername(user);
                    if (!pass.isEmpty()) {
                        u.setPassword(BCrypt.hashpw(pass, BCrypt.gensalt())); 
                    }
                    u.setIdRol(idRol);
                    u.setIdEstudiante(idEstudiante);
                    u.setIdProfesor(idProfesor);

                    if (usuarioController.actualizarUsuario(u)) {
                        JOptionPane.showMessageDialog(this, "Usuario actualizado con éxito.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar.");
                    }
                }
                cargarDatosTabla();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Los ID de Estudiante o Profesor deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para editar.");
            return;
        }
        Usuario u = listaUsuariosCache.get(fila);
        mostrarFormulario(u);
    }

    private void cambiarEstadoSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para cambiar su estado.");
            return;
        }
        Usuario u = listaUsuariosCache.get(fila);
        boolean nuevoEstado = !u.isEstado(); 
        
        if (usuarioController.cambiarEstadoUsuario(u.getIdUsuario(), nuevoEstado)) {
            JOptionPane.showMessageDialog(this, "El estado del usuario se actualizó correctamente.");
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar el estado.");
        }
    }

    private void eliminarUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para eliminarlo.");
            return;
        }
        Usuario u = listaUsuariosCache.get(fila);
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea eliminar DEFINITIVAMENTE al usuario '" + u.getUsername() + "'?", 
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (usuarioController.eliminarUsuarioFisico(u.getIdUsuario())) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar. Verifique dependencias en la base de datos.");
            }
        }
    }
}