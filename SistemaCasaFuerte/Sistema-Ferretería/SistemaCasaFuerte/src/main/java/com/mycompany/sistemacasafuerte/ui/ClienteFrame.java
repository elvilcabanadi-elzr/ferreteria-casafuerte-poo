package com.mycompany.sistemacasafuerte.ui;

import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.Comprobante;
import com.mycompany.sistemacasafuerte.modelo.DetallePedido;
import com.mycompany.sistemacasafuerte.modelo.Inventario;
import com.mycompany.sistemacasafuerte.modelo.Pago;
import com.mycompany.sistemacasafuerte.modelo.Producto;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.modelo.Venta;
import com.mycompany.sistemacasafuerte.servicio.AutenticacionService;
import com.mycompany.sistemacasafuerte.servicio.InventarioService;
import com.mycompany.sistemacasafuerte.servicio.VentaService;
import com.mycompany.sistemacasafuerte.repositorio.UsuarioRepositorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal para el rol Cliente. Permite consultar el
 * catálogo de productos, agregar productos al carrito, confirmar
 * la compra, registrar el pago y generar el comprobante.
 *
 * @author Eliezer Vilcabana
 */
public class ClienteFrame extends JFrame {

    private static final Color ROJO_CASA_FUERTE = new Color(183, 28, 28);
    private static final Color NEGRO = new Color(30, 30, 30);
    private static final Color FONDO = new Color(245, 245, 245);

    private final Cliente cliente;
    private final Vendedor vendedorAsignado;
    private final Inventario inventario;
    private final VentaService ventaService;
    private final AutenticacionService autenticacionService;
    private final InventarioService inventarioService;
    private final UsuarioRepositorio usuarioRepositorio;

    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotal;
    private JTable tablaProductos;

    public ClienteFrame(Cliente cliente, Vendedor vendedorAsignado,
                         Inventario inventario, VentaService ventaService,
                         AutenticacionService autenticacionService, InventarioService inventarioService,
                         UsuarioRepositorio usuarioRepositorio) {
        this.cliente = cliente;
        this.vendedorAsignado = vendedorAsignado;
        this.inventario = inventario;
        this.ventaService = ventaService;
        this.autenticacionService = autenticacionService;
        this.inventarioService = inventarioService;
        this.usuarioRepositorio = usuarioRepositorio;

        configurarVentana();
        construirInterfaz();
        cargarProductos();
    }

