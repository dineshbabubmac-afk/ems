package com.annztech.rewardsystem.external.email.service.impl;

import com.annztech.rewardsystem.external.email.config.EmailConfig;
import com.annztech.rewardsystem.external.email.service.EmailProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Service
@Slf4j
public class ZeptoEmailServiceImpl implements EmailProviderClient {

    private final EmailConfig emailConfig;

    public ZeptoEmailServiceImpl(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public void sendEmail(String fromAddress, String recipientEmail, String subject, String htmlBody) {
        log.info("Receipient email, subject: {}, {}", recipientEmail, subject);
        BufferedReader br = null;
        HttpURLConnection conn = null;
        String output = null;
        StringBuilder sb = new StringBuilder();
        log.info("Preparing to send email to: {}", recipientEmail);
        try {
            URL url = URI.create(emailConfig.getBaseUrl()).toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", emailConfig.getApiKey());
            JSONObject emailPayload = new JSONObject()
                    .put("from", new JSONObject()
                            .put("address", fromAddress)
                    )
                    .put("to", new JSONArray().put(
                            new JSONObject().put("email_address", new JSONObject()
                                    .put("address", recipientEmail)
                                    .put("name", "Receiver")
                            )
                    ))
                    .put("subject", subject)
                    .put("htmlbody", htmlBody);
            log.debug("Email payload: {}", emailPayload.toString());
            OutputStream os = conn.getOutputStream();
            os.write(emailPayload.toString().getBytes());
            os.flush();
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            log.info("Email sent successfully. Response: {}", sb.toString());
        } catch (Exception e) {
            log.error("Error occurred while sending email: {}", e.getMessage(), e);
            try {
                br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                log.error("Error response: {}", sb.toString());
            } catch (Exception ex) {
                log.error("Error reading error response: {}", ex.getMessage(), ex);
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                log.error("Error closing BufferedReader: {}", e.getMessage(), e);
            }
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.error("Error disconnecting connection: {}", e.getMessage(), e);
            }
        }
    }
}