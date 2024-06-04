package org.sku.zero.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.dto.request.AddShopRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final String NEW_SHOP_MAIL_SUBJECT = "어제 등록된 새로운 가게들을 소개합니다";
    private final String TEMPORARY_PASSWORD_MAIL_SUBJECT = "요청하신 임시 비밀번호 입니다.";
    private final String SENDER_EMAIL = "email@email.com";

    @Async
    public void sendMail(String to, AddShopRequest dto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        message.setFrom(SENDER_EMAIL);
        helper.setSubject(NEW_SHOP_MAIL_SUBJECT);
        helper.setTo(to);

        Context context = new Context();
        context.setVariable("shopName", dto.name());
        context.setVariable("shopAddress", dto.address());

        String html = templateEngine.process("newShop", context);
        helper.setText(html, true);

        mailSender.send(message);
         */
    }

    @Async
    public void sendVerifyCodeMail(String to, String verificationCode) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("이메일 확인");
        message.setText(String.format("이메일 검증을 위한 요청 코드는 다음과 같습니다 : %s", verificationCode));
        message.setFrom("");

        mailSender.send(message);
    }

    @Async
    public void sendPasswordResetLinkMail(String email, String uuid) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 초기화 링크");
        message.setText("비밀번호 초기화 링크 : http://localhost:3003/resetPassword/" + email + "/" + uuid);
        message.setFrom("");

        mailSender.send(message);
    }
}
