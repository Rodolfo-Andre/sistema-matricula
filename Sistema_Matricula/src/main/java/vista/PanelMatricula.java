package vista;

import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import gestor.GestorDatos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class PanelMatricula extends JPanel {

    private JPanel panelPrincipal;
    private JTable tablaMatriculas;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscarEstudiante;
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnNuevaMatricula;
    private JButton btnVerDetalle;
    private JButton btnEliminar;
    private JLabel lblTotalMostrados;
    private JLabel lblTotalMatriculas;

    public PanelMatricula() {
        setLayout(new BorderLayout());
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(240, 248, 255));

        JLabel titulo = new JLabel("PANEL DE MATRICULAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 51, 102));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        panelFiltros.add(new JLabel("Estudiante:"));
        txtBuscarEstudiante = new JTextField(15);
        txtBuscarEstudiante.addActionListener(e -> filtrarMatriculas());
        panelFiltros.add(txtBuscarEstudiante);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> filtrarMatriculas());
        panelFiltros.add(btnBuscar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        panelFiltros.add(btnLimpiar);

        panelCentral.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = {"Codigo Matricula", "Codigo Estudiante", "Codigo Curso", "Fecha Matricula"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMatriculas = new JTable(modeloTabla);
        tablaMatriculas.setRowHeight(25);
        tablaMatriculas.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaMatriculas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        sorter = new TableRowSorter<>(modeloTabla);
        tablaMatriculas.setRowSorter(sorter);

        tablaMatriculas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnVerDetalle.setEnabled(tablaMatriculas.getSelectedRow() != -1);
                btnEliminar.setEnabled(tablaMatriculas.getSelectedRow() != -1);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaMatriculas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Matriculas"));
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelAcciones.setBackground(Color.WHITE);

        btnNuevaMatricula = new JButton("Nueva Matricula");
        btnNuevaMatricula.addActionListener(e -> nuevaMatricula());
        panelAcciones.add(btnNuevaMatricula);

        btnVerDetalle = new JButton("Ver Detalle");
        btnVerDetalle.setEnabled(false);
        btnVerDetalle.addActionListener(e -> verDetalle());
        panelAcciones.add(btnVerDetalle);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarMatricula());
        panelAcciones.add(btnEliminar);

        panelCentral.add(panelAcciones, BorderLayout.SOUTH);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.add(new JLabel("Total de matriculas: "));
        lblTotalMatriculas = new JLabel("0");
        lblTotalMatriculas.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalMatriculas.setForeground(new Color(70, 130, 180));
        panelInfo.add(lblTotalMatriculas);
        panelInfo.add(new JLabel("  |  Mostrados: "));
        lblTotalMostrados = new JLabel("0");
        lblTotalMostrados.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalMostrados.setForeground(new Color(60, 179, 113));
        panelInfo.add(lblTotalMostrados);
        panelPrincipal.add(panelInfo, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Matricula> matriculas = GestorDatos.getMatriculas();

        if (matriculas != null) {
            for (Matricula m : matriculas) {
                modeloTabla.addRow(new Object[]{
                    m.getCodigoMatricula(),
                    m.getCodigoEstudiante(),
                    m.getCodigoCurso(),
                    m.getFechaMatricula()
                });
            }
        }

        lblTotalMatriculas.setText(String.valueOf(tablaMatriculas.getRowCount()));
        lblTotalMostrados.setText(String.valueOf(tablaMatriculas.getRowCount()));
    }

    private void filtrarMatriculas() {
        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        String busqueda = txtBuscarEstudiante.getText().trim();
        if (!busqueda.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1));
        }

        if (filtros.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }

        lblTotalMostrados.setText(String.valueOf(tablaMatriculas.getRowCount()));
    }

    private void limpiarFiltros() {
        txtBuscarEstudiante.setText("");
        sorter.setRowFilter(null);
        lblTotalMostrados.setText(String.valueOf(tablaMatriculas.getRowCount()));
    }

    private void nuevaMatricula() {
        List<Estudiante> estudiantes = GestorDatos.getEstudiantes();
        List<Curso> cursos = GestorDatos.getCursos();

        if (estudiantes == null || estudiantes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay estudiantes disponibles para matricular.",
                "Sin estudiantes",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cursos == null || cursos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay cursos disponibles para matricular.",
                "Sin cursos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        JComboBox<String> comboEstudiantes = new JComboBox<>();
        for (Estudiante e : estudiantes) {
            comboEstudiantes.addItem(e.getCodigo() + " - " + e.getNombre());
        }

        JComboBox<String> comboCursos = new JComboBox<>();
        for (Curso c : cursos) {
            comboCursos.addItem(c.getCodigo() + " - " + c.getNombre());
        }

        panel.add(new JLabel("Estudiante:"));
        panel.add(comboEstudiantes);
        panel.add(new JLabel("Curso:"));
        panel.add(comboCursos);

        int resultado = JOptionPane.showConfirmDialog(this, panel,
            "Nueva Matricula", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String seleccionEstudiante = (String) comboEstudiantes.getSelectedItem();
            String seleccionCurso = (String) comboCursos.getSelectedItem();

            if (seleccionEstudiante != null && seleccionCurso != null) {
                String codigoEstudiante = seleccionEstudiante.split(" - ")[0];
                String codigoCurso = seleccionCurso.split(" - ")[0];

                Matricula nueva = new Matricula(
                    "MAT-" + (System.currentTimeMillis() % 10000),
                    codigoEstudiante,
                    codigoCurso,
                    java.time.LocalDate.now().toString()
                );

                GestorDatos.agregarMatricula(nueva);
                cargarDatos();

                JOptionPane.showMessageDialog(this,
                    "Matricula creada exitosamente!",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void verDetalle() {
        int fila = tablaMatriculas.getSelectedRow();
        if (fila == -1) return;

        String codigoMatricula = (String) modeloTabla.getValueAt(fila, 0);
        String codigoEstudiante = (String) modeloTabla.getValueAt(fila, 1);
        String codigoCurso = (String) modeloTabla.getValueAt(fila, 2);
        String fecha = (String) modeloTabla.getValueAt(fila, 3);

        String detalle = String.format(
            "<html><b>Codigo Matricula:</b> %s<br>" +
            "<b>Codigo Estudiante:</b> %s<br>" +
            "<b>Codigo Curso:</b> %s<br>" +
            "<b>Fecha:</b> %s</html>",
            codigoMatricula, codigoEstudiante, codigoCurso, fecha
        );

        JOptionPane.showMessageDialog(this, detalle,
            "Detalle de Matricula", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarMatricula() {
        int fila = tablaMatriculas.getSelectedRow();
        if (fila == -1) return;

        String codigoMatricula = (String) modeloTabla.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "Desea eliminar la matricula " + codigoMatricula + "?",
            "Confirmar eliminacion",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            GestorDatos.eliminarMatricula(codigoMatricula);
            cargarDatos();
            JOptionPane.showMessageDialog(this,
                "Matricula eliminada exitosamente!",
                "Exito",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
