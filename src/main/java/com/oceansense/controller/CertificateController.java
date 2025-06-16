package com.oceansense.controller;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/quiz")
public class CertificateController {

    @PostMapping("/certificate")
    public ResponseEntity<byte[]> generateCertificate(@RequestBody CertificateRequest request) {
        try (PDDocument document = new PDDocument()) {
            // Create a landscape page
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            // Load template image
            PDImageXObject templateImage = PDImageXObject.createFromFile("C:\\Users\\adity\\Music\\project\\oceansense\\src\\main\\resources\\certificate_template.png", document);

            // Start content stream to draw on the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Draw the template image to fill the entire page
                contentStream.drawImage(templateImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

                // Set font and size
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 32);

                // Calculate text width for centering
                String userName = request.getUserName();
                float fontSize = 32;
                float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(userName) / 1000 * fontSize;

                // Calculate x-offset to center the text
                float pageWidth = page.getMediaBox().getWidth();
                float xOffset = (pageWidth - textWidth) / 2;

                // Set y-offset (adjust based on your template design)
                float yOffset = 290;

                // Begin text writing
                contentStream.beginText();
                contentStream.newLineAtOffset(xOffset, yOffset);
                contentStream.showText(userName);
                contentStream.endText();
            }

            // Write the document to a byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);

            // Set HTTP headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Certificate.pdf");

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Request class to accept JSON input
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
