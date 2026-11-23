package microrol.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import microrol.gestion.Inventario;
import microrol.gestion.SistemaVentas;
import microrol.modelo.DetalleVenta;
import microrol.modelo.Factura;
import microrol.modelo.Producto;

/**
 * Suite de pruebas automatizadas y reproducibles para el sistema MicroRol_Inventario.
 * Ejecuta pruebas unitarias y de integración sin requerir dependencias externas.
 */
public class TestSuite {

    private static int pruebasEjecutadas = 0;
    private static int pruebasExitosas = 0;
    private static int pruebasFallidas = 0;
    private static final double DELTA = 0.0001;

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("         MICROROL_INVENTARIO - SUITE DE PRUEBAS AUTOMATIZADAS             ");
        System.out.println("==========================================================================");

        long inicio = System.currentTimeMillis();

        // --- Pruebas Unitarias: microrol.modelo.Producto ---
        ejecutarPrueba("Producto - Creación y getters válidos", TestSuite::testProductoCreacion);
        ejecutarPrueba("Producto - Modificación mediante setters", TestSuite::testProductoSetters);

        // --- Pruebas Unitarias: microrol.modelo.DetalleVenta ---
        ejecutarPrueba("DetalleVenta - Cálculo de subtotal", TestSuite::testDetalleVentaSubtotal);
        ejecutarPrueba("DetalleVenta - Recálculo al cambiar cantidad", TestSuite::testDetalleVentaRecalculo);

        // --- Pruebas Unitarias: microrol.modelo.Factura ---
        ejecutarPrueba("Factura - Cálculo de total con bucle for", TestSuite::testFacturaCalculoTotal);
        ejecutarPrueba("Factura - Factura con múltiples detalles", TestSuite::testFacturaMultiplesDetalles);

        // --- Pruebas Unitarias: microrol.gestion.Inventario ---
        ejecutarPrueba("Inventario - Registro exitoso de producto", TestSuite::testInventarioRegistroExitoso);
        ejecutarPrueba("Inventario - Rechazo de código duplicado", TestSuite::testInventarioRechazoDuplicado);
        ejecutarPrueba("Inventario - Rechazo de precio <= 0", TestSuite::testInventarioRechazoPrecioInvalido);
        ejecutarPrueba("Inventario - Rechazo de cantidad <= 0", TestSuite::testInventarioRechazoCantidadInvalida);
        ejecutarPrueba("Inventario - Rechazo de código nulo o vacío", TestSuite::testInventarioRechazoCodigoVacio);
        ejecutarPrueba("Inventario - Búsqueda de producto por código (case-insensitive)", TestSuite::testInventarioBusquedaPorCodigo);
        ejecutarPrueba("Inventario - Actualización correcta de existencias", TestSuite::testInventarioActualizarStock);
        ejecutarPrueba("Inventario - Rechazo de actualización con stock insuficiente", TestSuite::testInventarioActualizarStockInsuficiente);

        // --- Pruebas de Integración: microrol.gestion.SistemaVentas ---
        ejecutarPrueba("SistemaVentas - Venta cancelada con inventario vacío", TestSuite::testVentaInventarioVacio);
        ejecutarPrueba("SistemaVentas - Venta cancelada voluntariamente sin generar factura", TestSuite::testVentaCanceladaSinFactura);
        ejecutarPrueba("SistemaVentas - Estructura CONTINUE ante código inexistente", TestSuite::testVentaContinueCodigoInexistente);
        ejecutarPrueba("SistemaVentas - Validación de cantidades inválidas y stock excedido", TestSuite::testVentaValidacionCantidades);
        ejecutarPrueba("SistemaVentas - Venta completa multi-producto y finalización con BREAK", TestSuite::testVentaCompletaConBreak);
        ejecutarPrueba("SistemaVentas - Facturación correlativa e historial de facturas", TestSuite::testHistorialFacturacion);

        long fin = System.currentTimeMillis();

