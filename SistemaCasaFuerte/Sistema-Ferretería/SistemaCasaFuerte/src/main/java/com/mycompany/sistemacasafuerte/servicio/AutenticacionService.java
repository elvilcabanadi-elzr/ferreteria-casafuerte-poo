package com.mycompany.sistemacasafuerte.servicio;

import com.mycompany.sistemacasafuerte.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de la lógica de negocio relacionada con
 * la autenticación de usuarios dentro del sistema, validando
 * las credenciales ingresadas contra los usuarios registrados.
 *
 * @author Eliezer Vilcabana
 */
public class AutenticacionService {

    private List<Usuario> usuarios;

    public AutenticacionService() {
        this.usuarios = new ArrayList<>();
    }

    /**
     * Registra un nuevo usuario dentro de la lista de usuarios
     * disponibles para autenticación.
     *
     * @param usuario usuario a registrar
     */
    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    /**
     * Carga una lista completa de usuarios (por ejemplo, obtenida
     * desde la base de datos) reemplazando los usuarios actuales
     * en memoria.
     *
     * @param usuariosCargados lista de usuarios a cargar
     */
    public void cargarUsuarios(List<Usuario> usuariosCargados) {
        this.usuarios = usuariosCargados;
    }
    /**
     * Elimina un usuario del sistema según su nombre de usuario.
     *
     * @param username nombre de usuario a eliminar
     * @return true si se eliminó correctamente, false si no se encontró
     */
    public boolean eliminarUsuario(String username) {
        return usuarios.removeIf(u -> u.getUsername().equals(username));
    }
    /**
     * Verifica si ya existe un usuario registrado con el username
     * indicado, para evitar duplicados al registrar nuevas cuentas.
     *
     * @param username nombre de usuario a verificar
     * @return true si ya existe, false si está disponible
     */
    public boolean existeUsername(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida las credenciales ingresadas contra los usuarios
     * registrados en el sistema.
     *
     * @param username nombre de usuario ingresado
     * @param password contraseña ingresada
     * @return el usuario autenticado si las credenciales son correctas,
     *         o null si no se encuentra ninguna coincidencia
     */
    public Usuario autenticar(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.login(username, password)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}