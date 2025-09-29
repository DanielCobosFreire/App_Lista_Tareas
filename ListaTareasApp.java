import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Aplicación GUI para gestión de lista de tareas
 * Permite añadir, marcar como completadas y eliminar tareas
 * 
 * @author Daniel Cobos Freire
 * @version 1.0
 */
public class ListaTareasApp extends JFrame {
    
    // Modelo de datos para la lista de tareas
    private DefaultListModel<Tarea> modeloTareas;
    private JList<Tarea> listaTareas;
    private JTextField campoTexto;
    private JButton btnAnadir, btnCompletar, btnEliminar;
    
    /**
     * Clase interna que representa una tarea con su estado de completado
     */
    private class Tarea {
        private String texto;
        private boolean completada;
        
        public Tarea(String texto) {
            this.texto = texto;
            this.completada = false;
        }
        
        public String getTexto() { return texto; }
        public boolean isCompletada() { return completada; }
        public void setCompletada(boolean completada) { this.completada = completada; }
        
        @Override
        public String toString() {
            return texto;
        }
    }
    
    /**
     * Renderer personalizado para mostrar las tareas con diferentes estilos
     * según su estado (completada o pendiente)
     */
    private class TareaRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                     int index, boolean isSelected, 
                                                     boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Tarea) {
                Tarea tarea = (Tarea) value;
                
                // Aplicar estilo según el estado de la tarea
                if (tarea.isCompletada()) {
                    // Tarea completada: texto tachado y color gris
                    setFont(getFont().deriveFont(Font.ITALIC));
                    setForeground(Color.GRAY);
                    setText("<html><strike>" + tarea.getTexto() + "</strike></html>");
                } else {
                    // Tarea pendiente: texto normal y color negro
                    setFont(getFont().deriveFont(Font.PLAIN));
                    setForeground(Color.BLACK);
                    setText(tarea.getTexto());
                }
                
                // Resaltar la selección
                if (isSelected) {
                    setBackground(new Color(220, 240, 255));
                }
            }
            
            return this;
        }
    }
    
    /**
     * Constructor - Inicializa la interfaz gráfica y los manejadores de eventos
     */
    public ListaTareasApp() {
        // Configuración de la ventana principal
        setTitle("Lista de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null); // Centrar en pantalla
        
        inicializarComponentes();
        configurarEventos();
        organizarLayout();
    }
    
    /**
     * Inicializa todos los componentes de la interfaz
     */
    private void inicializarComponentes() {
        // Modelo y lista de tareas
        modeloTareas = new DefaultListModel<>();
        listaTareas = new JList<>(modeloTareas);
        listaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTareas.setCellRenderer(new TareaRenderer());
        
        // Campo de texto para nueva tarea
        campoTexto = new JTextField();
        campoTexto.setToolTipText("Escribe una nueva tarea y presiona Enter");
        
        // Botones
        btnAnadir = new JButton("Añadir Tarea");
        btnCompletar = new JButton("Marcar como Completada");
        btnEliminar = new JButton("Eliminar Tarea");
        
        // Configurar tooltips para los botones
        btnAnadir.setToolTipText("Añadir una nueva tarea a la lista");
        btnCompletar.setToolTipText("Marcar la tarea seleccionada como completada");
        btnEliminar.setToolTipText("Eliminar la tarea seleccionada");
    }
    
    /**
     * Configura todos los manejadores de eventos
     */
    private void configurarEventos() {
        // Evento para añadir tarea con el botón
        btnAnadir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirTarea();
            }
        });
        
        // Evento para añadir tarea con la tecla Enter en el campo de texto
        campoTexto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirTarea();
            }
        });
        
        // Evento para marcar como completada con el botón
        btnCompletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarComoCompletada();
            }
        });
        
        // Evento para eliminar tarea con el botón
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTarea();
            }
        });
        
        // Evento de doble clic para marcar como completada
        listaTareas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    marcarComoCompletada();
                }
            }
        });
        
        // Evento de teclado para eliminar con la tecla Delete
        listaTareas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    eliminarTarea();
                }
            }
        });
        
        // Habilitar/deshabilitar botones según la selección
        listaTareas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean haySeleccion = !listaTareas.isSelectionEmpty();
                btnCompletar.setEnabled(haySeleccion);
                btnEliminar.setEnabled(haySeleccion);
            }
        });
        
        // Inicialmente deshabilitar botones que requieren selección
        btnCompletar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
    
    /**
     * Organiza el layout de la interfaz gráfica
     */
    private void organizarLayout() {
        // Panel principal con BorderLayout
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior para entrada de datos
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Nueva Tarea"));
        panelSuperior.add(new JLabel("Tarea:"), BorderLayout.WEST);
        panelSuperior.add(campoTexto, BorderLayout.CENTER);
        panelSuperior.add(btnAnadir, BorderLayout.EAST);
        
        // Panel central para la lista de tareas
        JScrollPane scrollLista = new JScrollPane(listaTareas);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Lista de Tareas"));
        scrollLista.setPreferredSize(new Dimension(400, 250));
        
        // Panel inferior para botones de acción
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnCompletar);
        panelInferior.add(btnEliminar);
        
        // Agregar componentes al frame principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollLista, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
        
        // Agregar padding alrededor del contenido
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    /**
     * Añade una nueva tarea a la lista
     * Valida que el campo no esté vacío
     */
    private void anadirTarea() {
        String texto = campoTexto.getText().trim();
        
        if (!texto.isEmpty()) {
            Tarea nuevaTarea = new Tarea(texto);
            modeloTareas.addElement(nuevaTarea);
            campoTexto.setText(""); // Limpiar campo
            campoTexto.requestFocus(); // Volver al campo de texto
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor, escribe una tarea", 
                "Campo vacío", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Marca la tarea seleccionada como completada
     * Si no hay selección, muestra un mensaje
     */
    private void marcarComoCompletada() {
        int indiceSeleccionado = listaTareas.getSelectedIndex();
        
        if (indiceSeleccionado != -1) {
            Tarea tarea = modeloTareas.getElementAt(indiceSeleccionado);
            tarea.setCompletada(true);
            listaTareas.repaint(); // Forzar redibujado para mostrar cambios visuales
            
            // Opcional: mover al final de la lista
            // modeloTareas.remove(indiceSeleccionado);
            // modeloTareas.addElement(tarea);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecciona una tarea primero", 
                "Sin selección", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Elimina la tarea seleccionada de la lista
     * Pide confirmación antes de eliminar
     */
    private void eliminarTarea() {
        int indiceSeleccionado = listaTareas.getSelectedIndex();
        
        if (indiceSeleccionado != -1) {
            Tarea tarea = modeloTareas.getElementAt(indiceSeleccionado);
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la tarea: '" + tarea.getTexto() + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                modeloTareas.remove(indiceSeleccionado);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecciona una tarea primero", 
                "Sin selección", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Método principal - punto de entrada de la aplicación
     */
    public static void main(String[] args) {
        // Ejecutar en el Event Dispatch Thread para seguridad en Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Establecer el look and feel del sistema
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                new ListaTareasApp().setVisible(true);
            }
        });
    }
}
