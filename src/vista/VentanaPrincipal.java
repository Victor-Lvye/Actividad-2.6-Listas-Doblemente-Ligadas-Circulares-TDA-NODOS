package vista;

import modelo.Proceso;
import tda.ListaCircular;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Interfaz gráfica principal del TDA Lista Circular.
 *
 * Capas:
 *   - modelo  : record Proceso (inmutable)
 *   - tda     : ListaCircular (lógica pura, sin referencias a Swing)
 *   - vista   : VentanaPrincipal (eventos → llama al TDA → refresca la tabla)
 *
 * La GUI no conoce la implementación interna del TDA; solo invoca su API pública.
 */
public class VentanaPrincipal extends JFrame {

    // ── TDA ─────────────────────────────────────────────────────────────────
    private final ListaCircular lista = new ListaCircular();

    // ── Colores ──────────────────────────────────────────────────────────────
    private static final Color COLOR_FONDO        = new Color(15, 23, 42);
    private static final Color COLOR_PANEL        = new Color(30, 41, 59);
    private static final Color COLOR_ACENTO       = new Color(99, 102, 241);
    private static final Color COLOR_ACENTO2      = new Color(236, 72, 153);
    private static final Color COLOR_TEXTO        = new Color(226, 232, 240);
    private static final Color COLOR_SUBTEXTO     = new Color(148, 163, 184);
    private static final Color COLOR_CAMPO        = new Color(51, 65, 85);
    private static final Color COLOR_BORDE        = new Color(71, 85, 105);
    private static final Color COLOR_FILA_PAR     = new Color(30, 41, 59);
    private static final Color COLOR_FILA_IMPAR   = new Color(44, 55, 74);
    private static final Color COLOR_CURSOR       = new Color(99, 102, 241, 80);
    private static final Color COLOR_VERDE        = new Color(34, 197, 94);
    private static final Color COLOR_ROJO         = new Color(239, 68, 68);
    private static final Color COLOR_AMARILLO     = new Color(234, 179, 8);

    // ── Campos del formulario ────────────────────────────────────────────────
    private JTextField txtId, txtNombre, txtPrioridad, txtTiempo, txtPosicion, txtBuscar;

    // ── Tabla ────────────────────────────────────────────────────────────────
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // ── Panel de cursor ──────────────────────────────────────────────────────
    private JLabel lblCursorDato, lblCursorPos, lblIndicadorCircular;

    // ── Panel de estado ──────────────────────────────────────────────────────
    private JLabel lblEstado;

    // =========================================================
    public VentanaPrincipal() {
        super("TDA Lista Circular — Planificador de Procesos");
        configurarVentana();
        construirUI();
        setVisible(true);
    }

