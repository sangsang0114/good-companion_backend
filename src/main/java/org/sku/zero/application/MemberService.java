package org.sku.zero.application;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.config.security.jwt.TokenProvider;
import org.sku.zero.domain.Member;
import org.sku.zero.dto.request.*;
import org.sku.zero.dto.response.LoginResponse;
import org.sku.zero.dto.response.NotificationSettingResponse;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.InternalServerErrorException;
import org.sku.zero.exception.NotFoundException;
import org.sku.zero.exception.UnauthorizedException;
import org.sku.zero.infrastructure.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        String accessToken = tokenProvider.generateToken(member, Duration.ofHours(12), "accessToken");
        String refreshToken = tokenProvider.generateToken(member, Duration.ofDays(1), "refreshToken");
        redisTemplate.opsForValue().set(
                "refreshToken:email:" + email,
                refreshToken,
                Duration.ofDays(1));
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
        String newAccessToken = tokenProvider.generateToken(member, Duration.ofHours(12), "accessToken");
        String newRefreshToken = null;
        //리프레시 토큰의 수명이 25분 이내로 남은 경우 리프레시 토큰도 재발급
        if (remain.getSeconds() > 0 && remain.getSeconds() <= 25 * 60) {
            newRefreshToken = tokenProvider.generateToken(member, Duration.ofDays(1), "refreshToken");
            redisTemplate.delete("refreshToken:email:" + email);
            redisTemplate.opsForValue().set(
                    "refreshToken:email:" + email,
                    newRefreshToken,
                    Duration.ofDays(1));
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

    public List<Member> findMemberByEmailFlagIsTrue(){
        return memberRepository.findMembersByEmailFlagIs(1);
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
        try {
            Member member = findByEmail(email);
            Long id = member.getId();
            mailService.sendPasswordResetLinkMail(email, uuid);
            redisTemplate.opsForValue().set("forgot_password:" + uuid, email, Duration.ofHours(3));
        } catch (MessagingException ex) {
            log.error("인증코드 전송에 실패했습니다");
            throw new InternalServerErrorException(ErrorCode.SERVER_ERROR);
        }
        return uuid;
    }

    public String validateUuid(String uuid) {
        String mail = redisTemplate.opsForValue().get("forgot_password:" + uuid);
        return mail;
    }

    @Transactional
    public Boolean resetPassword(ResetPasswordRequest request) {
        String uuid = request.uuid();
        String email = validateUuid(request.uuid());
        if (email != null) {
            Member member = findByEmail(email);
            member.updatePassword(bCryptPasswordEncoder.encode(request.password()));
            redisTemplate.delete("forgot_password:" + uuid);
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

    @Transactional
    public Boolean changeNickname(Principal principal, ChangeNicknameRequest request) {
        Member member = findByEmail(principal.getName());
        member.updateNickname(request.nickname());
        return true;
    }

    @Transactional
    public Boolean changeNotificationSetting(Principal principal, ChangeNotificationSettingRequest request) {
        Member member = findByEmail(principal.getName());
        Integer emailFlag = request.emailFlag() ? 1 : 0;
        Integer fcmFlag = request.fcmFlag() ? 1 : 0;
        member.updateNotificationSetting(emailFlag, fcmFlag);
        return true;
    }

    public NotificationSettingResponse getNotificationSettingByEmail(Principal principal) {
        Member member = findByEmail(principal.getName());
        return NotificationSettingResponse.toDto(member);
    }

    public Page<Member> listMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return memberPage;
    }

    @Transactional
    public Boolean updateFcmByAdmin(ModifyFcmTokenRequest request) {
        Member member = findById(request.memberId());
        member.updateFcmToken(request.fcmToken());
        return true;
    }

    @Transactional
    public Boolean updateFcm(ModifyFcmTokenRequest request, Principal principal) {
        Member member = findByEmail(principal.getName());
        member.updateFcmToken(request.fcmToken());
        return true;
    }
}
