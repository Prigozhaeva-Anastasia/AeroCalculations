package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.dto.MessageDTO;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import com.prigozhaeva.aerocalculations.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/messages")
public class MessageController {
    private MessageService messageService;
    private InvoiceService invoiceService;

    public MessageController(MessageService messageService, InvoiceService invoiceService) {
        this.messageService = messageService;
        this.invoiceService = invoiceService;
    }

    @GetMapping(value = "/unreadMsgs")
    public String unreadMsgs(Model model) {
        List<MessageDTO> messages = messageService.showUnreadMsgs();
        Collections.sort(messages, Comparator.comparing(MessageDTO::getLocalDateTime).reversed());
        model.addAttribute(MESSAGES, messages);
        return "proofOfPayment-views/mainFormForMessages";
    }

    @GetMapping(value = "/unreadMessage/{messageId}")
    public String showMessage(@PathVariable String messageId, Model model) {
        MessageDTO messageDTO = messageService.findById(messageId);
        messageService.markMessageAsSeen(messageId);
        model.addAttribute(CONFIRM_MESSAGE, "");
        model.addAttribute(MESSAGE, messageDTO);
        return "proofOfPayment-views/formForMessage";
    }

    @PostMapping(value = "/unreadMessage/signatureVerification")
    public String signatureVerification(MessageDTO messageDTO, Model model) {
        if (invoiceService.signatureVerification(messageDTO.getFilePath())) {
            try {
                String fileName = Paths.get(messageDTO.getFilePath()).getFileName().toString();
                int invoiceNumber = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf('.')));
                Invoice invoice = invoiceService.findInvoiceByInvoiceNumber(invoiceNumber);
                invoice.setPaymentState(PAID_STATUS);
                invoiceService.createOrUpdateInvoice(invoice);
            } catch (NumberFormatException e) {}
            model.addAttribute(CONFIRM_MESSAGE, "Документ подтвержден");
        } else {
            model.addAttribute(CONFIRM_MESSAGE, "Документ не подтвержден");
        }
        model.addAttribute(MESSAGE, messageDTO);
        return "proofOfPayment-views/formForMessage";
    }

    @GetMapping(value = "/readMsgs")
    public String readMsgs(Model model) {
        List<MessageDTO> messages = messageService.showReadMsgs();
        Collections.sort(messages, Comparator.comparing(MessageDTO::getLocalDateTime).reversed());
        model.addAttribute(MESSAGES, messages);
        return "proofOfPayment-views/mainFormForMessages";
    }
}
