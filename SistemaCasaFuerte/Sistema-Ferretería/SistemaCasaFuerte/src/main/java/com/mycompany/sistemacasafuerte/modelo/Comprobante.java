package com.mycompany.sistemacasafuerte.modelo;

import com.mycompany.sistemacasafuerte.util.Imprimible;

/**
 * Representa el comprobante de pago (boleta o factura) emitido
 * tras confirmarse el pago de una venta.
 *
 * @author Eliezer Vilcabana
 */
public class Comprobante implements Imprimible {

    private static int contador = 1;

    private String idComprobante;
    private String tipoComprobante;
    private String numero;
    private double montoTotal;
    private String fechaEmision;

    private String clienteNombre;
    private String clienteDni;
    private String clienteDireccion;
    private String vendedorNombre;

    public Comprobante(String idComprobante, String tipoComprobante, double montoTotal,
                        String clienteNombre, String clienteDni, String clienteDireccion,
                        String vendedorNombre) {
        this.idComprobante = idComprobante;
        this.tipoComprobante = tipoComprobante;
        this.montoTotal = montoTotal;
        this.clienteNombre = clienteNombre;
        this.clienteDni = clienteDni;
        this.clienteDireccion = clienteDireccion;
        this.vendedorNombre = vendedorNombre;
        this.numero = generarNumero();
        this.fechaEmision = java.time.LocalDate.now().toString();
    }

    /**
     * Genera un número correlativo simple para el comprobante,
     * con formato tipo B001-000001.
     */
    private String generarNumero() {
        String serie = tipoComprobante.equalsIgnoreCase("FACTURA") ? "F001" : "B001";
        String correlativo = String.format("%06d", contador++);
        return serie + "-" + correlativo;
    }

    /**
     * Imprime en consola el contenido del comprobante, mostrando
     * su tipo, número, fecha de emisión, datos del cliente,
     * vendedor y monto total.
     */
    @Override
    public void imprimir() {
        System.out.println("========================================");
        System.out.println("        FERRETERÍA CASA FUERTE");
        System.out.println("   Av. Chiclayo y pasaje Santa Rosa");
        System.out.println("        Chiclayo - Lambayeque");
        System.out.println("========================================");
        System.out.println("Tipo: " + tipoComprobante);
        System.out.println("Número: " + numero);
        System.out.println("Fecha de emisión: " + fechaEmision);
        System.out.println("----------------------------------------");
        System.out.println("Cliente: " + clienteNombre);
        System.out.println("DNI: " + clienteDni);
        System.out.println("Dirección: " + clienteDireccion);
        System.out.println("Atendido por: " + vendedorNombre);
        System.out.println("----------------------------------------");
        System.out.println("MONTO TOTAL: S/ " + String.format("%.2f", montoTotal));
        System.out.println("========================================");
    }

    public String getIdComprobante() {
        return idComprobante;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public String getNumero() {
        return numero;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getClienteDni() {
        return clienteDni;
    }

    public String getClienteDireccion() {
        return clienteDireccion;
    }

    public String getVendedorNombre() {
        return vendedorNombre;
    }

    @Override
    public String toString() {
        return tipoComprobante + " " + numero + " - S/ " + montoTotal + " (" + fechaEmision + ")";
    }
}
