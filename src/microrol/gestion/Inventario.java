package microrol.gestion;

import java.util.ArrayList;
import microrol.modelo.Producto;

/**
 * Gestiona el inventario de productos de MicroRol Soluciones S.A.
 * Mantiene la lista dinámica de productos en memoria y proporciona
 * métodos para su registro, búsqueda, visualización y actualización de stock.
 */
public class Inventario {
    private ArrayList<Producto> productos;

    /**
     * Constructor que inicializa la lista dinámica de productos.
     */
    public Inventario() {
        this.productos = new ArrayList<>();
    }

    /**
     * Registra un nuevo producto en el inventario previa validación de reglas de negocio:
     * - No permite valores nulos o códigos vacíos.
     * - No permite códigos duplicados.
     * - No permite precios menores o iguales a cero.
     * - No permite cantidades menores o iguales a cero.
     *
     * @param nuevoProducto Producto a registrar.
     * @return true si el producto fue registrado con éxito, false en caso contrario.
     */
    public boolean registrarProducto(Producto nuevoProducto) {
        if (nuevoProducto == null) {
            return false;
        }

        if (nuevoProducto.getCodigo() == null || nuevoProducto.getCodigo().trim().isEmpty()) {
            return false;
        }

        // Validación: Evitar códigos duplicados
        if (consultarProductoPorCodigo(nuevoProducto.getCodigo()) != null) {
            return false;
        }

        // Validación: No permitir precios menores o iguales a cero
        if (nuevoProducto.getPrecio() <= 0) {
            return false;
        }

        // Validación: No permitir cantidades menores o iguales a cero
        if (nuevoProducto.getCantidad() <= 0) {
            return false;
        }

        // Agregar el producto a la lista dinámica
        return this.productos.add(nuevoProducto);
    }

    /**
     * Consulta y busca un producto por su código identificador.
     *
     * @param codigo Código del producto a buscar.
     * @return El objeto Producto encontrado o null si no existe.
     */
    public Producto consultarProductoPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }

        // Recorrido con bucle FOR para buscar coincidencia por código
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Muestra en consola la lista completa de productos registrados en el inventario.
     * Utiliza una estructura repetitiva FOR para recorrer el listado.
     */
    public void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("\n[i] El inventario se encuentra actualmente vacío.");
            return;
        }

        System.out.println("\n==========================================================================");
        System.out.println("                     INVENTARIO ACTUAL DE PRODUCTOS                       ");
        System.out.println("==========================================================================");
        System.out.printf(" %-10s %-25s %-15s %-12s%n", "CÓDIGO", "NOMBRE", "PRECIO UNIT.", "EXISTENCIAS");
        System.out.println("--------------------------------------------------------------------------");

        // Bucle FOR obligatorio para recorrer y mostrar los productos del inventario
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            System.out.printf(" %-10s %-25s $%-14.2f %-12d%n",
                    p.getCodigo(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad());
        }

        System.out.println("==========================================================================");
        System.out.printf(" Total de productos registrados: %d%n%n", productos.size());
    }

    /**
     * Actualiza las existencias de un producto restando la cantidad vendida.
     *
     * @param codigo          Código del producto a actualizar.
     * @param cantidadVendida Unidades vendidas a descontar.
     * @return true si la actualización fue exitosa, false si no existe o stock insuficiente.
     */
    public boolean actualizarStock(String codigo, int cantidadVendida) {
        Producto producto = consultarProductoPorCodigo(codigo);
        if (producto == null || cantidadVendida <= 0) {
            return false;
        }

        if (producto.getCantidad() < cantidadVendida) {
            return false;
        }

        producto.setCantidad(producto.getCantidad() - cantidadVendida);
        return true;
    }

    /**
     * Indica si el inventario carece de productos.
     *
     * @return true si está vacío, false en caso contrario.
     */
    public boolean estaVacio() {
        return productos.isEmpty();
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }
}
