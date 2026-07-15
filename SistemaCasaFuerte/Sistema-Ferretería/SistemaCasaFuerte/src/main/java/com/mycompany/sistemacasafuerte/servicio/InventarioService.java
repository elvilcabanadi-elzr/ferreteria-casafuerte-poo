package com.mycompany.sistemacasafuerte.servicio;

import com.mycompany.sistemacasafuerte.modelo.Inventario;
import com.mycompany.sistemacasafuerte.modelo.Producto;
import com.mycompany.sistemacasafuerte.repositorio.ProductoRepositorio;

/**
 * Servicio encargado de la lógica de negocio relacionada con
 * la gestión del inventario de productos: registro, consulta,
 * actualización y eliminación de productos, delegando la
 * persistencia en ProductoRepositorio.
 *
 * @author Eliezer Vilcabana
 */
public class InventarioService {

    private Inventario inventario;
    private ProductoRepositorio productoRepositorio;

    public InventarioService(Inventario inventario, ProductoRepositorio productoRepositorio) {
        this.inventario = inventario;
        this.productoRepositorio = productoRepositorio;
    }

    /**
     * Registra un nuevo producto en el inventario y lo persiste
     * en la base de datos.
     *
     * @param p producto a registrar
     */
    public void registrarProducto(Producto p) {
        inventario.agregarProducto(p);
        productoRepositorio.insertar(p);
    }

    /**
     * Consulta y muestra todos los productos disponibles
     * actualmente en el inventario.
     */
    public void consultarInventario() {
        inventario.listarProductos();
    }

    /**
     * Actualiza el precio y la descripción de un producto existente,
     * tanto en memoria como en la base de datos.
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
            productoRepositorio.actualizar(p);
        } else {
            System.out.println("Producto no encontrado con ID: " + id);
        }
    }

    /**
     * Elimina un producto del inventario y de la base de datos.
     *
     * @param id identificador del producto a eliminar
     */
    public void eliminarProducto(String id) {
        Producto p = inventario.buscarProducto(id);
        if (p != null) {
            productoRepositorio.eliminar(id);
            inventario.eliminarProducto(id);
        } else {
            System.out.println("Producto no encontrado con ID: " + id);
        }
    }

    /**
     * Genera un identificador único para un nuevo producto,
     * basado en la cantidad de productos ya registrados.
     *
     * @param inventario inventario actual
     * @return nuevo identificador en formato "P" + número
     */
    public String generarNuevoId() {
        return "P" + (inventario.getProductos().size() + 1);
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
    /**
     * Persiste en la base de datos el stock actualizado de un
     * producto después de procesarse una venta.
     *
     * @param producto producto cuyo stock fue modificado
     */
    public void actualizarStockEnBD(Producto producto) {
        productoRepositorio.actualizar(producto);
    }
        // El ProductoRepositorio ya tiene el método actualizar(),
        // lo reutilizamos para guardar el nuevo stock.
        // (Se necesita acceso al repositorio, ver ajuste siguiente)
}