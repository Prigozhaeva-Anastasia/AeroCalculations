package com.prigozhaeva.aerocalculations.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private String messageId;
    private String senderEmail;
    private String subject;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime localDateTime;
    private String filePath;
    private String text;
}
