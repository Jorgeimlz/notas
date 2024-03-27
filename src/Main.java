package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
// Clase para representar una nota

class Nota {
    private String titulo;
    private String contenido;
    private List<String> etiquetas;

    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.etiquetas = new ArrayList<>();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    // Método para añadir una etiqueta
    public void addEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
    }
}


// Clase principal de la aplicación que implementa la interfaz gráfica
public class Main {
    private static ArrayList<Nota> notas = new ArrayList<>();
    private static JFrame frame = new JFrame("Aplicación de Notas");
    private static DefaultListModel<String> listaModelo = new DefaultListModel<>();
    private static JList<String> listaNotas = new JList<>(listaModelo);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> crearYMostrarGUI());
    }

    private static void crearYMostrarGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Panel superior con botones
        JPanel panelSuperior = new JPanel();
        JButton botonAgregar = new JButton("Agregar nota");
        JButton botonVer = new JButton("Ver notas");
        JButton botonEditar = new JButton("Editar nota");
        JButton botonEliminar = new JButton("Eliminar nota");
        panelSuperior.add(botonAgregar);
        panelSuperior.add(botonVer);
        panelSuperior.add(botonEditar);
        panelSuperior.add(botonEliminar);
        frame.add(panelSuperior, BorderLayout.NORTH);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        JTextField campoBusqueda = new JTextField();
        JButton botonBuscar = new JButton("Buscar");
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(botonBuscar, BorderLayout.EAST);
        frame.add(panelBusqueda, BorderLayout.SOUTH);

        // Lista de notas
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaNotas);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Funcionalidad de botones y campo de búsqueda
        botonAgregar.addActionListener(e -> agregarNota());
        botonVer.addActionListener(e -> verNotas());
        botonEditar.addActionListener(e -> editarNota());
        botonEliminar.addActionListener(e -> eliminarNota());
        botonBuscar.addActionListener(e -> buscarNotas(campoBusqueda.getText()));
        campoBusqueda.addActionListener(e -> buscarNotas(campoBusqueda.getText()));
        listaNotas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    verContenidoNota();
                }
            }
        });
        

        frame.setVisible(true);
    }
    private static void agregarNota() {
        JTextField tituloCampo = new JTextField();
        JTextArea contenidoArea = new JTextArea(5, 20);
        JTextField etiquetasCampo = new JTextField(); // Campo para ingresar etiquetas
        Object[] mensaje = {
            "Título:", tituloCampo,
            "Contenido:", contenidoArea,
            "Etiquetas (separadas por comas):", etiquetasCampo // Añade el campo de etiquetas al diálogo
        };
        int opcion = JOptionPane.showConfirmDialog(frame, mensaje, "Agregar nueva nota", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            Nota nota = new Nota(tituloCampo.getText(), contenidoArea.getText());
            // Procesa las etiquetas ingresadas
            String[] etiquetasArray = etiquetasCampo.getText().split(",");
            for (String etiqueta : etiquetasArray) {
                nota.addEtiqueta(etiqueta.trim()); // Añade cada etiqueta a la nota, asegurándote de quitar espacios en blanco
            }
            notas.add(nota);
            listaModelo.addElement(nota.getTitulo());
        }
    }
    
    

    private static void verNotas() {
        listaModelo.clear();
        for (Nota nota : notas) {
            listaModelo.addElement(nota.getTitulo());
        }
    }

    private static void editarNota() {
        int indice = listaNotas.getSelectedIndex();
        if (indice >= 0 && indice < notas.size()) {
            Nota nota = notas.get(indice);
            JTextField tituloCampo = new JTextField(nota.getTitulo());
            JTextArea contenidoArea = new JTextArea(nota.getContenido(), 5, 20);
            JTextField etiquetasCampo = new JTextField(String.join(", ", nota.getEtiquetas())); // Prellena el campo de etiquetas
            Object[] mensaje = {
                "Título:", tituloCampo,
                "Contenido:", contenidoArea,
                "Etiquetas (separadas por comas):", etiquetasCampo // Incluye el campo de etiquetas en el diálogo
            };
            int opcion = JOptionPane.showConfirmDialog(frame, mensaje, "Editar nota", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                nota.setContenido(contenidoArea.getText());
                String[] etiquetasArray = etiquetasCampo.getText().split(",");
                List<String> etiquetas = new ArrayList<>();
                for (String etiqueta : etiquetasArray) {
                    etiquetas.add(etiqueta.trim()); // Añade las etiquetas después de quitar espacios en blanco
                }
                nota.setEtiquetas(etiquetas);
                listaModelo.set(indice, tituloCampo.getText()); // Actualiza el título en la lista
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione una nota para editar.");
        }
    }
    

    private static void eliminarNota() {
        int indice = listaNotas.getSelectedIndex();
        if (indice >= 0 && indice < notas.size()) {
            notas.remove(indice);
            listaModelo.remove(indice);
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione una nota para eliminar.");
        }
    }
    private static void buscarNotas(String texto) {
        listaModelo.clear(); // Limpia la lista de notas actual
        for (Nota nota : notas) {
            // Convierte la lista de etiquetas a una cadena de texto para simplificar la búsqueda
            String etiquetasTexto = String.join(", ", nota.getEtiquetas()).toLowerCase();
            
            // Verifica si el texto de búsqueda coincide con el título, contenido o etiquetas de la nota
            if (nota.getTitulo().toLowerCase().contains(texto.toLowerCase()) ||
                nota.getContenido().toLowerCase().contains(texto.toLowerCase()) ||
                etiquetasTexto.contains(texto.toLowerCase())) {
                listaModelo.addElement(nota.getTitulo()); // Añade las notas que coincidan con la búsqueda
            }
        }
        if (listaModelo.isEmpty()) {
            listaModelo.addElement("No se encontraron notas que coincidan con la búsqueda."); // Mensaje si no hay coincidencias
        }
    }

    private static void verContenidoNota() {
        int indice = listaNotas.getSelectedIndex();
        if (indice >= 0 && indice < notas.size()) {
            Nota notaSeleccionada = notas.get(indice);
            // Usa HTML para formatear el color del texto de las etiquetas
            String etiquetasHTML = "<html><body>"
                    + "Título: " + notaSeleccionada.getTitulo() + "<br>"
                    + "<font color='red'>Etiquetas: " + String.join(", ", notaSeleccionada.getEtiquetas()) + "</font><br><br>"
                    + "Contenido:<br>" + notaSeleccionada.getContenido().replace("\n", "<br>")
                    + "</body></html>";
    
            // Crea y configura el JTextPane o JEditorPane
            JEditorPane editorPane = new JEditorPane("text/html", etiquetasHTML);
            editorPane.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(editorPane);
            scrollPane.setPreferredSize(new Dimension(350, 200));
    
            // Muestra el diálogo
            JOptionPane.showMessageDialog(frame, scrollPane, "Ver Nota", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione una nota para ver su contenido.", "Ninguna Nota Seleccionada", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    
    

}

