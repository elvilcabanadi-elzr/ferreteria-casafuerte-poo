package com.mycompany.sistemacasafuerte.modelo;

/**
 * Representa una línea de detalle dentro de una Venta, vinculando
 * un producto específico con la cantidad solicitada y su subtotal
 * correspondiente.
 *
 * @author Eliezer Vilcabana
 */
public class DetallePedido {

    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetallePedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        calcularSubtotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Calcula el subtotal de esta línea multiplicando el precio
     * del producto por la cantidad solicitada.
     */
    public void calcularSubtotal() {
        if (producto != null) {
            this.subtotal = producto.getPrecio() * cantidad;
        }
    }

    @Override
    public String toString() {
        return producto.getNombre() + " x" + cantidad + " = S/ " + subtotal;
    }
}