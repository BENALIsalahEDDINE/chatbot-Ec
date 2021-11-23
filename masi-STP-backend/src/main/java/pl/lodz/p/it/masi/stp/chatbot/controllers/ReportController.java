package pl.lodz.p.it.masi.stp.chatbot.controllers;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.masi.stp.chatbot.services.ConversationService;
import pl.lodz.p.it.masi.stp.chatbot.services.ReportService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = "/{ipAddress}/pdf", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity generateReport(@PathVariable String ipAddress) {
        try {
            byte[] out = reportService.generateReport(ipAddress);
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.add("content-disposition", "attachment; filename=" + ipAddress + LocalDateTime.now() + ".pdf");
            hHeaders.add("Content-Type", "application/pdf");
            return new ResponseEntity(out, hHeaders, HttpStatus.OK);
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        }
    }

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity generateFullReport() {
        try {
            byte[] out = reportService.generateReport(null);
            HttpHeaders hHeaders = new HttpHeaders();
            hHeaders.add("content-disposition", "attachment; filename=" + LocalDateTime.now() + ".pdf");
            hHeaders.add("Content-Type", "application/pdf");
            return new ResponseEntity(out, hHeaders, HttpStatus.OK);
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        }
    }
}
