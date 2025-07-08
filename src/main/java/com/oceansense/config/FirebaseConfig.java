package com.oceansense.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() throws IOException {
        Dotenv dotenv = Dotenv.load();

        String firebaseConfig = String.format("{\n" +
                "  \"type\": \"%s\",\n" +
                "  \"project_id\": \"%s\",\n" +
                "  \"private_key_id\": \"%s\",\n" +
                "  \"private_key\": \"%s\",\n" +
                "  \"client_email\": \"%s\",\n" +
                "  \"client_id\": \"%s\",\n" +
                "  \"auth_uri\": \"%s\",\n" +
                "  \"token_uri\": \"%s\",\n" +
                "  \"auth_provider_x509_cert_url\": \"%s\",\n" +
                "  \"client_x509_cert_url\": \"%s\"\n" +
                "}",
                dotenv.get("FIREBASE_TYPE"),
                dotenv.get("FIREBASE_PROJECT_ID"),
                dotenv.get("FIREBASE_PRIVATE_KEY_ID"),
                dotenv.get("FIREBASE_PRIVATE_KEY").replace("\\n", "\n"),
                dotenv.get("FIREBASE_CLIENT_EMAIL"),
                dotenv.get("FIREBASE_CLIENT_ID"),
                dotenv.get("FIREBASE_AUTH_URI"),
                dotenv.get("FIREBASE_TOKEN_URI"),
                dotenv.get("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"),
                dotenv.get("FIREBASE_CLIENT_X509_CERT_URL"));

        InputStream serviceAccount = new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(dotenv.get("FIREBASE_DATABASE_URL"))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
