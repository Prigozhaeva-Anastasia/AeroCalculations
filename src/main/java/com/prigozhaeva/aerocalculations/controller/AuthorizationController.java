package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.service.MessageService;
import com.sun.mail.smtp.SMTPSendFailedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;

import static com.prigozhaeva.aerocalculations.constant.Constant.EMAIL;
import static com.prigozhaeva.aerocalculations.constant.Constant.MESSAGE;

@Controller
public class AuthorizationController {
    private MessageService messageService;

    public AuthorizationController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/login")
    public String authorized() {
        return "main-views/main";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "authorization-views/authorization";
    }

    @GetMapping(value = "/forgotPasswordForm")
    public String forgotPasswordForm() {
        return "authorization-views/forgotPasswordForm";
    }

    @PostMapping(value = "/forgotPassword")
    public String forgotPassword(String email, Model model) {
        try {
            messageService.sendOTPToEmail(email);
        } catch (SMTPSendFailedException e) {
            model.addAttribute(MESSAGE, "Пользователь с таким email не найден");
            model.addAttribute(EMAIL, email);
            return "authorization-views/forgotPasswordForm";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "authorization-views/enterOtp";
    }
}
