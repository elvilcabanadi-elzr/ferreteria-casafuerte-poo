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