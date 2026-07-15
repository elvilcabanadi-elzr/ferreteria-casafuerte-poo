package com.mycompany.sistemacasafuerte.modelo;

/**
 * Representa una categoría de productos dentro del inventario
 * de la Ferretería Casa Fuerte (por ejemplo: herramientas,
 * construcción, plomería, eléctrico, pintura, ferretería general).
 *
 * @author Eliezer Vilcabana
 */
public class Categoria {

    private String idCategoria;
    private String nombre;
    private String descripcion;

    public Categoria(String idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}