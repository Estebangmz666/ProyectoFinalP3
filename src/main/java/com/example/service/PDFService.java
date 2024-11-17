package com.example.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.example.model.Transaction;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PDFService {

    public static void generateTransactionReport(List<Transaction> transactions, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDType1Font font = PDType1Font.HELVETICA_BOLD;
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 750);
                contentStream.showText("Reporte de Transacciones");
                contentStream.newLine();
                contentStream.newLine();
                for (Transaction transaction : transactions) {
                    contentStream.showText("ID: " + transaction.getTransactionId());
                    contentStream.newLine();
                    contentStream.showText("Fecha: " + transaction.getDate());
                    contentStream.newLine();
                    contentStream.showText("Monto: " + transaction.getAmount());
                    contentStream.newLine();
                    contentStream.showText("Descripción: " + transaction.getDescription());
                    contentStream.newLine();
                    contentStream.newLine();
                }
                contentStream.endText();
            }
            document.save(filePath);
            System.out.println("PDF generado con éxito en: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void generatePDFReport(List<Transaction> transactions, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Transacciones");
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            PDFService.generateTransactionReport(transactions, file.getAbsolutePath());
        }
    }
}