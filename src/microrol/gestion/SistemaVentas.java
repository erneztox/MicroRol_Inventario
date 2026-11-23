package microrol.gestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import microrol.modelo.DetalleVenta;
import microrol.modelo.Factura;
import microrol.modelo.Producto;

/**
 * Controla el proceso de ventas, la creación de detalles de venta,
 * la emisión de facturas y el mantenimiento del historial de facturación.
 */
public class SistemaVentas {
    private Inventario inventario;
    private ArrayList<Factura> historialFacturas;
    private int contadorFacturas;

    /**
     * Constructor que vincula el inventario y prepara el historial de facturas.
     *
     * @param inventario Instancia del inventario del sistema.
     */
    public SistemaVentas(Inventario inventario) {
        this.inventario = inventario;
        this.historialFacturas = new ArrayList<>();
        this.contadorFacturas = 1;
    }

    /**
     * Registra una venta completa interactuando con el usuario por consola.
     * Estructuras de control obligatorias implementadas:
     * - WHILE: Controla la incorporación sucesiva de productos a la venta.
     * - CONTINUE: Se ejecuta ante un código inexistente para reintentar sin interrumpir.
     * - BREAK: Se ejecuta cuando el usuario decide finalizar la incorporación de productos.
     * - FOR: Se utiliza para actualizar existencias y recorrer detalles.
     *
     * @param scanner Objeto Scanner para la captura de entradas.
     */
    public void registrarVenta(Scanner scanner) {
        if (inventario.estaVacio()) {
            System.out.println("\n[!] No es posible realizar ventas: No hay productos registrados en el inventario.");
            return;
        }

        // Paso 1: Mostrar los productos disponibles
        System.out.println("\n--- PRODUCTOS DISPONIBLES EN INVENTARIO ---");
        inventario.mostrarProductos();

        ArrayList<DetalleVenta> detallesVenta = new ArrayList<>();

        System.out.println("==========================================================================");
        System.out.println("                        REGISTRO DE NUEVA VENTA                           ");
        System.out.println("==========================================================================");

        // Estructura repetitiva WHILE obligatoria para incorporar productos a la venta
        while (true) {
            // Paso 2: Solicitar el código del producto que desea comprar
            System.out.print("\nIngrese el código del producto a comprar (o escriba 'CANCELAR' para salir): ");
            String codigo = scanner.nextLine().trim();

            if (codigo.equalsIgnoreCase("CANCELAR")) {
                if (detallesVenta.isEmpty()) {
                    System.out.println("[i] Registro de venta cancelado. No se generó ninguna factura.");
                    return;
                } else {
                    System.out.println("[i] Finalizando la incorporación de productos a la factura...");
                    break; // BREAK para terminar la incorporación de productos
                }
            }

            if (codigo.isEmpty()) {
                System.out.println("[!] El código no puede estar en blanco.");
                continue;
            }

            // Paso 3: Verificar que el producto exista
            Producto producto = inventario.consultarProductoPorCodigo(codigo);
            if (producto == null) {
                System.out.println("[!] Error: El producto con código '" + codigo + "' no existe en el inventario.");
                // Estructura CONTINUE obligatoria: vuelve a solicitar un código sin interrumpir la operación
                continue;
            }

            // Verificar existencias considerando las unidades ya seleccionadas en esta misma venta
            int cantidadYaEnVenta = 0;
            int indiceDetalleExistente = -1;
            for (int i = 0; i < detallesVenta.size(); i++) {
                if (detallesVenta.get(i).getProducto().getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                    cantidadYaEnVenta = detallesVenta.get(i).getCantidad();
                    indiceDetalleExistente = i;
                    break;
                }
            }

            int stockDisponibleReal = producto.getCantidad() - cantidadYaEnVenta;
            if (stockDisponibleReal <= 0) {
                System.out.println("[!] Lo sentimos, no queda stock disponible para el producto '"
                        + producto.getNombre() + "' (todo el inventario ya fue asignado a esta venta).");
                continue;
            }

            // Paso 4: Solicitar la cantidad y Paso 5: Comprobar existencias suficientes
            int cantidadDeseada = 0;
            boolean cantidadValida = false;
            while (!cantidadValida) {
                System.out.printf("Ingrese la cantidad a comprar de '%s' (Disponibles: %d): ",
                        producto.getNombre(), stockDisponibleReal);
                String entrada = scanner.nextLine().trim();
                try {
                    cantidadDeseada = Integer.parseInt(entrada);
                    if (cantidadDeseada <= 0) {
                        System.out.println("[!] Error: La cantidad debe ser un número entero mayor a cero.");
                    } else if (cantidadDeseada > stockDisponibleReal) {
                        System.out.printf("[!] Error: No hay suficiente stock. Solicitado: %d | Disponible: %d%n",
                                cantidadDeseada, stockDisponibleReal);
                    } else {
                        cantidadValida = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Error: Entrada no válida. Ingrese un valor numérico entero.");
                }
            }

            // Paso 6: Agregar el producto y su cantidad a los detalles de la venta
            if (indiceDetalleExistente >= 0) {
                DetalleVenta existente = detallesVenta.get(indiceDetalleExistente);
                existente.setCantidad(existente.getCantidad() + cantidadDeseada);
            } else {
                detallesVenta.add(new DetalleVenta(producto, cantidadDeseada));
            }

            System.out.printf("[+] Se agregaron %d unidades de '%s' a la venta (Subtotal: $%.2f).%n",
                    cantidadDeseada, producto.getNombre(), (cantidadDeseada * producto.getPrecio()));

            // Paso 7: Permitir agregar más productos a la misma venta
            System.out.print("\n¿Desea agregar otro producto a esta venta? (S/N): ");
            String respuesta = scanner.nextLine().trim();
            while (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N")
                    && !respuesta.equalsIgnoreCase("SI") && !respuesta.equalsIgnoreCase("NO")) {
                System.out.print("Por favor, ingrese 'S' (Sí) o 'N' (No): ");
                respuesta = scanner.nextLine().trim();
            }

            if (respuesta.equalsIgnoreCase("N") || respuesta.equalsIgnoreCase("NO")) {
                // Estructura BREAK obligatoria: finaliza el proceso de incorporación de productos
                break;
            }
        }

        // Validación: No generar facturas vacías
        if (detallesVenta.isEmpty()) {
            System.out.println("\n[!] No se agregaron productos. No se generó ninguna factura.");
            return;
        }

        // Paso 9: Descontar las unidades vendidas del inventario
        // Bucle FOR obligatorio para recorrer los detalles y actualizar existencias
        for (int i = 0; i < detallesVenta.size(); i++) {
            DetalleVenta d = detallesVenta.get(i);
            inventario.actualizarStock(d.getProducto().getCodigo(), d.getCantidad());
        }

        // Paso 8: Generar una factura individual con todos los datos requeridos
        Factura nuevaFactura = new Factura(contadorFacturas++, LocalDateTime.now(), detallesVenta);

        // Paso 10: Guardar la factura en el historial
        historialFacturas.add(nuevaFactura);

        System.out.println("\n[✓] ¡Venta registrada y procesada exitosamente!");
        nuevaFactura.mostrarFactura();
    }

    /**
     * Muestra el historial completo de facturas emitidas en el sistema.
     * Utiliza una estructura repetitiva FOR para recorrer el listado de facturas.
     */
    public void consultarFacturas() {
        if (historialFacturas.isEmpty()) {
            System.out.println("\n[i] No hay facturas emitidas en el historial del sistema.");
            return;
        }

        System.out.println("\n==========================================================================");
        System.out.println("                 HISTORIAL DE FACTURAS EMITIDAS                           ");
        System.out.println("==========================================================================");
        System.out.printf(" Total de facturas emitidas: %d%n", historialFacturas.size());

        // Bucle FOR para recorrer y mostrar cada factura en el historial
        for (int i = 0; i < historialFacturas.size(); i++) {
            Factura factura = historialFacturas.get(i);
            factura.mostrarFactura();
        }
    }

    public ArrayList<Factura> getHistorialFacturas() {
        return historialFacturas;
    }
}
