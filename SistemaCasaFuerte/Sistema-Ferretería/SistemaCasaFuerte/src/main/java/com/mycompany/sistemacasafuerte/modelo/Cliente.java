package com.mycompany.sistemacasafuerte.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa al cliente del sistema, quien puede consultar el
 * catálogo de productos, agregar productos a su carrito y
 * confirmar compras dentro de la Ferretería Casa Fuerte.
 *
 * Hereda de Usuario (2do nivel de herencia respecto a Persona)
 * e implementa IPersistible para su almacenamiento en base de datos.
 *
 * @author Eliezer Vilcabana
 */
public class Cliente extends Usuario {

    private String direccion;
    private List<Venta> historialPedidos;

    public Cliente(String nombre, String dni, String telefono, String username,
                    String password, String direccion) {
        super(nombre, dni, telefono, username, password);
        this.direccion = direccion;
        this.historialPedidos = new ArrayList<>();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Venta> getHistorialPedidos() {
        return historialPedidos;
    }

    /**
     * Muestra la información del cliente, incluyendo los datos
     * heredados de Persona y su dirección particular.
     */
    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Dirección: " + direccion);
        System.out.println("Rol: Cliente");
    }

}
