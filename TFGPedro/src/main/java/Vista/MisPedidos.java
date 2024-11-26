/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Controlador.MisPedidosController;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Pedro Rollán García
 */
public class MisPedidos extends javax.swing.JFrame {

  private JTable tablaPedidos;
  private DefaultTableModel modeloTabla;
  private JButton btnEnviarTicket;

  public MisPedidos() {
    FlatLightOwlIJTheme.setup();
    initComponents();
    inicializarVista();
    MisPedidosController.cargarPedidos(modeloTabla, Modelo.DatosUsuario.getNif(), this);
  }

  private void inicializarVista() {
    btnEnviarTicket = new JButton("Enviar ticket");

    btnEnviarTicket.setFont(new Font("Arial", Font.BOLD, 16));
    btnEnviarTicket.setBackground(new Color(0xb31731));
    btnEnviarTicket.setForeground(Color.WHITE);
    btnEnviarTicket.setFocusPainted(false);
    btnEnviarTicket.setPreferredSize(new Dimension(200, 50));
    btnEnviarTicket.putClientProperty("JButton.arc", 50);
    btnEnviarTicket.setCursor(new Cursor(Cursor.HAND_CURSOR));

    btnEnviarTicket.addActionListener(e -> MisPedidosController.enviarTicket(modeloTabla, this));

    modeloTabla = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    modeloTabla.addColumn("Pedido/Plato");
    modeloTabla.addColumn("Cantidad");
    modeloTabla.addColumn("Precio");

    tablaPedidos = new JTable(modeloTabla);
    tablaPedidos.setRowHeight(25);
    tablaPedidos.setDefaultRenderer(Object.class, new CustomTableRenderer());

    JPanel panelContenido = new JPanel();
    panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
    JScrollPane tablaScrollPane = new JScrollPane(tablaPedidos);
    panelContenido.add(tablaScrollPane);
    panelContenido.add(Box.createVerticalStrut(10));
    panelContenido.add(btnEnviarTicket);

    menuMisPedidos = new JScrollPane(panelContenido);
    this.setLayout(new BorderLayout());
    this.add(menuMisPedidos, BorderLayout.CENTER);
  }

  public JScrollPane getScrollPaneMisPedidos() {
    return menuMisPedidos;
  }

  private static class CustomTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (table.getValueAt(row, 1).equals("") && table.getValueAt(row, 2).equals("")
        && table.getValueAt(row, 0).toString().startsWith("Pedido #")) {
        c.setFont(c.getFont().deriveFont(Font.BOLD));
        int red = 180 + (int) (Math.random() * 75);
        int green = 180 + (int) (Math.random() * 75);
        int blue = 180 + (int) (Math.random() * 75);
        c.setBackground(new Color(red, green, blue));
      } else if (table.getValueAt(row, 0).equals("Total General")) {
        c.setFont(c.getFont().deriveFont(Font.BOLD));
        c.setBackground(new Color(200, 255, 200));
      } else {
        c.setFont(c.getFont().deriveFont(Font.PLAIN));
        c.setBackground(Color.WHITE);
      }
      return c;
    }
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

    menuMisPedidos = new javax.swing.JScrollPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    menuMisPedidos.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(menuMisPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 1144, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(menuMisPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

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
      java.util.logging.Logger.getLogger(MisPedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(MisPedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(MisPedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(MisPedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MisPedidos().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane menuMisPedidos;
  // End of variables declaration//GEN-END:variables
}
