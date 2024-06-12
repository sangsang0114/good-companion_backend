package org.sku.zero.application;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.config.security.jwt.TokenProvider;
import org.sku.zero.domain.Member;
import org.sku.zero.dto.request.AddMemberRequest;
import org.sku.zero.dto.request.LoginMemberRequest;
import org.sku.zero.dto.request.ResetPasswordRequest;
import org.sku.zero.dto.request.VerifyEmailRequest;
import org.sku.zero.dto.response.LoginResponse;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.NotFoundException;
import org.sku.zero.exception.UnauthorizedException;
import org.sku.zero.infrastructure.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final MailService mailService;

    @Transactional
    public Long register(AddMemberRequest request) {
        Member member = request.toEntity(bCryptPasswordEncoder);
        return memberRepository.save(member).getId();
    }

    public LoginResponse login(LoginMemberRequest request) {
        String email = request.email();
        String password = request.password();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.LOGIN_FAIL));
        if (!bCryptPasswordEncoder.matches(password, member.getPassword()))
            throw new UnauthorizedException(ErrorCode.LOGIN_FAIL);
        String accessToken = tokenProvider.generateToken(member, Duration.ofHours(3), "accessToken");
        String refreshToken = tokenProvider.generateToken(member, Duration.ofHours(3), "refreshToken");
        redisTemplate.opsForValue().set(
                "refreshToken:email:" + email,
                refreshToken,
                Duration.ofHours(3));
        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return response;
    }

    public LoginResponse regenerateAccessToken(String email, String refreshToken) {
        String prevRefreshToken = getRefreshTokenByEmail(email);
        if (!prevRefreshToken.equals(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.JWT_DECODE_FAIL);
        }

        Instant expirationTime = tokenProvider.getExpiration(refreshToken);
        Instant now = Instant.now();
        Duration remain = Duration.between(now, expirationTime);

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.LOGIN_FAIL));
        String newAccessToken = tokenProvider.generateToken(member, Duration.ofMinutes(20), "accessToken");
        String newRefreshToken = null;
        //리프레시 토큰의 수명이 25분 이내로 남은 경우 리프레시 토큰도 재발급
        if (remain.getSeconds() > 0 && remain.getSeconds() <= 25 * 60) {
            newRefreshToken = tokenProvider.generateToken(member, Duration.ofHours(3), "refreshToken");
            redisTemplate.delete("refreshToken:email:" + email);
            redisTemplate.opsForValue().set(
                    "refreshToken:email:" + email,
                    newRefreshToken,
                    Duration.ofHours(3));
        }
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public String getRefreshTokenByEmail(String email) {
        String jsonString = redisTemplate.opsForValue().get("refreshToken:email:" + email);
        if (jsonString == null)
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        return jsonString;
    }

    public boolean checkDuplicateByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return member;
    }

    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        return member;
    }

    public void logout(Principal principal) {
        String email = principal.getName();
        redisTemplate.delete("refreshToken:email:" + email);
    }

    public List<Member> findMembersByFcmFlagIsTrue() {
        return memberRepository.findMembersByFcmTokenIsNotNull();
    }

    public boolean checkDuplicateByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public Boolean verifyEmail(VerifyEmailRequest request) {
        int min = 10000000;
        int max = 99999999;
        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        String email = request.email();
        try {
            mailService.sendVerifyCodeMail(email, randomNumber + "");
            redisTemplate.opsForValue().set("email_verification_code:" + email,
                    randomNumber + "",
                    Duration.ofMinutes(4));
        } catch (MessagingException ex) {
            log.error("인증코드 전송에 실패하였습니다.");
        }
        return true;
    }

    public Boolean verifyCode(String email, String code) {
        String jsonString = redisTemplate.opsForValue().get("email_verification_code:" + email);
        if (jsonString == null) return false;
        return jsonString.equals(code);
    }

    public String forgotPassword(String email) {
        String uuid = UUID.randomUUID().toString();
        log.info(uuid);
        try {
            mailService.sendPasswordResetLinkMail(email, uuid);
            redisTemplate.opsForValue().set("forgot_password:" + email + ":" + uuid, uuid, Duration.ofHours(3));
        } catch (MessagingException ex) {
            log.error("인증코드 전송에 실패했습니다");
        }
        return uuid;
    }

    public boolean validateUuid(String email, String uuid) {
        String id = redisTemplate.opsForValue().get("forgot_password:" + email + ":" + uuid);
        System.out.println(id);
        return id != null && id.equals(uuid);
    }

    @Transactional
    public Boolean resetPassword(ResetPasswordRequest request) {
        String email = request.email();
        String uuid = request.uuid();
        if (validateUuid(request.email(), request.uuid())) {
            Member member = findByEmail(request.email());
            member.updatePassword(bCryptPasswordEncoder.encode(request.password()));
            redisTemplate.delete("forgot_password:" + email + ":" + uuid);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean changePassword(Principal principal, ChangePasswordRequest request) {
        Member member = findByEmail(principal.getName());
        if (bCryptPasswordEncoder.matches(request.currentPassword(), member.getPassword())) {
            System.out.println(request.newPassword());
            System.out.println(request.currentPassword());
            member.updatePassword(bCryptPasswordEncoder.encode(request.newPassword()));
        } else {
            throw new UnauthorizedException(ErrorCode.PASSWORD_INCORRECT);
        }
        return true;
    }
}
