package vista;

import controller.UsuarioController;
import modelo.Usuario;
import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 *
 * @author jairh
 */
public class Login extends JFrame {

    private UsuarioController usuarioController;

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public Login() {
        usuarioController = new UsuarioController();

        setTitle("Sistema de Matrícula - Login");
        setSize(350, 350); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(null); 

        URL urlLogo = getClass().getResource("/imagenes/logo_matricula.png");
        if (urlLogo != null) {
            ImageIcon logoOriginal = new ImageIcon(urlLogo);
            Image imagenEscalada = logoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imagenEscalada));
            lblLogo.setBounds(115, 15, 100, 100); 
            add(lblLogo);
        } else {
            System.err.println("No se pudo cargar la imagen. Verifica que logo_cursos.png esté en el paquete imagenes.");
        }

     
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 130, 80, 25);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(130, 130, 150, 25);
        add(txtUsuario);

 
        txtUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtPassword.requestFocus();
            }
        });

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 180, 80, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 180, 150, 25);
        add(txtPassword);

  
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });


        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(125, 230, 100, 30);
        add(btnIngresar);

    
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        String username = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

 
        Usuario usuarioLogueado = usuarioController.login(username, password);

        if (usuarioLogueado != null) {
 
            String nombreMostrado = usuarioLogueado.getNombreReal() != null ? usuarioLogueado.getNombreReal() : usuarioLogueado.getUsername();
            JOptionPane.showMessageDialog(this, "¡Bienvenido(a), " + nombreMostrado + "!");
            MenuPrincipal menu = new MenuPrincipal(usuarioLogueado);
            menu.setVisible(true);
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}