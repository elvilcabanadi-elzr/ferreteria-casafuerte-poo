package com.mycompany.sistemacasafuerte.ui;

import com.mycompany.sistemacasafuerte.modelo.Administrador;
import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.Inventario;
import com.mycompany.sistemacasafuerte.modelo.Usuario;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.servicio.AutenticacionService;
import com.mycompany.sistemacasafuerte.servicio.InventarioService;
import com.mycompany.sistemacasafuerte.servicio.VentaService;
import com.mycompany.sistemacasafuerte.repositorio.UsuarioRepositorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal para el rol Administrador. Extiende de
 * VendedorFrame, reutilizando toda la gestión de inventario, y
 * agrega un panel adicional para la gestión de usuarios del
 * sistema (privilegio exclusivo del Administrador).
 *
 * @author Eliezer Vilcabana
 */
public class AdministradorFrame extends VendedorFrame {

    private static final Color NEGRO = new Color(30, 30, 30);

    private final AutenticacionService autenticacionService;
    private final UsuarioRepositorio usuarioRepositorio;
    private DefaultTableModel modeloUsuarios;

    public AdministradorFrame(Administrador administrador, Inventario inventario,
                               InventarioService inventarioService, VentaService ventaService,
                               AutenticacionService autenticacionService,
                               UsuarioRepositorio usuarioRepositorio) {
        super(administrador, inventario, inventarioService, ventaService, autenticacionService, usuarioRepositorio);
        this.autenticacionService = autenticacionService;
        this.usuarioRepositorio = usuarioRepositorio;

        setTitle("Ferretería Casa Fuerte - Panel de Administrador");
        if (lblBienvenida != null) {
            lblBienvenida.setText("Administrador: " + administrador.getNombre());
        }
        agregarPanelUsuarios();
        cargarUsuarios();
    }

    /**
     * Agrega un panel adicional en una nueva ventana emergente
     * para la gestión de usuarios, accesible mediante un botón
     * en la ventana principal del administrador.
     */
    private void agregarPanelUsuarios() {
        JButton btnGestionUsuarios = new JButton("Gestionar Usuarios");
        btnGestionUsuarios.setBackground(NEGRO);
        btnGestionUsuarios.setForeground(Color.WHITE);
        btnGestionUsuarios.setFocusPainted(false);
        btnGestionUsuarios.addActionListener(e -> mostrarVentanaUsuarios());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperior.setOpaque(false);
        panelSuperior.add(btnGestionUsuarios);

        getContentPane().add(panelSuperior, BorderLayout.PAGE_START);
        revalidate();
    }

    private void mostrarVentanaUsuarios() {
        javax.swing.JDialog dialogo = new javax.swing.JDialog(this, "Gestión de Usuarios", true);
        dialogo.setSize(650, 400);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Usuarios Registrados en el Sistema");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);

        modeloUsuarios = new DefaultTableModel(
                new Object[]{"Nombre", "DNI", "Usuario", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.setRowHeight(24);
        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        JButton btnEliminar = new JButton("Eliminar usuario seleccionado");
        btnEliminar.setBackground(new Color(120, 0, 0));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(dialogo, "Selecciona un usuario de la tabla.");
                return;
            }
            String username = modeloUsuarios.getValueAt(fila, 2).toString();
            int confirmacion = JOptionPane.showConfirmDialog(dialogo,
                    "¿Eliminar al usuario \"" + username + "\"?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                autenticacionService.eliminarUsuario(username);
                cargarUsuarios();
            }
        });

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnEliminar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        dialogo.add(panel);
        cargarUsuarios();
        dialogo.setVisible(true);
    }

    private void cargarUsuarios() {
        if (modeloUsuarios == null) {
            return;
        }
        modeloUsuarios.setRowCount(0);
        for (Usuario u : autenticacionService.getUsuarios()) {
            modeloUsuarios.addRow(new Object[]{
                u.getNombre(), u.getDni(), u.getUsername(), obtenerRolUsuario(u)
            });
        }
    }

    private String obtenerRolUsuario(Usuario u) {
        if (u instanceof Administrador) {
            return "Administrador";
        } else if (u instanceof Vendedor) {
            return "Vendedor";
        } else if (u instanceof Cliente) {
            return "Cliente";
        }
        return "Desconocido";
    }
}