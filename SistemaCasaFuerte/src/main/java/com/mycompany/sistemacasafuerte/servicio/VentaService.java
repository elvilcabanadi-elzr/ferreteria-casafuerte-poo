package com.mycompany.sistemacasafuerte.servicio;

import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.DetallePedido;
import com.mycompany.sistemacasafuerte.modelo.Producto;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.modelo.Venta;
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

    public VentaService(InventarioService inventarioService) {
        this.carrito = new ArrayList<>();
        this.inventarioService = inventarioService;
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
     * Confirma la compra: genera una nueva Venta con los productos
     * del carrito, descuenta el stock correspondiente en el inventario
     * y vacía el carrito.
     *
     * @param idPedido identificador único de la venta
     * @param cliente cliente que realiza la compra
     * @param vendedor vendedor asignado a la venta
     * @return la venta generada, o null si el carrito está vacío
     */
    public Venta confirmarCompra(String idPedido, Cliente cliente, Vendedor vendedor) {
        if (carrito.isEmpty()) {
            System.out.println("No se puede confirmar una compra con el carrito vacío.");
            return null;
        }

        Venta venta = new Venta(idPedido, cliente, vendedor);

        for (DetallePedido d : carrito) {
            venta.agregarDetalle(d);
            d.getProducto().reducirStock(d.getCantidad());
        }

        venta.guardar();
        carrito.clear();

        return venta;
    }

    /**
     * Vacía el carrito de compras sin confirmar ninguna venta.
     */
    public void vaciarCarrito() {
        carrito.clear();
    }
}
