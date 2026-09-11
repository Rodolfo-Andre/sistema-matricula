package vista;

import gestor.GestorDatos;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    
    private JPanel panelContenido;
    private CardLayout cardLayout;
    private JPanel panelMenu;
    private PanelReportes panelReportesInstance; 
    private PanelEstudiantes panelEstudiantesInstance;
    
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
        panelPrincipal.setBackground(new Color(255, 248, 220));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(255, 215, 0));
        panelHeader.setPreferredSize(new Dimension(1100, 70));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 20));
        
        JLabel lblTituloHeader = new JLabel("SISTEMA DE MATRICULA UNIVERSITARIA");
        lblTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloHeader.setForeground(new Color(50, 50, 50));
        lblTituloHeader.setHorizontalAlignment(SwingConstants.CENTER);
        panelHeader.add(lblTituloHeader, BorderLayout.CENTER);
        
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(255, 248, 220));

        panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(new Color(60, 60, 80));
        panelMenu.setPreferredSize(new Dimension(230, 0));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(15, 10, 20, 10));

        JButton btnMenuTitulo = crearBotonMenu("MENU PRINCIPAL");
        btnMenuTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnMenuTitulo.setForeground(new Color(255, 215, 0));
        btnMenuTitulo.addActionListener(e -> mostrarPanel("Bienvenida"));
        panelMenu.add(btnMenuTitulo);
        
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(255, 215, 0));
        separador.setMaximumSize(new Dimension(200, 2));
        panelMenu.add(separador);
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
        
        JSeparator separador2 = new JSeparator();
        separador2.setForeground(new Color(255, 215, 0));
        separador2.setMaximumSize(new Dimension(200, 2));
        panelMenu.add(separador2);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton btnSalir = crearBotonMenu("Salir");
        panelMenu.add(btnSalir);
        
        panelCentral.add(panelMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(new Color(255, 248, 220));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
     
        panelReportesInstance = new PanelReportes();
        panelEstudiantesInstance = new PanelEstudiantes();
        
        panelContenido.add(crearPanelBienvenida(), "Bienvenida");
        panelContenido.add(panelEstudiantesInstance, "Estudiantes");
        panelContenido.add(crearPanelInfo("Gestion de Cursos", "Orlando Leon"), "Cursos");
        panelContenido.add(crearPanelInfo("Gestion de Matricula", "Equipo"), "Matricula");
        panelContenido.add(crearPanelInfo("Convalidacion", "Anthony"), "Convalidacion");
        panelContenido.add(panelReportesInstance, "Reportes"); 
        
        panelCentral.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setBackground(new Color(60, 60, 80));
        panelFooter.setPreferredSize(new Dimension(1100, 35));
        
        JLabel lblFooter = new JLabel("2026 - Sistema de Matricula Universitaria | Todos los derechos reservados");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(255, 215, 0));
        panelFooter.add(lblFooter);
        
        panelPrincipal.add(panelFooter, BorderLayout.SOUTH);

        btnEstudiantes.addActionListener(e -> {
            panelEstudiantesInstance.cargarDatosTabla(); 
            mostrarPanel("Estudiantes");
        });
        btnCursos.addActionListener(e -> mostrarPanel("Cursos"));
        btnMatricula.addActionListener(e -> mostrarPanel("Matricula"));
        btnConvalidacion.addActionListener(e -> mostrarPanel("Convalidacion"));
        btnReportes.addActionListener(e -> abrirReportes());
        
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
    
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 215, 0));
                } else {
                    g2.setColor(new Color(80, 80, 100));
                }
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
            BorderFactory.createLineBorder(new Color(100, 100, 120), 1),
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
                    boton.setForeground(new Color(50, 50, 50));
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
        panel.setBackground(new Color(255, 248, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JLabel lblTitulo = new JLabel("SISTEMA DE MATRICULA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitulo.setForeground(new Color(60, 60, 80));
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
        lblLinea.setForeground(new Color(255, 215, 0));
        gbc.gridy = 2;
        panel.add(lblLinea, gbc);
        
        JLabel lblDescripcion = new JLabel();
        lblDescripcion.setText("<html><center>Bienvenido al Sistema de Gestion de Matricula<br>"
            + "Seleccione una opcion del menu lateral para comenzar</center></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDescripcion.setForeground(new Color(80, 80, 100));
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
        panel.setBackground(new Color(255, 248, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(60, 60, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        JLabel lblLinea = new JLabel("-----------------------------");
        lblLinea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblLinea.setForeground(new Color(255, 215, 0));
        gbc.gridy = 1;
        panel.add(lblLinea, gbc);
        
        JLabel lblDesarrollador = new JLabel("Modulo desarrollado por: " + desarrollador);
        lblDesarrollador.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblDesarrollador.setForeground(new Color(80, 80, 100));
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
    
    private void abrirReportes() {
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

            panelReportesInstance.cargarDatosIniciales();
            mostrarPanel("Reportes");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir reportes: " + ex.getMessage(),
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