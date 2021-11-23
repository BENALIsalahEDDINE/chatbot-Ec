package pl.lodz.p.it.masi.stp.chatbot.services;

import com.itextpdf.text.DocumentException;

public interface ReportService {
    byte[] generateReport(String ipAddress) throws DocumentException;
}
