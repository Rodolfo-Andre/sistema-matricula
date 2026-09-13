/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author Anthony
 */
import java.awt.*;
import javax.swing.*;

public class PanelConvalidacion extends JPanel {

    private JTextField txtEstudiante;
    private JTextField txtCursoOrigen;
    private JTextField txtCreditosOrigen;
    private JTextField txtCursoDestino;
    private JTextField txtCreditosDestino;

    private JLabel lblResultado;

    public PanelConvalidacion() {

        setLayout(new BorderLayout());
        setBackground(new Color(255, 249, 225));

        crearInterfaz();
    }

    private void crearInterfaz() {

        // TITULO
        JLabel titulo = new JLabel("CONVALIDACION DE CURSOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(new Color(55, 58, 85));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        titulo.setBorder(
                BorderFactory.createEmptyBorder(30, 10, 25, 10)
        );

        add(titulo, BorderLayout.NORTH);


        // PANEL DEL FORMULARIO
        JPanel formulario = new JPanel(new GridBagLayout());

        formulario.setBackground(new Color(255, 249, 225));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        txtEstudiante = new JTextField(20);
        txtCursoOrigen = new JTextField(20);
        txtCreditosOrigen = new JTextField(20);

        txtCursoDestino = new JTextField(20);
        txtCreditosDestino = new JTextField(20);


        agregarCampo(
                formulario,
                gbc,
                0,
                "Estudiante:",
                txtEstudiante
        );

        agregarCampo(
                formulario,
                gbc,
                1,
                "Curso de origen:",
                txtCursoOrigen
        );

        agregarCampo(
                formulario,
                gbc,
                2,
                "Creditos del curso de origen:",
                txtCreditosOrigen
        );

        agregarCampo(
                formulario,
                gbc,
                3,
                "Curso a convalidar:",
                txtCursoDestino
        );

        agregarCampo(
                formulario,
                gbc,
                4,
                "Creditos del curso a convalidar:",
                txtCreditosDestino
        );


        // BOTON
        JButton btnEvaluar =
                new JButton("EVALUAR CONVALIDACION");

        btnEvaluar.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        formulario.add(btnEvaluar, gbc);


        // RESULTADO
        lblResultado =
                new JLabel("Estado: PENDIENTE");

        lblResultado.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        lblResultado.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy = 6;

        formulario.add(lblResultado, gbc);


        btnEvaluar.addActionListener(e -> evaluar());


        add(formulario, BorderLayout.CENTER);
    }


    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            JTextField campo) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;

        panel.add(new JLabel(texto), gbc);


        gbc.gridx = 1;

        panel.add(campo, gbc);
    }


    private void evaluar() {

        try {

            int creditosOrigen =
                    Integer.parseInt(
                            txtCreditosOrigen.getText()
                    );

            int creditosDestino =
                    Integer.parseInt(
                            txtCreditosDestino.getText()
                    );


            if (creditosOrigen >= creditosDestino) {

                lblResultado.setText(
                        "Estado: APROBADA"
                );

            } else {

                lblResultado.setText(
                        "Estado: RECHAZADA"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese correctamente los creditos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}