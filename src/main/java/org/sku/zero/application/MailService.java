package org.sku.zero.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.domain.redis.NewShop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final String NEW_SHOP_MAIL_SUBJECT = "지난 1일간 새로 추가된 가게 목록을 알려드릴게요";
    private final String EMAIL_VERIFICATION_MAIL_SUBJECT = "이메일 확인";
    private final String EMAIL_VERIFICATION_MAIL_CONTENT = "이메일 검증을 위한 요청 코드는 다음과 같습니다 :";
    private final String RESET_PASSWORD_MAIL_SUBJECT = "비밀번호 초기화 링크";
    private final String RESET_PASSWORD_MAIL_CONTENT = "비밀번호 초기화 링크";

    private final String SENDER_EMAIL = "id4email@naver.com";
    @Value("${web.url}")
    private String webUrl;

    @Async
    public void sendVerifyCodeMail(String to, String verificationCode) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(EMAIL_VERIFICATION_MAIL_SUBJECT);
        message.setText(EMAIL_VERIFICATION_MAIL_CONTENT + " " + verificationCode);
        message.setFrom(SENDER_EMAIL);

        mailSender.send(message);
    }

    @Async
    public void sendPasswordResetLinkMail(String email, String uuid) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(RESET_PASSWORD_MAIL_SUBJECT);
        message.setText(RESET_PASSWORD_MAIL_CONTENT + webUrl + "/resetPassword/" + uuid);
        message.setFrom(SENDER_EMAIL);

        mailSender.send(message);
    }

    @Async
    public void sendNewShopByBatchMail(String email, List<NewShop> newShops) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("shops", newShops);
        helper.setSubject(NEW_SHOP_MAIL_SUBJECT);
        helper.setText(templateEngine.process("newShop", context), true);
        helper.setFrom(SENDER_EMAIL);
        helper.setTo(email);

        mailSender.send(message);
    }
}
