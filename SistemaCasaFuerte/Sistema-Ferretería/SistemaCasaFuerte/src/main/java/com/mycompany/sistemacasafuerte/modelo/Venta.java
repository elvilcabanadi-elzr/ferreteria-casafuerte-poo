package com.mycompany.sistemacasafuerte.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una venta dentro del sistema, agrupando al cliente,
 * al vendedor, los productos comprados (mediante DetallePedido),
 * el pago realizado, el comprobante emitido y el estado de entrega
 * del pedido.
 *
 * El atributo "estado" refleja la etapa del proceso comercial:
 * COTIZACION (recién creada), CONFIRMADA (cliente confirmó la compra)
 * o PAGADA (ya se registró el pago y se emitió el comprobante).
 *
 * @author Eliezer Vilcabana
 */
public class Venta {

    private String idPedido;
    private Cliente cliente;
    private Vendedor vendedor;
    private List<DetallePedido> detalles;
    private double total;
    private String estado;
    private String estadoEntrega;
    private Pago pago;
    private Comprobante comprobante;

    public Venta(String idPedido, Cliente cliente, Vendedor vendedor) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.detalles = new ArrayList<>();
        this.estado = "COTIZACION";
        this.estadoEntrega = "PENDIENTE";
        this.total = 0.0;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    /**
     * Agrega una línea de detalle a la venta y recalcula el total.
     *
     * @param detalle línea de detalle a agregar
     */
    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        calcularTotal();
    }

    /**
     * Calcula el total de la venta sumando los subtotales de
     * cada línea de detalle.
     *
     * @return el total calculado de la venta
     */
    public double calcularTotal() {
        total = 0.0;
        for (DetallePedido d : detalles) {
            total += d.getSubtotal();
        }
        return total;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    /**
     * Confirma la venta, pasando su estado de COTIZACION a CONFIRMADA.
     * Solo se puede confirmar si tiene al menos un producto en el detalle.
     */
    public void confirmar() {
        if (detalles.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar una venta sin productos.");
        }
        this.estado = "CONFIRMADA";
    }

    /**
     * Registra el pago de la venta y actualiza su estado a PAGADA.
     * Solo se puede pagar una venta que ya fue confirmada.
     *
     * @param pago objeto Pago con el monto y método de pago
     */
    public void registrarPago(Pago pago) {
        if (!estado.equals("CONFIRMADA")) {
            throw new IllegalStateException("La venta debe estar confirmada antes de registrar el pago.");
        }
        this.pago = pago;
        this.estado = "PAGADA";
    }

    public Pago getPago() {
        return pago;
    }

    /**
     * Genera el comprobante de pago asociado a la venta. Solo se
     * puede generar si la venta ya fue pagada.
     *
     * @param tipoComprobante tipo de comprobante ("BOLETA" o "FACTURA")
     * @return el comprobante generado
     */
    public Comprobante generarComprobante(String tipoComprobante) {
        if (!estado.equals("PAGADA")) {
            throw new IllegalStateException("La venta debe estar pagada antes de generar el comprobante.");
        }
        this.comprobante = new Comprobante(idPedido, tipoComprobante, total,
                cliente.getNombre(), cliente.getDni(), cliente.getDireccion(),
                vendedor.getNombre());
        return comprobante;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public String getEstadoEntrega() {
        return estadoEntrega;
    }

    /**
     * Actualiza el estado de entrega del pedido.
     *
     * @param estadoEntrega nuevo estado: PENDIENTE, EN_CAMINO o ENTREGADO
     */
    public void setEstadoEntrega(String estadoEntrega) {
        this.estadoEntrega = estadoEntrega;
    }

    /**
     * Muestra un resumen completo de la venta, incluyendo cliente,
     * vendedor, productos comprados, total, estado del proceso y
     * estado de entrega.
     */
    public void mostrarResumen() {
        System.out.println("=== Resumen de Venta " + idPedido + " ===");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Vendedor: " + vendedor.getNombre());
        for (DetallePedido d : detalles) {
            System.out.println(" - " + d.toString());
        }
        System.out.println("Total: S/ " + total);
        System.out.println("Estado de venta: " + estado);
        System.out.println("Estado de entrega: " + estadoEntrega);
    }
}