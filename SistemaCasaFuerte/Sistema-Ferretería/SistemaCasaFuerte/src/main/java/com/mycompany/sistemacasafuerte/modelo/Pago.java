package com.mycompany.sistemacasafuerte.modelo;

/**
 * Representa el pago realizado por un cliente para completar
 * una venta, registrando el monto, el método de pago utilizado
 * y la fecha en que se efectuó.
 *
 * @author Eliezer Vilcabana
 */
public class Pago {

    private String idPago;
    private double monto;
    private String metodoPago;
    private String fechaPago;

    public Pago(String idPago, double monto, String metodoPago, String fechaPago) {
        this.idPago = idPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
    }

    public String getIdPago() {
        return idPago;
    }

    public double getMonto() {
        return monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    @Override
    public String toString() {
        return "Pago [" + metodoPago + "] S/ " + monto + " - " + fechaPago;
    }
}
