package com.mycompany.sistemacasafuerte.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria encargada de gestionar la conexión a la base
 * de datos SQLite del sistema (archivo casafuerte.db).
 *
 * @author Eliezer Vilcabana
 */
public class ConexionSQLite {

    private static final String URL = "jdbc:sqlite:casafuerte.db";

    /**
     * Establece y retorna una conexión activa hacia la base de
     * datos SQLite del sistema.
     *
     * @return conexión activa, o null si ocurre un error
     */
    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL);
            System.out.println("Conexión a SQLite establecida correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra una conexión activa hacia la base de datos.
     *
     * @param conexion conexión a cerrar
     */
    public static void cerrar(Connection conexion) {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}