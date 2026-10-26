package tda;

import modelo.Proceso;

/**
 * Nodo de la Lista Circular Doblemente Enlazada.
 * Contiene una referencia al record de dominio (Proceso)
 * y dos referencias de enlace: siguiente y anterior.
 */
public class Nodo {

    /** Dato almacenado en el nodo (record inmutable). */
    private Proceso dato;

    /** Referencia al nodo siguiente en el anillo circular. */
    Nodo siguiente;

    /** Referencia al nodo anterior en el anillo circular. */
    Nodo anterior;

    /**
     * Constructor que inicializa el nodo con un dato.
     * Los enlaces siguiente y anterior quedan en null hasta ser conectados al anillo.
     *
     * @param dato El record Proceso que almacena este nodo.
     */
    public Nodo(Proceso dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }

    /**
     * Retorna el record de dominio almacenado en este nodo.
     *
     * @return El Proceso guardado.
     */
    public Proceso getDato() {
        return dato;
    }

    /**
     * Actualiza el record de dominio almacenado en este nodo.
     *
     * @param dato El nuevo Proceso a almacenar.
     */
    public void setDato(Proceso dato) {
        this.dato = dato;
    }
}
