package com.mycompany.sistemacasafuerte.servicio;

import com.mycompany.sistemacasafuerte.modelo.Inventario;
import com.mycompany.sistemacasafuerte.modelo.Producto;

/**
 * Servicio encargado de la lógica de negocio relacionada con
 * la gestión del inventario de productos: registro, consulta,
 * actualización y eliminación de productos.
 *
 * @author Eliezer Vilcabana
 */
public class InventarioService {

    private Inventario inventario;

    public InventarioService(Inventario inventario) {
        this.inventario = inventario;
    }

    /**
     * Registra un nuevo producto en el inventario, delegando
     * también su persistencia en base de datos.
     *
     * @param p producto a registrar
     */
    public void registrarProducto(Producto p) {
        inventario.agregarProducto(p);
        p.guardar();
    }

    /**
     * Consulta y muestra todos los productos disponibles
     * actualmente en el inventario.
     */
    public void consultarInventario() {
        inventario.listarProductos();
    }

    /**
     * Actualiza el precio y la descripción de un producto existente.
     *
     * @param id identificador del producto a actualizar
     * @param nuevoPrecio nuevo precio del producto
     * @param nuevaDescripcion nueva descripción del producto
     */
    public void actualizarProducto(String id, double nuevoPrecio, String nuevaDescripcion) {
        Producto p = inventario.buscarProducto(id);
        if (p != null) {
            p.setPrecio(nuevoPrecio);
            p.setDescripcion(nuevaDescripcion);
            p.actualizar();
        } else {
            System.out.println("Producto no encontrado con ID: " + id);
        }
    }

    /**
     * Elimina un producto del inventario según su identificador.
     *
     * @param id identificador del producto a eliminar
     */
    public void eliminarProducto(String id) {
        Producto p = inventario.buscarProducto(id);
        if (p != null) {
            p.eliminar();
            inventario.eliminarProducto(id);
        } else {
            System.out.println("Producto no encontrado con ID: " + id);
        }
    }

    /**
     * Verifica si un producto tiene stock suficiente para una
     * cantidad solicitada.
     *
     * @param id identificador del producto
     * @param cantidad cantidad solicitada
     * @return true si hay stock suficiente, false en caso contrario
     */
    public boolean hayStockSuficiente(String id, int cantidad) {
        Producto p = inventario.buscarProducto(id);
        return p != null && p.getStock() >= cantidad;
    }
}