package tda;

import modelo.Proceso;

/**
 * TDA Lista Circular Doblemente Enlazada.
 *
 * Invariante: Cuando la lista tiene al menos un nodo,
 * se cumple siempre que ultimo.siguiente == primero  y  primero.anterior == ultimo.
 *
 * Restricciones:
 *  - No usa java.util.List, ArrayList, LinkedList ni arreglos estáticos.
 *  - Toda la gestión es manual mediante referencias circulares dobles.
 */
public class ListaCircular {

    /** Referencia al primer nodo del anillo. null si la lista está vacía. */
    private Nodo primero;

    /** Referencia al último nodo del anillo. null si la lista está vacía. */
    private Nodo ultimo;

    /** Cantidad de nodos en el anillo. */
    private int tamanio;

    /** Nodo actualmente "apuntado" por los botones de navegación. */
    private Nodo cursor;

    // =========================================================
    // Constructor
    // =========================================================

    public ListaCircular() {
        primero = null;
        ultimo = null;
        tamanio = 0;
        cursor = null;
    }

    // =========================================================
    // Operaciones auxiliares
    // =========================================================

    /**
     * Indica si la lista está vacía.
     *
     * @return true si primero y ultimo son null.
     */
    public boolean estaVacia() {
        return primero == null;
    }

    /**
     * Retorna la cantidad de nodos en el anillo.
     *
     * @return tamaño actual.
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * Vacía la lista reajustando primero, ultimo y cursor a null.
     */
    public void vaciar() {
        primero = null;
        ultimo = null;
        cursor = null;
        tamanio = 0;
    }

    /**
     * Retorna el Proceso actualmente apuntado por el cursor de navegación.
     * El cursor se inicializa en primero con la primera inserción.
     *
     * @return Proceso del nodo cursor, o null si la lista está vacía.
     */
    public Proceso getCursor() {
        return cursor == null ? null : cursor.getDato();
    }

    /**
     * Informa si el cursor apunta al primer nodo.
     */
    public boolean cursorEsPrimero() {
        return cursor != null && cursor == primero;
    }

    /**
     * Informa si el cursor apunta al último nodo.
     */
    public boolean cursorEsUltimo() {
        return cursor != null && cursor == ultimo;
    }

    /**
     * Retorna la posición (índice base-0) del cursor dentro del anillo.
     * Retorna -1 si la lista está vacía.
     */
    public int posicionCursor() {
        if (estaVacia()) return -1;
        Nodo aux = primero;
        for (int i = 0; i < tamanio; i++) {
            if (aux == cursor) return i;
            aux = aux.siguiente;
        }
        return -1;
    }

    // =========================================================
    // Operaciones de Inserción
    // =========================================================

    /**
     * Inserta un Proceso al principio del anillo (antes de primero).
     *
     * @param dato El record Proceso a insertar.
     */
    public void insertarAlPrincipio(Proceso dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia()) {
            // Caso base: único nodo, se auto-enlaza
            nuevo.siguiente = nuevo;
            nuevo.anterior  = nuevo;
            primero = nuevo;
            ultimo  = nuevo;
        } else {
            // Insertar antes de primero
            nuevo.siguiente       = primero;
            nuevo.anterior        = ultimo;
            primero.anterior      = nuevo;
            ultimo.siguiente      = nuevo;
            primero               = nuevo;
        }

