package com.mycompany.sistemacasafuerte.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el inventario general de productos de la Ferretería
 * Casa Fuerte, permitiendo agregar, eliminar, buscar y actualizar
 * el stock de los productos registrados en el sistema.
 *
 * @author Eliezer Vilcabana
 */
public class Inventario {

    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    /**
     * Agrega un nuevo producto al inventario.
     *
     * @param p producto a agregar
     */
    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    /**
     * Elimina un producto del inventario según su identificador.
     *
     * @param id identificador del producto a eliminar
     */
    public void eliminarProducto(String id) {
        productos.removeIf(p -> p.getIdProducto().equals(id));
    }

    /**
     * Busca un producto dentro del inventario según su identificador.
     *
     * @param id identificador del producto a buscar
     * @return el producto encontrado, o null si no existe
     */
    public Producto buscarProducto(String id) {
        for (Producto p : productos) {
            if (p.getIdProducto().equals(id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Lista todos los productos disponibles en el inventario,
     * mostrándolos por consola.
     */
    public void listarProductos() {
        for (Producto p : productos) {
            System.out.println(p.toString());
        }
    }

    /**
     * Actualiza el stock de un producto específico, ya sea sumando
     * nueva mercadería o descontando unidades vendidas.
     *
     * @param id identificador del producto
     * @param cantidad cantidad a modificar (positiva o negativa)
     */
    public void actualizarStock(String id, int cantidad) {
        Producto p = buscarProducto(id);
        if (p != null) {
            p.setStock(p.getStock() + cantidad);
        }
    }
}