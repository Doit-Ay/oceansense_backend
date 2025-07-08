package com.oceansense.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
        // Use a try-with-resources statement to ensure the document is closed
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            // --- FIX: Load the image from the classpath resources ---
            // This ensures the image is bundled with your application JAR
            InputStream in = new ClassPathResource("certificate_template.png").getInputStream();
            PDImageXObject templateImage = PDImageXObject.createFromByteArray(document, in.readAllBytes(), "template");
            in.close(); // Close the input stream
            // --- End of FIX ---

            // Use a try-with-resources statement for the content stream
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Draw the template image
                contentStream.drawImage(templateImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

                // Set font and add the user's name
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 32);
                String userName = request.getUserName();
                float fontSize = 32;
                float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(userName) / 1000 * fontSize;
                float pageWidth = page.getMediaBox().getWidth();
                float xOffset = (pageWidth - textWidth) / 2;
                float yOffset = 290; // Adjust this Y-offset as needed for your template

                contentStream.beginText();
                contentStream.newLineAtOffset(xOffset, yOffset);
                contentStream.showText(userName);
                contentStream.endText();
            }

            // Save the PDF to a byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);

            // Set headers for the response to trigger a download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Certificate.pdf");

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (IOException e) {
            // Log the error for debugging
            e.printStackTrace();
            // Return a generic 500 error to the client
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
