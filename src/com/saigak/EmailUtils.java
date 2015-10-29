package com.saigak;import com.sun.mail.smtp.SMTPMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.lang.String;import java.util.*;import java.util.List;import java.util.Map;import java.util.TreeMap;import java.util.UUID;

/**
 * Created by root on 22.10.15.
 */
public class EmailUtils {


    void send(Message message, String recipient) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        Transport.send(message);
    }

    void send(Message message, List<String> recipient) throws MessagingException {
        Address[] addresses = new Address[recipient.size()];
        int i = 0;
        for (String s : recipient) {
            addresses[i] = new InternetAddress(s);
            i++;
        }
        message.setRecipients(Message.RecipientType.TO, addresses);
        Transport.send(message);
    }

    MimeMultipart buildMultipart(String html) throws MessagingException, IOException {

        MimeMultipart content = new MimeMultipart("related");

        Document document = Jsoup.parse(html);
//        document.charset();
        Elements img = document.select("img");

        Map<String, File> map = new TreeMap<>();

        for (Element element : img) {
            UUID cid = UUID.randomUUID();
            File file = new File(element.attr("src"));
            element.attr("src", "cid:" + cid.toString());
            map.put(cid.toString(), file);
        }

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(document.html(), "UTF-8", "html");
        content.addBodyPart(textPart);

        for (Map.Entry<String, File> entry : map.entrySet()) {
            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile(entry.getValue());
            imagePart.setContentID("<" + entry.getKey() + ">");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            content.addBodyPart(imagePart);
        }

        return content;

    }


    public Session buildMAndrill() {

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.transport.protocol", "smtp");
        mailProperties.setProperty("mail.smtp.host", "smtp.mandrillapp.com");
        mailProperties.setProperty("mail.smtp.port", String.valueOf(587));
        mailProperties.setProperty("mail.smtp.user", "khrupalik@gmail.com");
        final Session session = Session.getInstance(mailProperties, null);
        session.setPasswordAuthentication(new URLName("smtp", "smtp.mandrillapp.com", -1, null, "khrupalik@gmail.com", null),
                new PasswordAuthentication("khrupalik@gmail.com", "6MoQigiQVKsJr1wr10jReg"));
        session.setDebug(true);
        return session;

    }

    public Session buildGoogleSession() {
        final String username = "sgrdhtfh@gmail.com";
        final String password = "moskit22212";
        Properties mailProps = new Properties();
        mailProps.put("mail.transport.protocol", "smtp");
        mailProps.put("mail.host", "smtp.gmail.com");
        mailProps.put("mail.from", "RamadaLviv");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.port", "587");
        mailProps.put("mail.smtp.auth", "true");
        // final, because we're using it in the closure below
        final PasswordAuthentication usernamePassword = new PasswordAuthentication(
                username, password);
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return usernamePassword;
            }
        };
        Session session = Session.getInstance(mailProps, auth);
        session.setDebug(true);
        return session;

    }
}
