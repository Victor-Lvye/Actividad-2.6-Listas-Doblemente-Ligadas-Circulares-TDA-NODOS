# TDA Lista Circular — Planificador de Procesos (Java Swing)

## Estructura del proyecto

```
tarea6/
├── src/
│   ├── modelo/
│   │   └── Proceso.java          ← record inmutable (entidad de dominio)
│   ├── tda/
│   │   ├── Nodo.java             ← nodo con referencias siguiente/anterior
│   │   └── ListaCircular.java    ← TDA Lista Circular doblemente enlazada
│   └── vista/
│       └── VentanaPrincipal.java ← GUI completa en Java Swing
├── out/                          ← bytecode generado por javac
├── compilar_ejecutar.bat         ← script Windows (doble clic)
└── compilar_ejecutar.ps1         ← script PowerShell
```

## Requisitos

- Java 16 o superior (se usa la construcción `record`)

## Compilar y ejecutar

### Opción A — doble clic en el .bat
```
compilar_ejecutar.bat
```

### Opción B — PowerShell
```powershell
.\compilar_ejecutar.ps1
```

### Opción C — manual
```powershell
javac -d out src/modelo/Proceso.java src/tda/Nodo.java src/tda/ListaCircular.java src/vista/VentanaPrincipal.java
java -cp out vista.VentanaPrincipal
```

## Operaciones implementadas

| Categoría | Operaciones |
|-----------|-------------|
| Inserción | Al principio · Al final · Por posición |
| Remoción  | Al principio · Al final · Por posición |
| Muestra   | Al principio · Al final · Por posición |
| Búsqueda  | Por nombre · Por ID |
| Navegación circular | Siguiente (ultimo → primero) · Anterior (primero → ultimo) |
| Auxiliares | Vaciar lista · Estado del cursor |

## Regla circular obligatoria

- Cursor en **último** nodo + botón **Siguiente** → muestra **primero**
- Cursor en **primer** nodo + botón **Anterior** → muestra **último**

La tabla resalta en color índigo el nodo actualmente apuntado por el cursor.

## Restricciones técnicas cumplidas

- ✅ Entidad modelada con `record Proceso` (Java)
- ✅ Sin uso de `java.util.List`, `ArrayList`, `LinkedList` ni arreglos estáticos para la estructura
- ✅ GUI construida con `javax.swing.*` pura
- ✅ Lógica del TDA totalmente separada de la vista
- ✅ Validaciones con `JOptionPane` para lista vacía, rango inválido, formato incorrecto
