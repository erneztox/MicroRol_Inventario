package microrol.modelo;

/**
 * Representa un producto individual dentro del catálogo e inventario
 * de MicroRol Soluciones S.A.
 */
public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;

    /**
     * Constructor para inicializar los datos de un producto.
     *
     * @param codigo   Código único identificador del producto.
     * @param nombre   Nombre o descripción del producto.
     * @param precio   Precio unitario del producto (debe ser mayor a 0).
     * @param cantidad Cantidad disponible en existencias (debe ser mayor a 0).
     */
    public Producto(String codigo, String nombre, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Métodos Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return String.format("Código: %-8s | Nombre: %-20s | Precio: $%-8.2f | Stock: %d",
                codigo, nombre, precio, cantidad);
    }
}
