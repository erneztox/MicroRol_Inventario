package microrol.modelo;

/**
 * Representa una línea o detalle dentro de una venta y su respectiva factura.
 * Contiene el producto vendido, la cantidad adquirida y el subtotal calculado.
 */
public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double subtotal;

    /**
     * Constructor que inicializa el producto y la cantidad,
     * calculando automáticamente el subtotal correspondiente.
     *
     * @param producto Producto vendido.
     * @param cantidad Cantidad de unidades vendidas.
     */
    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    /**
     * Calcula y retorna el subtotal de la línea de venta.
     *
     * @return El subtotal multiplicando cantidad por precio unitario del producto.
     */
    public double calcularSubtotal() {
        if (producto != null) {
            return this.cantidad * this.producto.getPrecio();
        }
        return 0.0;
    }

    // Getters y Setters
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-22s %-8d $%-11.2f $%-10.2f",
                producto.getCodigo(),
                producto.getNombre(),
                cantidad,
                producto.getPrecio(),
                subtotal);
    }
}
