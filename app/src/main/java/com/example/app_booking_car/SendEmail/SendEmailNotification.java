package com.example.app_booking_car.SendEmail;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailNotification {

    private String senderEmail = "quanlynhanviencuahangcaffe@gmail.com";
    private String senderPassword = "pqrqailzcdxlyuyk";

    public void sendNotification(String recipientEmail, String subject, String messageContent) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(messageContent);

            Transport.send(message);

            System.out.println("Gửi thông báo qua email thành công.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Gửi thông báo qua email không thành công.");
        }
    }
}