        tamanio++;
        if (cursor == null) cursor = primero;
    }

    /**
     * Inserta un Proceso al final del anillo (después de ultimo).
     *
     * @param dato El record Proceso a insertar.
     */
    public void insertarAlFinal(Proceso dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia()) {
            nuevo.siguiente = nuevo;
            nuevo.anterior  = nuevo;
            primero = nuevo;
            ultimo  = nuevo;
        } else {
            nuevo.siguiente       = primero;
            nuevo.anterior        = ultimo;
            ultimo.siguiente      = nuevo;
            primero.anterior      = nuevo;
            ultimo                = nuevo;
        }

        tamanio++;
        if (cursor == null) cursor = primero;
    }

    /**
     * Inserta un Proceso en la posición indicada (base-0).
     * Posición 0 equivale a insertarAlPrincipio.
     * Posición tamanio equivale a insertarAlFinal.
     *
     * @param dato     El record Proceso a insertar.
     * @param posicion Índice de inserción (0 … tamanio).
     * @throws IndexOutOfBoundsException si posicion < 0 o posicion > tamanio.
     */
    public void insertarEnPosicion(Proceso dato, int posicion) {
        if (posicion < 0 || posicion > tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición " + posicion + " fuera de rango [0, " + tamanio + "].");
        }
        if (posicion == 0) {
            insertarAlPrincipio(dato);
            return;
        }
        if (posicion == tamanio) {
            insertarAlFinal(dato);
            return;
        }

        // Buscar el nodo que actualmente ocupa la posición indicada
        Nodo aux = primero;
        for (int i = 0; i < posicion; i++) {
            aux = aux.siguiente;
        }

        // Insertar nuevo antes de aux
        Nodo nuevo = new Nodo(dato);
        Nodo previo = aux.anterior;

        nuevo.siguiente  = aux;
        nuevo.anterior   = previo;
        previo.siguiente = nuevo;
        aux.anterior     = nuevo;

        tamanio++;
    }

    // =========================================================
    // Operaciones de Remoción
    // =========================================================

    /**
     * Elimina el nodo primero del anillo.
     *
     * @return El Proceso que contenía el nodo eliminado.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso removerAlPrincipio() {
        verificarNoVacia("remover al principio");
        Proceso dato = primero.getDato();

        if (tamanio == 1) {
            limpiarUltimoNodo();
        } else {
            Nodo nuevoPrimero = primero.siguiente;
            nuevoPrimero.anterior = ultimo;
            ultimo.siguiente      = nuevoPrimero;
            if (cursor == primero) cursor = nuevoPrimero;
            primero = nuevoPrimero;
            tamanio--;
        }
        return dato;
    }

    /**
     * Elimina el nodo ultimo del anillo.
     *
     * @return El Proceso que contenía el nodo eliminado.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso removerAlFinal() {
        verificarNoVacia("remover al final");
        Proceso dato = ultimo.getDato();

        if (tamanio == 1) {
            limpiarUltimoNodo();
        } else {
            Nodo nuevoUltimo = ultimo.anterior;
            nuevoUltimo.siguiente = primero;
            primero.anterior      = nuevoUltimo;
            if (cursor == ultimo) cursor = nuevoUltimo;
            ultimo = nuevoUltimo;
            tamanio--;
        }
        return dato;
    }

    /**
     * Elimina el nodo en la posición indicada (base-0).
     *
     * @param posicion Índice del nodo a eliminar (0 … tamanio-1).
     * @return El Proceso eliminado.
     * @throws IndexOutOfBoundsException si posicion está fuera de rango.
     * @throws IllegalStateException     si la lista está vacía.
     */
    public Proceso removerEnPosicion(int posicion) {
        verificarNoVacia("remover por posición");
        verificarRango(posicion);

        if (posicion == 0)          return removerAlPrincipio();
        if (posicion == tamanio - 1) return removerAlFinal();

        Nodo aux = primero;
        for (int i = 0; i < posicion; i++) {
            aux = aux.siguiente;
        }

        Nodo previo   = aux.anterior;
        Nodo siguiente = aux.siguiente;
        previo.siguiente   = siguiente;
        siguiente.anterior = previo;
        if (cursor == aux) cursor = siguiente;
        tamanio--;
        return aux.getDato();
    }

    // =========================================================
    // Operaciones de Visualización / Muestra
    // =========================================================

    /**
     * Retorna el Proceso almacenado en primero sin eliminarlo.
     *
     * @return Proceso del primer nodo.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso mostrarAlPrincipio() {
        verificarNoVacia("mostrar al principio");
        return primero.getDato();
    }

    /**
     * Retorna el Proceso almacenado en ultimo sin eliminarlo.
     *
     * @return Proceso del último nodo.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso mostrarAlFinal() {
        verificarNoVacia("mostrar al final");
        return ultimo.getDato();
    }

    /**
     * Retorna el Proceso almacenado en la posición indicada (base-0).
     *
     * @param posicion Índice del nodo (0 … tamanio-1).
     * @return Proceso en esa posición.
     * @throws IndexOutOfBoundsException si posicion está fuera de rango.
     * @throws IllegalStateException     si la lista está vacía.
     */
    public Proceso mostrarEnPosicion(int posicion) {
        verificarNoVacia("mostrar por posición");
        verificarRango(posicion);

        Nodo aux = primero;
        for (int i = 0; i < posicion; i++) {
            aux = aux.siguiente;
        }
        return aux.getDato();
    }

    // =========================================================
    // Navegación Circular (regla obligatoria)
    // =========================================================

    /**
     * Avanza el cursor al nodo siguiente.
     * Regla circular: si cursor == ultimo, el siguiente es primero.
     *
     * @return El Proceso del nuevo nodo apuntado por el cursor.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso avanzarSiguiente() {
        verificarNoVacia("navegar siguiente");
        cursor = cursor.siguiente; // Por la propiedad circular, ultimo.siguiente == primero
        return cursor.getDato();
    }

    /**
     * Retrocede el cursor al nodo anterior.
     * Regla circular: si cursor == primero, el anterior es ultimo.
     *
     * @return El Proceso del nuevo nodo apuntado por el cursor.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso retrocederAnterior() {
        verificarNoVacia("navegar anterior");
        cursor = cursor.anterior; // Por la propiedad circular, primero.anterior == ultimo
        return cursor.getDato();
    }

    /**
     * Posiciona el cursor directamente en primero.
     *
     * @return El Proceso del nodo primero.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso irAlPrimero() {
        verificarNoVacia("ir al primero");
        cursor = primero;
        return cursor.getDato();
    }

    /**
     * Posiciona el cursor directamente en ultimo.
     *
     * @return El Proceso del nodo ultimo.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Proceso irAlUltimo() {
        verificarNoVacia("ir al ultimo");
        cursor = ultimo;
        return cursor.getDato();
    }

    // =========================================================
    // Operaciones de Búsqueda
    // =========================================================

    /**
     * Busca un Proceso cuyo campo 'nombre' coincida (ignorando mayúsculas)
     * con el valor entregado. Recorre el anillo completo una sola vez para
     * evitar bucle infinito.
     *
     * @param nombre Nombre del proceso a buscar.
     * @return El Proceso encontrado o null si no existe.
     */
    public Proceso buscarPorNombre(String nombre) {
        if (estaVacia() || nombre == null) return null;

        Nodo aux = primero;
        for (int i = 0; i < tamanio; i++) {
            if (aux.getDato().nombre().equalsIgnoreCase(nombre.trim())) {
                cursor = aux; // mover cursor al encontrado
                return aux.getDato();
            }
            aux = aux.siguiente;
        }
        return null;
    }

    /**
     * Busca un Proceso cuyo campo 'id' coincida con el valor entregado.
     *
     * @param id El identificador a buscar.
     * @return El Proceso encontrado o null si no existe.
     */
    public Proceso buscarPorId(int id) {
        if (estaVacia()) return null;

        Nodo aux = primero;
        for (int i = 0; i < tamanio; i++) {
            if (aux.getDato().id() == id) {
                cursor = aux;
                return aux.getDato();
            }
            aux = aux.siguiente;
        }
        return null;
    }

    /**
     * Retorna el índice (base-0) del nodo cuyo id coincide.
     * Retorna -1 si no se encuentra.
     *
     * @param id El identificador a buscar.
     * @return Posición o -1.
     */
    public int posicionDeId(int id) {
        if (estaVacia()) return -1;
        Nodo aux = primero;
        for (int i = 0; i < tamanio; i++) {
            if (aux.getDato().id() == id) return i;
            aux = aux.siguiente;
        }
        return -1;
    }

    // =========================================================
    // Obtener todos los procesos como arreglo (para la tabla GUI)
    // =========================================================

    /**
     * Retorna un arreglo con todos los Procesos del anillo en orden desde primero.
     * Retorna arreglo vacío si la lista está vacía.
     *
     * @return Arreglo de Proceso.
     */
    public Proceso[] obtenerTodos() {
        Proceso[] arreglo = new Proceso[tamanio];
        if (estaVacia()) return arreglo;

        Nodo aux = primero;
        for (int i = 0; i < tamanio; i++) {
            arreglo[i] = aux.getDato();
            aux = aux.siguiente;
        }
        return arreglo;
    }

    // =========================================================
    // Métodos privados de apoyo
    // =========================================================

    /** Lanza IllegalStateException si la lista está vacía. */
    private void verificarNoVacia(String operacion) {
        if (estaVacia()) {
            throw new IllegalStateException(
                    "No se puede ejecutar '" + operacion + "': la lista está vacía.");
        }
    }

    /** Lanza IndexOutOfBoundsException si posicion está fuera de [0, tamanio-1]. */
    private void verificarRango(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición " + posicion + " fuera de rango [0, " + (tamanio - 1) + "].");
        }
    }

    /** Limpia las referencias cuando se elimina el único nodo. */
    private void limpiarUltimoNodo() {
        primero = null;
        ultimo  = null;
        cursor  = null;
        tamanio = 0;
    }
}
