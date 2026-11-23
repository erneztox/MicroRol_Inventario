# MicroRol_Inventario

Sistema integral de gestión de inventarios y facturación interactiva por consola desarrollado en **Java 21** para la empresa ficticia **MicroRol Soluciones S.A.**, como proyecto académico para la asignatura **Programación Orientada a Objetos I**.

---

## 📌 Contexto del Proyecto

La empresa **MicroRol Soluciones S.A.** requiere un sistema en consola para:
1. Registrar productos en el catálogo de inventario con control de existencias.
2. Gestionar ventas con selección de múltiples artículos por transacción.
3. Emitir y almacenar facturas correlativas individuales por cada venta.
4. Consultar en cualquier momento el historial de facturas emitidas y el estado del inventario.

El proyecto está diseñado bajo principios estrictos de **Programación Orientada a Objetos (POO)**, encapsulamiento riguroso de atributos, uso exclusivo de la biblioteca estándar de Java (sin dependencias externas ni bases de datos) y estructuración modular por paquetes.

---

## 🏛️ Arquitectura del Sistema

```
MicroRol_Inventario/
├── .classpath
├── .project
├── .gitignore
├── run_tests.sh
├── README.md
└── src/
    └── microrol/
        ├── modelo/
        │   ├── Producto.java
        │   ├── DetalleVenta.java
        │   └── Factura.java
        ├── gestion/
        │   ├── Inventario.java
        │   └── SistemaVentas.java
        ├── principal/
        │   └── Main.java
        └── test/
            └── TestSuite.java
```

### Paquetes y Responsabilidades

| Paquete | Clase | Descripción |
| :--- | :--- | :--- |
| `microrol.modelo` | `Producto` | Entidad que almacena código, nombre, precio unitario y cantidad en existencia. |
| `microrol.modelo` | `DetalleVenta` | Representa una línea de compra con el producto asociado, cantidad vendida y cálculo automático del subtotal. |
| `microrol.modelo` | `Factura` | Comprobante emitido con número correlativo, fecha/hora (`LocalDateTime`), lista de detalles y cálculo del total. |
| `microrol.gestion` | `Inventario` | Gestiona el `ArrayList<Producto>`, búsqueda por código, visualización y actualización de stock tras ventas. |
| `microrol.gestion` | `SistemaVentas` | Coordina el proceso de venta interactivo, validación de stock disponible y registro en el historial de facturas. |
| `microrol.principal`| `Main` | Punto de entrada (`main`), despliegue del menú y manejo seguro de entradas numéricas por consola. |
| `microrol.test` | `TestSuite` | Suite de pruebas unitarias y de integración automatizadas y reproducibles (sin librerías externas). |

---

## 🔄 Estructuras de Control Obligatorias

El sistema implementa de forma explícita las estructuras de control repetitivas y de salto requeridas:

| Estructura | Archivo y Ubicación | Rol y Justificación Técnica |
| :---: | :--- | :--- |
| **`do-while`** | `Main.java`<br>`(Líneas 23–52)` | Mantiene el menú interactivo activo y garantiza al menos una ejecución hasta que el usuario seleccione la opción `5. Salir`. |
| **`while`** | `SistemaVentas.java`<br>`(Líneas 52–147)` | Controla el proceso de incorporación de productos a la venta, permitiendo agregar múltiples artículos a la misma factura. |
| **`continue`** | `SistemaVentas.java`<br>`(Línea 76)` | Se dispara ante el ingreso de un código de producto inexistente; muestra el error y salta a la siguiente iteración sin interrumpir ni reiniciar la venta. |
| **`break`** | `SistemaVentas.java`<br>`(Línea 145)` | Finaliza el bucle de incorporación de productos cuando el usuario responde negativamente (`N`) a la solicitud de agregar más productos. |
| **`break`** | `SistemaVentas.java`<br>`(Línea 63)` | Permite cancelar voluntariamente la venta si el usuario escribe `CANCELAR`. |
| **`for`** | `Inventario.java`<br>`(Líneas 64–71)` | Recorre la lista de productos para consultar coincidencias por código (insensible a mayúsculas). |
| **`for`** | `Inventario.java`<br>`(Líneas 89–97)` | Recorre e imprime la tabla de productos disponibles en el inventario. |
| **`for`** | `Factura.java`<br>`(Líneas 40–45)` | Recorre los detalles de venta acumulando los subtotales para obtener el total general. |
| **`for`** | `Factura.java`<br>`(Líneas 66–71)` | Recorre e imprime cada línea de detalle (código, producto, cantidad, precio unitario y subtotal). |
| **`for`** | `SistemaVentas.java`<br>`(Líneas 157–162)` | Recorre los detalles de la venta para descontar automáticamente las existencias del inventario. |
| **`for`** | `SistemaVentas.java`<br>`(Líneas 190–195)` | Recorre el historial de facturas para mostrarlas cronológicamente. |

---

## 🛡️ Reglas de Negocio y Validaciones

