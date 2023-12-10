package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/invoices")
public class InvoiceController {
    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping(value = "/index")
    public String invoices(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<InvoiceDTO> invoiceList;
        if (keyword.isEmpty()) {
            invoiceList = new CopyOnWriteArrayList<>(invoiceService.fetchAllDto());
        } else {
            invoiceList = new CopyOnWriteArrayList<>(invoiceService.findInvoiceDtoByInvoiceNumber(Integer.parseInt(keyword)));
        }
        model.addAttribute(LIST_INVOICES, invoiceList);
        model.addAttribute(KEYWORD, keyword);
        return "invoice-views/invoices";
    }

    @PostMapping(value = "/changePaymentStatus")
    public String changePaymentStatus(Long invoiceId, String paymentState) {
        //invoiceService.changePaymentStatus(invoiceId, paymentState);
        return "redirect:/invoices/index";
    }
}
