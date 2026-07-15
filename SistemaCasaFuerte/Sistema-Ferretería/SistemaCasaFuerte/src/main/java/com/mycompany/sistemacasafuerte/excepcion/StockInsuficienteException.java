package com.mycompany.sistemacasafuerte.excepcion;

/**
 * Excepción personalizada que se lanza cuando se intenta vender
 * o descontar una cantidad de stock mayor a la disponible para
 * un producto determinado.
 *
 * @author Eliezer Vilcabana
 */
public class StockInsuficienteException extends Exception {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}