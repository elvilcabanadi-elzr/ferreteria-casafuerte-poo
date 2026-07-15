package com.mycompany.sistemacasafuerte.repositorio;

import com.mycompany.sistemacasafuerte.modelo.Categoria;
import com.mycompany.sistemacasafuerte.modelo.Producto;
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
 * la tabla "productos" en la base de datos SQLite, incluyendo
 * operaciones de creación, consulta, actualización y eliminación.
 *
 * @author Eliezer Vilcabana
 */
public class ProductoRepositorio {

    /**
     * Crea la tabla de productos en la base de datos si aún no existe.
     */
    public void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS productos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL,"
                + "categoria TEXT,"
                + "precio REAL NOT NULL,"
                + "stock INTEGER NOT NULL,"
                + "descripcion TEXT"
                + ")";

        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla productos: " + e.getMessage());
        }
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param p producto a insertar
     */
    public void insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, stock, descripcion) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria() != null ? p.getCategoria().getNombre() : "");
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getDescripcion());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
    }

    /**
     * Obtiene el listado completo de productos almacenados en
     * la base de datos.
     *
     * @return lista de productos registrados
     */
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Categoria cat = new Categoria("", rs.getString("categoria"), "");
                Producto p = new Producto(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("descripcion"),
                        cat
                );
                productos.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return productos;
    }

    /**
     * Actualiza los datos de un producto existente en la base
     * de datos según su identificador.
     *
     * @param p producto con los datos actualizados
     */
    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, categoria = ?, precio = ?, "
                + "stock = ?, descripcion = ? WHERE id = ?";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria() != null ? p.getCategoria().getNombre() : "");
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getDescripcion());
            ps.setInt(6, Integer.parseInt(p.getIdProducto()));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto de la base de datos según su identificador.
     *
     * @param id identificador del producto a eliminar
     */
    public void eliminar(String id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        }
    }
}