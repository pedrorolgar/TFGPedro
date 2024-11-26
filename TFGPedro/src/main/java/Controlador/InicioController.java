/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.*;
import javax.swing.JOptionPane;

public class InicioController {

  private static final String URL = "jdbc:mysql://localhost:3306/bd_prueba";
  private static final String USER = "root";
  private static final String PASSWORD = "";

  // Método para establecer la conexión a la base de datos
  private Connection conectarBD() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  // Método para insertar los datos del cliente en la base de datos
  public void insertarCliente(String nombre, String nif, String email, String telefono) {
    String sql = "INSERT INTO cliente (nombre_cliente, nif, email, telefono) VALUES (?, ?, ?, ?)";
    try (Connection conn = conectarBD(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nombre);
      pstmt.setString(2, nif);
      pstmt.setString(3, email);
      pstmt.setString(4, telefono);
      pstmt.executeUpdate();
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "Error al guardar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // Método para borrar todos los datos de la tabla cliente
  public void borrarClientes() {
    ejecutarConsultaSimple("DELETE FROM cliente", "Error al borrar datos en 'cliente'");
  }

  // Método para borrar todos los datos de la tabla pedido
  public void borrarPedidos() {
    ejecutarConsultaSimple("DELETE FROM pedido", "Error al borrar datos en 'pedido'");
  }

  // Método para borrar todos los datos de la tabla detalle_pedido
  public void borrarDetallePedidos() {
    ejecutarConsultaSimple("DELETE FROM detalle_pedido", "Error al borrar datos en 'detalle_pedido'");
  }

  // Métodos para eliminar tablas
  public void borrarTablaCliente() {
    eliminarTabla("cliente", "Error al eliminar la tabla 'cliente'");
  }

  public void borrarTablaPedidos() {
    eliminarTabla("pedido", "Error al eliminar la tabla 'pedido'");
  }

  public void borrarTablaDetallePedido() {
    eliminarTabla("detalle_pedido", "Error al eliminar la tabla 'detalle_pedido'");
  }

  // Métodos para crear tablas
  public void crearTablaCliente() {
    String sql = """
                CREATE TABLE IF NOT EXISTS cliente (
                    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                    nombre_cliente VARCHAR(100) NOT NULL,
                    nif VARCHAR(20) NOT NULL UNIQUE,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    telefono VARCHAR(20)
                )
                """;
    ejecutarConsultaSimple(sql, "Error al crear la tabla 'cliente'");
  }

  public void crearTablaPedido() {
    String sql = """
                CREATE TABLE IF NOT EXISTS pedido (
                    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
                    id_cliente INT NOT NULL,
                    fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                    total_pedido DECIMAL(10,2) DEFAULT NULL,
                    estado_pedido VARCHAR(50) DEFAULT NULL,
                    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
                )
                """;
    ejecutarConsultaSimple(sql, "Error al crear la tabla 'pedido'");
  }

  public void crearTablaDetallePedido() {
    String sql = """
                CREATE TABLE IF NOT EXISTS detalle_pedido (
                    id_detalle_pedido INT AUTO_INCREMENT PRIMARY KEY,
                    id_pedido INT NOT NULL,
                    id_producto INT NOT NULL,
                    cantidad INT NOT NULL,
                    precio_total DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
                    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
                )
                """;
    ejecutarConsultaSimple(sql, "Error al crear la tabla 'detalle_pedido'");
  }

  // Método genérico para ejecutar consultas simples
  private void ejecutarConsultaSimple(String sql, String mensajeError) {
    try (Connection conn = conectarBD(); Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, mensajeError + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // Método genérico para eliminar tablas
  private void eliminarTabla(String tabla, String mensajeError) {
    String sql = "DROP TABLE IF EXISTS " + tabla;
    ejecutarConsultaSimple(sql, mensajeError);
  }
}
