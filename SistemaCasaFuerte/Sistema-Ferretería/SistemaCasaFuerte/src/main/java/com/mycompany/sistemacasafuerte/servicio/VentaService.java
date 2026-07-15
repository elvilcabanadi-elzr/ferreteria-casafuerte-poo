package com.mycompany.sistemacasafuerte.servicio;

import com.mycompany.sistemacasafuerte.excepcion.StockInsuficienteException;
import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.DetallePedido;
import com.mycompany.sistemacasafuerte.modelo.Producto;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.modelo.Venta;
import com.mycompany.sistemacasafuerte.repositorio.VentaRepositorio;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de la lógica de negocio del proceso de compra
 * y venta: gestión del carrito de compras, validación de stock,
 * generación de la venta y actualización del inventario.
 *
 * @author Eliezer Vilcabana
 */
public class VentaService {

    private List<DetallePedido> carrito;
    private InventarioService inventarioService;
    private VentaRepositorio ventaRepositorio;

    public VentaService(InventarioService inventarioService, VentaRepositorio ventaRepositorio) {
        this.carrito = new ArrayList<>();
        this.inventarioService = inventarioService;
        this.ventaRepositorio = ventaRepositorio;
    }

    /**
     * Agrega un producto al carrito de compras, validando primero
     * que exista stock suficiente.
     *
     * @param producto producto a agregar
     * @param cantidad cantidad deseada
     * @return true si se agregó correctamente, false si no hay stock suficiente
     */
    public boolean agregarAlCarrito(Producto producto, int cantidad) {
        if (!inventarioService.hayStockSuficiente(producto.getIdProducto(), cantidad)) {
            System.out.println("Stock insuficiente para: " + producto.getNombre());
            return false;
        }
        carrito.add(new DetallePedido(producto, cantidad));
        return true;
    }

    /**
     * Muestra el contenido actual del carrito de compras.
     */
    public void verCarrito() {
        if (carrito.isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }
        for (DetallePedido d : carrito) {
            System.out.println(d.toString());
        }
    }
    /**
     * Obtiene el contenido actual del carrito de compras, para
     * ser mostrado en la interfaz gráfica.
     *
     * @return lista de líneas de detalle en el carrito
     */
    public List<DetallePedido> getCarrito() {
        return carrito;
    }

    /**
     * Confirma la compra: genera una nueva Venta con los productos
     * del carrito, descuenta el stock correspondiente en el inventario
     * y vacía el carrito.
     *
     * @param idPedido identificador único de la venta
     * @param cliente cliente que realiza la compra
     * @param vendedor vendedor asignado a la venta
     * @return la venta generada, o null si el carrito está vacío
     */
    /**
     * Confirma la compra: genera una nueva Venta con los productos
     * del carrito, descuenta el stock correspondiente en el inventario
     * y vacía el carrito.
     *
     * @param idPedido identificador único de la venta
     * @param cliente cliente que realiza la compra
     * @param vendedor vendedor asignado a la venta
     * @return la venta generada, o null si el carrito está vacío o hay stock insuficiente
     */
    public Venta confirmarCompra(String idPedido, Cliente cliente, Vendedor vendedor) {
        if (carrito.isEmpty()) {
            System.out.println("No se puede confirmar una compra con el carrito vacío.");
            return null;
        }

        Venta venta = new Venta(idPedido, cliente, vendedor);

       try {
            for (DetallePedido d : carrito) {
                d.getProducto().reducirStock(d.getCantidad());
                venta.agregarDetalle(d);
                inventarioService.actualizarStockEnBD(d.getProducto());
            }
        } catch (StockInsuficienteException e) {
            System.out.println("No se pudo confirmar la venta: " + e.getMessage());
            return null;
        }

        venta.confirmar();
        carrito.clear();

        return venta;
    }

    /**
     * Vacía el carrito de compras sin confirmar ninguna venta.
     */
    public void vaciarCarrito() {
        carrito.clear();
    }
    /**
     * Persiste en la base de datos una venta que ya fue confirmada,
     * pagada y con su comprobante generado.
     *
     * @param venta venta completa a guardar
     */
    public void guardarVentaCompleta(Venta venta) {
        ventaRepositorio.insertar(venta);
    }

    /**
     * Obtiene el historial completo de ventas registradas en el
     * sistema, para reportes de ingresos y seguimiento de pedidos.
     *
     * @return lista de ventas almacenadas
     */
    public List<Venta> obtenerHistorialVentas() {
        return ventaRepositorio.listarTodas();
    }
}
