package com.mycompany.sistemacasafuerte.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una venta confirmada dentro del sistema, agrupando
 * al cliente, al vendedor, los productos comprados (mediante
 * DetallePedido) y el estado de entrega del pedido.
 *
 * Implementa IPersistible para su almacenamiento en base de datos.
 *
 * @author Eliezer Vilcabana
 */
public class Venta implements IPersistible {

    private String idPedido;
    private Cliente cliente;
    private Vendedor vendedor;
    private List<DetallePedido> detalles;
    private double total;
    private String estadoEntrega;

    public Venta(String idPedido, Cliente cliente, Vendedor vendedor) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.detalles = new ArrayList<>();
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

    public String getEstadoEntrega() {
        return estadoEntrega;
    }

    /**
     * Actualiza el estado de entrega del pedido.
     *
     * @param estado nuevo estado: PENDIENTE, EN_CAMINO o ENTREGADO
     */
    public void setEstadoEntrega(String estado) {
        this.estadoEntrega = estado;
    }

    /**
     * Muestra un resumen completo de la venta, incluyendo cliente,
     * vendedor, productos comprados, total y estado de entrega.
     */
    public void mostrarResumen() {
        System.out.println("=== Resumen de Venta " + idPedido + " ===");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Vendedor: " + vendedor.getNombre());
        for (DetallePedido d : detalles) {
            System.out.println(" - " + d.toString());
        }
        System.out.println("Total: S/ " + total);
        System.out.println("Estado de entrega: " + estadoEntrega);
    }

    @Override
    public void guardar() {
        System.out.println("Guardando venta " + idPedido + " en la base de datos...");
    }

    @Override
    public void actualizar() {
        System.out.println("Actualizando venta " + idPedido + "...");
    }

    @Override
    public void eliminar() {
        System.out.println("Eliminando venta " + idPedido + " del sistema...");
    }
}