    private void configurarVentana() {
        setTitle("Ferretería Casa Fuerte - Panel de Cliente");
        setSize(950, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelPrincipal.add(crearEncabezado(), BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentro.setBackground(FONDO);
        panelCentro.add(crearPanelCatalogo());
        panelCentro.add(crearPanelCarrito());
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ROJO_CASA_FUERTE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblBienvenida = new JLabel("Bienvenido, " + cliente.getNombre());
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Ferretería Casa Fuerte — Catálogo de Productos");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(255, 220, 220));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(lblBienvenida);
        textos.add(lblSubtitulo);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panel.add(btnCerrarSesion, BorderLayout.EAST);
        panel.add(textos, BorderLayout.WEST);
        return panel;
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Cerrar sesión?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            dispose();
            LoginFrame login = new LoginFrame(autenticacionService, inventario, ventaService, inventarioService, usuarioRepositorio);
            login.setVisible(true);
        }
    }

    // ---------- PANEL IZQUIERDO: CATÁLOGO ----------

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel titulo = new JLabel("Catálogo de Productos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField txtBuscar = new JTextField();
        txtBuscar.setPreferredSize(new Dimension(0, 26));

        JPanel panelTitulo = new JPanel(new BorderLayout(5, 5));
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo, BorderLayout.NORTH);
        panelTitulo.add(txtBuscar, BorderLayout.SOUTH);
        panel.add(panelTitulo, BorderLayout.NORTH);

        modeloProductos = new DefaultTableModel(
                new Object[]{"ID", "Producto", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setRowHeight(24);

        javax.swing.table.TableRowSorter<DefaultTableModel> sorter =
                new javax.swing.table.TableRowSorter<>(modeloProductos);
        tablaProductos.setRowSorter(sorter);

        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filtrar() {
                String texto = txtBuscar.getText().trim();
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(javax.swing.RowFilter.regexFilter(
                            "(?i)" + java.util.regex.Pattern.quote(texto), 1));
                }
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        JScrollPane scroll = new JScrollPane(tablaProductos);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregar.setOpaque(false);
        panelAgregar.add(new JLabel("Cantidad:"));
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinnerCantidad.setPreferredSize(new Dimension(60, 26));
        panelAgregar.add(spinnerCantidad);

        JButton btnAgregar = new JButton("Agregar al carrito");
        btnAgregar.setBackground(ROJO_CASA_FUERTE);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> agregarAlCarrito((int) spinnerCantidad.getValue()));
        panelAgregar.add(btnAgregar);

        panel.add(panelAgregar, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarProductos() {
        modeloProductos.setRowCount(0);
        for (Producto p : inventario.getProductos()) {
            modeloProductos.addRow(new Object[]{
                p.getIdProducto(), p.getNombre(),
                "S/ " + String.format("%.2f", p.getPrecio()), p.getStock()
            });
        }
    }

    // ---------- PANEL DERECHO: CARRITO ----------

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel titulo = new JLabel("Mi Carrito");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);

        modeloCarrito = new DefaultTableModel(
                new Object[]{"Producto", "Cant.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setRowHeight(24);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setOpaque(false);

        lblTotal = new JLabel("Total: S/ 0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnConfirmar = new JButton("Confirmar Compra");
        btnConfirmar.setBackground(NEGRO);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.addActionListener(e -> confirmarCompra());

        panelInferior.add(lblTotal);
        panelInferior.add(Box.createRigidArea(new Dimension(0, 8)));
        panelInferior.add(btnConfirmar);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    private void agregarAlCarrito(int cantidad) {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.");
            return;
        }
        int filaModelo = tablaProductos.convertRowIndexToModel(fila);
        String id = modeloProductos.getValueAt(filaModelo, 0).toString();
        Producto producto = inventario.buscarProducto(id);

        boolean agregado = ventaService.agregarAlCarrito(producto, cantidad);
        if (!agregado) {
            JOptionPane.showMessageDialog(this,
                    "Stock insuficiente para " + producto.getNombre() + ".",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        actualizarCarrito();
    }

    private void actualizarCarrito() {
        modeloCarrito.setRowCount(0);
        double total = 0;
        for (DetallePedido d : ventaService.getCarrito()) {
            modeloCarrito.addRow(new Object[]{
                d.getProducto().getNombre(), d.getCantidad(),
                "S/ " + String.format("%.2f", d.getSubtotal())
            });
            total += d.getSubtotal();
        }
        lblTotal.setText("Total: S/ " + String.format("%.2f", total));
    }

    // ---------- CONFIRMAR COMPRA → PAGO → COMPROBANTE ----------

    private void confirmarCompra() {
        if (ventaService.getCarrito().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tu carrito está vacío.");
            return;
        }

        String idPedido = "V-" + System.currentTimeMillis();
        Venta venta = ventaService.confirmarCompra(idPedido, cliente, vendedorAsignado);

        if (venta == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo confirmar la compra (verifica el stock disponible).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] metodos = {"Efectivo", "Tarjeta", "Yape/Plin"};
        JComboBox<String> comboMetodo = new JComboBox<>(new DefaultComboBoxModel<>(metodos));

        int opcion = JOptionPane.showConfirmDialog(this, comboMetodo,
                "Selecciona el método de pago - Total: S/ " + String.format("%.2f", venta.getTotal()),
                JOptionPane.OK_CANCEL_OPTION);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        Pago pago = new Pago("P-" + System.currentTimeMillis(), venta.getTotal(),
                (String) comboMetodo.getSelectedItem(), LocalDate.now().toString());
        venta.registrarPago(pago);

        Comprobante comprobante = venta.generarComprobante("BOLETA");
        comprobante.imprimir();
        ventaService.guardarVentaCompleta(venta);

        JOptionPane.showMessageDialog(this,
                "¡Compra realizada con éxito!\n\n"
                + "Comprobante: " + comprobante.getNumero() + "\n"
                + "Cliente: " + cliente.getNombre() + " - DNI: " + cliente.getDni() + "\n"
                + "Monto total: S/ " + String.format("%.2f", comprobante.getMontoTotal()) + "\n"
                + "Fecha: " + comprobante.getFechaEmision() + "\n"
                + "Método de pago: " + pago.getMetodoPago(),
                "Compra Confirmada",
                JOptionPane.INFORMATION_MESSAGE);

        actualizarCarrito();
        cargarProductos();
    }
}