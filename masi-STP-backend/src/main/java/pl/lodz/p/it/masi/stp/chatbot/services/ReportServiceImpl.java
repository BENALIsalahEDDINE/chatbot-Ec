package pl.lodz.p.it.masi.stp.chatbot.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.masi.stp.chatbot.model.collections.logging.ConversationLog;
import pl.lodz.p.it.masi.stp.chatbot.model.collections.logging.MessageLog;
import pl.lodz.p.it.masi.stp.chatbot.repositories.ConversationLogsRepository;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReportServiceImpl implements ReportService {

    private final ConversationLogsRepository logsRepository;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);

    private static Font subCatFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);

    @Autowired
    public ReportServiceImpl(ConversationLogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    @Override
    public byte[] generateReport(String ipAddress) throws DocumentException {
        byte[] out;
        Document document = new Document();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);

        List<ConversationLog> conversationLogs;

        if (ipAddress != null) {
            conversationLogs = logsRepository.findAllByUserIp(ipAddress);
        } else {
            conversationLogs = logsRepository.findAll();
        }

        document.open();

        if (ipAddress != null) {
            document.addTitle(ipAddress + LocalDateTime.now());
        } else {
            document.addTitle(LocalDateTime.now().toString());
        }

        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        if (ipAddress != null) {
            preface.add(new Paragraph("Report for " + ipAddress + " at: " + LocalDateTime.now().toString(), catFont));
        } else {
            preface.add(new Paragraph("Report for all conversations at: " + LocalDateTime.now().toString(), catFont));
        }

        addEmptyLine(preface, 5);

        addAclContent(document, conversationLogs);

        addQeiContent(document, conversationLogs);

        addCfiReport(document, conversationLogs);

        addCusReport(document, conversationLogs);

        addCesReport(document, conversationLogs);

        document.close();
        out = os.toByteArray();
        return out;
    }

    private void addAclContent(Document document, List<ConversationLog> conversationLogs) throws DocumentException {
        Paragraph aclTitle = new Paragraph();
        aclTitle.add(new Paragraph("--------   ACL report   --------", catFont));
        aclTitle.add(new Paragraph("AVERAGE: " + countAverageNumberOfQuestions(), subCatFont));
        addEmptyLine(aclTitle, 3);
        document.add(aclTitle);

        for (ConversationLog conversationLog : conversationLogs) {
            Paragraph acl = new Paragraph();
            acl.add(new Paragraph("ACL for " + conversationLog.getConversationId(), subCatFont));
            if (conversationLog.getUserIp() != null) {
                acl.add(new Paragraph("User ip: " + conversationLog.getUserIp(), subCatFont));
            }
            acl.add(new Paragraph("Total number of questions: " + conversationLog.getQuestionsCounter(), subFont));
            document.add(acl);
        }
    }

    private void addQeiContent(Document document, List<ConversationLog> conversationLogs) throws DocumentException {
        Paragraph qeiTitle = new Paragraph();
        addEmptyLine(qeiTitle, 2);
        qeiTitle.add(new Paragraph("--------   QEI report   --------", catFont));
        document.add(qeiTitle);

        for (ConversationLog conversationLog : conversationLogs) {
            Paragraph qei = new Paragraph();
            qei.add(new Paragraph("QEI for " + conversationLog.getConversationId(), subCatFont));
            if (conversationLog.getUserIp() != null) {
                qei.add(new Paragraph("User ip: " + conversationLog.getUserIp(), subCatFont));
            }
            document.add(qei);
            for (int i = 0; i < conversationLog.getMessagesLogs().size(); i++) {
                Paragraph messageQei = new Paragraph();
                BigInteger resultCount = conversationLog.getMessagesLogs().get(i).getResultsCount();
                if (!Objects.equals(resultCount, BigInteger.ZERO) && resultCount != null) {
                    if (i != 0) {
                        MessageLog messageLog = conversationLog.getMessagesLogs().get(i - 1);
                        if (messageLog.getWatsonOutput().contains("I'm sorry but I didn't get that. Could you be more specific?")) {
                            continue;
                        } else {
                            BigInteger previousResultCount = messageLog.getResultsCount();
                            if (previousResultCount != null) {
                                messageQei.add(new Paragraph("Question: " + messageLog.getWatsonOutput().toString(), subFont));
                                messageQei.add(new Paragraph("QEI: " + countQEI(previousResultCount.doubleValue(), resultCount.doubleValue())));
                            }
                        }
                    } else {
                        messageQei.add(new Paragraph("Question: init question", subFont));
                        messageQei.add(new Paragraph("QEI: " + countQEI(167795d, resultCount.doubleValue())));
                    }
                }
                document.add(messageQei);
            }
        }
    }

    private void addCfiReport(Document document, List<ConversationLog> conversationLogs) throws DocumentException {
        Paragraph cfiTitle = new Paragraph();
        addEmptyLine(cfiTitle, 2);
        cfiTitle.add(new Paragraph("--------   CFI report   --------", catFont));
        cfiTitle.add(new Paragraph("AVERAGE: " + countAverageMisunderstoodQuestions(), subCatFont));
        document.add(cfiTitle);

        for (ConversationLog conversationLog : conversationLogs) {
            Paragraph cfi = new Paragraph();
            cfi.add(new Paragraph("CFI for " + conversationLog.getConversationId(), subCatFont));
            if (conversationLog.getUserIp() != null) {
                cfi.add(new Paragraph("User ip: " + conversationLog.getUserIp(), subCatFont));
            }
            cfi.add(new Paragraph("Total number of misunderstood questions: " + conversationLog.getMisunderstoodQuestionsCounter(), subFont));
            document.add(cfi);
        }
    }

    private void addCusReport(Document document, List<ConversationLog> conversationLogs) throws DocumentException {
        Paragraph cusTitle = new Paragraph();
        addEmptyLine(cusTitle, 2);
        cusTitle.add(new Paragraph("--------   CUS report   --------", catFont));
        cusTitle.add(new Paragraph("AVERAGE: " + countAverageChatbotUsability(), subCatFont));
        document.add(cusTitle);

        for (ConversationLog conversationLog : conversationLogs) {
            Paragraph cus = new Paragraph();
            cus.add(new Paragraph("CUS for " + conversationLog.getConversationId(), subCatFont));
            if (conversationLog.getUserIp() != null) {
                cus.add(new Paragraph("User ip: " + conversationLog.getUserIp(), subCatFont));
            }
            cus.add(new Paragraph("Chatbot usability score: " + conversationLog.getChatbotUsabilityScore(), subFont));
            document.add(cus);
        }
    }

    private void addCesReport(Document document, List<ConversationLog> conversationLogs) throws DocumentException {
        Paragraph cesTitle = new Paragraph();
        addEmptyLine(cesTitle, 2);
        cesTitle.add(new Paragraph("--------   CES report   --------", catFont));
        cesTitle.add(new Paragraph("AVERAGE: " + countAverageChatbotEffectiveness(), subCatFont));
        document.add(cesTitle);

        for (ConversationLog conversationLog : conversationLogs) {
            Paragraph ces = new Paragraph();
            ces.add(new Paragraph("CES for " + conversationLog.getConversationId(), subCatFont));
            if (conversationLog.getUserIp() != null) {
                ces.add(new Paragraph("User ip: " + conversationLog.getUserIp(), subCatFont));
            }
            ces.add(new Paragraph("Chatbot effectiveness score: " + conversationLog.getChatbotEffectivenessScore(), subFont));
            document.add(ces);
        }
    }

    private Double countAverageNumberOfQuestions() {
        List<ConversationLog> conversationLogs = logsRepository.findAll();
        Double sum = 0.0;
        for (ConversationLog conversationLog : conversationLogs) {
            sum += conversationLog.getQuestionsCounter();
        }
        return sum / conversationLogs.size();
    }

    private Double countAverageMisunderstoodQuestions() {
        List<ConversationLog> conversationLogs = logsRepository.findAll();
        Double sum = 0.0;
        for (ConversationLog conversationLog : conversationLogs) {
            sum += conversationLog.getMisunderstoodQuestionsCounter();
        }
        return sum / conversationLogs.size();
    }

    private Double countQEI(Double numberOfProductsBefore, Double numberOfProductsAfter) {
        if (numberOfProductsBefore == 0) {
            numberOfProductsBefore = 167795d;
        }
        return ((numberOfProductsBefore - numberOfProductsAfter) / numberOfProductsBefore) * 100;
    }

    private Double countAverageChatbotUsability() {
        List<ConversationLog> conversationLogs = logsRepository.findAll();
        Double sum = 0.0;
        for (ConversationLog conversationLog : conversationLogs) {
            if (conversationLog.getChatbotUsabilityScore() != null) {
                if (isNumeric(conversationLog.getChatbotUsabilityScore())) {
                    sum += Double.parseDouble(conversationLog.getChatbotUsabilityScore());
                } else {
                    sum += 5;
                }
            } else {
                sum += 5;
            }
        }
        return sum / conversationLogs.size();
    }

    private Double countAverageChatbotEffectiveness() {
        List<ConversationLog> conversationLogs = logsRepository.findAll();
        Double sum = 0.0;
        for (ConversationLog conversationLog : conversationLogs) {
            if (conversationLog.getChatbotUsabilityScore() != null) {
                if (isNumeric(conversationLog.getChatbotEffectivenessScore())) {
                    sum += Double.parseDouble(conversationLog.getChatbotEffectivenessScore());
                } else {
                    sum += 5;
                }
            } else {
                sum += 5;
            }
        }
        return sum / conversationLogs.size();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