    // =========================================================
    // Configuración de la ventana
    // =========================================================
    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 780);
        setMinimumSize(new Dimension(960, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));
    }

    // =========================================================
    // Construcción de la UI
    // =========================================================
    private void construirUI() {
        add(crearPanelSuperior(),  BorderLayout.NORTH);
        add(crearPanelCentral(),   BorderLayout.CENTER);
        add(crearBarraEstado(),    BorderLayout.SOUTH);
    }

    // ── Panel superior: título ───────────────────────────────────────────────
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("⬡  Lista Circular Doblemente Enlazada");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);

        JLabel subtitulo = new JLabel("Planificador Round-Robin  ·  TDA con Records Java");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setForeground(COLOR_SUBTEXTO);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        panel.add(textos, BorderLayout.WEST);

        JLabel icono = new JLabel("∞", SwingConstants.RIGHT);
        icono.setFont(new Font("Segoe UI", Font.BOLD, 36));
        icono.setForeground(COLOR_ACENTO);
        panel.add(icono, BorderLayout.EAST);

        return panel;
    }

    // ── Panel central: columna izquierda (controles) + derecha (tabla + cursor)
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(12, 12, 0, 12));

        panel.add(crearColumnaIzquierda(), BorderLayout.WEST);
        panel.add(crearColumnaDerecha(),   BorderLayout.CENTER);

        return panel;
    }

    // ── Columna izquierda ────────────────────────────────────────────────────
    private JPanel crearColumnaIzquierda() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(COLOR_FONDO);
        col.setPreferredSize(new Dimension(310, 0));

        col.add(crearPanelFormulario());
        col.add(Box.createVerticalStrut(10));
        col.add(crearPanelInsercion());
        col.add(Box.createVerticalStrut(10));
        col.add(crearPanelRemocion());
        col.add(Box.createVerticalStrut(10));
        col.add(crearPanelMuestra());
        col.add(Box.createVerticalStrut(10));
        col.add(crearPanelBusqueda());
        col.add(Box.createVerticalStrut(10));
        col.add(crearPanelAuxiliares());
        col.add(Box.createVerticalGlue());

        return col;
    }

    // ── Columna derecha ──────────────────────────────────────────────────────
    private JPanel crearColumnaDerecha() {
        JPanel col = new JPanel(new BorderLayout(0, 10));
        col.setBackground(COLOR_FONDO);

        col.add(crearPanelCursor(),     BorderLayout.NORTH);
        col.add(crearPanelTabla(),      BorderLayout.CENTER);

        return col;
    }

    // =========================================================
    // Panel Formulario
    // =========================================================
    private JPanel crearPanelFormulario() {
        JPanel panel = crearPanelTitulado("📋  Datos del Proceso");

        panel.setLayout(new GridLayout(0, 2, 6, 6));

        panel.add(etiqueta("ID:"));
        txtId = campo("0");
        panel.add(txtId);

        panel.add(etiqueta("Nombre:"));
        txtNombre = campo("ej. Kernel");
        panel.add(txtNombre);

        panel.add(etiqueta("Prioridad (1-10):"));
        txtPrioridad = campo("1");
        panel.add(txtPrioridad);

        panel.add(etiqueta("Tiempo CPU (ms):"));
        txtTiempo = campo("10.0");
        panel.add(txtTiempo);

        panel.add(etiqueta("Posición (base 0):"));
        txtPosicion = campo("0");
        panel.add(txtPosicion);

        return panel;
    }

    // =========================================================
    // Panel Inserción
    // =========================================================
    private JPanel crearPanelInsercion() {
        JPanel panel = crearPanelTitulado("➕  Insertar");
        panel.setLayout(new GridLayout(1, 3, 6, 0));

        panel.add(boton("Al Principio", COLOR_ACENTO,  e -> accionInsertarPrincipio()));
        panel.add(boton("Al Final",     COLOR_ACENTO,  e -> accionInsertarFinal()));
        panel.add(boton("Por Posición", COLOR_AMARILLO, e -> accionInsertarPosicion()));

        return panel;
    }

    // =========================================================
    // Panel Remoción
    // =========================================================
    private JPanel crearPanelRemocion() {
        JPanel panel = crearPanelTitulado("➖  Remover");
        panel.setLayout(new GridLayout(1, 3, 6, 0));

        panel.add(boton("Al Principio", COLOR_ROJO,    e -> accionRemoverPrincipio()));
        panel.add(boton("Al Final",     COLOR_ROJO,    e -> accionRemoverFinal()));
        panel.add(boton("Por Posición", COLOR_AMARILLO, e -> accionRemoverPosicion()));

        return panel;
    }

    // =========================================================
    // Panel Muestra
    // =========================================================
    private JPanel crearPanelMuestra() {
        JPanel panel = crearPanelTitulado("🔍  Mostrar");
        panel.setLayout(new GridLayout(1, 3, 6, 0));

        panel.add(boton("Primero",     COLOR_VERDE, e -> accionMostrarPrincipio()));
        panel.add(boton("Último",      COLOR_VERDE, e -> accionMostrarFinal()));
        panel.add(boton("Por Posición",COLOR_VERDE, e -> accionMostrarPosicion()));

        return panel;
    }

    // =========================================================
    // Panel Búsqueda
    // =========================================================
    private JPanel crearPanelBusqueda() {
        JPanel panel = crearPanelTitulado("🔎  Buscar");

        JPanel fila1 = new JPanel(new BorderLayout(6, 0));
        fila1.setOpaque(false);
        fila1.add(etiqueta("Nombre / ID:"), BorderLayout.WEST);
        txtBuscar = campo("nombre o id");
        fila1.add(txtBuscar, BorderLayout.CENTER);

        JPanel fila2 = new JPanel(new GridLayout(1, 2, 6, 0));
        fila2.setOpaque(false);
        fila2.add(boton("Por Nombre", COLOR_ACENTO2, e -> accionBuscarNombre()));
        fila2.add(boton("Por ID",     COLOR_ACENTO2, e -> accionBuscarId()));

        panel.setLayout(new GridLayout(2, 1, 0, 6));
        panel.add(fila1);
        panel.add(fila2);

        return panel;
    }

    // =========================================================
    // Panel Auxiliares (vaciar)
    // =========================================================
    private JPanel crearPanelAuxiliares() {
        JPanel panel = crearPanelTitulado("⚙️  Auxiliares");
        panel.setLayout(new GridLayout(1, 1));
        panel.add(boton("Vaciar Lista", COLOR_ROJO, e -> accionVaciar()));
        return panel;
    }

    // =========================================================
    // Panel Cursor (navegación circular)
    // =========================================================
    private JPanel crearPanelCursor() {
        JPanel panel = crearPanelTitulado("🔄  Nodo actual  ·  Navegación Circular");
        panel.setLayout(new BorderLayout(10, 6));

        // Info del nodo cursor
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
        info.setOpaque(false);

        lblCursorDato = new JLabel("(lista vacía)", SwingConstants.CENTER);
        lblCursorDato.setFont(new Font("Consolas", Font.BOLD, 13));
        lblCursorDato.setForeground(COLOR_TEXTO);

        lblCursorPos = new JLabel("Posición: —", SwingConstants.CENTER);
        lblCursorPos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCursorPos.setForeground(COLOR_SUBTEXTO);

        info.add(lblCursorDato);
        info.add(lblCursorPos);

        // Indicador circular
        lblIndicadorCircular = new JLabel("", SwingConstants.CENTER);
        lblIndicadorCircular.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblIndicadorCircular.setForeground(COLOR_AMARILLO);

        // Botones navegación
        JPanel botones = new JPanel(new GridLayout(1, 2, 8, 0));
        botones.setOpaque(false);
        JButton btnAnterior = boton("◀  Anterior", new Color(79, 70, 229), e -> accionAnterior());
        JButton btnSiguiente = boton("Siguiente  ▶", new Color(79, 70, 229), e -> accionSiguiente());
        botones.add(btnAnterior);
        botones.add(btnSiguiente);

        JPanel derecha = new JPanel(new GridLayout(2, 1, 0, 4));
        derecha.setOpaque(false);
        derecha.add(lblIndicadorCircular);
        derecha.add(botones);

        panel.add(info,    BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

        return panel;
    }

    // =========================================================
    // Panel Tabla
    // =========================================================
    private JPanel crearPanelTabla() {
        JPanel panel = crearPanelTitulado("📊  Contenido del Anillo");
        panel.setLayout(new BorderLayout());

        String[] columnas = {"Pos", "ID", "Nombre", "Prioridad", "Tiempo (ms)", "← Ant", "Sig →"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabla = new JTable(modeloTabla) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                // Resaltar la fila del cursor
                int cursorPos = lista.posicionCursor();
                if (row == cursorPos) {
                    c.setBackground(new Color(99, 102, 241, 120));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? COLOR_FILA_PAR : COLOR_FILA_IMPAR);
                    c.setForeground(COLOR_TEXTO);
                }
                return c;
            }
        };

        tabla.setBackground(COLOR_FILA_PAR);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Consolas", Font.PLAIN, 12));
        tabla.getTableHeader().setBackground(COLOR_PANEL);
        tabla.getTableHeader().setForeground(COLOR_SUBTEXTO);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setSelectionBackground(COLOR_ACENTO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);

        // Ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(35);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(45);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(COLOR_FILA_PAR);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // Barra de estado
    // =========================================================
    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(23, 32, 50));
        barra.setBorder(new EmptyBorder(6, 16, 6, 16));

        lblEstado = new JLabel("Listo. La lista está vacía.");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(COLOR_SUBTEXTO);

        JLabel tam = new JLabel("Tamaño: 0", SwingConstants.RIGHT);
        tam.setName("lblTamanio");
        tam.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tam.setForeground(COLOR_ACENTO);
        barra.add(lblEstado, BorderLayout.WEST);
        barra.add(tam,       BorderLayout.EAST);
        return barra;
    }

    // =========================================================
    // Helpers de construcción de componentes
    // =========================================================
    private JPanel crearPanelTitulado(String titulo) {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_PANEL);
        TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDE), titulo);
        borde.setTitleColor(COLOR_SUBTEXTO);
        borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 11));
        panel.setBorder(BorderFactory.createCompoundBorder(
                borde, new EmptyBorder(4, 6, 6, 6)));
        return panel;
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(COLOR_SUBTEXTO);
        return lbl;
    }

    private JTextField campo(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setBackground(COLOR_CAMPO);
        tf.setForeground(COLOR_TEXTO);
        tf.setCaretColor(COLOR_TEXTO);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                new EmptyBorder(3, 6, 3, 6)));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        // Limpiar placeholder al foco
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) tf.setText("");
            }
        });
        return tf;
    }

    private JButton boton(String texto, Color colorBase, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBase.darker()),
                new EmptyBorder(5, 8, 5, 8)));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(accion);
        // Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(colorBase.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });
        return btn;
    }

    // =========================================================
    // Leer y validar campos del formulario
    // =========================================================

    /** Construye y retorna un Proceso a partir de los campos del formulario.
     *  Lanza IllegalArgumentException con mensaje amigable si hay errores. */
    private Proceso leerProceso() {
        int id;
        String nombre;
        int prioridad;
        double tiempo;

        try {
            id = Integer.parseInt(txtId.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo ID debe ser un número entero.");
        }

        nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El campo Nombre no puede estar vacío.");
        }

        try {
            prioridad = Integer.parseInt(txtPrioridad.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La Prioridad debe ser un número entero (1-10).");
        }

        try {
            tiempo = Double.parseDouble(txtTiempo.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El Tiempo CPU debe ser un número decimal.");
        }

        return new Proceso(id, nombre, prioridad, tiempo); // el record valida el resto
    }

    private int leerPosicion() {
        try {
            return Integer.parseInt(txtPosicion.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo Posición debe ser un número entero.");
        }
    }

    // =========================================================
    // Acciones — Inserción
    // =========================================================
    private void accionInsertarPrincipio() {
        try {
            Proceso p = leerProceso();
            lista.insertarAlPrincipio(p);
            estado("✅ Proceso '" + p.nombre() + "' insertado al principio.");
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionInsertarFinal() {
        try {
            Proceso p = leerProceso();
            lista.insertarAlFinal(p);
            estado("✅ Proceso '" + p.nombre() + "' insertado al final.");
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionInsertarPosicion() {
        try {
            Proceso p   = leerProceso();
            int pos     = leerPosicion();
            lista.insertarEnPosicion(p, pos);
            estado("✅ Proceso '" + p.nombre() + "' insertado en posición " + pos + ".");
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    // =========================================================
    // Acciones — Remoción
    // =========================================================
    private void accionRemoverPrincipio() {
        try {
            Proceso p = lista.removerAlPrincipio();
            estado("🗑️ Proceso '" + p.nombre() + "' removido del principio.");
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionRemoverFinal() {
        try {
            Proceso p = lista.removerAlFinal();
            estado("🗑️ Proceso '" + p.nombre() + "' removido del final.");
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionRemoverPosicion() {
        try {
            int pos   = leerPosicion();
            Proceso p = lista.removerEnPosicion(pos);
            estado("🗑️ Proceso '" + p.nombre() + "' removido de posición " + pos + ".");
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    // =========================================================
    // Acciones — Muestra
    // =========================================================
    private void accionMostrarPrincipio() {
        try {
            Proceso p = lista.mostrarAlPrincipio();
            lista.irAlPrimero();
            estado("🔍 Primero → " + p);
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionMostrarFinal() {
        try {
            Proceso p = lista.mostrarAlFinal();
            lista.irAlUltimo();
            estado("🔍 Último → " + p);
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionMostrarPosicion() {
        try {
            int pos   = leerPosicion();
            Proceso p = lista.mostrarEnPosicion(pos);
            estado("🔍 Posición " + pos + " → " + p);
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    // =========================================================
    // Acciones — Navegación circular
    // =========================================================
    private void accionSiguiente() {
        try {
            boolean eraUltimo = lista.cursorEsUltimo();
            Proceso p = lista.avanzarSiguiente();
            String extra = eraUltimo ? "  🔄 [ultimo → primero]" : "";
            estado("▶ Siguiente → " + p.nombre() + extra);
            if (eraUltimo) {
                lblIndicadorCircular.setText("🔄 ultimo → primero");
            } else if (lista.cursorEsUltimo()) {
                lblIndicadorCircular.setText("⚠️ En el último nodo");
            } else if (lista.cursorEsPrimero()) {
                lblIndicadorCircular.setText("⚠️ En el primer nodo");
            } else {
                lblIndicadorCircular.setText("");
            }
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionAnterior() {
        try {
            boolean eraPrimero = lista.cursorEsPrimero();
            Proceso p = lista.retrocederAnterior();
            String extra = eraPrimero ? "  🔄 [primero → ultimo]" : "";
            estado("◀ Anterior → " + p.nombre() + extra);
            if (eraPrimero) {
                lblIndicadorCircular.setText("🔄 primero → ultimo");
            } else if (lista.cursorEsPrimero()) {
                lblIndicadorCircular.setText("⚠️ En el primer nodo");
            } else if (lista.cursorEsUltimo()) {
                lblIndicadorCircular.setText("⚠️ En el último nodo");
            } else {
                lblIndicadorCircular.setText("");
            }
            refrescar();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    // =========================================================
    // Acciones — Búsqueda
    // =========================================================
    private void accionBuscarNombre() {
        try {
            String val = txtBuscar.getText().trim();
            if (val.isEmpty()) {
                error("Ingresa un nombre para buscar.");
                return;
            }
            if (lista.estaVacia()) {
                error("La lista está vacía.");
                return;
            }
            Proceso p = lista.buscarPorNombre(val);
            if (p == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró ningún proceso con nombre: '" + val + "'",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                estado("🔎 Búsqueda por nombre '" + val + "': sin resultados.");
            } else {
                int pos = lista.posicionCursor();
                estado("🔎 Encontrado en posición " + pos + " → " + p);
                refrescar();
            }
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void accionBuscarId() {
        try {
            String val = txtBuscar.getText().trim();
            int id;
            try {
                id = Integer.parseInt(val);
            } catch (NumberFormatException e) {
                error("Para buscar por ID ingresa un número entero.");
                return;
            }
            if (lista.estaVacia()) {
                error("La lista está vacía.");
                return;
            }
            Proceso p = lista.buscarPorId(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró ningún proceso con ID: " + id,
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                estado("🔎 Búsqueda por ID " + id + ": sin resultados.");
            } else {
                int pos = lista.posicionDeId(id);
                estado("🔎 Encontrado en posición " + pos + " → " + p);
                refrescar();
            }
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    // =========================================================
    // Acción — Vaciar
    // =========================================================
    private void accionVaciar() {
        if (lista.estaVacia()) {
            JOptionPane.showMessageDialog(this, "La lista ya está vacía.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de vaciar toda la lista?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            lista.vaciar();
            estado("🗑️ Lista vaciada.");
            refrescar();
        }
    }

    // =========================================================
    // Refresco de la tabla y panel cursor
    // =========================================================
    private void refrescar() {
        // Vaciar tabla
        modeloTabla.setRowCount(0);

        Proceso[] procesos = lista.obtenerTodos();
        int cursorPos      = lista.posicionCursor();

        for (int i = 0; i < procesos.length; i++) {
            Proceso p = procesos[i];
            // Calcular nombres anterior y siguiente para mostrar en tabla
            int iAnt = (i - 1 + procesos.length) % procesos.length;
            int iSig = (i + 1) % procesos.length;
            String ant = procesos[iAnt].nombre();
            String sig = procesos[iSig].nombre();

            modeloTabla.addRow(new Object[]{
                    i,
                    p.id(),
                    p.nombre(),
                    p.prioridad(),
                    String.format("%.2f", p.tiempo()),
                    "← " + ant,
                    sig + " →"
            });
        }

        // Panel cursor
        Proceso cur = lista.getCursor();
        if (cur == null) {
            lblCursorDato.setText("(lista vacía)");
            lblCursorPos.setText("Posición: —");
            lblIndicadorCircular.setText("");
        } else {
            lblCursorDato.setText(String.format("[%d]  %s  |  P:%d  T:%.1f ms",
                    cur.id(), cur.nombre(), cur.prioridad(), cur.tiempo()));
            lblCursorPos.setText("Posición: " + cursorPos + " / " + (lista.getTamanio() - 1)
                    + "   —   Tamaño: " + lista.getTamanio());
        }

        // Indicador circular automático
        if (!lista.estaVacia() && lista.getTamanio() > 1) {
            if (lista.cursorEsPrimero() && lblIndicadorCircular.getText().isEmpty()) {
                lblIndicadorCircular.setText("⚠️ En el primer nodo (← irá a último)");
            } else if (lista.cursorEsUltimo() && lblIndicadorCircular.getText().isEmpty()) {
                lblIndicadorCircular.setText("⚠️ En el último nodo (→ irá a primero)");
            }
        }

        // Barra de estado — tamaño
        Component[] comps = ((JPanel) getContentPane().getComponent(2)).getComponents();
        for (Component c : comps) {
            if (c instanceof JLabel lbl && "lblTamanio".equals(lbl.getName())) {
                lbl.setText("Tamaño: " + lista.getTamanio());
                break;
            }
        }

        tabla.repaint();
        // Scroll a la fila del cursor
        if (cursorPos >= 0 && cursorPos < tabla.getRowCount()) {
            tabla.scrollRectToVisible(tabla.getCellRect(cursorPos, 0, true));
        }
    }

    // =========================================================
    // Helpers de mensajes
    // =========================================================
    private void estado(String msg) {
        lblEstado.setText(msg);
    }

    private void error(String msg) {
        lblEstado.setText("⚠️ " + msg);
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // =========================================================
    // main
    // =========================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
