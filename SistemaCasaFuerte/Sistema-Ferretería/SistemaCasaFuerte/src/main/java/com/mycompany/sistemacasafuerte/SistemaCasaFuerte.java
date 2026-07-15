package com.mycompany.sistemacasafuerte;

import com.mycompany.sistemacasafuerte.modelo.Administrador;
import com.mycompany.sistemacasafuerte.modelo.Categoria;
import com.mycompany.sistemacasafuerte.modelo.Cliente;
import com.mycompany.sistemacasafuerte.modelo.Inventario;
import com.mycompany.sistemacasafuerte.modelo.Producto;
import com.mycompany.sistemacasafuerte.modelo.Vendedor;
import com.mycompany.sistemacasafuerte.repositorio.ProductoRepositorio;
import com.mycompany.sistemacasafuerte.servicio.AutenticacionService;
import com.mycompany.sistemacasafuerte.servicio.InventarioService;
import com.mycompany.sistemacasafuerte.servicio.VentaService;
import com.mycompany.sistemacasafuerte.ui.LoginFrame;
import com.mycompany.sistemacasafuerte.modelo.Usuario;
import com.mycompany.sistemacasafuerte.repositorio.UsuarioRepositorio;
import com.mycompany.sistemacasafuerte.repositorio.VentaRepositorio;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del Sistema de Compra y Venta de la Ferretería
 * Casa Fuerte. Inicializa los servicios del sistema y lanza la
 * ventana de inicio de sesión.
 *
 * NOTA: los usuarios y productos se crean en memoria por ahora,
 * a modo de datos de prueba, hasta que se conecte la persistencia
 * real con la base de datos SQLite.
 *
 * Credenciales de prueba:
 *   Cliente        -> usuario: cliente1   / clave: 1234
 *   Vendedor       -> usuario: vendedor1  / clave: 1234
 *   Administrador  -> usuario: admin1     / clave: 1234
 *
 * @author Eliezer Vilcabana
 */
public class SistemaCasaFuerte {

