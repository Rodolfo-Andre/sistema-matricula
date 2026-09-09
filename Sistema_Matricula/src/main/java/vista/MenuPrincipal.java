package vista;

import gestor.GestorDatos;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private static final Color COLOR_FONDO = new Color(255, 248, 220);
    private static final Color COLOR_DORADO = new Color(255, 215, 0);
    private static final Color COLOR_OSCURO = new Color(60, 60, 80);
    private static final Color COLOR_GRIS = new Color(100, 100, 120);
    private static final Color COLOR_TEXTO = new Color(50, 50, 50);
    private static final Color COLOR_SUBTEXTO = new Color(80, 80, 100);
    private static final Color COLOR_TITULO = new Color(60, 60, 80);

    private JPanel panelContenido;
    private CardLayout cardLayout;
    private JPanel panelMenu;
    private PanelReportes panelReportesInstance;
    private PanelMatricula panelMatriculaInstance;

    public MenuPrincipal() {
        initComponents();
        setTitle("Sistema de Matrícula Universitaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
    }

    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);

        JPanel panelHeader = crearHeader();
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(COLOR_FONDO);

        panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(COLOR_OSCURO);
        panelMenu.setPreferredSize(new Dimension(230, 0));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(15, 10, 20, 10));

        JButton btnMenuTitulo = crearBotonMenu("MENU PRINCIPAL");
        btnMenuTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnMenuTitulo.setForeground(COLOR_DORADO);
        btnMenuTitulo.addActionListener(e -> mostrarPanel("Bienvenida"));
        panelMenu.add(btnMenuTitulo);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(crearSeparador());
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnEstudiantes = crearBotonMenu("Gestion de Estudiantes");
        JButton btnCursos = crearBotonMenu("Gestion de Cursos");
        JButton btnMatricula = crearBotonMenu("Gestion de Matricula");
        JButton btnConvalidacion = crearBotonMenu("Convalidacion");
        JButton btnReportes = crearBotonMenu("Reportes");

        panelMenu.add(btnEstudiantes);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 8)));
        panelMenu.add(btnCursos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 8)));
        panelMenu.add(btnMatricula);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 8)));
        panelMenu.add(btnConvalidacion);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 8)));
        panelMenu.add(btnReportes);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 20)));
        panelMenu.add(crearSeparador());
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnSalir = crearBotonMenu("Salir");
        panelMenu.add(btnSalir);

        panelCentral.add(panelMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panelReportesInstance = new PanelReportes();
        panelMatriculaInstance = new PanelMatricula();

        panelContenido.add(crearPanelBienvenida(), "Bienvenida");
        panelContenido.add(crearPanelInfo("Gestion de Estudiantes", "Jayson"), "Estudiantes");
        panelContenido.add(crearPanelInfo("Gestion de Cursos", "Orlando Leon"), "Cursos");
        panelContenido.add(panelMatriculaInstance, "Matricula");
        panelContenido.add(crearPanelInfo("Convalidacion", "Anthony"), "Convalidacion");
        panelContenido.add(panelReportesInstance, "Reportes");

        panelCentral.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        panelPrincipal.add(crearFooter(), BorderLayout.SOUTH);

        btnEstudiantes.addActionListener(e -> mostrarPanel("Estudiantes"));
        btnCursos.addActionListener(e -> mostrarPanel("Cursos"));
        btnMatricula.addActionListener(e -> abrirPanelConDatos(
            panelMatriculaInstance::cargarDatos, "Matricula", "matriculas"));
        btnConvalidacion.addActionListener(e -> mostrarPanel("Convalidacion"));
        btnReportes.addActionListener(e -> abrirPanelConDatos(
            panelReportesInstance::cargarDatosIniciales, "Reportes", "reportes"));

        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Esta seguro que desea salir del sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        add(panelPrincipal);
    }

    private JPanel crearHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_DORADO);
        panel.setPreferredSize(new Dimension(1100, 70));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 20));

        JLabel lblTitulo = new JLabel("SISTEMA DE MATRICULA UNIVERSITARIA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(COLOR_OSCURO);
        panel.setPreferredSize(new Dimension(1100, 35));

        JLabel lblFooter = new JLabel("2026 - Sistema de Matricula Universitaria | Todos los derechos reservados");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(COLOR_DORADO);
        panel.add(lblFooter);

        return panel;
    }

    private JSeparator crearSeparador() {
        JSeparator separador = new JSeparator();
        separador.setForeground(COLOR_DORADO);
        separador.setMaximumSize(new Dimension(200, 2));
        return separador;
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_DORADO : COLOR_OSCURO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };

        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GRIS, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 42));
        boton.setPreferredSize(new Dimension(200, 42));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!texto.equals("MENU PRINCIPAL")) {
                    boton.setForeground(COLOR_TEXTO);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!texto.equals("MENU PRINCIPAL")) {
                    boton.setForeground(Color.WHITE);
                }
            }
        });

        return boton;
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel lblTitulo = new JLabel("SISTEMA DE MATRICULA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitulo.setForeground(COLOR_TITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Universidad ");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblSubtitulo.setForeground(new Color(100, 100, 120));
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);

        JLabel lblLinea = new JLabel("________________________________");
        lblLinea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblLinea.setForeground(COLOR_DORADO);
        gbc.gridy = 2;
        panel.add(lblLinea, gbc);

        JLabel lblDescripcion = new JLabel();
        lblDescripcion.setText("<html><center>Bienvenido al Sistema de Gestion de Matricula<br>"
            + "Seleccione una opcion del menu lateral para comenzar</center></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDescripcion.setForeground(COLOR_SUBTEXTO);
        gbc.gridy = 3;
        panel.add(lblDescripcion, gbc);

        JLabel lblVersion = new JLabel("Version 1.0");
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblVersion.setForeground(new Color(180, 180, 180));
        gbc.gridy = 4;
        panel.add(lblVersion, gbc);

        return panel;
    }

    private JPanel crearPanelInfo(String titulo, String desarrollador) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(COLOR_TITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblLinea = new JLabel("-----------------------------");
        lblLinea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblLinea.setForeground(COLOR_DORADO);
        gbc.gridy = 1;
        panel.add(lblLinea, gbc);

        JLabel lblDesarrollador = new JLabel("Modulo desarrollado por: " + desarrollador);
        lblDesarrollador.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblDesarrollador.setForeground(COLOR_SUBTEXTO);
        gbc.gridy = 2;
        panel.add(lblDesarrollador, gbc);

        JLabel lblEstado = new JLabel("Modulo en desarrollo...");
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblEstado.setForeground(new Color(180, 180, 180));
        gbc.gridy = 3;
        panel.add(lblEstado, gbc);

        return panel;
    }

    private void mostrarPanel(String nombre) {
        cardLayout.show(panelContenido, nombre);
    }

    private void abrirPanelConDatos(Runnable cargadorDatos, String nombrePanel, String nombreError) {
        try {
            if (!GestorDatos.tieneDatos()) {
                int respuesta = JOptionPane.showConfirmDialog(this,
                    "No hay datos cargados en el sistema.\nDesea cargar datos de ejemplo?",
                    "Datos vacios",
                    JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    GestorDatos.cargarDatosEjemplo();
                    JOptionPane.showMessageDialog(this,
                        "Datos de ejemplo cargados exitosamente",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    return;
                }
            }

            cargadorDatos.run();
            mostrarPanel(nombrePanel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir " + nombreError + ": " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        gestor.ConexionBD.inicializarBD();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
