package com.mycompany.sistemacasafuerte.modelo;

/**
 * Representa al administrador del sistema, quien posee todos los
 * permisos de un Vendedor más privilegios adicionales, como la
 * eliminación de productos y la gestión de usuarios.
 *
 * Hereda de Vendedor, lo que constituye el 2do nivel de herencia
 * respecto a Usuario, y el 3er nivel respecto a Persona.
 *
 * @author Eliezer Vilcabana
 */
public class Administrador extends Vendedor {

    private String nivelAcceso;

    public Administrador(String nombre, String dni, String telefono, String username,
                          String password, String codigoEmpleado, String nivelAcceso) {
        super(nombre, dni, telefono, username, password, codigoEmpleado);
        this.nivelAcceso = nivelAcceso;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    /**
     * Elimina un producto del inventario del sistema.
     * Solo el Administrador tiene permiso para esta acción.
     *
     * @param id identificador del producto a eliminar
     */
    public void eliminarProducto(String id) {
        System.out.println("Administrador " + getNombre() + " eliminando producto ID: " + id);
    }

    /**
     * Permite gestionar (crear, editar o eliminar) usuarios del sistema.
     * Solo el Administrador tiene permiso para esta acción.
     */
    public void gestionarUsuarios() {
        System.out.println("Administrador " + getNombre() + " gestionando usuarios del sistema...");
    }

    /**
     * Muestra la información del administrador, incluyendo los datos
     * heredados de Vendedor y su nivel de acceso.
     */
    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Nivel de acceso: " + nivelAcceso);
        System.out.println("Rol: Administrador");
    }
}
