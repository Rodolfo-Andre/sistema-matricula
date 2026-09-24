package vista;

import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PanelPortalEstudiante extends JPanel {

    private Usuario usuarioLogueado;

    private CardLayout cardLayoutInterno;
    private JPanel panelContenedor;
    
    private JTable tablaMisCursos;
    private DefaultTableModel modeloMisCursos;
    private JTable tablaDisponibles;
    private DefaultTableModel modeloDisponibles;

    public PanelPortalEstudiante(Usuario usuario) {
        this.usuarioLogueado = usuario;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 248, 220));

        JLabel lblTitulo = new JLabel("MI PORTAL ESTUDIANTIL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(60, 60, 80));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        cardLayoutInterno = new CardLayout();
        panelContenedor = new JPanel(cardLayoutInterno);
        
        panelContenedor.add(crearPanelMisCursos(), "Cursos");
        panelContenedor.add(crearPanelMatriculaEnLinea(), "Matricula");

        add(panelContenedor, BorderLayout.CENTER);
    }

    public void mostrarVista(String nombreVista) {
        cardLayoutInterno.show(panelContenedor, nombreVista);
    }

    private JPanel crearPanelMisCursos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 248, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"Código", "Curso", "Créditos", "Docente", "Día", "Horario", "Aula"};
        modeloMisCursos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaMisCursos = new JTable(modeloMisCursos);
        tablaMisCursos.setRowHeight(25);
        tablaMisCursos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        panel.add(new JScrollPane(tablaMisCursos), BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar Mi Horario");
        JButton btnDescargarPDF = new JButton("Descargar Horario (PDF)");
        
        Font fontBtn = new Font("Segoe UI", Font.BOLD, 14);
        btnActualizar.setFont(fontBtn);
        btnDescargarPDF.setFont(fontBtn);
        btnDescargarPDF.setBackground(new Color(192, 57, 43)); 
        btnDescargarPDF.setForeground(Color.WHITE);

        btnActualizar.addActionListener(e -> cargarMisCursos());
        btnDescargarPDF.addActionListener(e -> generarPDFHorario());
        
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBotones.setBackground(new Color(255, 248, 220));
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnDescargarPDF);
        panel.add(pnlBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelMatriculaEnLinea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 248, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID Sec", "Curso", "Créditos", "Docente", "Día", "Horario", "Aula"};
        modeloDisponibles = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaDisponibles = new JTable(modeloDisponibles);
        tablaDisponibles.setRowHeight(25);
        tablaDisponibles.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaDisponibles.getColumnModel().getColumn(0).setMinWidth(0);
        tablaDisponibles.getColumnModel().getColumn(0).setMaxWidth(0);

        panel.add(new JScrollPane(tablaDisponibles), BorderLayout.CENTER);

        JButton btnCargarDisp = new JButton("Ver Cursos Disponibles");
        JButton btnMatricular = new JButton("¡Matricularme en Curso Seleccionado!");
        
        Font fontBtn = new Font("Segoe UI", Font.BOLD, 14);
        btnCargarDisp.setFont(fontBtn);
        btnMatricular.setFont(fontBtn);
        btnMatricular.setBackground(new Color(46, 204, 113)); 
        btnMatricular.setForeground(Color.WHITE);

        btnCargarDisp.addActionListener(e -> cargarCursosDisponibles());
        btnMatricular.addActionListener(e -> procesarMatricula());

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBotones.setBackground(new Color(255, 248, 220));
        pnlBotones.add(btnCargarDisp);
        pnlBotones.add(btnMatricular);
        panel.add(pnlBotones, BorderLayout.SOUTH);

        return panel;
    }


    public void cargarDatosIniciales() {
        if (usuarioLogueado.getIdEstudiante() == null) {
            JOptionPane.showMessageDialog(this, "Error: Esta cuenta no tiene un ID de Estudiante vinculado.");
            return;
        }
        cargarMisCursos();
    }

    private void cargarMisCursos() {
        modeloMisCursos.setRowCount(0);
    }

    private void cargarCursosDisponibles() {
        modeloDisponibles.setRowCount(0);
    }

    private void procesarMatricula() {
        int fila = tablaDisponibles.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un curso de la tabla para matricularte.");
            return;
        }
        String nombreCurso = modeloDisponibles.getValueAt(fila, 1).toString();
        JOptionPane.showMessageDialog(this, "¡Simulación de Matrícula exitosa en " + nombreCurso + "!");
    }

    private void generarPDFHorario() {
        if (tablaMisCursos.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay cursos en el horario para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String rutaEscritorio = System.getProperty("user.home") + "/Desktop/MiHorario_Matricula.pdf";
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(rutaEscritorio));
            
            documento.open();

            com.itextpdf.text.Font fuenteTitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            Paragraph titulo = new Paragraph("Horario de Clases - Estudiante: " + usuarioLogueado.getUsername(), fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new Paragraph(" ")); 

            PdfPTable tablaPdf = new PdfPTable(tablaMisCursos.getColumnCount());
            tablaPdf.setWidthPercentage(100);

            for (int i = 0; i < tablaMisCursos.getColumnCount(); i++) {
                PdfPCell celda = new PdfPCell(new Phrase(tablaMisCursos.getColumnName(i), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD)));
                celda.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaPdf.addCell(celda);
            }

            for (int fila = 0; fila < tablaMisCursos.getRowCount(); fila++) {
                for (int col = 0; col < tablaMisCursos.getColumnCount(); col++) {
                    Object valor = tablaMisCursos.getValueAt(fila, col);
                    tablaPdf.addCell(valor != null ? valor.toString() : "");
                }
            }
            
            documento.add(tablaPdf);
            documento.close();
            
            JOptionPane.showMessageDialog(this, "¡Horario descargado exitosamente en tu Escritorio!\nArchivo: MiHorario_Matricula.pdf", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}