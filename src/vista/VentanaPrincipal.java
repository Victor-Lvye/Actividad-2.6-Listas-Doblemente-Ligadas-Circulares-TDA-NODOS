package vista;

import modelo.Proceso;
import tda.ListaCircular;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Interfaz gráfica principal del TDA Lista Circular.
 * Diseño simple usando el Look & Feel del sistema operativo.
 *
 * Capas:
 *   modelo → record Proceso
 *   tda    → ListaCircular (sin referencias a Swing)
 *   vista  → VentanaPrincipal (solo llama la API del TDA)
 */
public class VentanaPrincipal extends JFrame {

    private final ListaCircular lista = new ListaCircular();

    // ── Formulario ───────────────────────────────────────────
    private JTextField txtId, txtNombre, txtPrioridad, txtTiempo;
    private JTextField txtPosicion, txtBuscar;

    // ── Tabla ────────────────────────────────────────────────
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    // ── Panel de cursor / navegación ─────────────────────────
    private JLabel lblCursorInfo;
    private JLabel lblIndicador;

    // ── Barra de estado ──────────────────────────────────────
    private JLabel lblEstado;
    private JLabel lblTamanio;

    // =========================================================
    public VentanaPrincipal() {
        super("Lista Circular — TDA con Records Java");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 560));
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(6, 6));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        add(crearPanelIzquierdo(), BorderLayout.WEST);
        add(crearPanelDerecho(),   BorderLayout.CENTER);
        add(crearBarraEstado(),    BorderLayout.SOUTH);

        setVisible(true);
    }

    // =========================================================
    // Panel izquierdo: formulario + botones
    // =========================================================
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(270, 0));

        panel.add(crearPanelFormulario());
        panel.add(Box.createVerticalStrut(6));
        panel.add(crearPanelInsercion());
        panel.add(Box.createVerticalStrut(6));
        panel.add(crearPanelRemocion());
        panel.add(Box.createVerticalStrut(6));
        panel.add(crearPanelMuestra());
        panel.add(Box.createVerticalStrut(6));
        panel.add(crearPanelBusqueda());
        panel.add(Box.createVerticalStrut(6));
        panel.add(crearPanelAuxiliares());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ── Formulario ───────────────────────────────────────────
    private JPanel crearPanelFormulario() {
        JPanel p = titulado("Datos del Proceso");
        p.setLayout(new GridLayout(0, 2, 4, 4));

        p.add(new JLabel("ID:"));
        txtId = new JTextField("0", 8);
        p.add(txtId);

        p.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(8);
        p.add(txtNombre);

        p.add(new JLabel("Prioridad (1-10):"));
        txtPrioridad = new JTextField("1", 8);
        p.add(txtPrioridad);

        p.add(new JLabel("Tiempo CPU (ms):"));
        txtTiempo = new JTextField("10.0", 8);
        p.add(txtTiempo);

        p.add(new JLabel("Posición (base 0):"));
        txtPosicion = new JTextField("0", 8);
        p.add(txtPosicion);

        return p;
    }

    // ── Inserción ─────────────────────────────────────────────
    private JPanel crearPanelInsercion() {
        JPanel p = titulado("Insertar");
        p.setLayout(new GridLayout(1, 3, 4, 0));
        p.add(btn("Al principio", e -> accionInsertarPrincipio()));
        p.add(btn("Al final",     e -> accionInsertarFinal()));
        p.add(btn("Por posición", e -> accionInsertarPosicion()));
        return p;
    }

    // ── Remoción ──────────────────────────────────────────────
    private JPanel crearPanelRemocion() {
        JPanel p = titulado("Remover");
        p.setLayout(new GridLayout(1, 3, 4, 0));
        p.add(btn("Al principio", e -> accionRemoverPrincipio()));
        p.add(btn("Al final",     e -> accionRemoverFinal()));
        p.add(btn("Por posición", e -> accionRemoverPosicion()));
        return p;
    }

    // ── Muestra ───────────────────────────────────────────────
    private JPanel crearPanelMuestra() {
        JPanel p = titulado("Mostrar");
        p.setLayout(new GridLayout(1, 3, 4, 0));
        p.add(btn("Primero",      e -> accionMostrarPrincipio()));
        p.add(btn("Ultimo",       e -> accionMostrarFinal()));
        p.add(btn("Por posicion", e -> accionMostrarPosicion()));
        return p;
    }

    // ── Búsqueda ──────────────────────────────────────────────
    private JPanel crearPanelBusqueda() {
        JPanel p = titulado("Buscar");
        p.setLayout(new GridLayout(2, 1, 0, 4));

        JPanel fila1 = new JPanel(new BorderLayout(4, 0));
        fila1.setOpaque(false);
        fila1.add(new JLabel("Valor:"), BorderLayout.WEST);
        txtBuscar = new JTextField(8);
        fila1.add(txtBuscar, BorderLayout.CENTER);

        JPanel fila2 = new JPanel(new GridLayout(1, 2, 4, 0));
        fila2.setOpaque(false);
        fila2.add(btn("Por nombre", e -> accionBuscarNombre()));
        fila2.add(btn("Por ID",     e -> accionBuscarId()));

        p.add(fila1);
        p.add(fila2);
        return p;
    }

    // ── Auxiliares ────────────────────────────────────────────
    private JPanel crearPanelAuxiliares() {
        JPanel p = titulado("Auxiliares");
        p.setLayout(new GridLayout(1, 1));
        p.add(btn("Vaciar lista", e -> accionVaciar()));
        return p;
    }

    // =========================================================
    // Panel derecho: navegación + tabla
    // =========================================================
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));

        panel.add(crearPanelNavegacion(), BorderLayout.NORTH);
        panel.add(crearPanelTabla(),      BorderLayout.CENTER);

        return panel;
    }

    // ── Navegación circular ───────────────────────────────────
    private JPanel crearPanelNavegacion() {
        JPanel p = titulado("Nodo actual  /  Navegacion Circular");
        p.setLayout(new BorderLayout(10, 4));

        lblCursorInfo = new JLabel("(lista vacia)");
        lblCursorInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        lblIndicador = new JLabel(" ");
        lblIndicador.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
        info.setOpaque(false);
        info.add(lblCursorInfo);
        info.add(lblIndicador);

        JPanel botones = new JPanel(new GridLayout(1, 2, 6, 0));
        botones.setOpaque(false);
        botones.add(btn("< Anterior", e -> accionAnterior()));
        botones.add(btn("Siguiente >", e -> accionSiguiente()));

        p.add(info,    BorderLayout.CENTER);
        p.add(botones, BorderLayout.EAST);
        return p;
    }

    // ── Tabla ─────────────────────────────────────────────────
    private JPanel crearPanelTabla() {
        JPanel p = titulado("Contenido del anillo");
        p.setLayout(new BorderLayout());

        String[] cols = {"Pos", "ID", "Nombre", "Prioridad", "Tiempo(ms)", "<- Ant", "Sig ->"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Anchos de columna
        int[] anchos = {35, 45, 130, 65, 90, 90, 90};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    // ── Barra de estado ───────────────────────────────────────
    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(3, 4, 3, 4)));

        lblEstado  = new JLabel("Listo.");
        lblTamanio = new JLabel("Tamanio: 0");
        lblTamanio.setFont(lblTamanio.getFont().deriveFont(Font.BOLD));

        barra.add(lblEstado,  BorderLayout.WEST);
        barra.add(lblTamanio, BorderLayout.EAST);
        return barra;
    }

    // =========================================================
    // Helpers de construcción
    // =========================================================
    private JPanel titulado(String titulo) {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titulo,
                TitledBorder.LEFT, TitledBorder.TOP));
        return p;
    }

    private JButton btn(String texto, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.addActionListener(accion);
        return b;
    }

    // =========================================================
    // Leer y validar campos
    // =========================================================
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
        return new Proceso(id, nombre, prioridad, tiempo);
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
            estado("Insertado al principio: " + p.nombre());
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionInsertarFinal() {
        try {
            Proceso p = leerProceso();
            lista.insertarAlFinal(p);
            estado("Insertado al final: " + p.nombre());
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionInsertarPosicion() {
        try {
            Proceso p = leerProceso();
            int pos   = leerPosicion();
            lista.insertarEnPosicion(p, pos);
            estado("Insertado en posicion " + pos + ": " + p.nombre());
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    // =========================================================
    // Acciones — Remoción
    // =========================================================
    private void accionRemoverPrincipio() {
        try {
            Proceso p = lista.removerAlPrincipio();
            estado("Removido del principio: " + p.nombre());
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionRemoverFinal() {
        try {
            Proceso p = lista.removerAlFinal();
            estado("Removido del final: " + p.nombre());
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionRemoverPosicion() {
        try {
            int pos   = leerPosicion();
            Proceso p = lista.removerEnPosicion(pos);
            estado("Removido de posicion " + pos + ": " + p.nombre());
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    // =========================================================
    // Acciones — Muestra
    // =========================================================
    private void accionMostrarPrincipio() {
        try {
            Proceso p = lista.mostrarAlPrincipio();
            lista.irAlPrimero();
            estado("Primero -> " + p);
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionMostrarFinal() {
        try {
            Proceso p = lista.mostrarAlFinal();
            lista.irAlUltimo();
            estado("Ultimo -> " + p);
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionMostrarPosicion() {
        try {
            int pos   = leerPosicion();
            Proceso p = lista.mostrarEnPosicion(pos);
            estado("Posicion " + pos + " -> " + p);
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    // =========================================================
    // Acciones — Navegación circular
    // =========================================================
    private void accionSiguiente() {
        try {
            boolean eraUltimo = lista.cursorEsUltimo();
            Proceso p = lista.avanzarSiguiente();
            if (eraUltimo) {
                lblIndicador.setText("[Circular: ultimo -> primero]");
                estado("Siguiente (circular): " + p.nombre());
            } else {
                lblIndicador.setText(lista.cursorEsUltimo() ? "[En el ultimo nodo]" : "");
                estado("Siguiente -> " + p.nombre());
            }
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionAnterior() {
        try {
            boolean eraPrimero = lista.cursorEsPrimero();
            Proceso p = lista.retrocederAnterior();
            if (eraPrimero) {
                lblIndicador.setText("[Circular: primero -> ultimo]");
                estado("Anterior (circular): " + p.nombre());
            } else {
                lblIndicador.setText(lista.cursorEsPrimero() ? "[En el primer nodo]" : "");
                estado("Anterior -> " + p.nombre());
            }
            refrescar();
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    // =========================================================
    // Acciones — Búsqueda
    // =========================================================
    private void accionBuscarNombre() {
        try {
            String val = txtBuscar.getText().trim();
            if (val.isEmpty()) { error("Ingresa un nombre para buscar."); return; }
            if (lista.estaVacia()) { error("La lista esta vacia."); return; }

            Proceso p = lista.buscarPorNombre(val);
            if (p == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontro ningun proceso con nombre: " + val,
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                estado("Busqueda por nombre '" + val + "': sin resultados.");
            } else {
                estado("Encontrado en posicion " + lista.posicionCursor() + " -> " + p);
                refrescar();
            }
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void accionBuscarId() {
        try {
            String val = txtBuscar.getText().trim();
            int id;
            try {
                id = Integer.parseInt(val);
            } catch (NumberFormatException e) {
                error("Para buscar por ID ingresa un numero entero.");
                return;
            }
            if (lista.estaVacia()) { error("La lista esta vacia."); return; }

            Proceso p = lista.buscarPorId(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontro ningun proceso con ID: " + id,
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                estado("Busqueda por ID " + id + ": sin resultados.");
            } else {
                estado("Encontrado en posicion " + lista.posicionDeId(id) + " -> " + p);
                refrescar();
            }
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    // =========================================================
    // Acción — Vaciar
    // =========================================================
    private void accionVaciar() {
        if (lista.estaVacia()) {
            JOptionPane.showMessageDialog(this, "La lista ya esta vacia.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Vaciar toda la lista?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            lista.vaciar();
            estado("Lista vaciada.");
            refrescar();
        }
    }

    // =========================================================
    // Refrescar tabla y panel de cursor
    // =========================================================
    private void refrescar() {
        modeloTabla.setRowCount(0);

        Proceso[] procesos = lista.obtenerTodos();
        int curPos = lista.posicionCursor();

        for (int i = 0; i < procesos.length; i++) {
            Proceso p  = procesos[i];
            int iAnt   = (i - 1 + procesos.length) % procesos.length;
            int iSig   = (i + 1) % procesos.length;
            modeloTabla.addRow(new Object[]{
                    i,
                    p.id(),
                    p.nombre(),
                    p.prioridad(),
                    String.format("%.2f", p.tiempo()),
                    "<- " + procesos[iAnt].nombre(),
                    procesos[iSig].nombre() + " ->"
            });
        }

        // Resaltar fila del cursor
        if (curPos >= 0 && curPos < tabla.getRowCount()) {
            tabla.setRowSelectionInterval(curPos, curPos);
            tabla.scrollRectToVisible(tabla.getCellRect(curPos, 0, true));
        } else {
            tabla.clearSelection();
        }

        // Panel cursor
        Proceso cur = lista.getCursor();
        if (cur == null) {
            lblCursorInfo.setText("(lista vacia)");
            lblIndicador.setText(" ");
        } else {
            lblCursorInfo.setText(String.format(
                    "[%d] %s  |  Prioridad: %d  Tiempo: %.1f ms  |  Pos: %d/%d",
                    cur.id(), cur.nombre(), cur.prioridad(), cur.tiempo(),
                    curPos, lista.getTamanio() - 1));
            // Indicador automático si aún no hay mensaje de navegación
            if (lblIndicador.getText().isBlank()) {
                if (lista.cursorEsPrimero() && lista.getTamanio() > 1)
                    lblIndicador.setText("[En el primer nodo — Anterior ira al ultimo]");
                else if (lista.cursorEsUltimo() && lista.getTamanio() > 1)
                    lblIndicador.setText("[En el ultimo nodo — Siguiente ira al primero]");
            }
        }

        lblTamanio.setText("Tamanio: " + lista.getTamanio());
    }

    // =========================================================
    // Helpers de mensajes
    // =========================================================
    private void estado(String msg) {
        lblEstado.setText(msg);
    }

    private void error(String msg) {
        lblEstado.setText("Error: " + msg);
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
