package com.mycompany.sistemacasafuerte.repositorio;

import com.mycompany.sistemacasafuerte.modelo.Administrador;
import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.Usuario;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.util.ConexionSQLite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de gestionar el acceso a los datos de
 * la tabla "usuarios" en la base de datos SQLite. Almacena en
 * una sola tabla a Cliente, Vendedor y Administrador,
 * diferenciándolos mediante la columna "rol".
 *
 * @author Eliezer Vilcabana
 */
public class UsuarioRepositorio {

    /**
     * Crea la tabla de usuarios en la base de datos si aún no existe.
     */
    public void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL,"
                + "dni TEXT NOT NULL UNIQUE,"
                + "telefono TEXT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "rol TEXT NOT NULL,"
                + "direccion TEXT,"
                + "codigo_empleado TEXT,"
                + "nivel_acceso TEXT"
                + ")";

        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla usuarios: " + e.getMessage());
        }
    }

    /**
     * Inserta un nuevo usuario (Cliente, Vendedor o Administrador)
     * en la base de datos.
     *
     * @param u usuario a insertar
     */
    public void insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, dni, telefono, username, password, "
                + "rol, direccion, codigo_empleado, nivel_acceso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getDni());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getUsername());
            ps.setString(5, obtenerPassword(u));

            if (u instanceof Administrador admin) {
                ps.setString(6, "ADMINISTRADOR");
                ps.setString(7, null);
                ps.setString(8, admin.getCodigoEmpleado());
                ps.setString(9, admin.getNivelAcceso());
            } else if (u instanceof Vendedor vendedor) {
                ps.setString(6, "VENDEDOR");
                ps.setString(7, null);
                ps.setString(8, vendedor.getCodigoEmpleado());
                ps.setString(9, null);
            } else if (u instanceof Cliente cliente) {
                ps.setString(6, "CLIENTE");
                ps.setString(7, cliente.getDireccion());
                ps.setString(8, null);
                ps.setString(9, null);
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    /**
     * Obtiene el listado completo de usuarios registrados en la
     * base de datos, reconstruyendo el objeto correspondiente
     * (Cliente, Vendedor o Administrador) según su rol.
     *
     * @return lista de usuarios registrados
     */
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(construirUsuario(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Construye el objeto Usuario correspondiente (Cliente, Vendedor
     * o Administrador) a partir de una fila del ResultSet, según
     * el valor de la columna "rol".
     */
    private Usuario construirUsuario(ResultSet rs) throws SQLException {
        String rol = rs.getString("rol");
        String nombre = rs.getString("nombre");
        String dni = rs.getString("dni");
        String telefono = rs.getString("telefono");
        String username = rs.getString("username");
        String password = rs.getString("password");

        switch (rol) {
            case "ADMINISTRADOR":
                return new Administrador(nombre, dni, telefono, username, password,
                        rs.getString("codigo_empleado"), rs.getString("nivel_acceso"));
            case "VENDEDOR":
                return new Vendedor(nombre, dni, telefono, username, password,
                        rs.getString("codigo_empleado"));
            case "CLIENTE":
            default:
                return new Cliente(nombre, dni, telefono, username, password,
                        rs.getString("direccion"));
        }
    }

    /**
     * Obtiene la contraseña de un usuario. Método auxiliar debido
     * a que el atributo password es privado en la clase Usuario.
     */
    private String obtenerPassword(Usuario u) {
        return u.getPassword();
    }
}
