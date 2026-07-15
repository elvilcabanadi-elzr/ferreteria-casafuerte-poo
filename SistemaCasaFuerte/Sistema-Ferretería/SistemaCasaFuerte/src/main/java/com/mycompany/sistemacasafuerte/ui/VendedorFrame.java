package com.mycompany.sistemacasafuerte.ui;

import com.mycompany.sistemacasafuerte.modelo.Categoria;
import com.mycompany.sistemacasafuerte.modelo.Inventario;
import com.mycompany.sistemacasafuerte.modelo.Producto;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.servicio.InventarioService;
import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.Usuario;
import com.mycompany.sistemacasafuerte.servicio.AutenticacionService;
import com.mycompany.sistemacasafuerte.servicio.VentaService;
import com.mycompany.sistemacasafuerte.repositorio.UsuarioRepositorio;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal para el rol Vendedor. Permite registrar
 * nuevos productos, consultar el inventario, actualizar precios
 * y eliminar productos que ya no se comercialicen.
 *
 * @author Eliezer Vilcabana
 */
public class VendedorFrame extends JFrame {

    private static final Color ROJO_CASA_FUERTE = new Color(183, 28, 28);
    private static final Color NEGRO = new Color(30, 30, 30);
    private static final Color FONDO = new Color(245, 245, 245);

    private final Vendedor vendedor;
    protected JLabel lblBienvenida;
    protected final VentaService ventaService;
    protected final AutenticacionService autenticacionService;
    protected final UsuarioRepositorio usuarioRepositorio;
    private final Inventario inventario;
    private final InventarioService inventarioService;

    private DefaultTableModel modeloProductos;
    private JTable tablaProductos;

    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtDescripcion;
    private JTextField txtCategoria;

   public VendedorFrame(Vendedor vendedor, Inventario inventario, InventarioService inventarioService,
                          VentaService ventaService, AutenticacionService autenticacionService,
                          UsuarioRepositorio usuarioRepositorio) {
        this.vendedor = vendedor;
        this.inventario = inventario;
        this.inventarioService = inventarioService;
        this.ventaService = ventaService;
        this.autenticacionService = autenticacionService;
        this.usuarioRepositorio = usuarioRepositorio;

        configurarVentana();
        construirInterfaz();
        cargarProductos();
    }

