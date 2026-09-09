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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class PanelMatricula extends JPanel {

    private static final Color COLOR_FONDO = new Color(240, 248, 255);
    private static final Color COLOR_HEADER = new Color(0, 51, 102);
    private static final Color COLOR_AZUL = new Color(70, 130, 180);
    private static final Color COLOR_VERDE = new Color(60, 179, 113);
    private static final Color COLOR_NARANJA = new Color(255, 140, 0);

    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> comboFiltroEstudiante;
    private JComboBox<String> comboFiltroPeriodo;
    private JTextField txtBuscarCurso;
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnNuevaMatricula;
    private JButton btnVerDetalle;
    private JButton btnEliminar;
    private JTable tablaMatriculas;
    private JLabel lblTotalMostrados;
    private JLabel lblTotalMatriculas;
    private JLabel lblTotalEstudiantes;
    private JLabel lblPeriodoActual;

    public PanelMatricula() {
        setLayout(new BorderLayout());
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(COLOR_FONDO);

        panelPrincipal.add(crearPanelResumen(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelCentral(), BorderLayout.CENTER);
        panelPrincipal.add(crearPanelInfo(), BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel crearPanelResumen() {
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(COLOR_FONDO);

        JLabel titulo = new JLabel("PANEL DE MATRICULAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(COLOR_HEADER);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        panelTitulo.add(titulo, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        JPanel card1 = crearTarjeta("Total Matriculas", "0", COLOR_AZUL);
        lblTotalMatriculas = (JLabel) ((JPanel) card1.getComponent(1)).getComponent(0);

        JPanel card2 = crearTarjeta("Estudiantes Matriculados", "0", COLOR_VERDE);
        lblTotalEstudiantes = (JLabel) ((JPanel) card2.getComponent(1)).getComponent(0);

        JPanel card3 = crearTarjeta("Periodo Actual", "2026-1", COLOR_NARANJA);
        lblPeriodoActual = (JLabel) ((JPanel) card3.getComponent(1)).getComponent(0);

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);

        panelTitulo.add(panel, BorderLayout.SOUTH);
        return panelTitulo;
    }

    private JPanel crearTarjeta(String titulo, String valor, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setForeground(color);
        card.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelValor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelValor.setBackground(Color.WHITE);
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 26));
        lblValor.setForeground(color);
        panelValor.add(lblValor);
        card.add(panelValor, BorderLayout.CENTER);

        return card;
    }

    private JPanel crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        panelFiltros.add(new JLabel("Estudiante:"));
        comboFiltroEstudiante = new JComboBox<>();
        comboFiltroEstudiante.addItem("Todos");
        comboFiltroEstudiante.setPreferredSize(new Dimension(200, 25));
        comboFiltroEstudiante.addActionListener(e -> filtrarMatriculas());
        panelFiltros.add(comboFiltroEstudiante);

        panelFiltros.add(Box.createHorizontalStrut(10));

        panelFiltros.add(new JLabel("Periodo:"));
        comboFiltroPeriodo = new JComboBox<>();
        comboFiltroPeriodo.addItem("Todos");
        comboFiltroPeriodo.setPreferredSize(new Dimension(100, 25));
        comboFiltroPeriodo.addActionListener(e -> filtrarMatriculas());
        panelFiltros.add(comboFiltroPeriodo);

        panelFiltros.add(Box.createHorizontalStrut(10));

        panelFiltros.add(new JLabel("Curso:"));
        txtBuscarCurso = new JTextField(15);
        txtBuscarCurso.addActionListener(e -> filtrarMatriculas());
        panelFiltros.add(txtBuscarCurso);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> filtrarMatriculas());
        panelFiltros.add(btnBuscar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        panelFiltros.add(btnLimpiar);

        panelCentral.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = {"Matricula", "Estudiante", "Curso", "Periodo"};
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
        tablaMatriculas.getTableHeader().setReorderingAllowed(false);

        sorter = new TableRowSorter<>(modeloTabla);
        tablaMatriculas.setRowSorter(sorter);

        tablaMatriculas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean seleccion = tablaMatriculas.getSelectedRow() != -1;
                btnVerDetalle.setEnabled(seleccion);
                btnEliminar.setEnabled(seleccion);
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

        return panelCentral;
    }

    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Mostrados: "));
        lblTotalMostrados = new JLabel("0");
        lblTotalMostrados.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalMostrados.setForeground(COLOR_VERDE);
        panel.add(lblTotalMostrados);
        return panel;
    }

    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Matricula> matriculas = GestorDatos.getMatriculas();
        cargarFiltros();

        Set<String> estudiantesMatriculados = new LinkedHashSet<>();

        if (matriculas != null) {
            for (Matricula m : matriculas) {
                modeloTabla.addRow(new Object[]{
                    m.getCodigoMatricula(),
                    m.getNombreEstudianteDisplay(),
                    m.getNombreCursoDisplay(),
                    m.getPeriodo()
                });
                estudiantesMatriculados.add(m.getCodigoEstudiante());
            }
        }

        lblTotalMatriculas.setText(String.valueOf(tablaMatriculas.getRowCount()));
        lblTotalEstudiantes.setText(String.valueOf(estudiantesMatriculados.size()));
        lblPeriodoActual.setText(obtenerPeriodoMasReciente(matriculas));
        lblTotalMostrados.setText(String.valueOf(tablaMatriculas.getRowCount()));
    }

    private void cargarFiltros() {
        String seleccionEstudiante = (String) comboFiltroEstudiante.getSelectedItem();
        comboFiltroEstudiante.removeAllItems();
        comboFiltroEstudiante.addItem("Todos");

        List<Estudiante> estudiantes = GestorDatos.getEstudiantes();
        if (estudiantes != null) {
            for (Estudiante e : estudiantes) {
                comboFiltroEstudiante.addItem(e.getNombre());
            }
        }

        if (seleccionEstudiante != null) {
            comboFiltroEstudiante.setSelectedItem(seleccionEstudiante);
        }

        String seleccionPeriodo = (String) comboFiltroPeriodo.getSelectedItem();
        comboFiltroPeriodo.removeAllItems();
        comboFiltroPeriodo.addItem("Todos");

        List<Matricula> matriculas = GestorDatos.getMatriculas();
        if (matriculas != null) {
            Set<String> periodos = new LinkedHashSet<>();
            for (Matricula m : matriculas) {
                if (m.getPeriodo() != null && !m.getPeriodo().isEmpty()) {
                    periodos.add(m.getPeriodo());
                }
            }
            for (String p : periodos) {
                comboFiltroPeriodo.addItem(p);
            }
        }

        if (seleccionPeriodo != null) {
            comboFiltroPeriodo.setSelectedItem(seleccionPeriodo);
        }
    }

    private void filtrarMatriculas() {
        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        String seleccionEstudiante = (String) comboFiltroEstudiante.getSelectedItem();
        if (seleccionEstudiante != null && !seleccionEstudiante.equals("Todos")) {
            filtros.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(seleccionEstudiante), 1));
        }

        String seleccionPeriodo = (String) comboFiltroPeriodo.getSelectedItem();
        if (seleccionPeriodo != null && !seleccionPeriodo.equals("Todos")) {
            filtros.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(seleccionPeriodo) + "$", 3));
        }

        String busquedaCurso = txtBuscarCurso.getText().trim();
        if (!busquedaCurso.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + busquedaCurso, 2));
        }

        if (filtros.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }

        lblTotalMostrados.setText(String.valueOf(tablaMatriculas.getRowCount()));
    }

    private void limpiarFiltros() {
        comboFiltroEstudiante.setSelectedIndex(0);
        comboFiltroPeriodo.setSelectedIndex(0);
        txtBuscarCurso.setText("");
        sorter.setRowFilter(null);
        lblTotalMostrados.setText(String.valueOf(tablaMatriculas.getRowCount()));
    }

    private String obtenerPeriodoMasReciente(List<Matricula> matriculas) {
        if (matriculas == null || matriculas.isEmpty()) return "Sin datos";
        String masReciente = "";
        for (Matricula m : matriculas) {
            if (m.getPeriodo() != null && m.getPeriodo().compareTo(masReciente) > 0) {
                masReciente = m.getPeriodo();
            }
        }
        return masReciente.isEmpty() ? "Sin datos" : masReciente;
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

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JComboBox<String> comboEstudiantes = new JComboBox<>();
        for (Estudiante e : estudiantes) {
            comboEstudiantes.addItem(e.getCodigo() + " - " + e.getNombre());
        }

        JComboBox<String> comboCursos = new JComboBox<>();
        for (Curso c : cursos) {
            comboCursos.addItem(c.getCodigo() + " - " + c.getNombre());
        }

        JTextField txtPeriodo = new JTextField("2026-1", 10);

        panel.add(new JLabel("Estudiante:"));
        panel.add(comboEstudiantes);
        panel.add(new JLabel("Curso:"));
        panel.add(comboCursos);
        panel.add(new JLabel("Periodo:"));
        panel.add(txtPeriodo);

        int resultado = JOptionPane.showConfirmDialog(this, panel,
            "Nueva Matricula", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String seleccionEstudiante = (String) comboEstudiantes.getSelectedItem();
            String seleccionCurso = (String) comboCursos.getSelectedItem();
            String periodo = txtPeriodo.getText().trim();

            if (seleccionEstudiante != null && seleccionCurso != null && !periodo.isEmpty()) {
                String codigoEstudiante = seleccionEstudiante.split(" - ")[0];
                String codigoCurso = seleccionCurso.split(" - ")[0];

                Matricula nueva = new Matricula(
                    "MAT-" + (System.currentTimeMillis() % 10000),
                    codigoEstudiante,
                    codigoCurso,
                    java.time.LocalDate.now().toString()
                );
                nueva.setPeriodo(periodo);

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

        List<Matricula> matriculas = GestorDatos.getMatriculas();
        Matricula mat = null;
        if (matriculas != null) {
            for (Matricula m : matriculas) {
                if (m.getCodigoMatricula().equals(codigoMatricula)) {
                    mat = m;
                    break;
                }
            }
        }

        if (mat == null) return;

        String detalle = String.format(
            "<html><b>Matricula:</b> %s<br>" +
            "<b>Estudiante:</b> %s<br>" +
            "<b>Carrera:</b> %s<br>" +
            "<b>Curso:</b> %s<br>" +
            "<b>Profesor:</b> %s<br>" +
            "<b>Horario:</b> %s<br>" +
            "<b>Periodo:</b> %s<br>" +
            "<b>Fecha:</b> %s</html>",
            mat.getCodigoMatricula(),
            mat.getNombreEstudianteDisplay(),
            mat.getCarreraDisplay(),
            mat.getNombreCursoDisplay(),
            mat.getNombreProfesorDisplay(),
            mat.getHorarioDisplay(),
            mat.getPeriodo(),
            mat.getFechaMatricula()
        );

        JOptionPane.showMessageDialog(this, detalle,
            "Detalle de Matricula", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarMatricula() {
        int fila = tablaMatriculas.getSelectedRow();
        if (fila == -1) return;

        String codigoMatricula = (String) modeloTabla.getValueAt(fila, 0);
        String nombreEstudiante = (String) modeloTabla.getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "Desea eliminar la matricula " + codigoMatricula + "\nEstudiante: " + nombreEstudiante + "?",
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
