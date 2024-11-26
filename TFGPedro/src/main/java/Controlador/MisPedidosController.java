/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Pedro Rollán García
 */
public class MisPedidosController {

  public static void cargarPedidos(DefaultTableModel modeloTabla, String nifUsuario, JFrame frame) {
    String url = "jdbc:mysql://localhost:3306/bd_prueba";
    String user = "root";
    String password = "";

    String queryPedidos = """
                    SELECT p.id_pedido, pr.nombre_producto, dp.cantidad, dp.precio_total
                    FROM pedido p
                    JOIN cliente c ON p.id_cliente = c.id_cliente
                    JOIN detalle_pedido dp ON p.id_pedido = dp.id_pedido
                    JOIN producto pr ON dp.id_producto = pr.id_producto
                    WHERE c.nif = ?
                    ORDER BY p.id_pedido;
                    """;

    try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(queryPedidos)) {

      stmt.setString(1, nifUsuario);
      ResultSet rs = stmt.executeQuery();

      modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

      int pedidoActual = -1;
      double totalGeneral = 0.0;

      while (rs.next()) {
        int idPedido = rs.getInt("id_pedido");
        String nombreProducto = rs.getString("nombre_producto");
        int cantidad = rs.getInt("cantidad");
        double precioTotal = rs.getDouble("precio_total");

        if (idPedido != pedidoActual) {
          pedidoActual = idPedido;
          modeloTabla.addRow(new Object[]{"Pedido #" + pedidoActual, "", ""});
        }

        modeloTabla.addRow(new Object[]{nombreProducto, cantidad, String.format("%.2f €", precioTotal)});
        totalGeneral += precioTotal;
      }

      modeloTabla.addRow(new Object[]{"", "", ""}); // Separación
      modeloTabla.addRow(new Object[]{"Total General", "", String.format("%.2f €", totalGeneral)});

    } catch (SQLException ex) {
      Logger.getLogger(MisPedidosController.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(frame, "Error al cargar los pedidos: " + ex.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public static void enviarTicket(DefaultTableModel modeloTabla, JFrame frame) {
    StringBuilder ticket = new StringBuilder();
    ticket.append("<h1>Resumen de tu Pedido</h1>");
    ticket.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
    ticket.append("<tr><th>Pedido/Plato</th><th>Cantidad</th><th>Precio</th></tr>");

    for (int i = 0; i < modeloTabla.getRowCount(); i++) {
      String pedido = (String) modeloTabla.getValueAt(i, 0);
      String cantidad = modeloTabla.getValueAt(i, 1) != null ? modeloTabla.getValueAt(i, 1).toString() : "";
      String precio = modeloTabla.getValueAt(i, 2) != null ? modeloTabla.getValueAt(i, 2).toString() : "";

      ticket.append("<tr>")
        .append("<td>").append(pedido).append("</td>")
        .append("<td>").append(cantidad).append("</td>")
        .append("<td>").append(precio).append("</td>")
        .append("</tr>");
    }
    ticket.append("</table>");

    String destinatario = Modelo.DatosUsuario.getCorreo();

    if (destinatario != null && !destinatario.trim().isEmpty()) {
      boolean enviado = EmailUtil.sendTicketEmail(destinatario, "Ticket de Pedido", ticket.toString());
      if (enviado) {
        JOptionPane.showMessageDialog(frame, "El ticket fue enviado exitosamente.",
          "Éxito", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame, "Hubo un problema al enviar el ticket.",
          "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(frame, "Debe ingresar un correo válido.",
        "Aviso", JOptionPane.WARNING_MESSAGE);
    }
  }
}
