/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sistemacasafuerte.ui;

import com.mycompany.sistemacasafuerte.modelo.Administrador;
import com.mycompany.sistemacasafuerte.modelo.Inventario;
import com.mycompany.sistemacasafuerte.servicio.VentaService;
import com.mycompany.sistemacasafuerte.modelo.Administrador;
import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.Usuario;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.servicio.AutenticacionService;
import com.mycompany.sistemacasafuerte.servicio.InventarioService;
import com.mycompany.sistemacasafuerte.repositorio.UsuarioRepositorio;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Ventana de inicio de sesión del Sistema de Compra y Venta de la
 * Ferretería Casa Fuerte. Valida las credenciales ingresadas contra
 * el {@link AutenticacionService} y da la bienvenida al usuario
 * según su rol (Cliente, Vendedor o Administrador).
 *
 * @author Eliezer Vilcabana
 */
public class LoginFrame extends JFrame {

    private final AutenticacionService autenticacionService;
    private final Inventario inventario;
    private final VentaService ventaService;
    private final InventarioService inventarioService;
    private final UsuarioRepositorio usuarioRepositorio;

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblMensaje;

    public LoginFrame(AutenticacionService autenticacionService, Inventario inventario,
                       VentaService ventaService, InventarioService inventarioService,
                       UsuarioRepositorio usuarioRepositorio) {
        this.autenticacionService = autenticacionService;
        this.inventario = inventario;
        this.ventaService = ventaService;
        this.inventarioService = inventarioService;
        this.usuarioRepositorio = usuarioRepositorio;
        configurarVentana();
        construirInterfaz();
        }
    private void configurarVentana() {
        setTitle("Ferretería Casa Fuerte - Iniciar Sesión");
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        panelPrincipal.add(crearLogo());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));
        panelPrincipal.add(crearTitulo());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(crearEtiqueta("Usuario:"));
        panelPrincipal.add(crearCampoTexto());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPrincipal.add(crearEtiqueta("Contraseña:"));
        panelPrincipal.add(crearCampoPassword());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));
        panelPrincipal.add(crearBotonIngresar());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnRegistrarse = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrarse.setBorderPainted(false);
        btnRegistrarse.setContentAreaFilled(false);
        btnRegistrarse.setForeground(new Color(150, 0, 0));
        btnRegistrarse.addActionListener(e -> mostrarFormularioRegistro());
        panelPrincipal.add(btnRegistrarse);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(new Color(180, 0, 0));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblMensaje);

        add(panelPrincipal);
    }

    /**
     * Carga el logo de la empresa desde los recursos del proyecto
     * (src/main/resources/imagenes/logo.png). Si el archivo no
     * existe todavía, muestra un texto de reemplazo en su lugar.
     */
    private JLabel crearLogo() {
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setHorizontalAlignment(JLabel.CENTER);

        URL logoUrl = getClass().getResource("/imagenes/logo.png");
        if (logoUrl != null) {
            ImageIcon iconoOriginal = new ImageIcon(logoUrl);
            Image imagenEscalada = iconoOriginal.getImage()
                    .getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagenEscalada));
        } else {
            lblLogo.setText("[ Logo no encontrado: coloca logo.png en src/main/resources/imagenes ]");
            lblLogo.setFont(new Font("SansSerif", Font.ITALIC, 10));
            lblLogo.setForeground(Color.GRAY);
        }
        return lblLogo;
    }

    private JPanel crearTitulo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("FERRETERÍA CASA FUERTE");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Compra y Venta");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(lblSubtitulo);
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return lbl;
    }

    private JTextField crearCampoTexto() {
        txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txtUsuario;
    }

    private JPasswordField crearCampoPassword() {
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txtPassword;
    }

    private JButton crearBotonIngresar() {
        JButton btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnIngresar.addActionListener(e -> intentarLogin());
        return btnIngresar;
    }

    /**
     * Valida los datos ingresados y, si las credenciales son
     * correctas, muestra un mensaje de bienvenida indicando el
     * nombre y el rol del usuario autenticado.
     */
    
   private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Por favor complete usuario y contraseña.");
            return;
        }

        Usuario usuarioAutenticado = autenticacionService.autenticar(usuario, password);

        if (usuarioAutenticado == null) {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
            return;
        }

        lblMensaje.setForeground(new Color(0, 128, 0));
        lblMensaje.setText("¡Bienvenido, " + usuarioAutenticado.getNombre() + "!");

      if (usuarioAutenticado instanceof Cliente clienteAutenticado) {
            Vendedor vendedorAsignado = buscarPrimerVendedor();
            ClienteFrame clienteFrame = new ClienteFrame(
        clienteAutenticado, vendedorAsignado, inventario, ventaService,
        autenticacionService, inventarioService, usuarioRepositorio);;
            clienteFrame.setVisible(true);
            dispose();
            
        } else if (usuarioAutenticado instanceof Administrador administradorAutenticado) {
            AdministradorFrame adminFrame = new AdministradorFrame(
                    administradorAutenticado, inventario, inventarioService, ventaService,
                    autenticacionService, usuarioRepositorio);
            adminFrame.setVisible(true);
            dispose();
        } else if (usuarioAutenticado instanceof Vendedor vendedorAutenticado) {
            VendedorFrame vendedorFrame = new VendedorFrame(
                    vendedorAutenticado, inventario, inventarioService, ventaService,
                    autenticacionService, usuarioRepositorio);
            vendedorFrame.setVisible(true);
            dispose();
        }
   }

    /**
     * Busca el primer Vendedor registrado en el sistema para
     * asignarlo automáticamente a las compras del Cliente.
     */
    private Vendedor buscarPrimerVendedor() {
        for (Usuario u : autenticacionService.getUsuarios()) {
            if (u instanceof Vendedor v) {
                return v;
            }
        }
        return null;
    }
    /**
     * Determina el rol del usuario autenticado recorriendo la
     * jerarquía de herencia (Administrador → Vendedor → Cliente).
     */
    private String obtenerRol(Usuario usuario) {
        if (usuario instanceof Administrador) {
            return "Administrador";
        } else if (usuario instanceof Vendedor) {
            return "Vendedor";
        } else if (usuario instanceof Cliente) {
            return "Cliente";
        }
        return "Desconocido";
    }
    private void mostrarFormularioRegistro() {
        JTextField txtNombre = new JTextField();
        JTextField txtDni = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtUsername = new JTextField();
        JPasswordField txtPasswordReg = new JPasswordField();

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 8, 8));
        panelForm.add(new JLabel("Nombre completo:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("DNI:"));
        panelForm.add(txtDni);
        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Dirección:"));
        panelForm.add(txtDireccion);
        panelForm.add(new JLabel("Usuario:"));
        panelForm.add(txtUsername);
        panelForm.add(new JLabel("Contraseña:"));
        panelForm.add(txtPasswordReg);

        int opcion = JOptionPane.showConfirmDialog(this, panelForm,
                "Registro de Nuevo Cliente", JOptionPane.OK_CANCEL_OPTION);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPasswordReg.getPassword());

        if (nombre.isEmpty() || dni.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios (excepto teléfono/dirección).");
            return;
        }

        if (autenticacionService.existeUsername(username)) {
            JOptionPane.showMessageDialog(this,
                    "Ese nombre de usuario ya está en uso. Elige otro.",
                    "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente nuevoCliente = new Cliente(nombre, dni, telefono, username, password, direccion);
        autenticacionService.registrarUsuario(nuevoCliente);
        usuarioRepositorio.insertar(nuevoCliente);

        JOptionPane.showMessageDialog(this,
                "¡Registro exitoso! Ya puedes iniciar sesión con tu usuario y contraseña.",
                "Cuenta creada", JOptionPane.INFORMATION_MESSAGE);
    }
}