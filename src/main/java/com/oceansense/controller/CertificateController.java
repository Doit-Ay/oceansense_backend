package com.oceansense.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/quiz")
public class CertificateController {

    @PostMapping("/certificate")
    public ResponseEntity<byte[]> generateCertificate(@RequestBody CertificateRequest request) {
        // Use a try-with-resources statement to ensure all streams and documents are closed,
        // which is crucial for managing memory.
        try (PDDocument document = new PDDocument();
             InputStream imageStream = new ClassPathResource("certificate_template.png").getInputStream();
             InputStream fontStream = new ClassPathResource("fonts/LiberationSans-Regular.ttf").getInputStream()) {

            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            // --- OPTIMIZATION 1: Load the font directly from your resources ---
            // This avoids the slow system font scan and ensures the font is always available.
            PDType0Font font = PDType0Font.load(document, fontStream);

            // Load the template image from resources
            PDImageXObject templateImage = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "template");

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(templateImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

                // Use the loaded font
                contentStream.setFont(font, 32);

                String userName = request.getUserName();
                float fontSize = 32;
                float textWidth = font.getStringWidth(userName) / 1000 * fontSize;
                float pageWidth = page.getMediaBox().getWidth();
                float xOffset = (pageWidth - textWidth) / 2;
                float yOffset = 290; // Adjust as needed

                contentStream.beginText();
                contentStream.newLineAtOffset(xOffset, yOffset);
                contentStream.showText(userName);
                contentStream.endText();
            }

            // Save the PDF to a byte array in memory
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Certificate.pdf");

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            // Return a server error if something goes wrong
            return ResponseEntity.status(500).build();
        }
    }

    // Inner class for the request body
    public static class CertificateRequest {
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
