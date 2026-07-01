package com.mycompany.sistemacasafuerte.modelo;

/**
 * Representa un producto del inventario de la Ferretería Casa Fuerte,
 * como herramientas, materiales de construcción, productos de
 * plomería, eléctricos, pinturas, entre otros.
 *
 * Implementa IPersistible para su almacenamiento en base de datos.
 *
 * @author Eliezer Vilcabana
 */
public class Producto implements IPersistible {

    private String idProducto;
    private String nombre;
    private double precio;
    private int stock;
    private String descripcion;
    private Categoria categoria;

    public Producto(String idProducto, String nombre, double precio, int stock,
                     String descripcion, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Reduce el stock disponible del producto luego de confirmarse
     * una venta. Valida que la cantidad solicitada no supere el
     * stock disponible.
     *
     * @param cantidad cantidad a descontar del stock
     * @throws IllegalArgumentException si la cantidad supera el stock disponible
     */
    public void reducirStock(int cantidad) {
        if (cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + nombre);
        }
        this.stock -= cantidad;
    }

    @Override
    public void guardar() {
        System.out.println("Guardando producto " + nombre + " en la base de datos...");
    }

    @Override
    public void actualizar() {
        System.out.println("Actualizando producto " + nombre + "...");
    }

    @Override
    public void eliminar() {
        System.out.println("Eliminando producto " + nombre + " del sistema...");
    }

    @Override
    public String toString() {
        return nombre + " - S/ " + precio + " (Stock: " + stock + ")";
    }
}
