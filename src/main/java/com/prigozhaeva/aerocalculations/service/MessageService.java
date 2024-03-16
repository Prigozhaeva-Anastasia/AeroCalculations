package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.dto.MessageDTO;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.util.List;

public interface MessageService {
    List<MessageDTO> showUnreadMsgs();
    MessageDTO findById(String messageId);
    void markMessageAsSeen(String messageId);
    List<MessageDTO> showReadMsgs();
    int sendOTPToEmail(String email) throws MessagingException;
}