        System.out.println("\n==========================================================================");
        System.out.println("                         RESUMEN DE EJECUCIÓN                             ");
        System.out.println("==========================================================================");
        System.out.printf("  Total de pruebas ejecutadas : %d%n", pruebasEjecutadas);
        System.out.printf("  Pruebas exitosas            : %d%n", pruebasExitosas);
        System.out.printf("  Pruebas fallidas            : %d%n", pruebasFallidas);
        System.out.printf("  Tiempo total de ejecución   : %d ms%n", (fin - inicio));
        System.out.println("--------------------------------------------------------------------------");

        if (pruebasFallidas == 0) {
            System.out.println("  ESTADO FINAL: [PASSED] - ¡TODAS LAS PRUEBAS PASARON SATISFACTORIAMENTE!");
            System.out.println("==========================================================================\n");
            System.exit(0);
        } else {
            System.out.println("  ESTADO FINAL: [FAILED] - SE DETECTARON FALLOS EN LA SUITE.");
            System.out.println("==========================================================================\n");
            System.exit(1);
        }
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE PRUEBAS UNITARIAS
    // =========================================================================

    private static void testProductoCreacion() {
        Producto p = new Producto("P001", "Laptop Gamer", 1200.50, 10);
        assertEquals("P001", p.getCodigo(), "El código del producto no coincide");
        assertEquals("Laptop Gamer", p.getNombre(), "El nombre del producto no coincide");
        assertEquals(1200.50, p.getPrecio(), "El precio del producto no coincide");
        assertEquals(10, p.getCantidad(), "La cantidad del producto no coincide");
    }

    private static void testProductoSetters() {
        Producto p = new Producto("P001", "Laptop Gamer", 1200.50, 10);
        p.setNombre("Laptop Profesional");
        p.setPrecio(1350.00);
        p.setCantidad(8);

        assertEquals("Laptop Profesional", p.getNombre(), "Fallo al actualizar nombre");
        assertEquals(1350.00, p.getPrecio(), "Fallo al actualizar precio");
        assertEquals(8, p.getCantidad(), "Fallo al actualizar cantidad");
    }

    private static void testDetalleVentaSubtotal() {
        Producto p = new Producto("P002", "Mouse Inalámbrico", 25.00, 30);
        DetalleVenta dv = new DetalleVenta(p, 4);

        assertEquals(100.00, dv.getSubtotal(), "El subtotal debe ser 25.00 * 4 = 100.00");
    }

    private static void testDetalleVentaRecalculo() {
        Producto p = new Producto("P002", "Mouse Inalámbrico", 25.00, 30);
        DetalleVenta dv = new DetalleVenta(p, 2);
        assertEquals(50.00, dv.getSubtotal(), "Subtotal inicial incorrecto");

        dv.setCantidad(5);
        assertEquals(125.00, dv.getSubtotal(), "El subtotal no se recalculó correctamente al modificar cantidad");
    }

    private static void testFacturaCalculoTotal() {
        Producto p1 = new Producto("P01", "Monitor 24", 150.00, 10);
        Producto p2 = new Producto("P02", "Teclado Mecánico", 50.00, 20);

        ArrayList<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(new DetalleVenta(p1, 2)); // Subtotal: 300.00
        detalles.add(new DetalleVenta(p2, 3)); // Subtotal: 150.00

        Factura factura = new Factura(1, LocalDateTime.now(), detalles);
        assertEquals(450.00, factura.getTotal(), "El total de la factura debe ser 300.00 + 150.00 = 450.00");
    }

    private static void testFacturaMultiplesDetalles() {
        Producto p1 = new Producto("A01", "Cable HDMI", 10.00, 50);
        Producto p2 = new Producto("A02", "Adaptador USB", 15.50, 40);
        Producto p3 = new Producto("A03", "Memoria USB", 20.00, 30);

        ArrayList<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(new DetalleVenta(p1, 1)); // 10.00
        detalles.add(new DetalleVenta(p2, 2)); // 31.00
        detalles.add(new DetalleVenta(p3, 3)); // 60.00

        Factura factura = new Factura(101, LocalDateTime.now(), detalles);
        assertEquals(101.00, factura.getTotal(), "Total de factura múltiple incorrecto");
        assertEquals(3, factura.getDetalles().size(), "La cantidad de detalles en factura no coincide");
    }

