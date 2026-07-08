package com.mycompany.sistemacasafuerte.repositorio;

import com.mycompany.sistemacasafuerte.modelo.Venta;
import com.mycompany.sistemacasafuerte.util.ConexionSQLite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Repositorio encargado de gestionar el acceso a los datos de
 * las tablas "ventas" y "detalle_pedido" en la base de datos
 * SQLite, correspondientes al registro de las compras realizadas
 * dentro del sistema.
 *
 * @author Eliezer Vilcabana
 */
public class VentaRepositorio {

    /**
     * Crea las tablas de ventas y detalle_pedido en la base de
     * datos si aún no existen.
     */
    public void crearTabla() {
        String sqlVentas = "CREATE TABLE IF NOT EXISTS ventas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "cliente_id INTEGER NOT NULL,"
                + "vendedor_id INTEGER NOT NULL,"
                + "total REAL NOT NULL,"
                + "estado_entrega TEXT DEFAULT 'PENDIENTE',"
                + "fecha TEXT DEFAULT CURRENT_TIMESTAMP"
                + ")";

        String sqlDetalle = "CREATE TABLE IF NOT EXISTS detalle_pedido ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "venta_id INTEGER NOT NULL,"
                + "producto_id INTEGER NOT NULL,"
                + "cantidad INTEGER NOT NULL,"
                + "subtotal REAL NOT NULL"
                + ")";

        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement()) {
            st.execute(sqlVentas);
            st.execute(sqlDetalle);
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas de ventas: " + e.getMessage());
        }
    }

    /**
     * Inserta una nueva venta junto con el detalle de sus productos
     * en la base de datos.
     *
     * @param venta venta a insertar, con sus detalles ya cargados
     */
    public void insertar(Venta venta) {
        String sqlVenta = "INSERT INTO ventas (cliente_id, vendedor_id, total, estado_entrega) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, Integer.parseInt(venta.getCliente().getDni()));
            ps.setInt(2, Integer.parseInt(venta.getVendedor().getDni()));
            ps.setDouble(3, venta.getTotal());
            ps.setString(4, venta.getEstadoEntrega());
            ps.executeUpdate();

            var generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int ventaId = generatedKeys.getInt(1);
                insertarDetalles(con, ventaId, venta);
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar venta: " + e.getMessage());
        }
    }

    /**
     * Inserta las líneas de detalle asociadas a una venta.
     */
    private void insertarDetalles(Connection con, int ventaId, Venta venta) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (venta_id, producto_id, cantidad, subtotal) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (var detalle : venta.getDetalles()) {
                ps.setInt(1, ventaId);
                ps.setInt(2, Integer.parseInt(detalle.getProducto().getIdProducto()));
                ps.setInt(3, detalle.getCantidad());
                ps.setDouble(4, detalle.getSubtotal());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