  public static void main(String[] args) {
        // 1. Crear las tablas en SQLite si no existen todavía
        UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
        ProductoRepositorio productoRepositorio = new ProductoRepositorio();
        VentaRepositorio ventaRepositorio = new VentaRepositorio();

        usuarioRepositorio.crearTabla();
        productoRepositorio.crearTabla();
        ventaRepositorio.crearTabla();

        // 2. Cargar usuarios desde la base de datos
        AutenticacionService autenticacionService = new AutenticacionService();
        List<Usuario> usuariosGuardados = usuarioRepositorio.listarTodos();

        if (usuariosGuardados.isEmpty()) {
            // Primera ejecución: no hay usuarios en la BD, se crean y se guardan
            List<Usuario> usuariosDemo = crearUsuariosDemo();
            for (Usuario u : usuariosDemo) {
                usuarioRepositorio.insertar(u);
            }
            autenticacionService.cargarUsuarios(usuariosDemo);
        } else {
            autenticacionService.cargarUsuarios(usuariosGuardados);
        }

        // 3. Cargar productos desde la base de datos
        Inventario inventario = new Inventario();
        List<Producto> productosGuardados = productoRepositorio.listarTodos();

        if (productosGuardados.isEmpty()) {
            // Primera ejecución: no hay productos en la BD, se crean y se guardan
            List<Producto> productosDemo = crearProductosDemo();
            for (Producto p : productosDemo) {
                productoRepositorio.insertar(p);
                inventario.agregarProducto(p);
            }
        } else {
            for (Producto p : productosGuardados) {
                inventario.agregarProducto(p);
            }
        }

        // 4. Construir servicios
        InventarioService inventarioService = new InventarioService(inventario, productoRepositorio);
        VentaService ventaService = new VentaService(inventarioService, ventaRepositorio);

        // 5. Lanzar la interfaz gráfica
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(autenticacionService, inventario, ventaService, inventarioService, usuarioRepositorio);
            login.setVisible(true);
        });
    }
   /**
     * Crea usuarios de prueba (Cliente, Vendedor, Administrador),
     * utilizados únicamente en la primera ejecución del sistema,
     * cuando la base de datos aún no tiene usuarios registrados.
     */
    private static List<Usuario> crearUsuariosDemo() {
        List<Usuario> usuarios = new ArrayList<>();

        usuarios.add(new Cliente(
                "Juan Pérez", "12345678", "987654321",
                "cliente1", "1234", "Av. Chiclayo 123"));

        usuarios.add(new Vendedor(
                "María López", "87654321", "912345678",
                "vendedor1", "1234", "V-001"));

        usuarios.add(new Administrador(
                "Yoel Alejandría", "11223344", "999888777",
                "admin1", "1234", "A-001", "TOTAL"));

        return usuarios;
    }
    
    /**
     * Genera un catálogo amplio de productos de prueba (60 items),
     * combinando nombres base con variaciones, utilizado únicamente
     * en la primera ejecución del sistema.
     */
    private static List<Producto> crearProductosDemo() {
        List<Producto> productos = new ArrayList<>();

        Categoria herramientas = new Categoria("C1", "Herramientas", "Herramientas manuales y eléctricas");
        Categoria construccion = new Categoria("C2", "Construcción", "Materiales de construcción");
        Categoria plomeria = new Categoria("C3", "Plomería", "Accesorios y tubería");
        Categoria electrico = new Categoria("C4", "Eléctrico", "Materiales eléctricos");
        Categoria pintura = new Categoria("C5", "Pintura", "Pinturas y accesorios");
        Categoria ferreteria = new Categoria("C6", "Ferretería General", "Productos varios de ferretería");

        String[][] datos = {
            {"Martillo de acero", "25.50", "15", "Herramientas"},
            {"Taladro eléctrico 1/2 pulg.", "189.90", "8", "Herramientas"},
            {"Destornillador plano", "8.90", "40", "Herramientas"},
            {"Destornillador estrella", "8.90", "40", "Herramientas"},
            {"Llave inglesa 10 pulg.", "22.00", "20", "Herramientas"},
            {"Alicate universal", "18.50", "25", "Herramientas"},
            {"Sierra manual", "35.00", "12", "Herramientas"},
            {"Cinta métrica 5m", "12.00", "30", "Herramientas"},
            {"Nivel de burbuja 60cm", "28.00", "18", "Herramientas"},
            {"Amoladora angular", "220.00", "6", "Herramientas"},
            {"Juego de brocas (10 pzs)", "45.00", "20", "Herramientas"},
            {"Cautín 40W", "15.00", "15", "Herramientas"},
            {"Cemento (bolsa 42.5kg)", "32.00", "60", "Construcción"},
            {"Fierro corrugado 1/2 pulg.", "38.00", "50", "Construcción"},
            {"Ladrillo King Kong (unidad)", "1.20", "500", "Construcción"},
            {"Arena gruesa (m3)", "45.00", "20", "Construcción"},
            {"Yeso (bolsa 25kg)", "18.00", "35", "Construcción"},
            {"Malla electrosoldada", "55.00", "15", "Construcción"},
            {"Bloque de concreto", "2.80", "300", "Construcción"},
            {"Vigueta prefabricada", "25.00", "40", "Construcción"},
            {"Tubo PVC 1/2 pulg. (6m)", "12.50", "45", "Plomería"},
            {"Tubo PVC 4 pulg. (6m)", "38.00", "20", "Plomería"},
            {"Codo PVC 1/2 pulg.", "1.50", "80", "Plomería"},
            {"Llave de paso 1/2 pulg.", "15.00", "25", "Plomería"},
            {"Inodoro estándar", "280.00", "6", "Plomería"},
            {"Lavatorio de baño", "150.00", "8", "Plomería"},
            {"Silicona sellante", "12.00", "30", "Plomería"},
            {"Teflón (rollo)", "1.20", "100", "Plomería"},
            {"Grifo de cocina", "65.00", "12", "Plomería"},
            {"Manguera flexible 40cm", "9.00", "30", "Plomería"},
            {"Cable eléctrico THW 12 AWG (m)", "2.50", "200", "Eléctrico"},
            {"Interruptor simple", "6.50", "40", "Eléctrico"},
            {"Tomacorriente doble", "8.00", "40", "Eléctrico"},
            {"Foco LED 9W", "7.50", "60", "Eléctrico"},
            {"Caja de pase octogonal", "3.00", "50", "Eléctrico"},
            {"Tablero eléctrico 4 polos", "45.00", "10", "Eléctrico"},
            {"Cinta aislante", "2.00", "80", "Eléctrico"},
            {"Extensión eléctrica 5m", "18.00", "25", "Eléctrico"},
            {"Pintura látex blanco (gal.)", "45.00", "30", "Pintura"},
            {"Pintura esmalte (gal.)", "55.00", "20", "Pintura"},
            {"Brocha 4 pulg.", "6.00", "35", "Pintura"},
            {"Rodillo para pintura", "9.50", "30", "Pintura"},
            {"Lija N°80", "1.00", "100", "Pintura"},
            {"Thinner (galón)", "22.00", "15", "Pintura"},
            {"Masilla para pared (kg)", "8.00", "25", "Pintura"},
            {"Cinta de pintor", "4.50", "40", "Pintura"},
            {"Bolsa de Clavos 1kg", "8.50", "50", "Ferretería General"},
            {"Bolsa de Tornillos 1kg", "9.00", "45", "Ferretería General"},
            {"Candado 40mm", "15.00", "30", "Ferretería General"},
            {"Bisagra de 3 pulg.", "3.50", "60", "Ferretería General"},
            {"Cerradura de pomo", "35.00", "18", "Ferretería General"},
            {"Alambre galvanizado (kg)", "6.50", "40", "Ferretería General"},
            {"Soga de nylon 10m", "12.00", "25", "Ferretería General"},
            {"Guantes de trabajo", "10.00", "40", "Ferretería General"},
            {"Casco de seguridad", "25.00", "20", "Ferretería General"},
            {"Cinta de embalaje", "3.50", "50", "Ferretería General"},
            {"Escoba de cerdas", "10.00", "30", "Ferretería General"},
            {"Carretilla buggy", "180.00", "6", "Ferretería General"},
            {"Pala punta cuadrada", "28.00", "20", "Ferretería General"},
            {"Manguera de jardín 15m", "45.00", "12", "Ferretería General"}
        };

        for (int i = 0; i < datos.length; i++) {
            String nombre = datos[i][0];
            double precio = Double.parseDouble(datos[i][1]);
            int stock = Integer.parseInt(datos[i][2]);
            String nombreCategoria = datos[i][3];

            Categoria categoria = switch (nombreCategoria) {
                case "Herramientas" -> herramientas;
                case "Construcción" -> construccion;
                case "Plomería" -> plomeria;
                case "Eléctrico" -> electrico;
                case "Pintura" -> pintura;
                default -> ferreteria;
            };

            productos.add(new Producto("P" + (i + 1), nombre, precio, stock,
                    nombreCategoria + " - " + nombre, categoria));
        }

        return productos;
    }
    
}