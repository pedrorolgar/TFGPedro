package Controlador;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class BebidasController {

  private Connection connection;

  public BebidasController() {
    establecerConexion();
  }

  // Establecer la conexión a la base de datos
  private void establecerConexion() {
    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_prueba", "root", "");
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }
  }

  // Cargar las bebidas desde la base de datos y retornar el panel con los datos
  public JScrollPane cargarBebidas(JScrollPane menuBebidas, String nifUsuario) {
    String query = "SELECT * FROM producto WHERE categoria = 'Bebida'";
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      // Crear el panel contenedor para las bebidas
      JPanel panelContenedor = new JPanel();
      panelContenedor.setLayout(new GridLayout(0, 3, 10, 10));

      // Crear un panel para cada bebida
      while (rs.next()) {
        String nombre = rs.getString("nombre_producto");
        String descripcion = rs.getString("descripcion");
        double precioUnitario = rs.getDouble("precio_unitario");
        int idProducto = rs.getInt("id_producto");

        JPanel panel = crearPanelBebida(nombre, descripcion, precioUnitario, idProducto, nifUsuario);

        JPanel margenPanel = new JPanel();
        margenPanel.setLayout(new BorderLayout());
        margenPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        margenPanel.add(panel);
        panelContenedor.add(margenPanel);
      }

      // Configurar el panel dentro del JScrollPane
      menuBebidas.setViewportView(panelContenedor);
      menuBebidas.revalidate();
      menuBebidas.repaint();
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error al cargar los datos", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return menuBebidas;
  }

  // Crear el panel para cada bebida
  private JPanel crearPanelBebida(String nombre, String descripcion, double precioUnitario, int idProducto, String nifUsuario) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    Font tituloFuente = new Font("SansSerif", Font.BOLD, 14);
    Color tituloColor = new Color(60, 63, 65);

    panel.setBorder(BorderFactory.createTitledBorder(
      BorderFactory.createLineBorder(Color.LIGHT_GRAY),
      nombre,
      javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
      javax.swing.border.TitledBorder.DEFAULT_POSITION,
      tituloFuente,
      tituloColor
    ));

    JTextArea descripcionArea = new JTextArea(descripcion);
    descripcionArea.setLineWrap(true);
    descripcionArea.setWrapStyleWord(true);
    descripcionArea.setEditable(false);
    descripcionArea.setPreferredSize(new Dimension(200, 100));
    descripcionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    JLabel precioLabel = new JLabel("Precio: $" + String.format("%.2f", precioUnitario));
    precioLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
    precioLabel.setForeground(new Color(100, 100, 100));

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BorderLayout());
    infoPanel.add(descripcionArea, BorderLayout.CENTER);
    infoPanel.add(precioLabel, BorderLayout.SOUTH);

    panel.add(infoPanel, BorderLayout.CENTER);

    JTextField cantidadField = new JTextField(5);
    JButton addButton = new JButton("Añadir a mi pedido");
    addButton.addActionListener(e -> {
      String cantidadTexto = cantidadField.getText();
      if (!cantidadTexto.isEmpty() && cantidadTexto.matches("\\d+")) {
        int cantidad = Integer.parseInt(cantidadTexto);
        if (cantidad > 0) {
          guardarPedido(nifUsuario, idProducto, cantidad, precioUnitario);
          JOptionPane.showMessageDialog(panel, "Añadido " + cantidad + " de " + nombre + " a tu pedido.");
        } else {
          JOptionPane.showMessageDialog(panel, "La cantidad debe ser mayor que 0.", "Error", JOptionPane.WARNING_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(panel, "Por favor, introduce una cantidad válida.", "Error", JOptionPane.WARNING_MESSAGE);
      }
    });

    JPanel controlPanel = new JPanel();
    controlPanel.add(new JLabel("Cantidad: "));
    controlPanel.add(cantidadField);
    controlPanel.add(addButton);

    panel.add(controlPanel, BorderLayout.SOUTH);

    return panel;
  }

  // Guardar el pedido en la base de datos
  private void guardarPedido(String nifUsuario, int idProducto, int cantidad, double precioUnitario) {
    try {
      String queryCliente = "SELECT id_cliente FROM cliente WHERE nif = ?";
      PreparedStatement stmtCliente = connection.prepareStatement(queryCliente);
      stmtCliente.setString(1, nifUsuario);
      ResultSet rsCliente = stmtCliente.executeQuery();

      if (rsCliente.next()) {
        int idCliente = rsCliente.getInt("id_cliente");

        String queryPedido = "SELECT id_pedido FROM pedido WHERE id_cliente = ? AND estado_pedido = 'Activo'";
        PreparedStatement stmtPedido = connection.prepareStatement(queryPedido);
        stmtPedido.setInt(1, idCliente);
        ResultSet rsPedido = stmtPedido.executeQuery();

        int idPedido = Modelo.DatosUsuario.getNpedido();

        if (rsPedido.next()) {
          idPedido = rsPedido.getInt("id_pedido");
        } else {
          String insertPedido = "INSERT INTO pedido (id_cliente, total_pedido, estado_pedido) VALUES (?, 0, 'Activo')";
          PreparedStatement stmtInsertPedido = connection.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS);
          stmtInsertPedido.setInt(1, idCliente);
          stmtInsertPedido.executeUpdate();
          ResultSet rsInsertPedido = stmtInsertPedido.getGeneratedKeys();
          rsInsertPedido.next();
          idPedido = rsInsertPedido.getInt(1);
          Modelo.DatosUsuario.setNpedido(idPedido);
        }

        double precioTotal = cantidad * precioUnitario;

        String insertDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_total) VALUES (?, ?, ?, ?)";
        PreparedStatement stmtInsertDetalle = connection.prepareStatement(insertDetalle);
        stmtInsertDetalle.setInt(1, idPedido);
        stmtInsertDetalle.setInt(2, idProducto);
        stmtInsertDetalle.setInt(3, cantidad);
        stmtInsertDetalle.setDouble(4, precioTotal);
        stmtInsertDetalle.executeUpdate();

        String updateTotal = "UPDATE pedido SET total_pedido = (SELECT SUM(precio_total) FROM detalle_pedido WHERE id_pedido = ?) WHERE id_pedido = ?";
        PreparedStatement stmtUpdateTotal = connection.prepareStatement(updateTotal);
        stmtUpdateTotal.setInt(1, idPedido);
        stmtUpdateTotal.setInt(2, idPedido);
        stmtUpdateTotal.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error al guardar el pedido", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
