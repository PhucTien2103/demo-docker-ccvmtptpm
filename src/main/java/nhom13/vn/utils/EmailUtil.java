package nhom13.vn.utils;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailUtil {

    public static boolean sendEmail(String toEmail, String newPassword) {

        final String fromEmail = "levanphong511@gmail.com";
        final String appPassword = "mejxzqzjhjpwrnmx";

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, appPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromEmail));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            message.setSubject("Reset Password");

            message.setText("Your new password is: " + newPassword);

            Transport.send(message);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }
    }
}