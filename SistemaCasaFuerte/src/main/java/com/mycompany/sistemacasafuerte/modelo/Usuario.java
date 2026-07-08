package com.mycompany.sistemacasafuerte.modelo;

/**
 * Clase abstracta que representa a un usuario del sistema,
 * es decir, toda Persona que puede iniciar sesión mediante
 * un nombre de usuario y una contraseña.
 *
 * Hereda de Persona (1er nivel de herencia) y es la base
 * de Cliente y Vendedor (2do nivel de herencia).
 *
 * @author Eliezer Vilcabana
 */
public abstract class Usuario extends Persona {

    private String username;
    private String password;

    public Usuario(String nombre, String dni, String telefono, String username, String password) {
        super(nombre, dni, telefono);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    /**
     * Valida las credenciales ingresadas contra las almacenadas
     * en el objeto Usuario.
     *
     * @param user     nombre de usuario ingresado
     * @param pass     contraseña ingresada
     * @return true si las credenciales coinciden, false en caso contrario
     */
    public boolean login(String user, String pass) {
        return this.username.equals(user) && this.password.equals(pass);
    }
}
