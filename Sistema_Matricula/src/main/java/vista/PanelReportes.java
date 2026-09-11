package vista;

import modelo.Curso;
import modelo.Estudiante;
import gestor.GestorDatos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelReportes extends JPanel {

    private JTabbedPane tabbedPane;
    private JTable tablaEstudiantes, tablaCursos;
    private DefaultTableModel modeloTablaEstudiantes, modeloTablaCursos;
    private TableRowSorter<DefaultTableModel> sorterEstudiantes;
    
    private JComboBox<String> filtroCarrera;
    private JComboBox<Integer> filtroCiclo;
    private JTextField txtBuscar;
    private JLabel lblTotalMostrados, lblTotalEstudiantes, lblTotalCursos, lblPromedioCiclo;
   
    private CardLayout cardLayoutTablas;
    private JPanel panelTablasEstadisticas;
    private JTable tablaEstCarrera, tablaEstCiclo, tablaEstCursos;
    private DefaultTableModel modeloEstCarrera, modeloEstCiclo, modeloEstCursos;
    private JButton btnVerCarrera, btnVerCiclo, btnVerCursos;

    private JPanel panelGraficos;

    private final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    private final Font FONT_SUBTITULO = new Font("Arial", Font.BOLD, 15);
    private final Font FONT_ETIQUETA = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_COMPONENTE = new Font("Arial", Font.PLAIN, 14);
    private final Font FONT_BOTON = new Font("Arial", Font.BOLD, 13);
    private final Font FONT_TAB = new Font("Arial", Font.BOLD, 15);

    public PanelReportes() {
        setLayout(new BorderLayout());
        initComponents();
        cargarDatosIniciales();
        
        addAncestorListener(new AncestorListener() {
            @Override public void ancestorAdded(AncestorEvent e) { cargarDatosIniciales(); }
            @Override public void ancestorRemoved(AncestorEvent e) {}
            @Override public void ancestorMoved(AncestorEvent e) {}
        });
    }

    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("PANEL DE REPORTES Y ESTADÍSTICAS", SwingConstants.CENTER);
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(new Color(0, 51, 102));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_TAB);
        tabbedPane.addTab("Estudiantes", crearPanelEstudiantes());
        tabbedPane.addTab("Cursos", crearPanelCursos());
        tabbedPane.addTab("Estadísticas", crearPanelEstadisticas());

        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel crearPanelEstudiantes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panelFiltros.setBackground(Color.WHITE);
        
        TitledBorder bFiltros = BorderFactory.createTitledBorder("Filtros de Búsqueda");
        bFiltros.setTitleFont(FONT_SUBTITULO);
        panelFiltros.setBorder(bFiltros);

        filtroCarrera = new JComboBox<>();
        filtroCarrera.setFont(FONT_COMPONENTE);
        filtroCarrera.setPreferredSize(new Dimension(220, 32));
        filtroCarrera.addActionListener(e -> filtrarEstudiantes());

        filtroCiclo = new JComboBox<>();
        filtroCiclo.setFont(FONT_COMPONENTE);
        filtroCiclo.addItem(0);
        for (int i = 1; i <= 10; i++) filtroCiclo.addItem(i);
        filtroCiclo.setPreferredSize(new Dimension(80, 32));
        filtroCiclo.addActionListener(e -> filtrarEstudiantes());

        txtBuscar = new JTextField(15);
        txtBuscar.setFont(FONT_COMPONENTE);
        txtBuscar.setPreferredSize(new Dimension(180, 32));
        txtBuscar.addActionListener(e -> filtrarEstudiantes());

        JButton btnBuscar = crearBoton("Buscar");
        btnBuscar.addActionListener(e -> filtrarEstudiantes());

        JButton btnLimpiar = crearBoton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFiltros());

        JLabel lblCar = new JLabel("Carrera:"); lblCar.setFont(FONT_ETIQUETA);
        JLabel lblCic = new JLabel("Ciclo:"); lblCic.setFont(FONT_ETIQUETA);
        JLabel lblBus = new JLabel("Buscar:"); lblBus.setFont(FONT_ETIQUETA);

        panelFiltros.add(lblCar); panelFiltros.add(filtroCarrera);
        panelFiltros.add(lblCic); panelFiltros.add(filtroCiclo);
        panelFiltros.add(lblBus); panelFiltros.add(txtBuscar);
        panelFiltros.add(btnBuscar);
        panelFiltros.add(btnLimpiar);

        panel.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = {"Código", "Nombres y Apellidos", "Carrera", "Ciclo"};
        modeloTablaEstudiantes = crearModeloNoEditable(columnas);
        
        tablaEstudiantes = new JTable(modeloTablaEstudiantes);
        configurarEstiloTabla(tablaEstudiantes);

        sorterEstudiantes = new TableRowSorter<>(modeloTablaEstudiantes);
        tablaEstudiantes.setRowSorter(sorterEstudiantes);

        JScrollPane scroll = new JScrollPane(tablaEstudiantes);
        TitledBorder bLista = BorderFactory.createTitledBorder("Lista de Estudiantes Registrados");
        bLista.setTitleFont(FONT_SUBTITULO);
        scroll.setBorder(bLista);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(Color.WHITE);
        lblTotalMostrados = new JLabel("0");
        lblTotalMostrados.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalMostrados.setForeground(new Color(70, 130, 180));
        
        JLabel lblTot = new JLabel("Total de estudiantes mostrados: ");
        lblTot.setFont(FONT_ETIQUETA);
        panelInfo.add(lblTot);
        panelInfo.add(lblTotalMostrados);
        
        panel.add(panelInfo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] columnas = {"Código", "Nombre del Curso", "Créditos"};
        modeloTablaCursos = crearModeloNoEditable(columnas);

        tablaCursos = new JTable(modeloTablaCursos);
        configurarEstiloTabla(tablaCursos);
        tablaCursos.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(tablaCursos);
        TitledBorder border = BorderFactory.createTitledBorder("Lista de Cursos Disponibles");
        border.setTitleFont(FONT_SUBTITULO);
        scroll.setBorder(border);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 20, 20));
        panelTarjetas.setBackground(Color.WHITE);

        JPanel card1 = crearTarjeta("Total Estudiantes", "0", new Color(70, 130, 180));
        JPanel card2 = crearTarjeta("Total Cursos", "0", new Color(60, 179, 113));
        JPanel card3 = crearTarjeta("Promedio Ciclo", "0.0", new Color(255, 140, 0));

        lblTotalEstudiantes = (JLabel) ((JPanel) card1.getComponent(1)).getComponent(0);
        lblTotalCursos = (JLabel) ((JPanel) card2.getComponent(1)).getComponent(0);
        lblPromedioCiclo = (JLabel) ((JPanel) card3.getComponent(1)).getComponent(0);

        panelTarjetas.add(card1); panelTarjetas.add(card2); panelTarjetas.add(card3);
        panel.add(panelTarjetas, BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel(new GridLayout(1, 2, 12, 12));
        panelCuerpo.setBackground(Color.WHITE);

        JPanel panelDetalles = new JPanel(new BorderLayout(0, 8));
        TitledBorder bRes = BorderFactory.createTitledBorder("Resumen Consolidado");
        bRes.setTitleFont(FONT_SUBTITULO);
        panelDetalles.setBorder(bRes);
        panelDetalles.setBackground(Color.WHITE);

        JPanel panelBotonesTablas = new JPanel(new GridLayout(1, 3, 5, 0));
        panelBotonesTablas.setBackground(Color.WHITE);

        btnVerCarrera = new JButton("Por Carrera");
        btnVerCiclo = new JButton("Por Ciclo");
        btnVerCursos = new JButton("Por Cursos");

        for (JButton btn : new JButton[]{btnVerCarrera, btnVerCiclo, btnVerCursos}) {
            btn.setFont(FONT_BOTON);
            btn.setPreferredSize(new Dimension(0, 36));
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        btnVerCarrera.addActionListener(e -> seleccionarBotonTabla("CARRERA"));
        btnVerCiclo.addActionListener(e -> seleccionarBotonTabla("CICLO"));
        btnVerCursos.addActionListener(e -> seleccionarBotonTabla("CURSOS"));

        panelBotonesTablas.add(btnVerCarrera);
        panelBotonesTablas.add(btnVerCiclo);
        panelBotonesTablas.add(btnVerCursos);

        panelDetalles.add(panelBotonesTablas, BorderLayout.NORTH);

        cardLayoutTablas = new CardLayout();
        panelTablasEstadisticas = new JPanel(cardLayoutTablas);
        panelTablasEstadisticas.setBackground(Color.WHITE);

        modeloEstCarrera = crearModeloNoEditable(new String[]{"Carrera", "Cant. Estudiantes", "Porcentaje"});
        tablaEstCarrera = new JTable(modeloEstCarrera);
        configurarEstiloTabla(tablaEstCarrera);

        modeloEstCiclo = crearModeloNoEditable(new String[]{"Ciclo Académico", "Cant. Estudiantes", "Porcentaje"});
        tablaEstCiclo = new JTable(modeloEstCiclo);
        configurarEstiloTabla(tablaEstCiclo);

        modeloEstCursos = crearModeloNoEditable(new String[]{"Métrica / Indicador", "Valor"});
        tablaEstCursos = new JTable(modeloEstCursos);
        configurarEstiloTabla(tablaEstCursos);

        panelTablasEstadisticas.add(new JScrollPane(tablaEstCarrera), "CARRERA");
        panelTablasEstadisticas.add(new JScrollPane(tablaEstCiclo), "CICLO");
        panelTablasEstadisticas.add(new JScrollPane(tablaEstCursos), "CURSOS");

        panelDetalles.add(panelTablasEstadisticas, BorderLayout.CENTER);

        panelGraficos = new JPanel();
        panelGraficos.setLayout(new BoxLayout(panelGraficos, BoxLayout.Y_AXIS));
        panelGraficos.setBackground(Color.WHITE);

        JScrollPane scrollGraficos = new JScrollPane(panelGraficos);
        TitledBorder bDis = BorderFactory.createTitledBorder("Distribución por Carrera (%)");
        bDis.setTitleFont(FONT_SUBTITULO);
        scrollGraficos.setBorder(bDis);

        panelCuerpo.add(panelDetalles);
        panelCuerpo.add(scrollGraficos);
        panel.add(panelCuerpo, BorderLayout.CENTER);

        JButton btnActualizar = crearBoton("Actualizar Estadísticas");
        btnActualizar.setPreferredSize(new Dimension(btnActualizar.getPreferredSize().width, 38));
        btnActualizar.addActionListener(e -> actualizarEstadisticas());
        panel.add(btnActualizar, BorderLayout.SOUTH);

        seleccionarBotonTabla("CARRERA");

        return panel;
    }

    private void seleccionarBotonTabla(String nombreVista) {
        cardLayoutTablas.show(panelTablasEstadisticas, nombreVista);

        Color amarilloActivo = new Color(255, 213, 79); 
        Color grisInactivo = new Color(240, 240, 240);
        Color bordeGris = new Color(200, 200, 200);

        configurarEstadoBoton(btnVerCarrera, nombreVista.equals("CARRERA"), amarilloActivo, grisInactivo, bordeGris);
        configurarEstadoBoton(btnVerCiclo, nombreVista.equals("CICLO"), amarilloActivo, grisInactivo, bordeGris);
        configurarEstadoBoton(btnVerCursos, nombreVista.equals("CURSOS"), amarilloActivo, grisInactivo, bordeGris);
    }

    private void configurarEstadoBoton(JButton btn, boolean activo, Color bgActivo, Color bgInactivo, Color colorBorde) {
        btn.setForeground(Color.BLACK); 
        if (activo) {
            btn.setBackground(bgActivo);
            btn.setBorder(BorderFactory.createLineBorder(new Color(230, 160, 0), 2)); 
        } else {
            btn.setBackground(bgInactivo);
            btn.setBorder(BorderFactory.createLineBorder(colorBorde, 1));
        }
    }

    public void cargarDatosIniciales() {
        cargarFiltrosCarrera();
        cargarEstudiantes();
        cargarCursos();
        actualizarEstadisticas();
    }

    private void cargarEstudiantes() {
        modeloTablaEstudiantes.setRowCount(0);
        List<Estudiante> estudiantes = GestorDatos.getEstudiantes();

        if (estudiantes != null) {
            for (Estudiante e : estudiantes) {
                String carrera = (e.getCarrera() != null && !e.getCarrera().isEmpty()) 
                                ? e.getCarrera() : String.valueOf(e.getIdCarrera());

                modeloTablaEstudiantes.addRow(new Object[]{
                    e.getCodigo(), e.getNombres() + " " + e.getApellidos(), carrera, e.getCiclo()
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
                modeloTablaCursos.addRow(new Object[]{ c.getCodigo(), c.getNombre(), c.getCreditos() });
            }
        }
    }

    private void cargarFiltrosCarrera() {
        filtroCarrera.removeAllItems();
        filtroCarrera.addItem("Todas");
        List<String> carreras = GestorDatos.getCarreras();
        if (carreras != null) {
            for (String carrera : carreras) filtroCarrera.addItem(carrera);
        }
    }

    private void filtrarEstudiantes() {
        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        String carrera = (String) filtroCarrera.getSelectedItem();
        if (carrera != null && !carrera.equals("Todas")) {
            filtros.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(carrera) + "$", 2));
        }

        Integer ciclo = (Integer) filtroCiclo.getSelectedItem();
        if (ciclo != null && ciclo > 0) {
            filtros.add(RowFilter.regexFilter("^" + ciclo + "$", 3));
        }

        String busqueda = txtBuscar.getText().trim();
        if (!busqueda.isEmpty()) {
            String regex = "(?i)" + java.util.regex.Pattern.quote(busqueda);
            List<RowFilter<Object, Object>> subFiltros = new ArrayList<>();
            subFiltros.add(RowFilter.regexFilter(regex, 0));
            subFiltros.add(RowFilter.regexFilter(regex, 1));
            filtros.add(RowFilter.orFilter(subFiltros));
        }

        sorterEstudiantes.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
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
        List<Estudiante> estudiantes = GestorDatos.getEstudiantes() != null ? GestorDatos.getEstudiantes() : new ArrayList<>();
        List<Curso> cursos = GestorDatos.getCursos() != null ? GestorDatos.getCursos() : new ArrayList<>();

        int totalAlumnos = estudiantes.size();
        lblTotalEstudiantes.setText(String.valueOf(totalAlumnos));
        lblTotalCursos.setText(String.valueOf(cursos.size()));

        int sumaCiclos = estudiantes.stream().mapToInt(Estudiante::getCiclo).sum();
        double promedio = estudiantes.isEmpty() ? 0 : (double) sumaCiclos / totalAlumnos;
        lblPromedioCiclo.setText(String.format("%.1f", promedio));

        modeloEstCarrera.setRowCount(0);
        modeloEstCiclo.setRowCount(0);
        modeloEstCursos.setRowCount(0);
        panelGraficos.removeAll();

        List<String> carreras = GestorDatos.getCarreras();
        if (carreras != null) {
            for (String carrera : carreras) {
                long contador = estudiantes.stream()
                        .filter(e -> carrera != null && carrera.equalsIgnoreCase(e.getCarrera()))
                        .count();

                double porcentaje = (totalAlumnos > 0) ? ((double) contador * 100 / totalAlumnos) : 0;
                
                modeloEstCarrera.addRow(new Object[]{
                    carrera, 
                    contador + " estudiante(s)", 
                    String.format("%.1f %%", porcentaje)
                });

                panelGraficos.add(crearBarraProgreso(carrera + " (" + contador + ")", (int) porcentaje));
            }
        }

        panelGraficos.revalidate();
        panelGraficos.repaint();

        for (int ciclo = 1; ciclo <= 10; ciclo++) {
            int c = ciclo;
            long cnt = estudiantes.stream().filter(e -> e.getCiclo() == c).count();
            if (cnt > 0) {
                double porcentajeCiclo = (totalAlumnos > 0) ? ((double) cnt * 100 / totalAlumnos) : 0;
                modeloEstCiclo.addRow(new Object[]{
                    "Ciclo " + ciclo, 
                    cnt + " estudiante(s)", 
                    String.format("%.1f %%", porcentajeCiclo)
                });
            }
        }

        int totalCreditos = cursos.stream().mapToInt(Curso::getCreditos).sum();
        double promedioCreditos = cursos.isEmpty() ? 0 : (double) totalCreditos / cursos.size();

        modeloEstCursos.addRow(new Object[]{"Total de Cursos Registrados", cursos.size()});
        modeloEstCursos.addRow(new Object[]{"Suma Total de Créditos", totalCreditos});
        modeloEstCursos.addRow(new Object[]{"Promedio de Créditos por Curso", String.format("%.2f", promedioCreditos)});
    }

    // --- MÉTODOS AUXILIARES ---

    private DefaultTableModel crearModeloNoEditable(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private void configurarEstiloTabla(JTable tabla) {
        tabla.setRowHeight(30);
        tabla.setFont(FONT_COMPONENTE);
        tabla.getTableHeader().setFont(FONT_ETIQUETA);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 32));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        if (tabla.getColumnCount() > 1) {
            for (int i = 1; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOTON);
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 32));
        return btn;
    }

    private JPanel crearTarjeta(String titulo, String valor, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setFont(FONT_ETIQUETA);
        lblTit.setForeground(color);

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(new Font("Arial", Font.BOLD, 32));
        lblVal.setForeground(color);

        JPanel pnlVal = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlVal.setBackground(Color.WHITE);
        pnlVal.add(lblVal);

        card.add(lblTit, BorderLayout.NORTH);
        card.add(pnlVal, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearBarraProgreso(String etiqueta, int porcentaje) {
        JPanel item = new JPanel(new BorderLayout(5, 5));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(FONT_ETIQUETA);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(porcentaje);
        bar.setStringPainted(true);
        bar.setFont(FONT_BOTON);
        bar.setPreferredSize(new Dimension(bar.getPreferredSize().width, 24));
        bar.setForeground(new Color(70, 130, 180));

        item.add(lbl, BorderLayout.NORTH);
        item.add(bar, BorderLayout.CENTER);
        return item;
    }
}