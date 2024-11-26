/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import lombok.Getter;

/**
 *
 * @author Pedro Rollán García
 */
@Getter
public class Entrantes extends javax.swing.JFrame {

  private Connection connection;
  private String nifUsuario;
  private int numPedido = 0;
  private int idProducto;
  private int cantidad;
  private double precioUnitario;

  public Entrantes() {
    FlatLightOwlIJTheme.setup();
    initComponents();
    establecerConexion();
    cargarEntrantes();
    this.nifUsuario = Modelo.DatosUsuario.getNif();

    // Obtener el último número de pedido y asignarlo al numPedido
    this.numPedido = obtenerUltimoNumeroPedido() + 1;  // Se obtiene el último id_pedido y se incrementa
    // Guardar el numPedido en DatosUsuario
    Modelo.DatosUsuario.setNpedido(numPedido);
  }

  // Establecer la conexión en la base de datos
  private void establecerConexion() {
    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_prueba", "root", "");
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }
  }

  // Obtener el último número de pedido desde la base de datos
  private int obtenerUltimoNumeroPedido() {
    int ultimoNumeroPedido = 0;
    try {
      String query = "SELECT MAX(id_pedido) AS ultimoPedido FROM pedido";
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        ultimoNumeroPedido = rs.getInt("ultimoPedido");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al obtener el último número de pedido", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return ultimoNumeroPedido;
  }

  private void cargarEntrantes() {
    String query = "SELECT * FROM producto WHERE categoria = 'Entrante'";
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      JPanel panelContenedor = new JPanel();
      panelContenedor.setLayout(new GridLayout(0, 3, 10, 10));

      while (rs.next()) {
        String nombre = rs.getString("nombre_producto");
        String descripcion = rs.getString("descripcion");
        double precioUnitario = rs.getDouble("precio_unitario");
        int idProducto = rs.getInt("id_producto");

        JPanel panel = crearPanelEntrante(nombre, descripcion, precioUnitario, idProducto);

        JPanel margenPanel = new JPanel();
        margenPanel.setLayout(new BorderLayout());
        margenPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        margenPanel.add(panel);
        panelContenedor.add(margenPanel);
      }

      MenuEntrantes.setViewportView(panelContenedor);
      MenuEntrantes.revalidate();
      MenuEntrantes.repaint();
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al cargar los datos", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private JPanel crearPanelEntrante(String nombre, String descripcion, double precioUnitario, int idProducto) {
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
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String cantidadTexto = cantidadField.getText();
        if (!cantidadTexto.isEmpty() && cantidadTexto.matches("\\d+")) {
          cantidad = Integer.parseInt(cantidadTexto);
          if (cantidad > 0) {
            // Asignamos el idProducto y el precioUnitario a las variables de la clase
            Entrantes.this.idProducto = idProducto;
            Entrantes.this.precioUnitario = precioUnitario;

            // Llamamos al método guardarPedido, pasándole idProducto, cantidad y precioUnitario
            guardarPedido(idProducto, cantidad, precioUnitario);
            JOptionPane.showMessageDialog(panel, "Añadido " + cantidad + " de " + nombre + " a tu pedido.");
          } else {
            JOptionPane.showMessageDialog(panel, "La cantidad debe ser mayor que 0.", "Error", JOptionPane.WARNING_MESSAGE);
          }
        } else {
          JOptionPane.showMessageDialog(panel, "Por favor, introduce una cantidad válida.", "Error", JOptionPane.WARNING_MESSAGE);
        }
      }
    });

    JPanel controlPanel = new JPanel();
    controlPanel.add(new JLabel("Cantidad: "));
    controlPanel.add(cantidadField);
    controlPanel.add(addButton);

    panel.add(controlPanel, BorderLayout.SOUTH);

    return panel;
  }

  private void guardarPedido(int idProducto, int cantidad, double precioUnitario) {
    try {
      // Verificar si el cliente existe
      int idCliente = obtenerIdCliente(nifUsuario);
      if (idCliente == -1) {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Verificar si el pedido existe, si no, crear un nuevo pedido
      if (!verificarPedidoExistente(numPedido)) {
        // Crear un nuevo pedido si no existe
        String insertPedido = "INSERT INTO pedido (id_pedido, id_cliente, total_pedido, estado_pedido) VALUES (?, ?, 0, 'Activo')";
        PreparedStatement stmtInsertPedido = connection.prepareStatement(insertPedido);
        stmtInsertPedido.setInt(1, numPedido);
        stmtInsertPedido.setInt(2, idCliente);
        stmtInsertPedido.executeUpdate();
      }

      // Insertar el detalle del pedido en la tabla detalle_pedido
      double precioTotal = cantidad * precioUnitario;
      String insertDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_total) VALUES (?, ?, ?, ?)";
      PreparedStatement stmtInsertDetalle = connection.prepareStatement(insertDetalle);
      stmtInsertDetalle.setInt(1, numPedido);
      stmtInsertDetalle.setInt(2, idProducto);
      stmtInsertDetalle.setInt(3, cantidad);
      stmtInsertDetalle.setDouble(4, precioTotal);
      stmtInsertDetalle.executeUpdate();  // Ejecutar la inserción del detalle

      // Actualizar el total del pedido
      String updateTotal = "UPDATE pedido SET total_pedido = (SELECT SUM(precio_total) FROM detalle_pedido WHERE id_pedido = ?) WHERE id_pedido = ?";
      PreparedStatement stmtUpdateTotal = connection.prepareStatement(updateTotal);
      stmtUpdateTotal.setInt(1, numPedido);
      stmtUpdateTotal.setInt(2, numPedido);
      stmtUpdateTotal.executeUpdate();  // Actualizar el total del pedido

    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al guardar el pedido", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

// Método para obtener el id_cliente utilizando el NIF
// Método para obtener el id_cliente utilizando el NIF
  private int obtenerIdCliente(String nifUsuario) throws SQLException {
    String query = "SELECT id_cliente FROM cliente WHERE nif = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, nifUsuario);
    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      return rs.getInt("id_cliente");
    }

    return -1;  // Si no se encuentra el cliente, devolvemos -1
  }

  private boolean verificarPedidoExistente(int numPedido) {
    try {
      String query = "SELECT COUNT(*) FROM pedido WHERE id_pedido = ?";
      PreparedStatement stmt = connection.prepareStatement(query);
      stmt.setInt(1, numPedido);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) > 0;  // Retorna true si el pedido existe
      }
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al verificar el pedido", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;  // Retorna false si ocurre un error o si el pedido no existe
  }

  private boolean verificarNumPedidoUnico(int numPedido) {
    try {
      String query = "SELECT COUNT(*) FROM pedido WHERE id_pedido = ?";
      PreparedStatement stmt = connection.prepareStatement(query);
      stmt.setInt(1, numPedido);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) == 0;  // Si el conteo es 0, el pedido no existe
      }
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al verificar el número de pedido", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;  // Retorna false si ocurre un error o si ya existe el pedido
  }

  private int obtenerPedidoActivo(String nifUsuario) throws SQLException {
    String query = "SELECT id_pedido FROM pedido WHERE id_cliente = ? AND estado_pedido = 'Activo'";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, obtenerIdCliente(nifUsuario));  // Usamos el id_cliente de este usuario
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      return rs.getInt("id_pedido");  // Retorna el id_pedido si existe
    }
    return -1;  // Si no existe, retornamos -1 (significa que no hay un pedido activo)
  }

  private void marcarPedidoAnteriorInactivo(String nifUsuario) {
    try {
      // Obtener el id_cliente a partir del nifUsuario
      int idCliente = obtenerIdCliente(nifUsuario);
      if (idCliente == -1) {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Obtener el id del pedido activo del cliente
      int pedidoActivo = obtenerPedidoActivo(nifUsuario);
      if (pedidoActivo != -1) {
        // Actualizamos el pedido activo anterior a "Inactivo"
        String updateEstadoPedido = "UPDATE pedido SET estado_pedido = 'Inactivo' WHERE id_pedido = ?";
        PreparedStatement stmtUpdateEstado = connection.prepareStatement(updateEstadoPedido);
        stmtUpdateEstado.setInt(1, pedidoActivo);
        stmtUpdateEstado.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al actualizar el estado del pedido anterior", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public JScrollPane getScrollPaneEntrantes() {
    return MenuEntrantes;
  }

  public void mostrarTablePanel(JTable p) {
    p.setSize(1144, 797);
    p.setLocation(0, 0);

    MenuEntrantes.removeAll();
    MenuEntrantes.add(p, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    MenuEntrantes.revalidate();
    MenuEntrantes.repaint();
  }

  public void mostrarScrollPanel(JScrollPane p) {
    p.setSize(1144, 797);
    p.setLocation(0, 0);

    MenuEntrantes.removeAll();
    MenuEntrantes.add(p, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    MenuEntrantes.revalidate();
    MenuEntrantes.repaint();
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    MenuLateral = new javax.swing.JPanel();
    PanelEntrantes = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    PanelPlatos = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    PanelBebidas = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    PanelPostres = new javax.swing.JPanel();
    jLabel4 = new javax.swing.JLabel();
    PanelPedidos = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    PanelPedir = new javax.swing.JPanel();
    jLabel6 = new javax.swing.JLabel();
    SalirPanel = new javax.swing.JPanel();
    jLabel7 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    MenuSuperior = new javax.swing.JPanel();
    MenuEntrantes = new javax.swing.JScrollPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    MenuLateral.setBackground(new java.awt.Color(126, 15, 34));

    PanelEntrantes.setBackground(new java.awt.Color(150, 17, 40));
    PanelEntrantes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PanelEntrantes.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PanelEntrantesMouseClicked(evt);
      }
    });

    jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(255, 255, 255));
    jLabel1.setText("ENTRANTES");

    javax.swing.GroupLayout PanelEntrantesLayout = new javax.swing.GroupLayout(PanelEntrantes);
    PanelEntrantes.setLayout(PanelEntrantesLayout);
    PanelEntrantesLayout.setHorizontalGroup(
      PanelEntrantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelEntrantesLayout.createSequentialGroup()
        .addGap(50, 50, 50)
        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    PanelEntrantesLayout.setVerticalGroup(
      PanelEntrantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelEntrantesLayout.createSequentialGroup()
        .addContainerGap(26, Short.MAX_VALUE)
        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(24, 24, 24))
    );

    PanelPlatos.setBackground(new java.awt.Color(150, 17, 40));
    PanelPlatos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PanelPlatos.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PanelPlatosMouseClicked(evt);
      }
    });

    jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(255, 255, 255));
    jLabel2.setText("PLATOS");

    javax.swing.GroupLayout PanelPlatosLayout = new javax.swing.GroupLayout(PanelPlatos);
    PanelPlatos.setLayout(PanelPlatosLayout);
    PanelPlatosLayout.setHorizontalGroup(
      PanelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelPlatosLayout.createSequentialGroup()
        .addGap(51, 51, 51)
        .addComponent(jLabel2)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    PanelPlatosLayout.setVerticalGroup(
      PanelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPlatosLayout.createSequentialGroup()
        .addContainerGap(44, Short.MAX_VALUE)
        .addComponent(jLabel2)
        .addGap(40, 40, 40))
    );

    PanelBebidas.setBackground(new java.awt.Color(150, 17, 40));
    PanelBebidas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PanelBebidas.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PanelBebidasMouseClicked(evt);
      }
    });

    jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(255, 255, 255));
    jLabel3.setText("BEBIDAS");

    javax.swing.GroupLayout PanelBebidasLayout = new javax.swing.GroupLayout(PanelBebidas);
    PanelBebidas.setLayout(PanelBebidasLayout);
    PanelBebidasLayout.setHorizontalGroup(
      PanelBebidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelBebidasLayout.createSequentialGroup()
        .addGap(50, 50, 50)
        .addComponent(jLabel3)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    PanelBebidasLayout.setVerticalGroup(
      PanelBebidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelBebidasLayout.createSequentialGroup()
        .addGap(42, 42, 42)
        .addComponent(jLabel3)
        .addContainerGap(42, Short.MAX_VALUE))
    );

    PanelPostres.setBackground(new java.awt.Color(150, 17, 40));
    PanelPostres.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PanelPostres.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PanelPostresMouseClicked(evt);
      }
    });

    jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(255, 255, 255));
    jLabel4.setText("POSTRES");

    javax.swing.GroupLayout PanelPostresLayout = new javax.swing.GroupLayout(PanelPostres);
    PanelPostres.setLayout(PanelPostresLayout);
    PanelPostresLayout.setHorizontalGroup(
      PanelPostresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelPostresLayout.createSequentialGroup()
        .addGap(49, 49, 49)
        .addComponent(jLabel4)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    PanelPostresLayout.setVerticalGroup(
      PanelPostresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelPostresLayout.createSequentialGroup()
        .addGap(41, 41, 41)
        .addComponent(jLabel4)
        .addContainerGap(43, Short.MAX_VALUE))
    );

    PanelPedidos.setBackground(new java.awt.Color(150, 17, 40));
    PanelPedidos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PanelPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PanelPedidosMouseClicked(evt);
      }
    });

    jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(255, 255, 255));
    jLabel5.setText("MIS PEDIDOS");

    javax.swing.GroupLayout PanelPedidosLayout = new javax.swing.GroupLayout(PanelPedidos);
    PanelPedidos.setLayout(PanelPedidosLayout);
    PanelPedidosLayout.setHorizontalGroup(
      PanelPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelPedidosLayout.createSequentialGroup()
        .addGap(46, 46, 46)
        .addComponent(jLabel5)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    PanelPedidosLayout.setVerticalGroup(
      PanelPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPedidosLayout.createSequentialGroup()
        .addContainerGap(46, Short.MAX_VALUE)
        .addComponent(jLabel5)
        .addGap(38, 38, 38))
    );

    PanelPedir.setBackground(new java.awt.Color(150, 17, 40));
    PanelPedir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PanelPedir.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
    PanelPedir.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        PanelPedirMouseClicked(evt);
      }
    });

    jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(255, 255, 255));
    jLabel6.setText("FINALIZAR PEDIDO");
    jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabel6MouseClicked(evt);
      }
    });

    javax.swing.GroupLayout PanelPedirLayout = new javax.swing.GroupLayout(PanelPedir);
    PanelPedir.setLayout(PanelPedirLayout);
    PanelPedirLayout.setHorizontalGroup(
      PanelPedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPedirLayout.createSequentialGroup()
        .addContainerGap(45, Short.MAX_VALUE)
        .addComponent(jLabel6)
        .addGap(37, 37, 37))
    );
    PanelPedirLayout.setVerticalGroup(
      PanelPedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(PanelPedirLayout.createSequentialGroup()
        .addGap(36, 36, 36)
        .addComponent(jLabel6)
        .addContainerGap(36, Short.MAX_VALUE))
    );

    SalirPanel.setBackground(new java.awt.Color(87, 0, 19));
    SalirPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    SalirPanel.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        SalirPanelMouseClicked(evt);
      }
    });

    jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel7.setForeground(new java.awt.Color(255, 255, 255));
    jLabel7.setText("SALIR");

    javax.swing.GroupLayout SalirPanelLayout = new javax.swing.GroupLayout(SalirPanel);
    SalirPanel.setLayout(SalirPanelLayout);
    SalirPanelLayout.setHorizontalGroup(
      SalirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(SalirPanelLayout.createSequentialGroup()
        .addGap(97, 97, 97)
        .addComponent(jLabel7)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    SalirPanelLayout.setVerticalGroup(
      SalirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(SalirPanelLayout.createSequentialGroup()
        .addGap(42, 42, 42)
        .addComponent(jLabel7)
        .addContainerGap(42, Short.MAX_VALUE))
    );

    jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(255, 255, 255));
    jLabel8.setText("Sabores Digitales");

    jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LogoPngPequeño.png"))); // NOI18N

    javax.swing.GroupLayout MenuLateralLayout = new javax.swing.GroupLayout(MenuLateral);
    MenuLateral.setLayout(MenuLateralLayout);
    MenuLateralLayout.setHorizontalGroup(
      MenuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(PanelEntrantes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(PanelPlatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(PanelBebidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(PanelPostres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(PanelPedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(PanelPedir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(SalirPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(MenuLateralLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(MenuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(MenuLateralLayout.createSequentialGroup()
            .addGap(35, 35, 35)
            .addComponent(jLabel8))
          .addComponent(jLabel9))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    MenuLateralLayout.setVerticalGroup(
      MenuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(MenuLateralLayout.createSequentialGroup()
        .addGap(13, 13, 13)
        .addGroup(MenuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(MenuLateralLayout.createSequentialGroup()
            .addGap(4, 4, 4)
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(PanelEntrantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(PanelPlatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(PanelBebidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(PanelPostres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(PanelPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(PanelPedir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(SalirPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(44, Short.MAX_VALUE))
    );

    getContentPane().add(MenuLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 256, 863));

    MenuSuperior.setBackground(new java.awt.Color(126, 15, 34));
    MenuSuperior.setForeground(new java.awt.Color(204, 204, 204));

    javax.swing.GroupLayout MenuSuperiorLayout = new javax.swing.GroupLayout(MenuSuperior);
    MenuSuperior.setLayout(MenuSuperiorLayout);
    MenuSuperiorLayout.setHorizontalGroup(
      MenuSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 1144, Short.MAX_VALUE)
    );
    MenuSuperiorLayout.setVerticalGroup(
      MenuSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 66, Short.MAX_VALUE)
    );

    getContentPane().add(MenuSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(256, 0, 1144, 66));

    MenuEntrantes.setBackground(new java.awt.Color(176, 71, 94));
    MenuEntrantes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    getContentPane().add(MenuEntrantes, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 66, 1144, 797));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void PanelEntrantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelEntrantesMouseClicked
// TODO add your handling code here:setVisible(true);
    this.dispose();

    // Crea una nueva instancia de la clase Entrantes y la hace visible
    Entrantes ventanaEntrantes = new Entrantes();
    ventanaEntrantes.setVisible(true);

  }//GEN-LAST:event_PanelEntrantesMouseClicked

  private void PanelPlatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPlatosMouseClicked

    setVisible(true);
    mostrarScrollPanel(new Platos().getScrollPanePlatos()); // TODO add your handling code here:
  }//GEN-LAST:event_PanelPlatosMouseClicked

  private void PanelBebidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelBebidasMouseClicked

    setVisible(true);
    mostrarScrollPanel(new Bebidas().getScrollPaneBebidas());    // TODO add your handling code here:
  }//GEN-LAST:event_PanelBebidasMouseClicked

  private void PanelPostresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPostresMouseClicked

    setVisible(true);
    mostrarScrollPanel(new Postres().getScrollPanePostres());      // TODO add your handling code here:
  }//GEN-LAST:event_PanelPostresMouseClicked

  private void PanelPedirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPedirMouseClicked
    try {
      // Obtener el id_cliente a partir del nifUsuario
      int idCliente = obtenerIdCliente(nifUsuario);

      if (idCliente == -1) {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Comprobar si hay un pedido activo
      int pedidoActivo = obtenerPedidoActivo(nifUsuario);
      if (pedidoActivo == -1) {
        // Obtener el último id_pedido y sumarle 1 para el siguiente pedido
        int ultimoPedido = obtenerUltimoNumeroPedido();
        numPedido = ultimoPedido + 1; // Ahora sumamos 1 al último id_pedido

        String insertPedido = "INSERT INTO pedido (id_pedido, id_cliente, total_pedido, estado_pedido) VALUES (?, ?, 0, 'Activo')";
        PreparedStatement stmtInsertPedido = connection.prepareStatement(insertPedido);
        stmtInsertPedido.setInt(1, numPedido); // Inserta el nuevo id_pedido
        stmtInsertPedido.setInt(2, idCliente); // Inserta el id_cliente
        stmtInsertPedido.executeUpdate();
      }

      // Después de crear el nuevo pedido, marcamos el anterior como "Inactivo"
      marcarPedidoAnteriorInactivo(nifUsuario);
      JOptionPane.showMessageDialog(this, "El pedido se ha realizado exitosamente.", "Pedido Realizado", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al procesar el pedido", "Error", JOptionPane.ERROR_MESSAGE);
    }

// Actualizar el número de pedido en los datos del usuario
    numPedido = Modelo.DatosUsuario.setNpedido(numPedido);
  }//GEN-LAST:event_PanelPedirMouseClicked

  private void SalirPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalirPanelMouseClicked
    // TODO add your handling code here:
    this.dispose();

    // Crea una nueva instancia de la clase Entrantes y la hace visible
    Inicio ventanaInicio = new Inicio();
    ventanaInicio.setVisible(true);
  }//GEN-LAST:event_SalirPanelMouseClicked

  private void PanelPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPedidosMouseClicked
    // TODO add your handling code here:
    setVisible(true);
    mostrarScrollPanel(new MisPedidos().getScrollPaneMisPedidos());
  }//GEN-LAST:event_PanelPedidosMouseClicked

  private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
    // TODO add your handling code here:
    try {
      // Obtener el id_cliente a partir del nifUsuario
      int idCliente = obtenerIdCliente(nifUsuario);

      if (idCliente == -1) {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Comprobar si hay un pedido activo
      int pedidoActivo = obtenerPedidoActivo(nifUsuario);
      if (pedidoActivo == -1) {
        // Obtener el último id_pedido y sumarle 1 para el siguiente pedido
        int ultimoPedido = obtenerUltimoNumeroPedido();
        numPedido = ultimoPedido + 1; // Ahora sumamos 1 al último id_pedido

        String insertPedido = "INSERT INTO pedido (id_pedido, id_cliente, total_pedido, estado_pedido) VALUES (?, ?, 0, 'Activo')";
        PreparedStatement stmtInsertPedido = connection.prepareStatement(insertPedido);
        stmtInsertPedido.setInt(1, numPedido); // Inserta el nuevo id_pedido
        stmtInsertPedido.setInt(2, idCliente); // Inserta el id_cliente
        stmtInsertPedido.executeUpdate();
      }

      // Después de crear el nuevo pedido, marcamos el anterior como "Inactivo"
      marcarPedidoAnteriorInactivo(nifUsuario);
      JOptionPane.showMessageDialog(this, "El pedido se ha realizado exitosamente.", "Pedido Realizado", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error al procesar el pedido", "Error", JOptionPane.ERROR_MESSAGE);
    }

// Actualizar el número de pedido en los datos del usuario
    numPedido = Modelo.DatosUsuario.setNpedido(numPedido);
  }//GEN-LAST:event_jLabel6MouseClicked

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;

        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(Entrantes.class
        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Entrantes.class
        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Entrantes.class
        .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Entrantes.class
        .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Entrantes().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane MenuEntrantes;
  private javax.swing.JPanel MenuLateral;
  private javax.swing.JPanel MenuSuperior;
  private javax.swing.JPanel PanelBebidas;
  private javax.swing.JPanel PanelEntrantes;
  private javax.swing.JPanel PanelPedidos;
  private javax.swing.JPanel PanelPedir;
  private javax.swing.JPanel PanelPlatos;
  private javax.swing.JPanel PanelPostres;
  private javax.swing.JPanel SalirPanel;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  // End of variables declaration//GEN-END:variables
}
