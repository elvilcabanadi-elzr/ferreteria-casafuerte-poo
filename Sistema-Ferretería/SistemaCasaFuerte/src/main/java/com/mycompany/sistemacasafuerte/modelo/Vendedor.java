package com.mycompany.sistemacasafuerte.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa al vendedor del sistema, encargado de registrar
 * productos, gestionar el inventario y procesar los pedidos
 * generados por los clientes.
 *
 * Hereda de Usuario (2do nivel de herencia respecto a Persona)
 * e implementa IPersistible para su almacenamiento en base de datos.
 * Es además la clase padre de Administrador (3er nivel de herencia).
 *
 * @author Eliezer Vilcabana
 */
public class Vendedor extends Usuario implements IPersistible {

    private String codigoEmpleado;
    private List<Venta> ventasRealizadas;

    public Vendedor(String nombre, String dni, String telefono, String username,
                     String password, String codigoEmpleado) {
        super(nombre, dni, telefono, username, password);
        this.codigoEmpleado = codigoEmpleado;
        this.ventasRealizadas = new ArrayList<>();
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public List<Venta> getVentasRealizadas() {
        return ventasRealizadas;
    }

    /**
     * Registra un nuevo producto en el inventario del sistema.
     * La lógica real de guardado se delega al InventarioService.
     *
     * @param p producto a registrar
     */
    public void registrarProducto(Producto p) {
        System.out.println("Vendedor " + getNombre() + " registrando producto: " + p.getNombre());
    }

    /**
     * Procesa un pedido confirmado por un cliente, actualizando
     * su estado de entrega.
     *
     * @param v venta a procesar
     */
    public void procesarPedido(Venta v) {
        ventasRealizadas.add(v);
        System.out.println("Vendedor " + getNombre() + " procesando pedido " + v.getIdPedido());
    }

    public void consultarInventario() {
        System.out.println("Consultando inventario disponible...");
    }

    /**
     * Muestra la información del vendedor, incluyendo los datos
     * heredados de Persona y su código de empleado.
     */
    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Código de empleado: " + codigoEmpleado);
        System.out.println("Rol: Vendedor");
    }

    @Override
    public void guardar() {
        System.out.println("Guardando vendedor " + getNombre() + " en la base de datos...");
    }

    @Override
    public void actualizar() {
        System.out.println("Actualizando datos del vendedor " + getNombre() + "...");
    }

    @Override
    public void eliminar() {
        System.out.println("Eliminando vendedor " + getNombre() + " del sistema...");
    }
}
