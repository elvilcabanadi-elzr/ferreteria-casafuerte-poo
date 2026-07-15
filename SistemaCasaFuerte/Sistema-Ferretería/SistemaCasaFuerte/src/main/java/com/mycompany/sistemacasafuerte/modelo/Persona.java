package com.mycompany.sistemacasafuerte.modelo;

/**
 * Clase abstracta que representa a una persona dentro del sistema.
 * Contiene los atributos y comportamientos comunes que comparten
 * todos los actores del sistema (Cliente, Vendedor, Administrador).
 *
 * @author Eliezer Vilcabana
 */
public abstract class Persona {
    
    private String nombre;
    private String dni;
    private String telefono;

    public Persona(String nombre, String dni, String telefono) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Muestra la información básica de la persona.
     * Puede ser sobrescrito por las subclases para mostrar
     * información adicional específica de cada rol.
     */
    public void mostrarInfo() {
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Teléfono: " + telefono);
    }
}