package vista;

import controller.CursoController;
import modelo.Curso;
import gestor.ConexionBD;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.List;

public class PanelConvalidacion extends JPanel {

    private static final Color COLOR_FONDO = new Color(255, 248, 220);
    private static final Color COLOR_TITULO = new Color(0, 51, 102);

    private static final Font FONT_TITULO_PRINCIPAL = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_ETIQUETA = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_CAMPO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 13);

 
    private final CursoController cursoController;

    private JTextField txtEstudianteConval;
    private JTextField txtCursoOrigen;
    private JTextField txtCreditosOrigen;
    private JComboBox<CursoItem> cmbCursoDestino;
    private JTextField txtCreditosDestino;
    private JLabel lblResultado;

    private int idEstudianteSeleccionado = -1;
    private JTextField txtEstCodigo;
    private JTextField txtEstDni;
    private JTextField txtEstNombres;
    private JTextField txtEstApellidos;
    private JComboBox<CarreraItem> cmbEstCarrera;
    private JComboBox<Integer> cmbEstCiclo;

    private JTextField txtBuscarEst;
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTablaEstudiantes;

    public PanelConvalidacion() {
        this.cursoController = new CursoController();
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        cargarCursosCombo();
        cargarCarrerasCombo();
        cargarEstudiantesTabla("");
    }

    private void initComponents() {
        // TÍTULO GENERAL
        JLabel lblTitulo = new JLabel("SISTEMA DE CONVALIDACIÓN DE CURSOS", SwingConstants.CENTER);
        lblTitulo.setFont(FONT_TITULO_PRINCIPAL);
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelMitades = new JPanel(new GridLayout(1, 2, 12, 0));
        panelMitades.setOpaque(false);

        panelMitades.add(crearMitadIzquierdaConvalidacion());
        panelMitades.add(crearMitadDerechaEstudiantes());

        add(panelMitades, BorderLayout.CENTER);
    }

  
    private JPanel crearMitadIzquierdaConvalidacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Trámite de Convalidación",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_SUBTITULO, COLOR_TITULO
        ));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtEstudianteConval = new JTextField();
        txtEstudianteConval.setFont(FONT_CAMPO);
        txtEstudianteConval.setPreferredSize(new Dimension(0, 30));
        txtEstudianteConval.setEditable(false);
        txtEstudianteConval.setToolTipText("Seleccione un estudiante de la tabla derecha");

        txtCursoOrigen = new JTextField();
        txtCursoOrigen.setFont(FONT_CAMPO);
        txtCursoOrigen.setPreferredSize(new Dimension(0, 30));

        txtCreditosOrigen = new JTextField();
        txtCreditosOrigen.setFont(FONT_CAMPO);
        txtCreditosOrigen.setPreferredSize(new Dimension(0, 30));

        cmbCursoDestino = new JComboBox<>();
        cmbCursoDestino.setFont(FONT_CAMPO);
        cmbCursoDestino.setPreferredSize(new Dimension(0, 30));
        cmbCursoDestino.addActionListener(e -> seleccionarCursoDestino());

        txtCreditosDestino = new JTextField();
        txtCreditosDestino.setFont(FONT_CAMPO);
        txtCreditosDestino.setPreferredSize(new Dimension(0, 30));
        txtCreditosDestino.setEditable(false);

        int row = 0;
        agregarFilaFormulario(form, gbc, "Estudiante:", txtEstudianteConval, row++);
        agregarFilaFormulario(form, gbc, "Curso de Origen (Externo):", txtCursoOrigen, row++);
        agregarFilaFormulario(form, gbc, "Créditos Curso Origen:", txtCreditosOrigen, row++);
        agregarFilaFormulario(form, gbc, "Curso a Convalidar:", cmbCursoDestino, row++);
        agregarFilaFormulario(form, gbc, "Créditos Curso Destino:", txtCreditosDestino, row++);

        // Panel de Botones de Convalidación
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlBotones.setOpaque(false);

        JButton btnEvaluar = new JButton("EVALUAR CONVALIDACIÓN");
        btnEvaluar.setFont(FONT_BOTON);
        btnEvaluar.setPreferredSize(new Dimension(190, 35));
        btnEvaluar.addActionListener(e -> evaluarConvalidacion());

        JButton btnLimpiarConval = new JButton("Limpiar Trámite");
        btnLimpiarConval.setFont(FONT_BOTON);
        btnLimpiarConval.setPreferredSize(new Dimension(140, 35));
        btnLimpiarConval.addActionListener(e -> limpiarFormularioConvalidacion());

        pnlBotones.add(btnEvaluar);
        pnlBotones.add(btnLimpiarConval);

        gbc.gridx = 0; gbc.gridy = row++;
        gbc.gridwidth = 2;
        form.add(pnlBotones, gbc);

        // Etiqueta de Resultado
        lblResultado = new JLabel("Estado: PENDIENTE DE EVALUACIÓN", SwingConstants.CENTER);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblResultado.setForeground(new Color(90, 90, 100));
        lblResultado.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        gbc.gridy = row++;
        form.add(lblResultado, gbc);

        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

  
    private JPanel crearMitadDerechaEstudiantes() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        JPanel pnlCrudEstudiante = new JPanel(new BorderLayout(5, 5));
        pnlCrudEstudiante.setOpaque(false);
        pnlCrudEstudiante.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Datos / CRUD Estudiante",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_SUBTITULO, COLOR_TITULO
        ));

        JPanel camposEst = new JPanel(new GridLayout(3, 4, 6, 6));
        camposEst.setOpaque(false);
        camposEst.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        txtEstCodigo = new JTextField();
        txtEstDni = new JTextField();
        txtEstNombres = new JTextField();
        txtEstApellidos = new JTextField();
        cmbEstCarrera = new JComboBox<>();
        cmbEstCiclo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        aplicarFuenteCampos(txtEstCodigo, txtEstDni, txtEstNombres, txtEstApellidos, cmbEstCarrera, cmbEstCiclo);

        camposEst.add(crearLabel("Código:"));
        camposEst.add(txtEstCodigo);
        camposEst.add(crearLabel("DNI:"));
        camposEst.add(txtEstDni);

        camposEst.add(crearLabel("Nombres:"));
        camposEst.add(txtEstNombres);
        camposEst.add(crearLabel("Apellidos:"));
        camposEst.add(txtEstApellidos);

        camposEst.add(crearLabel("Carrera:"));
        camposEst.add(cmbEstCarrera);
        camposEst.add(crearLabel("Ciclo:"));
        camposEst.add(cmbEstCiclo);

        pnlCrudEstudiante.add(camposEst, BorderLayout.CENTER);

        // Botones del CRUD
        JPanel pnlBotonesCrud = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        pnlBotonesCrud.setOpaque(false);

        JButton btnGuardarEst = new JButton("Guardar");
        JButton btnActualizarEst = new JButton("Actualizar");
        JButton btnLimpiarEst = new JButton("Limpiar");

        JButton[] botones = {btnGuardarEst, btnActualizarEst, btnLimpiarEst};
        for (JButton b : botones) {
            b.setFont(FONT_BOTON);
            pnlBotonesCrud.add(b);
        }

        btnGuardarEst.addActionListener(e -> guardarEstudiante());
        btnActualizarEst.addActionListener(e -> actualizarEstudiante());
      
        btnLimpiarEst.addActionListener(e -> limpiarFormularioEstudiante());

        pnlCrudEstudiante.add(pnlBotonesCrud, BorderLayout.SOUTH);

        JPanel pnlTablaEstudiante = new JPanel(new BorderLayout(5, 5));
        pnlTablaEstudiante.setOpaque(false);
        pnlTablaEstudiante.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Listado de Estudiantes (Seleccione uno)",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_SUBTITULO, COLOR_TITULO
        ));

        // Barra de búsqueda
        JPanel pnlBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 5));
        pnlBuscar.setOpaque(false);
        txtBuscarEst = new JTextField(12);
        txtBuscarEst.setFont(FONT_CAMPO);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(FONT_BOTON);
        JButton btnTodos = new JButton("Todos");
        btnTodos.setFont(FONT_BOTON);

        btnBuscar.addActionListener(e -> cargarEstudiantesTabla(txtBuscarEst.getText().trim()));
        btnTodos.addActionListener(e -> {
            txtBuscarEst.setText("");
            cargarEstudiantesTabla("");
        });

        pnlBuscar.add(new JLabel("Filtro (DNI/Nombre):"));
        pnlBuscar.add(txtBuscarEst);
        pnlBuscar.add(btnBuscar);
        pnlBuscar.add(btnTodos);

        pnlTablaEstudiante.add(pnlBuscar, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Código", "DNI", "Nombres", "Apellidos", "Carrera", "Ciclo", "id_carrera"};
        modeloTablaEstudiantes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaEstudiantes = new JTable(modeloTablaEstudiantes);
        tablaEstudiantes.setFont(FONT_TABLA_CELDA);
        tablaEstudiantes.setRowHeight(26);
        tablaEstudiantes.getTableHeader().setFont(FONT_TABLA_HEADER);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaEstudiantes.getColumnModel().getColumn(0).setMinWidth(0);
        tablaEstudiantes.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaEstudiantes.getColumnModel().getColumn(0).setWidth(0);
        tablaEstudiantes.getColumnModel().getColumn(7).setMinWidth(0);
        tablaEstudiantes.getColumnModel().getColumn(7).setMaxWidth(0);
        tablaEstudiantes.getColumnModel().getColumn(7).setWidth(0);

        tablaEstudiantes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarEstudianteTabla();
            }
        });

        pnlTablaEstudiante.add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        JSplitPane splitDerecho = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlCrudEstudiante, pnlTablaEstudiante);
        splitDerecho.setDividerLocation(180);
        splitDerecho.setOpaque(false);
        splitDerecho.setBorder(null);

        panel.add(splitDerecho, BorderLayout.CENTER);
        return panel;
    }

    public void cargarCursosCombo() {
        cmbCursoDestino.removeAllItems();
        cmbCursoDestino.addItem(new CursoItem(0, "-- Seleccione un curso --", 0));

        List<Curso> listaCursos = cursoController.listarCursos();
        if (listaCursos != null) {
            for (Curso c : listaCursos) {
                cmbCursoDestino.addItem(new CursoItem(c.getIdCurso(), c.getNombre(), c.getCreditos()));
            }
        }
    }

    private void seleccionarCursoDestino() {
        CursoItem item = (CursoItem) cmbCursoDestino.getSelectedItem();
        if (item != null && item.getIdCurso() > 0) {
            txtCreditosDestino.setText(String.valueOf(item.getCreditos()));
        } else {
            txtCreditosDestino.setText("");
        }
    }

    private void evaluarConvalidacion() {
        if (txtEstudianteConval.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante de la lista antes de evaluar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CursoItem cursoSel = (CursoItem) cmbCursoDestino.getSelectedItem();
        if (cursoSel == null || cursoSel.getIdCurso() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un curso a convalidar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int creditosOrigen = Integer.parseInt(txtCreditosOrigen.getText().trim());
            int creditosDestino = Integer.parseInt(txtCreditosDestino.getText().trim());

            if (creditosOrigen <= 0) {
                JOptionPane.showMessageDialog(this, "Los créditos deben ser mayores a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (creditosOrigen >= creditosDestino) {
                lblResultado.setText("Estado: APROBADA (Cumple con los créditos mínimos)");
                lblResultado.setForeground(new Color(0, 130, 50));
            } else {
                lblResultado.setText("Estado: RECHAZADA (Créditos insuficientes: " + creditosOrigen + " < " + creditosDestino + ")");
                lblResultado.setForeground(new Color(180, 20, 20));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido en los créditos del curso de origen.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormularioConvalidacion() {
        txtEstudianteConval.setText("");
        txtCursoOrigen.setText("");
        txtCreditosOrigen.setText("");
        if (cmbCursoDestino.getItemCount() > 0) {
            cmbCursoDestino.setSelectedIndex(0);
        }
        txtCreditosDestino.setText("");
        lblResultado.setText("Estado: PENDIENTE DE EVALUACIÓN");
        lblResultado.setForeground(new Color(90, 90, 100));
        tablaEstudiantes.clearSelection();
    }

 
    private void cargarCarrerasCombo() {
        cmbEstCarrera.removeAllItems();
        cmbEstCarrera.addItem(new CarreraItem(0, "-- Seleccione carrera --"));

        String sql = "{CALL sp_ListarCarreras()}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                cmbEstCarrera.addItem(new CarreraItem(rs.getInt("id_carrera"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar carreras: " + e.getMessage());
        }
    }

    private void cargarEstudiantesTabla(String filtro) {
        modeloTablaEstudiantes.setRowCount(0);
        String sql = filtro.isEmpty() ? "{CALL sp_ListarEstudiantes()}" : "{CALL sp_BuscarEstudiantesPorFiltro(?)}";

        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            if (!filtro.isEmpty()) {
                cstmt.setString(1, filtro);
            }

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    modeloTablaEstudiantes.addRow(new Object[]{
                        rs.getInt("id_estudiante"),
                        rs.getString("codigo"),
                        rs.getString("dni"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("carrera"),
                        rs.getInt("ciclo"),
                        rs.getInt("id_carrera")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar estudiantes: " + e.getMessage());
        }
    }

    private void seleccionarEstudianteTabla() {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila != -1) {
            idEstudianteSeleccionado = Integer.parseInt(tablaEstudiantes.getValueAt(fila, 0).toString());
            String codigo = tablaEstudiantes.getValueAt(fila, 1).toString();
            String dni = tablaEstudiantes.getValueAt(fila, 2).toString();
            String nombres = tablaEstudiantes.getValueAt(fila, 3).toString();
            String apellidos = tablaEstudiantes.getValueAt(fila, 4).toString();
            int ciclo = Integer.parseInt(tablaEstudiantes.getValueAt(fila, 6).toString());
            int idCarrera = Integer.parseInt(tablaEstudiantes.getValueAt(fila, 7).toString());

            txtEstCodigo.setText(codigo);
            txtEstDni.setText(dni);
            txtEstNombres.setText(nombres);
            txtEstApellidos.setText(apellidos);
            cmbEstCiclo.setSelectedItem(ciclo);

            for (int i = 0; i < cmbEstCarrera.getItemCount(); i++) {
                if (cmbEstCarrera.getItemAt(i).getIdCarrera() == idCarrera) {
                    cmbEstCarrera.setSelectedIndex(i);
                    break;
                }
            }

            txtEstudianteConval.setText(nombres + " " + apellidos);
        }
    }

    private void guardarEstudiante() {
        String cod = txtEstCodigo.getText().trim();
        String dni = txtEstDni.getText().trim();
        String nom = txtEstNombres.getText().trim();
        String ape = txtEstApellidos.getText().trim();
        CarreraItem carrera = (CarreraItem) cmbEstCarrera.getSelectedItem();
        int ciclo = (Integer) cmbEstCiclo.getSelectedItem();

        if (cod.isEmpty() || dni.isEmpty() || nom.isEmpty() || ape.isEmpty() || carrera == null || carrera.getIdCarrera() <= 0) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos y seleccione una carrera válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "{CALL sp_InsertarEstudiante(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, cod);
            cstmt.setString(2, dni);
            cstmt.setString(3, nom);
            cstmt.setString(4, ape);
            cstmt.setInt(5, carrera.getIdCarrera());
            cstmt.setInt(6, ciclo);

            if (cstmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Estudiante guardado exitosamente.");
                cargarEstudiantesTabla("");
                limpiarFormularioEstudiante();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar estudiante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstudiante() {
        if (idEstudianteSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante de la tabla para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cod = txtEstCodigo.getText().trim();
        String dni = txtEstDni.getText().trim();
        String nom = txtEstNombres.getText().trim();
        String ape = txtEstApellidos.getText().trim();
        CarreraItem carrera = (CarreraItem) cmbEstCarrera.getSelectedItem();
        int ciclo = (Integer) cmbEstCiclo.getSelectedItem();

        if (carrera == null || carrera.getIdCarrera() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una carrera válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "{CALL sp_ActualizarEstudiante(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionBD.conectar();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idEstudianteSeleccionado);
            cstmt.setString(2, cod);
            cstmt.setString(3, dni);
            cstmt.setString(4, nom);
            cstmt.setString(5, ape);
            cstmt.setInt(6, carrera.getIdCarrera());
            cstmt.setInt(7, ciclo);

            if (cstmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Estudiante actualizado exitosamente.");
                cargarEstudiantesTabla("");
                limpiarFormularioEstudiante();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar estudiante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormularioEstudiante() {
        idEstudianteSeleccionado = -1;
        txtEstCodigo.setText("");
        txtEstDni.setText("");
        txtEstNombres.setText("");
        txtEstApellidos.setText("");
        if (cmbEstCarrera.getItemCount() > 0) {
            cmbEstCarrera.setSelectedIndex(0); 
        }
        cmbEstCiclo.setSelectedIndex(0);
        tablaEstudiantes.clearSelection();
    }

 
    private void agregarFilaFormulario(JPanel panel, GridBagConstraints gbc, String label, JComponent comp, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_ETIQUETA);
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(comp, gbc);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private void aplicarFuenteCampos(JComponent... componentes) {
        for (JComponent c : componentes) {
            c.setFont(FONT_CAMPO);
        }
    }

    private static class CursoItem {
        private final int idCurso;
        private final String nombre;
        private final int creditos;

        public CursoItem(int idCurso, String nombre, int creditos) {
            this.idCurso = idCurso;
            this.nombre = nombre;
            this.creditos = creditos;
        }

        public int getIdCurso() { return idCurso; }
        public int getCreditos() { return creditos; }

        @Override
        public String toString() {
            return nombre; 
        }
    }

    private static class CarreraItem {
        private final int idCarrera;
        private final String nombre;

        public CarreraItem(int idCarrera, String nombre) {
            this.idCarrera = idCarrera;
            this.nombre = nombre;
        }

        public int getIdCarrera() { return idCarrera; }

        @Override
        public String toString() { return nombre; }
    }
}