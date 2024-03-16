package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.dto.MessageDTO;
import com.prigozhaeva.aerocalculations.service.MessageService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import com.sun.mail.smtp.SMTPSendFailedException;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.mail.search.MessageIDTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static com.prigozhaeva.aerocalculations.constant.Constant.MINSK_AIRPORT_EMAIL;
import static com.prigozhaeva.aerocalculations.constant.Constant.MINSK_AIRPORT_PASSWORD_FOR_EMAIL;

@Service
public class MessageServiceImpl implements MessageService {
    private MappingUtils mappingUtils;

    public MessageServiceImpl(MappingUtils mappingUtils) {
        this.mappingUtils = mappingUtils;
    }

    private Store connectToMailServer() throws MessagingException {
        String host = "imap.mail.ru";
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");
        Session session = Session.getInstance(properties);
        Store store = session.getStore("imap");
        store.connect(host, MINSK_AIRPORT_EMAIL, MINSK_AIRPORT_PASSWORD_FOR_EMAIL);
        return store;
    }

    @Override
    public List<MessageDTO> showUnreadMsgs() {
        List<MessageDTO> messageList = new ArrayList<>();
        try (Store store = connectToMailServer();
             Folder inbox = store.getFolder("INBOX")) {
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (Message message : messages) {
                messageList.add(mappingUtils.mapToMessageDTO(message));
            }
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        return messageList;
    }

    @Override
    public MessageDTO findById(String messageId) {
        try (Store store = connectToMailServer();
             Folder inbox = store.getFolder("INBOX")) {
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(new MessageIDTerm(messageId));
            if (messages.length > 0) {
                return mappingUtils.mapToMessageDTOWithAttachment(messages[0]);
            }
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void markMessageAsSeen(String messageId) {
        try (Store store = connectToMailServer();
             Folder inbox = store.getFolder("INBOX")) {
            inbox.open(Folder.READ_WRITE);
            Message[] messages = inbox.search(new MessageIDTerm(messageId));
            for (Message message : messages) {
                message.setFlag(Flags.Flag.SEEN, true);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MessageDTO> showReadMsgs() {
        List<MessageDTO> messageList = new ArrayList<>();
        try (Store store = connectToMailServer();
             Folder inbox = store.getFolder("INBOX")) {
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
            for (Message message : messages) {
                messageList.add(mappingUtils.mapToMessageDTO(message));
            }
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        return messageList;
    }

    @Override
    public int sendOTPToEmail(String email) throws MessagingException {
        Random rand = new Random();
        int otpValue = rand.nextInt(1255650);
        String to = email;
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.mail.ru");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MINSK_AIRPORT_EMAIL, MINSK_AIRPORT_PASSWORD_FOR_EMAIL);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MINSK_AIRPORT_EMAIL));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Восстановление пароля");
        message.setText("Ваш OTP: " + otpValue);
        Transport.send(message);
        return otpValue;
    }
}
