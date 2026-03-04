package com.autosales.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String to, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Подтверждение регистрации в AutoSales");

            String htmlContent = """
                <h3>Здравствуйте!</h3>
                <p>Для завершения регистрации перейдите по ссылке:</p>
                <p><a href="%s">Подтвердить email</a></p>
                <p>Ссылка действительна 24 часа.</p>
                """.formatted(verificationLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Письмо подтверждения отправлено на {}", to);
        } catch (Exception e) {
            log.error("Ошибка отправки письма на {}: {}", to, e.getMessage());
            throw new RuntimeException("Не удалось отправить письмо подтверждения", e);
        }
    }

    public void sendSaleNotification(String to, Integer saleId, String status) {
        // опционально
    }
}