- **Códigos Únicos**: No se permite registrar productos con códigos existentes (insensible a mayúsculas/minúsculas).
- **Precios Estrictamente Positivos**: Se rechaza cualquier precio menor o igual a cero (`precio <= 0`).
- **Cantidades Estrictamente Positivas**: Se rechaza cualquier cantidad inicial o de compra menor o igual a cero (`cantidad <= 0`).
- **Validación de Existencias**: No se permite concretar ventas que superen el stock real disponible ni reservas acumuladas en la misma venta.
- **Facturas No Vacías**: Si el usuario no agrega productos o cancela la operación, no se genera factura vacía ni se descuenta inventario.
- **Prevención de Excepciones**: Uso de lectura con `scanner.nextLine()` y captura de `NumberFormatException`, evitando cierres abruptos por `InputMismatchException`.

---

## 🧪 Suite de Pruebas Automatizada y Reproducible

El proyecto incluye una suite de pruebas automatizada, determinista e independiente ([`TestSuite.java`](src/microrol/test/TestSuite.java)) que evalúa **20 casos de prueba** (unitarios y de integración con simulación de I/O de consola):

- **Unitarias Modelo**: Creación de productos, modificación con setters, cálculo de subtotales en `DetalleVenta` y cálculo de totales en `Factura`.
- **Unitarias Inventario**: Registro exitoso, rechazo de duplicados, validación de precios y cantidades inválidas, búsqueda insensible a mayúsculas y descuento de stock.
- **Integración Ventas**:
  - Cancelación sin productos sin emisión de factura.
  - Validación del salto `CONTINUE` al ingresar códigos erróneos.
  - Validación de cantidades erróneas y stock excedido.
  - Venta multi-producto finalizada con `BREAK`.
  - Descuento real en inventario y generación correlativa en el historial.

### Ejecución de Pruebas

#### Desde la Terminal (Linux / macOS / Windows con Git Bash)
```bash
./run_tests.sh
```

O manualmente:
```bash
mkdir -p bin
javac -d bin src/microrol/modelo/*.java src/microrol/gestion/*.java src/microrol/principal/*.java src/microrol/test/*.java
java -cp bin microrol.test.TestSuite
```

#### Salida Esperada:
```
==========================================================================
         MICROROL_INVENTARIO - SUITE DE PRUEBAS AUTOMATIZADAS             
==========================================================================
[RUN] Producto - Creación y getters válidos                          [PASSED]
[RUN] Producto - Modificación mediante setters                       [PASSED]
[RUN] DetalleVenta - Cálculo de subtotal                             [PASSED]
[RUN] DetalleVenta - Recálculo al cambiar cantidad                   [PASSED]
[RUN] Factura - Cálculo de total con bucle for                       [PASSED]
[RUN] Factura - Factura con múltiples detalles                       [PASSED]
[RUN] Inventario - Registro exitoso de producto                      [PASSED]
[RUN] Inventario - Rechazo de código duplicado                       [PASSED]
[RUN] Inventario - Rechazo de precio <= 0                            [PASSED]
[RUN] Inventario - Rechazo de cantidad <= 0                          [PASSED]
[RUN] Inventario - Rechazo de código nulo o vacío                    [PASSED]
[RUN] Inventario - Búsqueda de producto por código (case-insensitive) [PASSED]
[RUN] Inventario - Actualización correcta de existencias             [PASSED]
[RUN] Inventario - Rechazo de actualización con stock insuficiente   [PASSED]
[RUN] SistemaVentas - Venta cancelada con inventario vacío           [PASSED]
[RUN] SistemaVentas - Venta cancelada voluntariamente sin generar factura [PASSED]
[RUN] SistemaVentas - Estructura CONTINUE ante código inexistente    [PASSED]
[RUN] SistemaVentas - Validación de cantidades inválidas y stock excedido [PASSED]
[RUN] SistemaVentas - Venta completa multi-producto y finalización con BREAK [PASSED]
[RUN] SistemaVentas - Facturación correlativa e historial de facturas [PASSED]

==========================================================================
                         RESUMEN DE EJECUCIÓN                             
==========================================================================
  Total de pruebas ejecutadas : 20
  Pruebas exitosas            : 20
  Pruebas fallidas            : 0
  Tiempo total de ejecución   : 66 ms
--------------------------------------------------------------------------
  ESTADO FINAL: [PASSED] - ¡TODAS LAS PRUEBAS PASARON SATISFACTORIAMENTE!
==========================================================================
```

---

## 💻 Ejecución del Sistema Principal

### Desde la Terminal
```bash
mkdir -p bin
javac -d bin src/microrol/modelo/*.java src/microrol/gestion/*.java src/microrol/principal/*.java
java -cp bin microrol.principal.Main
```

### Desde Eclipse IDE
1. Abra Eclipse IDE con soporte para **Java 21**.
2. Vaya a `File` > `Open Projects from File System...`
3. Seleccione el directorio raíz del proyecto (`MicroRol_Inventario`).
4. En el explorador de paquetes, navegue a `src/microrol/principal/Main.java`.
5. Clic derecho en `Main.java` > `Run As` > `Java Application`.
6. Para ejecutar las pruebas en Eclipse: Clic derecho en `src/microrol/test/TestSuite.java` > `Run As` > `Java Application`.