    private static void testInventarioRegistroExitoso() {
        Inventario inv = new Inventario();
        boolean res = inv.registrarProducto(new Producto("P01", "Impresora", 200.00, 5));
        assertTrue(res, "El producto válido debió registrarse");
        assertEquals(1, inv.getProductos().size(), "El tamaño del inventario debe ser 1");
    }

    private static void testInventarioRechazoDuplicado() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Impresora", 200.00, 5));

        boolean resDuplicado = inv.registrarProducto(new Producto("P01", "Impresora Láser", 300.00, 2));
        assertFalse(resDuplicado, "No debe permitirse registrar un producto con código duplicado");
        assertEquals(1, inv.getProductos().size(), "El inventario no debe haber aumentado de tamaño");
    }

    private static void testInventarioRechazoPrecioInvalido() {
        Inventario inv = new Inventario();
        boolean resCero = inv.registrarProducto(new Producto("P01", "Producto Cero", 0.0, 5));
        boolean resNegativo = inv.registrarProducto(new Producto("P02", "Producto Negativo", -15.0, 5));

        assertFalse(resCero, "No debe permitirse precio igual a 0");
        assertFalse(resNegativo, "No debe permitirse precio negativo");
        assertEquals(0, inv.getProductos().size(), "El inventario debe seguir vacío");
    }

    private static void testInventarioRechazoCantidadInvalida() {
        Inventario inv = new Inventario();
        boolean resCero = inv.registrarProducto(new Producto("P01", "Stock Cero", 50.0, 0));
        boolean resNegativo = inv.registrarProducto(new Producto("P02", "Stock Negativo", 50.0, -10));

        assertFalse(resCero, "No debe permitirse cantidad inicial igual a 0");
        assertFalse(resNegativo, "No debe permitirse cantidad inicial negativa");
        assertEquals(0, inv.getProductos().size(), "El inventario debe seguir vacío");
    }

    private static void testInventarioRechazoCodigoVacio() {
        Inventario inv = new Inventario();
        assertFalse(inv.registrarProducto(new Producto("", "Sin Código", 50.0, 5)), "No permitir código vacío");
        assertFalse(inv.registrarProducto(new Producto("   ", "Espacios", 50.0, 5)), "No permitir código con solo espacios");
        assertFalse(inv.registrarProducto(new Producto(null, "Nulo", 50.0, 5)), "No permitir código nulo");
    }

    private static void testInventarioBusquedaPorCodigo() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("PROD-100", "Servidor", 5000.00, 2));

        Producto encontrado = inv.consultarProductoPorCodigo("prod-100");
        assertNotNull(encontrado, "La búsqueda debe ser insensible a mayúsculas y minúsculas");
        assertEquals("Servidor", encontrado.getNombre(), "El nombre del producto encontrado no coincide");

        Producto noExiste = inv.consultarProductoPorCodigo("NO_EXISTE");
        assertNull(noExiste, "Producto inexistente debe retornar null");
    }

    private static void testInventarioActualizarStock() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Router", 80.00, 15));

        boolean res = inv.actualizarStock("P01", 5);
        assertTrue(res, "La actualización de stock debió ser exitosa");

        Producto p = inv.consultarProductoPorCodigo("P01");
        assertEquals(10, p.getCantidad(), "El stock restante debe ser 10");
    }

    private static void testInventarioActualizarStockInsuficiente() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Router", 80.00, 5));

        boolean resExceso = inv.actualizarStock("P01", 10);
        assertFalse(resExceso, "No debe permitirse descontar más stock del disponible");

        Producto p = inv.consultarProductoPorCodigo("P01");
        assertEquals(5, p.getCantidad(), "El stock debió mantenerse intacto en 5");
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE PRUEBAS DE INTEGRACIÓN: SISTEMA DE VENTAS
    // =========================================================================

    private static void testVentaInventarioVacio() {
        Inventario inv = new Inventario();
        SistemaVentas sv = new SistemaVentas(inv);

        ByteArrayOutputStream salida = capturarSalidaConsola(() -> {
            sv.registrarVenta(new java.util.Scanner(""));
        });

        assertEquals(0, sv.getHistorialFacturas().size(), "No debe generarse factura si el inventario está vacío");
        assertTrue(salida.toString().contains("No hay productos registrados"),
                "Debe informar que el inventario está vacío");
    }

    private static void testVentaCanceladaSinFactura() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Disco SSD", 90.00, 10));
        SistemaVentas sv = new SistemaVentas(inv);

        // Entrada simulada: usuario escribe CANCELAR inmediatamente
        String entradaSimulada = "CANCELAR\n";
        java.util.Scanner scanner = new java.util.Scanner(new ByteArrayInputStream(entradaSimulada.getBytes(StandardCharsets.UTF_8)));

        capturarSalidaConsola(() -> sv.registrarVenta(scanner));

        assertEquals(0, sv.getHistorialFacturas().size(), "No debe generarse factura si la venta fue cancelada sin productos");
        assertEquals(10, inv.consultarProductoPorCodigo("P01").getCantidad(), "El inventario no debe modificarse");
    }

    private static void testVentaContinueCodigoInexistente() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Disco SSD", 90.00, 10));
        SistemaVentas sv = new SistemaVentas(inv);

        // Flujo simulado:
        // 1. Ingresa código erróneo: "CODIGO_FALSO" -> Ejecuta CONTINUE y vuelve a solicitar
        // 2. Ingresa código válido: "P01"
        // 3. Cantidad: "2"
        // 4. ¿Agregar otro producto? "N" -> Ejecuta BREAK
        String entradaSimulada = "CODIGO_FALSO\nP01\n2\nN\n";
        java.util.Scanner scanner = new java.util.Scanner(new ByteArrayInputStream(entradaSimulada.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream salida = capturarSalidaConsola(() -> sv.registrarVenta(scanner));

        assertTrue(salida.toString().contains("no existe en el inventario"),
                "Debe mostrarse el mensaje de error del CONTINUE ante código inexistente");
        assertEquals(1, sv.getHistorialFacturas().size(), "La factura debió generarse exitosamente a pesar del error inicial");
        assertEquals(8, inv.consultarProductoPorCodigo("P01").getCantidad(), "El stock debe haberse actualizado a 8");
    }

    private static void testVentaValidacionCantidades() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Memoria RAM", 45.00, 5));
        SistemaVentas sv = new SistemaVentas(inv);

        // Flujo simulado:
        // 1. Código: "P01"
        // 2. Cantidad negativa: "-2" (rechazado)
        // 3. Cantidad cero: "0" (rechazado)
        // 4. Cantidad no numérica: "abc" (rechazado)
        // 5. Cantidad superior al stock: "20" (rechazado)
        // 6. Cantidad válida: "3"
        // 7. ¿Agregar otro producto? "N"
        String entradaSimulada = "P01\n-2\n0\nabc\n20\n3\nN\n";
        java.util.Scanner scanner = new java.util.Scanner(new ByteArrayInputStream(entradaSimulada.getBytes(StandardCharsets.UTF_8)));

        capturarSalidaConsola(() -> sv.registrarVenta(scanner));

        assertEquals(1, sv.getHistorialFacturas().size(), "Debe generarse una factura válida");
        Factura f = sv.getHistorialFacturas().get(0);
        assertEquals(135.00, f.getTotal(), "Total de 3 * 45.00 = 135.00");
        assertEquals(2, inv.consultarProductoPorCodigo("P01").getCantidad(), "Stock restante debe ser 5 - 3 = 2");
    }

    private static void testVentaCompletaConBreak() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Tablet 10", 300.00, 10));
        inv.registrarProducto(new Producto("P02", "Funda Protectora", 25.00, 20));
        SistemaVentas sv = new SistemaVentas(inv);

        // Flujo de venta de múltiples productos:
        // 1. Producto 1: P01, cantidad: 2 (Subtotal: 600.00)
        // 2. Agregar otro: S
        // 3. Producto 2: P02, cantidad: 4 (Subtotal: 100.00)
        // 4. Agregar otro: N -> BREAK
        String entradaSimulada = "P01\n2\nS\nP02\n4\nN\n";
        java.util.Scanner scanner = new java.util.Scanner(new ByteArrayInputStream(entradaSimulada.getBytes(StandardCharsets.UTF_8)));

        capturarSalidaConsola(() -> sv.registrarVenta(scanner));

        assertEquals(1, sv.getHistorialFacturas().size(), "Debe existir exactamente 1 factura");
        Factura factura = sv.getHistorialFacturas().get(0);
        assertEquals(700.00, factura.getTotal(), "Total debe ser 600.00 + 100.00 = 700.00");
        assertEquals(2, factura.getDetalles().size(), "La factura debe poseer 2 detalles");

        // Verificar descuento de stock en ambos productos
        assertEquals(8, inv.consultarProductoPorCodigo("P01").getCantidad(), "Stock de Tablet debe ser 8");
        assertEquals(16, inv.consultarProductoPorCodigo("P02").getCantidad(), "Stock de Funda debe ser 16");
    }

    private static void testHistorialFacturacion() {
        Inventario inv = new Inventario();
        inv.registrarProducto(new Producto("P01", "Auriculares", 35.00, 50));
        SistemaVentas sv = new SistemaVentas(inv);

        // Venta 1
        String entrada1 = "P01\n2\nN\n";
        capturarSalidaConsola(() -> sv.registrarVenta(new java.util.Scanner(new ByteArrayInputStream(entrada1.getBytes(StandardCharsets.UTF_8)))));

        // Venta 2
        String entrada2 = "P01\n3\nN\n";
        capturarSalidaConsola(() -> sv.registrarVenta(new java.util.Scanner(new ByteArrayInputStream(entrada2.getBytes(StandardCharsets.UTF_8)))));

        assertEquals(2, sv.getHistorialFacturas().size(), "Deben haberse registrado 2 facturas");
        assertEquals(1, sv.getHistorialFacturas().get(0).getNumeroFactura(), "La factura 1 debe tener correlativo 1");
        assertEquals(2, sv.getHistorialFacturas().get(1).getNumeroFactura(), "La factura 2 debe tener correlativo 2");
        assertEquals(45, inv.consultarProductoPorCodigo("P01").getCantidad(), "Stock restante debe ser 50 - 2 - 3 = 45");
    }

    // =========================================================================
    // UTILIDADES DE LA SUITE DE PRUEBAS
    // =========================================================================

    private static void ejecutarPrueba(String nombre, Runnable prueba) {
        pruebasEjecutadas++;
        System.out.printf("[RUN] %-62s ", nombre);
        try {
            prueba.run();
            pruebasExitosas++;
            System.out.println("[PASSED]");
        } catch (Throwable t) {
            pruebasFallidas++;
            System.out.println("[FAILED]");
            System.out.println("      >>> Error: " + t.getMessage());
        }
    }

    private static ByteArrayOutputStream capturarSalidaConsola(Runnable accion) {
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
            System.setOut(ps);
            accion.run();
        } finally {
            System.setOut(salidaOriginal);
        }
        return baos;
    }

    private static void assertEquals(String esperado, String actual, String mensaje) {
        if (esperado == null && actual == null) return;
        if (esperado != null && esperado.equals(actual)) return;
        throw new AssertionError(mensaje + " | Esperado: '" + esperado + "', Actual: '" + actual + "'");
    }

    private static void assertEquals(int esperado, int actual, String mensaje) {
        if (esperado != actual) {
            throw new AssertionError(mensaje + " | Esperado: " + esperado + ", Actual: " + actual);
        }
    }

    private static void assertEquals(double esperado, double actual, String mensaje) {
        if (Math.abs(esperado - actual) > DELTA) {
            throw new AssertionError(mensaje + " | Esperado: " + esperado + ", Actual: " + actual);
        }
    }

    private static void assertTrue(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje + " | Se esperaba true pero se obtuvo false");
        }
    }

    private static void assertFalse(boolean condicion, String mensaje) {
        if (condicion) {
            throw new AssertionError(mensaje + " | Se esperaba false pero se obtuvo true");
        }
    }

    private static void assertNotNull(Object obj, String mensaje) {
        if (obj == null) {
            throw new AssertionError(mensaje + " | Se esperaba un objeto no nulo");
        }
    }

    private static void assertNull(Object obj, String mensaje) {
        if (obj != null) {
            throw new AssertionError(mensaje + " | Se esperaba null pero se obtuvo: " + obj);
        }
    }
}
