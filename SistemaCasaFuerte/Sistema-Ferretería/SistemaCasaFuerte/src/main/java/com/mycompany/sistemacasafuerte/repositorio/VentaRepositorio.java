package com.mycompany.sistemacasafuerte.repositorio;

import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.Comprobante;
import com.mycompany.sistemacasafuerte.modelo.DetallePedido;
import com.mycompany.sistemacasafuerte.modelo.Pago;
import com.mycompany.sistemacasafuerte.modelo.Producto;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.modelo.Venta;
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
 * las ventas, incluyendo su detalle de productos, el pago
 * registrado y el comprobante emitido.
 *
 * @author Eliezer Vilcabana
 */
public class VentaRepositorio {

    public void crearTabla() {
        String sqlVentas = "CREATE TABLE IF NOT EXISTS ventas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "cliente_dni TEXT NOT NULL,"
                + "vendedor_dni TEXT NOT NULL,"
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
     * Inserta una venta completa (con pago y comprobante ya
     * generados) junto con el detalle de sus productos.
     *
     * @param venta venta a insertar
     */
    public void insertar(Venta venta) {
        String sqlVenta = "INSERT INTO ventas (cliente_dni, vendedor_dni, total, estado_entrega) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, venta.getCliente().getDni());
            ps.setString(2, venta.getVendedor().getDni());
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

    private void insertarDetalles(Connection con, int ventaId, Venta venta) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (venta_id, producto_nombre, cantidad, subtotal) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (DetallePedido detalle : venta.getDetalles()) {
                ps.setInt(1, ventaId);
                ps.setString(2, detalle.getProducto().getNombre());
                ps.setInt(3, detalle.getCantidad());
                ps.setDouble(4, detalle.getSubtotal());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    /**
     * Muestra un resumen de todas las compras realizadas por un
     * cliente específico, identificado por su DNI.
     *
     * @param dniCliente DNI del cliente a consultar
     */
    public void mostrarHistorialPorClienteDni(String dniCliente) {
        String sql = "SELECT v.id, v.total, v.estado_entrega, v.fecha, u.nombre AS nombre_vendedor "
                + "FROM ventas v JOIN usuarios u ON v.vendedor_dni = u.dni "
                + "WHERE v.cliente_dni = ? ORDER BY v.fecha DESC";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dniCliente);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    imprimirFilaResumen(rs);
                }
                if (!hayResultados) {
                    System.out.println("Este cliente no registra compras.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar historial del cliente: " + e.getMessage());
        }
    }

    /**
     * Muestra un resumen de todos los pedidos procesados por un
     * vendedor específico, identificado por su DNI.
     *
     * @param dniVendedor DNI del vendedor a consultar
     */
    public void mostrarHistorialPorVendedorDni(String dniVendedor) {
        String sql = "SELECT v.id, v.total, v.estado_entrega, v.fecha, u.nombre AS nombre_cliente "
                + "FROM ventas v JOIN usuarios u ON v.cliente_dni = u.dni "
                + "WHERE v.vendedor_dni = ? ORDER BY v.fecha DESC";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dniVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    imprimirFilaResumen(rs);
                }
                if (!hayResultados) {
                    System.out.println("Este vendedor no registra pedidos procesados.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar historial del vendedor: " + e.getMessage());
        }
    }

    /**
     * Muestra un resumen de todas las ventas registradas en el
     * sistema. Uso exclusivo del Administrador.
     */
    public void mostrarTodasLasVentas() {
        String sql = "SELECT v.id, v.total, v.estado_entrega, v.fecha, "
                + "uc.nombre AS nombre_cliente, uv.nombre AS nombre_vendedor "
                + "FROM ventas v "
                + "JOIN usuarios uc ON v.cliente_dni = uc.dni "
                + "JOIN usuarios uv ON v.vendedor_dni = uv.dni "
                + "ORDER BY v.fecha DESC";

        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.println("Pedido #" + rs.getInt("id")
                        + " | Cliente: " + rs.getString("nombre_cliente")
                        + " | Vendedor: " + rs.getString("nombre_vendedor")
                        + " | Total: S/ " + rs.getDouble("total")
                        + " | Estado: " + rs.getString("estado_entrega")
                        + " | Fecha: " + rs.getString("fecha"));
            }
            if (!hayResultados) {
                System.out.println("No hay ventas registradas en el sistema.");
            }

        } catch (SQLException e) {
            System.out.println("Error al listar todas las ventas: " + e.getMessage());
        }
    }

    /**
     * Actualiza el estado de entrega de un pedido ya registrado
     * (PENDIENTE, EN_CAMINO o ENTREGADO).
     *
     * @param idVenta identificador del pedido a actualizar
     * @param nuevoEstado nuevo estado de entrega
     */
    public void actualizarEstado(int idVenta, String nuevoEstado) {
        String sql = "UPDATE ventas SET estado_entrega = ? WHERE id = ?";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idVenta);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Estado del pedido #" + idVenta + " actualizado a: " + nuevoEstado);
            } else {
                System.out.println("No se encontró un pedido con ID: " + idVenta);
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado del pedido: " + e.getMessage());
        }
    }

    /**
     * Imprime una fila de resultado en formato de resumen de pedido.
     * Método auxiliar usado por las consultas de historial.
     */
    private void imprimirFilaResumen(ResultSet rs) throws SQLException {
        System.out.println("Pedido #" + rs.getInt("id")
                + " | Total: S/ " + rs.getDouble("total")
                + " | Estado: " + rs.getString("estado_entrega")
                + " | Fecha: " + rs.getString("fecha"));
    }

    /**
     * Obtiene el listado completo de ventas registradas, ordenadas
     * de la más reciente a la más antigua.
     *
     * @return lista de ventas almacenadas en la base de datos
     */
    public List<Venta> listarTodas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY id DESC";

        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cliente_nombre"), rs.getString("cliente_dni"),
                        "", "", "", "");
                Vendedor vendedor = new Vendedor(
                        rs.getString("vendedor_nombre"), rs.getString("vendedor_dni"),
                        "", "", "", "");

                Venta venta = new Venta(String.valueOf(rs.getInt("id")), cliente, vendedor);
                venta.setEstadoEntrega(rs.getString("estado_entrega"));

                ventas.add(venta);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar ventas: " + e.getMessage());
        }

        return ventas;
    }

    /**
     * Actualiza el estado de entrega de una venta específica.
     *
     * @param idVenta identificador de la venta
     * @param nuevoEstado nuevo estado: PENDIENTE, EN_CAMINO o ENTREGADO
     */
    public void actualizarEstadoEntrega(String idVenta, String nuevoEstado) {
        String sql = "UPDATE ventas SET estado_entrega = ? WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(idVenta));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de entrega: " + e.getMessage());
        }
    }

    /**
     * Calcula el total de ingresos acumulados de todas las ventas
     * registradas en el sistema.
     *
     * @return suma total de todas las ventas
     */
    public double calcularIngresosTotales() {
        String sql = "SELECT SUM(total) as total FROM ventas";
        try (Connection con = ConexionSQLite.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular ingresos: " + e.getMessage());
        }
        return 0.0;
    }
}