package modelo;

/**
 * Record inmutable que representa la entidad de dominio almacenada en la Lista Circular.
 * Modela un Proceso (inspirado en Round-Robin Scheduling).
 * Al ser un record, Java genera automáticamente constructor canónico,
 * métodos accessors (id(), nombre(), prioridad(), tiempo()), equals, hashCode y toString.
 */
public record Proceso(int id, String nombre, int prioridad, double tiempo) {

    /**
     * Constructor compacto con validaciones de dominio.
     */
    public Proceso {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del proceso no puede estar vacío.");
        }
        if (prioridad < 1 || prioridad > 10) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 10.");
        }
        if (tiempo <= 0) {
            throw new IllegalArgumentException("El tiempo de CPU debe ser mayor que 0.");
        }
        if (id < 0) {
            throw new IllegalArgumentException("El ID no puede ser negativo.");
        }
    }

    @Override
    public String toString() {
        return String.format("Proceso{id=%d, nombre='%s', prioridad=%d, tiempo=%.2f ms}",
                id, nombre, prioridad, tiempo);
    }
}
