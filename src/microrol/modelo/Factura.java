package microrol.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Representa una factura de venta emitida por MicroRol Soluciones S.A.
 * Contiene el número correlativo, la fecha/hora de emisión, la lista
 * de detalles y el total calculado.
 */
public class Factura {
    private int numeroFactura;
    private LocalDateTime fechaEmision;
    private ArrayList<DetalleVenta> detalles;
    private double total;

    /**
     * Constructor para inicializar una factura con sus detalles.
     * Calcula automáticamente el total a partir de los detalles incluidos.
     *
     * @param numeroFactura Número correlativo de la factura.
     * @param fechaEmision  Fecha y hora de emisión.
     * @param detalles      Lista de detalles de la venta efectuada.
     */
    public Factura(int numeroFactura, LocalDateTime fechaEmision, ArrayList<DetalleVenta> detalles) {
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.detalles = detalles;
        this.total = calcularTotal();
    }

    /**
     * Calcula la sumatoria de los subtotales de cada detalle utilizando
     * una estructura repetitiva FOR.
     *
     * @return El importe total de la factura.
     */
    public double calcularTotal() {
        double acumulador = 0.0;
        // Bucle FOR para recorrer los detalles de la factura y sumar los subtotales
        for (int i = 0; i < detalles.size(); i++) {
            acumulador += detalles.get(i).getSubtotal();
        }
        this.total = acumulador;
        return this.total;
    }

    /**
     * Muestra en consola la factura formateada de manera clara y ordenada,
     * recorriendo sus detalles mediante una estructura repetitiva FOR.
     */
    public void mostrarFactura() {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("\n==========================================================================");
        System.out.println("                        MICROROL SOLUCIONES S.A.                          ");
        System.out.println("                             FACTURA DE VENTA                             ");
        System.out.println("==========================================================================");
        System.out.printf(" Factura N° : %06d%n", numeroFactura);
        System.out.printf(" Fecha      : %s%n", fechaEmision.format(formateador));
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf(" %-10s %-22s %-8s %-12s %-10s%n", "CÓDIGO", "PRODUCTO", "CANT.", "P. UNITARIO", "SUBTOTAL");
        System.out.println("--------------------------------------------------------------------------");

        // Bucle FOR para recorrer y mostrar los productos, cantidades y subtotales
        for (int i = 0; i < detalles.size(); i++) {
            DetalleVenta detalle = detalles.get(i);
            System.out.println(" " + detalle.toString());
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf(" %-54s TOTAL: $%.2f%n", "", this.total);
        System.out.println("==========================================================================\n");
    }

    // Getters
    public int getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public ArrayList<DetalleVenta> getDetalles() {
        return detalles;
    }

    public double getTotal() {
        return total;
    }
}