    private void configurarVentana() {
        setTitle("Ferretería Casa Fuerte - Panel de Vendedor");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelPrincipal.add(crearEncabezado(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelInventario(), BorderLayout.CENTER);
        panelPrincipal.add(crearPanelFormulario(), BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ROJO_CASA_FUERTE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        lblBienvenida = new JLabel("Vendedor: " + vendedor.getNombre());
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Ferretería Casa Fuerte — Gestión de Inventario");
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

    protected void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Cerrar sesión?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            dispose();
            LoginFrame login = new LoginFrame(autenticacionService, inventario, ventaService,
                    inventarioService, usuarioRepositorio);
            login.setVisible(true);
        }
    }

    // ---------- TABLA DE INVENTARIO ----------
    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel titulo = new JLabel("Inventario de Productos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);

        modeloProductos = new DefaultTableModel(
                new Object[]{"ID", "Producto", "Categoría", "Precio", "Stock", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setRowHeight(24);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

       JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);

        JButton btnRegistrarVenta = new JButton("Registrar Venta");
        btnRegistrarVenta.setBackground(ROJO_CASA_FUERTE);
        btnRegistrarVenta.setForeground(Color.WHITE);
        btnRegistrarVenta.setFocusPainted(false);
        btnRegistrarVenta.addActionListener(e -> abrirVentanaNuevaVenta());
        panelBotones.add(btnRegistrarVenta);

        JButton btnEliminar = new JButton("Eliminar producto seleccionado");
        btnEliminar.setBackground(new Color(120, 0, 0));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> eliminarProducto());
        panelBotones.add(btnEliminar);

        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarProductos() {
        modeloProductos.setRowCount(0);
        for (Producto p : inventario.getProductos()) {
            modeloProductos.addRow(new Object[]{
                p.getIdProducto(), p.getNombre(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "",
                "S/ " + String.format("%.2f", p.getPrecio()),
                p.getStock(), p.getDescripcion()
            });
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.");
            return;
        }
        String id = modeloProductos.getValueAt(fila, 0).toString();
        String nombre = modeloProductos.getValueAt(fila, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el producto \"" + nombre + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            inventarioService.eliminarProducto(id);
            cargarProductos();
        }
    }

    // ---------- FORMULARIO PARA REGISTRAR PRODUCTO ----------

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel titulo = new JLabel("Registrar Nuevo Producto");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridLayout(1, 5, 8, 0));
        campos.setOpaque(false);

        txtNombre = crearCampoConEtiqueta(campos, "Nombre");
        txtCategoria = crearCampoConEtiqueta(campos, "Categoría");
        txtPrecio = crearCampoConEtiqueta(campos, "Precio");
        txtStock = crearCampoConEtiqueta(campos, "Stock");
        txtDescripcion = crearCampoConEtiqueta(campos, "Descripción");

        panel.add(campos, BorderLayout.CENTER);

        JButton btnRegistrar = new JButton("Registrar Producto");
        btnRegistrar.setBackground(NEGRO);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrarProducto());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setOpaque(false);
        panelBoton.add(btnRegistrar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        return panel;
    }

    private JTextField crearCampoConEtiqueta(JPanel contenedor, String etiqueta) {
        JPanel wrapper = new JPanel(new BorderLayout(2, 2));
        wrapper.setOpaque(false);
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JTextField campo = new JTextField();
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(campo, BorderLayout.CENTER);
        contenedor.add(wrapper);
        return campo;
    }

    private void registrarProducto() {
        String nombre = txtNombre.getText().trim();
        String nombreCategoria = txtCategoria.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty() || nombreCategoria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y categoría son obligatorios.");
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Precio y stock deben ser valores numéricos válidos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (precio < 0 || stock < 0) {
            JOptionPane.showMessageDialog(this, "Precio y stock no pueden ser negativos.");
            return;
        }

        String id = inventarioService.generarNuevoId();
        Categoria categoria = new Categoria("C-" + nombreCategoria.hashCode(), nombreCategoria, "");
        Producto nuevo = new Producto(id, nombre, precio, stock, descripcion, categoria);

        inventarioService.registrarProducto(nuevo);
        cargarProductos();
        limpiarFormulario();

        JOptionPane.showMessageDialog(this,
                "Producto \"" + nombre + "\" registrado correctamente.",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtDescripcion.setText("");
    }
    // ---------- REGISTRAR VENTA (Vendedor atendiendo presencialmente) ----------

    private void abrirVentanaNuevaVenta() {
        List<Cliente> clientes = new ArrayList<>();
        for (Usuario u : autenticacionService.getUsuarios()) {
            if (u instanceof Cliente c) {
                clientes.add(c);
            }
        }

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay clientes registrados en el sistema.");
            return;
        }

        JComboBox<Cliente> comboClientes = new JComboBox<>(clientes.toArray(new Cliente[0]));
        comboClientes.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente c) {
                    setText(c.getNombre() + " - DNI: " + c.getDni());
                }
                return this;
            }
        });

        int opcionCliente = JOptionPane.showConfirmDialog(this, comboClientes,
                "Selecciona el cliente para esta venta",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcionCliente != JOptionPane.OK_OPTION) {
            return;
        }

        Cliente clienteSeleccionado = (Cliente) comboClientes.getSelectedItem();
        mostrarDialogoCarritoVenta(clienteSeleccionado);
    }

    private void mostrarDialogoCarritoVenta(Cliente cliente) {
        javax.swing.JDialog dialogo = new javax.swing.JDialog(this, "Nueva Venta - " + cliente.getNombre(), true);
        dialogo.setSize(700, 480);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ---- Selector de producto con buscador ----
        DefaultComboBoxModel<Producto> modeloCombo = new DefaultComboBoxModel<>(
                inventario.getProductos().toArray(new Producto[0]));
        JComboBox<Producto> comboProductos = new JComboBox<>(modeloCombo);
        comboProductos.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Producto p) {
                    setText(p.getNombre() + " - S/ " + p.getPrecio() + " (Stock: " + p.getStock() + ")");
                }
                return this;
            }
        });

        JTextField txtBuscarProducto = new JTextField();
        txtBuscarProducto.setPreferredSize(new Dimension(200, 26));
        txtBuscarProducto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filtrar() {
                String texto = txtBuscarProducto.getText().trim().toLowerCase();
                modeloCombo.removeAllElements();
                for (Producto p : inventario.getProductos()) {
                    if (p.getNombre().toLowerCase().contains(texto)) {
                        modeloCombo.addElement(p);
                    }
                }
                if (modeloCombo.getSize() > 0) {
                    comboProductos.setSelectedIndex(0);
                    comboProductos.showPopup();
                }
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        javax.swing.JSpinner spinnerCant = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 999, 1));

        JPanel panelAgregar = new JPanel(new BorderLayout(8, 8));
        JPanel filaSelector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaSelector.add(new JLabel("Buscar:"));
        filaSelector.add(txtBuscarProducto);
        filaSelector.add(new JLabel("Producto:"));
        filaSelector.add(comboProductos);
        filaSelector.add(new JLabel("Cantidad:"));
        filaSelector.add(spinnerCant);

        JButton btnAgregarProducto = new JButton("Agregar a la venta");

        // ---- Tabla del carrito de esta venta ----
        DefaultTableModel modeloCarritoVenta = new DefaultTableModel(
                new Object[]{"Producto", "Cant.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tablaCarritoVenta = new JTable(modeloCarritoVenta);

        JLabel lblTotalVenta = new JLabel("Total: S/ 0.00");
        lblTotalVenta.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnAgregarProducto.addActionListener(e -> {
            Producto producto = (Producto) comboProductos.getSelectedItem();
            if (producto == null) {
                JOptionPane.showMessageDialog(dialogo, "Selecciona un producto.");
                return;
            }
            int cantidad = (int) spinnerCant.getValue();

            boolean agregado = ventaService.agregarAlCarrito(producto, cantidad);
            if (!agregado) {
                JOptionPane.showMessageDialog(dialogo, "Stock insuficiente para " + producto.getNombre());
                return;
            }

            modeloCarritoVenta.setRowCount(0);
            double total = 0;
            for (var d : ventaService.getCarrito()) {
                modeloCarritoVenta.addRow(new Object[]{
                    d.getProducto().getNombre(), d.getCantidad(),
                    "S/ " + String.format("%.2f", d.getSubtotal())
                });
                total += d.getSubtotal();
            }
            lblTotalVenta.setText("Total: S/ " + String.format("%.2f", total));
        });

        filaSelector.add(btnAgregarProducto);
        panelAgregar.add(filaSelector, BorderLayout.NORTH);

        JButton btnConfirmarVenta = new JButton("Confirmar Venta");
        btnConfirmarVenta.setBackground(NEGRO);
        btnConfirmarVenta.setForeground(Color.WHITE);
        btnConfirmarVenta.addActionListener(e -> {
            if (ventaService.getCarrito().isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Agrega al menos un producto a la venta.");
                return;
            }

            String idPedido = "V-" + System.currentTimeMillis();
            var venta = ventaService.confirmarCompra(idPedido, cliente, vendedor);

            if (venta == null) {
                JOptionPane.showMessageDialog(dialogo, "No se pudo confirmar la venta.");
                return;
            }

            String[] metodos = {"Efectivo", "Tarjeta", "Yape/Plin"};
            JComboBox<String> comboMetodo = new JComboBox<>(metodos);
            JOptionPane.showConfirmDialog(dialogo, comboMetodo,
                    "Método de pago - Total: S/ " + String.format("%.2f", venta.getTotal()),
                    JOptionPane.OK_CANCEL_OPTION);

            var pago = new com.mycompany.sistemacasafuerte.modelo.Pago(
                    "P-" + System.currentTimeMillis(), venta.getTotal(),
                    (String) comboMetodo.getSelectedItem(), java.time.LocalDate.now().toString());
            venta.registrarPago(pago);
            var comprobante = venta.generarComprobante("BOLETA");
            comprobante.imprimir();
            ventaService.guardarVentaCompleta(venta);

            JOptionPane.showMessageDialog(dialogo,
                    "Venta registrada con éxito.\nComprobante: " + comprobante.getNumero(),
                    "Venta Confirmada", JOptionPane.INFORMATION_MESSAGE);

            cargarProductos();
            dialogo.dispose();
        });

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(lblTotalVenta, BorderLayout.WEST);
        panelSur.add(btnConfirmarVenta, BorderLayout.EAST);

        panel.add(panelAgregar, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCarritoVenta), BorderLayout.CENTER);
        panel.add(panelSur, BorderLayout.SOUTH);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }
}