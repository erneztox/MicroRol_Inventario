package microrol.principal;

import java.util.Scanner;
import microrol.gestion.Inventario;
import microrol.gestion.SistemaVentas;
import microrol.modelo.Producto;

/**
 * Clase principal del sistema MicroRol_Inventario.
 * Contiene el método main, el menú interactivo de la consola y la
 * gestión del ciclo principal del programa.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventario inventario = new Inventario();
        SistemaVentas sistemaVentas = new SistemaVentas(inventario);

        int opcion = 0;

        // Estructura repetitiva DO-WHILE obligatoria:
        // Mantiene el menú principal activo hasta que el usuario decida salir (opción 5).
        do {
            mostrarEncabezadoYMenu();
            opcion = leerEntero(scanner, "Seleccione una opción (1-5): ");

            switch (opcion) {
                case 1:
                    registrarNuevoProducto(scanner, inventario);
                    break;
                case 2:
                    inventario.mostrarProductos();
                    break;
                case 3:
                    sistemaVentas.registrarVenta(scanner);
                    break;
                case 4:
                    sistemaVentas.consultarFacturas();
                    break;
                case 5:
                    System.out.println("\n==========================================================================");
                    System.out.println("     Gracias por utilizar el sistema de MicroRol Soluciones S.A.         ");
                    System.out.println("                       ¡Hasta pronto!                                     ");
                    System.out.println("==========================================================================");
                    break;
                default:
                    System.out.println("\n[!] Opción fuera de rango. Por favor ingrese un número del 1 al 5.");
            }

        } while (opcion != 5);

        scanner.close();
    }

    /**
     * Muestra el encabezado de la empresa y las opciones del menú en consola.
     */
    private static void mostrarEncabezadoYMenu() {
        System.out.println("\n==========================================================================");
        System.out.println("                     MICROROL SOLUCIONES S.A.                             ");
        System.out.println("                 SISTEMA DE GESTIÓN DE INVENTARIOS                        ");
        System.out.println("==========================================================================");
        System.out.println("  1. Registrar producto");
        System.out.println("  2. Mostrar inventario");
        System.out.println("  3. Registrar venta");
        System.out.println("  4. Consultar facturas");
        System.out.println("  5. Salir");
        System.out.println("==========================================================================");
    }

    /**
     * Gestiona el registro de un nuevo producto validando:
     * - No permitir códigos duplicados o vacíos.
     * - No permitir precios negativos o iguales a cero.
     * - No permitir cantidades negativas o iguales a cero.
     *
     * @param scanner    Scanner para lectura de datos.
     * @param inventario Instancia del inventario.
     */
    private static void registrarNuevoProducto(Scanner scanner, Inventario inventario) {
        System.out.println("\n--- REGISTRO DE NUEVO PRODUCTO ---");

        System.out.print("Ingrese el código del producto (o 'CANCELAR' para volver): ");
        String codigo = scanner.nextLine().trim();

        if (codigo.equalsIgnoreCase("CANCELAR")) {
            System.out.println("[i] Registro cancelado por el usuario.");
            return;
        }

        if (codigo.isEmpty()) {
            System.out.println("[!] Error: El código del producto no puede estar vacío.");
            return;
        }

        // Validación: Evitar códigos duplicados
        if (inventario.consultarProductoPorCodigo(codigo) != null) {
            System.out.println("[!] Error: Ya existe un producto con el código '" + codigo + "'. No se permiten duplicados.");
            return;
        }

        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("[!] Error: El nombre del producto no puede estar vacío.");
            return;
        }

        // Validación: Precio mayor a cero
        double precio = 0.0;
        while (true) {
            System.out.print("Ingrese el precio unitario del producto: ");
            String entradaPrecio = scanner.nextLine().trim();
            try {
                precio = Double.parseDouble(entradaPrecio);
                if (precio <= 0) {
                    System.out.println("[!] Error: El precio debe ser un número mayor a cero.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Error: Debe ingresar un valor numérico válido para el precio (ej. 12.50).");
            }
        }

        // Validación: Cantidad mayor a cero
        int cantidad = 0;
        while (true) {
            System.out.print("Ingrese la cantidad inicial disponible: ");
            String entradaCantidad = scanner.nextLine().trim();
            try {
                cantidad = Integer.parseInt(entradaCantidad);
                if (cantidad <= 0) {
                    System.out.println("[!] Error: La cantidad debe ser un número entero mayor a cero.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Error: Debe ingresar un número entero válido para la cantidad.");
            }
        }

        // Creación y almacenamiento del producto
        Producto nuevo = new Producto(codigo, nombre, precio, cantidad);
        boolean registrado = inventario.registrarProducto(nuevo);

        if (registrado) {
            System.out.printf("[✓] ¡Producto '%s' (Código: %s) registrado exitosamente!%n", nombre, codigo);
        } else {
            System.out.println("[!] No se pudo registrar el producto. Verifique los datos ingresados.");
        }
    }

    /**
     * Lee un entero por consola evitando errores de tipo InputMismatchException.
     *
     * @param scanner Scanner para captura de datos.
     * @param mensaje Mensaje o prompt mostrado al usuario.
     * @return El número entero leído, o -1 si la entrada es inválida.
     */
    private static int leerEntero(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
