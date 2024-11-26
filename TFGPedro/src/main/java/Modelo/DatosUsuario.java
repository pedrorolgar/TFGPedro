/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.*;

/**
 *
 * @author Pedro Rollán García
 */
public class DatosUsuario {
// Campo estático para almacenar el NIF

  private static int nPedido;
  private static String nif;
  private static String correo;

  // Método para establecer el NIF
  public static void setNif(String nif) {
    DatosUsuario.nif = nif;
  }

  // Método para obtener el NIF
  public static String getNif() {
    return nif;
  }

  public static int setNpedido(int nPedido) {
    DatosUsuario.nPedido = nPedido;
    return nPedido;
  }

  // Método para obtener el NIF
  public static int getNpedido() {
    return nPedido;
  }

  public static void setCorreo(String correo) {
    DatosUsuario.correo = correo;
  }

  // Método para obtener el NIF
  public static String getCorreo() {
    return correo;
  }
}
