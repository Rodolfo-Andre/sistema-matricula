package vista;

import modelo.Curso;
import modelo.Estudiante;
import gestor.GestorDatos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelReportes extends JPanel {

    private JPanel panelPrincipal;
    private JTabbedPane tabbedPane;

    // Componentes para Reporte de Estudiantes
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTablaEstudiantes;
    private TableRowSorter<DefaultTableModel> sorterEstudiantes;
    private JComboBox<String> filtroCarrera;
    private JComboBox<Integer> filtroCiclo;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnLimpiarFiltros;
    private JLabel lblTotalMostrados;

    // Componentes para Reporte de Cursos
    private JTable tablaCursos;
    private DefaultTableModel modeloTablaCursos;

    // Componentes para Estadísticas
    private JLabel lblTotalEstudiantes;
    private JLabel lblTotalCursos;
    private JLabel lblPromedioCiclo;
    private JTextArea txtEstadisticas;

    public PanelReportes() {
        setLayout(new BorderLayout());
        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(240, 248, 255));

        // Título
        JLabel titulo = new JLabel("PANEL DE REPORTES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 51, 102));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // Pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("Estudiantes", crearPanelEstudiantes());
        tabbedPane.addTab("Cursos", crearPanelCursos());
        tabbedPane.addTab("Estadísticas", crearPanelEstadisticas());
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel crearPanelEstudiantes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        panelFiltros.add(new JLabel("Carrera:"));
        filtroCarrera = new JComboBox<>();
        filtroCarrera.addItem("Todas");
        filtroCarrera.setPreferredSize(new Dimension(150, 25));
        filtroCarrera.addActionListener(e -> filtrarEstudiantes());
        panelFiltros.add(filtroCarrera);

        panelFiltros.add(new JLabel("Ciclo:"));
        filtroCiclo = new JComboBox<>();
        filtroCiclo.addItem(0);
        for (int i = 1; i <= 10; i++) {
            filtroCiclo.addItem(i);
        }
        filtroCiclo.setPreferredSize(new Dimension(60, 25));
        filtroCiclo.addActionListener(e -> filtrarEstudiantes());
        panelFiltros.add(filtroCiclo);

        panelFiltros.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        txtBuscar.addActionListener(e -> filtrarEstudiantes());
        panelFiltros.add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> filtrarEstudiantes());
        panelFiltros.add(btnBuscar);

        btnLimpiarFiltros = new JButton("Limpiar");
        btnLimpiarFiltros.addActionListener(e -> limpiarFiltros());
        panelFiltros.add(btnLimpiarFiltros);

        panel.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = {"Código", "Nombre", "Carrera", "Ciclo"};
        modeloTablaEstudiantes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTablaEstudiantes);
        tablaEstudiantes.setRowHeight(25);
        tablaEstudiantes.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaEstudiantes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        sorterEstudiantes = new TableRowSorter<>(modeloTablaEstudiantes);
        tablaEstudiantes.setRowSorter(sorterEstudiantes);

        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Estudiantes"));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.add(new JLabel("Total de estudiantes mostrados: "));
        lblTotalMostrados = new JLabel("0");
        lblTotalMostrados.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalMostrados.setForeground(new Color(70, 130, 180));
        panelInfo.add(lblTotalMostrados);
        panel.add(panelInfo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"Código", "Nombre", "Créditos"};
        modeloTablaCursos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCursos = new JTable(modeloTablaCursos);
        tablaCursos.setRowHeight(25);
        tablaCursos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaCursos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaCursos.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Cursos"));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 20, 20));
        panelTarjetas.setBackground(Color.WHITE);

        JPanel card1 = crearTarjeta("Total Estudiantes", "0", new Color(70, 130, 180));
        panelTarjetas.add(card1);
        lblTotalEstudiantes = (JLabel) ((JPanel) card1.getComponent(1)).getComponent(0);

        JPanel card2 = crearTarjeta("Total Cursos", "0", new Color(60, 179, 113));
        panelTarjetas.add(card2);
        lblTotalCursos = (JLabel) ((JPanel) card2.getComponent(1)).getComponent(0);

        JPanel card3 = crearTarjeta("Promedio Ciclo", "0.0", new Color(255, 140, 0));
        panelTarjetas.add(card3);
        lblPromedioCiclo = (JLabel) ((JPanel) card3.getComponent(1)).getComponent(0);

        panel.add(panelTarjetas, BorderLayout.NORTH);

        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Estadísticas Detalladas"));

        txtEstadisticas = new JTextArea();
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtEstadisticas.setBackground(new Color(248, 248, 255));

        JScrollPane scrollEstadisticas = new JScrollPane(txtEstadisticas);
        panelDetalles.add(scrollEstadisticas, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar Estadísticas");
        btnActualizar.addActionListener(e -> actualizarEstadisticas());
        panelDetalles.add(btnActualizar, BorderLayout.SOUTH);

        panel.add(panelDetalles, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjeta(String titulo, String valor, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(color);
        card.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelValor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelValor.setBackground(Color.WHITE);
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 28));
        lblValor.setForeground(color);
        panelValor.add(lblValor);
        card.add(panelValor, BorderLayout.CENTER);

        return card;
    }

    public void cargarDatosIniciales() {
        cargarEstudiantes();
        cargarCursos();
        cargarFiltrosCarrera();
        actualizarEstadisticas();
    }

    private void cargarEstudiantes() {
        modeloTablaEstudiantes.setRowCount(0);
        List<Estudiante> estudiantes = GestorDatos.getEstudiantes();

        if (estudiantes != null) {
            for (Estudiante e : estudiantes) {
                modeloTablaEstudiantes.addRow(new Object[]{
                    e.getCodigo(), e.getNombre(), e.getCarrera(), e.getCiclo()
                });
            }
        }

        lblTotalMostrados.setText(String.valueOf(tablaEstudiantes.getRowCount()));
    }

    private void cargarCursos() {
        modeloTablaCursos.setRowCount(0);
        List<Curso> cursos = GestorDatos.getCursos();

        if (cursos != null) {
            for (Curso c : cursos) {
                modeloTablaCursos.addRow(new Object[]{
                    c.getCodigo(), c.getNombre(), c.getCreditos()
                });
            }
        }
    }

    private void cargarFiltrosCarrera() {
        filtroCarrera.removeAllItems();
        filtroCarrera.addItem("Todas");

        List<String> carreras = GestorDatos.getCarreras();
        if (carreras != null) {
            for (String carrera : carreras) {
                filtroCarrera.addItem(carrera);
            }
        }
    }

    private void filtrarEstudiantes() {
        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        String carrera = (String) filtroCarrera.getSelectedItem();
        if (carrera != null && !carrera.equals("Todas")) {
            filtros.add(RowFilter.regexFilter("^" + carrera + "$", 2));
        }

        Integer ciclo = (Integer) filtroCiclo.getSelectedItem();
        if (ciclo != null && ciclo > 0) {
            filtros.add(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, ciclo, 3));
        }

        String busqueda = txtBuscar.getText().trim();
        if (!busqueda.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1));
        }

        if (filtros.isEmpty()) {
            sorterEstudiantes.setRowFilter(null);
        } else {
            sorterEstudiantes.setRowFilter(RowFilter.andFilter(filtros));
        }

        lblTotalMostrados.setText(String.valueOf(tablaEstudiantes.getRowCount()));
    }

    private void limpiarFiltros() {
        filtroCarrera.setSelectedIndex(0);
        filtroCiclo.setSelectedIndex(0);
        txtBuscar.setText("");
        sorterEstudiantes.setRowFilter(null);
        lblTotalMostrados.setText(String.valueOf(tablaEstudiantes.getRowCount()));
    }

    private void actualizarEstadisticas() {
        List<Estudiante> estudiantes = GestorDatos.getEstudiantes();
        List<Curso> cursos = GestorDatos.getCursos();

        if (estudiantes == null) {
            estudiantes = new ArrayList<>();
        }
        if (cursos == null) {
            cursos = new ArrayList<>();
        }

        lblTotalEstudiantes.setText(String.valueOf(estudiantes.size()));
        lblTotalCursos.setText(String.valueOf(cursos.size()));

        int sumaCiclos = 0;
        for (Estudiante e : estudiantes) {
            sumaCiclos += e.getCiclo();
        }

        double promedio = estudiantes.isEmpty() ? 0 : (double) sumaCiclos / estudiantes.size();
        lblPromedioCiclo.setText(String.format("%.1f", promedio));

        StringBuilder sb = new StringBuilder();
        sb.append("ESTADISTICAS DETALLADAS\n");
        sb.append("========================\n\n");

        sb.append("Distribución por Carrera:\n");
        List<String> carreras = GestorDatos.getCarreras();
        if (carreras != null) {
            for (String carrera : carreras) {
                long contador = 0;
                for (Estudiante e : estudiantes) {
                    if (carrera.equals(e.getCarrera())) {
                        contador++;
                    }
                }
                sb.append("  - ").append(carrera).append(": ").append(contador).append(" estudiantes\n");
            }
        }

        sb.append("\nDistribución por Ciclo:\n");
        for (int ciclo = 1; ciclo <= 10; ciclo++) {
            int currentCiclo = ciclo;
            long contador = 0;
            for (Estudiante e : estudiantes) {
                if (e.getCiclo() == currentCiclo) {
                    contador++;
                }
            }
            if (contador > 0) {
                sb.append("  - Ciclo ").append(ciclo).append(": ").append(contador).append(" estudiantes\n");
            }
        }

        sb.append("\nEstadísticas de Cursos:\n");
        sb.append("  - Total de cursos: ").append(cursos.size()).append("\n");

        int totalCreditos = 0;
        for (Curso c : cursos) {
            totalCreditos += c.getCreditos();
        }
        sb.append("  - Total de créditos: ").append(totalCreditos).append("\n");

        if (!cursos.isEmpty()) {
            double promedioCreditos = (double) totalCreditos / cursos.size();
            sb.append("  - Promedio de créditos por curso: ").append(String.format("%.1f", promedioCreditos));
        }

        txtEstadisticas.setText(sb.toString());
    }
}
