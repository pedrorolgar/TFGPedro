/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Clase para enviar correos electrónicos.
 */
public class EmailUtil {

  private final static String FROMEMAIL = "pedrorollangarcia.03@gmail.com";
  private final static String PASSWORD = "tvcrfmabvkpacpbs"; // Contraseña de aplicación

  /**
   * Envía un correo electrónico con el ticket generado.
   *
   * @param toEmail Dirección de correo del destinatario.
   * @param subject Asunto del correo.
   * @param body Contenido del ticket.
   *
   * @return true si se envió correctamente; false en caso contrario.
   */
  public static boolean sendTicketEmail(String toEmail, String subject, String body) {
    try {
      // Configuración de las propiedades para Gmail
      Properties props = new Properties();
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true"); // Habilitar STARTTLS
      props.put("mail.smtp.ssl.enable", "false");    // Usar STARTTLS, no SSL

      // Autenticación
      Authenticator auth = new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(FROMEMAIL, PASSWORD);
        }
      };

      // Crear la sesión de correo
      Session session = Session.getInstance(props, auth);

      // Crear el mensaje
      MimeMessage msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(FROMEMAIL, "NoReply-Tickets"));
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
      msg.setSubject(subject, "UTF-8");
      msg.setSentDate(new Date());
      msg.setContent(body, "text/html; charset=UTF-8");

      // Enviar el correo
      Transport.send(msg);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
