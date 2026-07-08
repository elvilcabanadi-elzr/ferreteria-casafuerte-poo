package com.mycompany.sistemacasafuerte.modelo;

/**
 * Interfaz que define el contrato de persistencia para las clases
 * que necesitan guardar, actualizar o eliminar su información
 * en la base de datos del sistema.
 *
 * @author Eliezer Vilcabana
 */
public interface IPersistible {
    
    /**
     * Guarda el objeto en la base de datos.
     */
    void guardar();
    
    /**
     * Actualiza la información del objeto en la base de datos.
     */
    void actualizar();
    
    /**
     * Elimina el objeto de la base de datos.
     */
    void eliminar();
}