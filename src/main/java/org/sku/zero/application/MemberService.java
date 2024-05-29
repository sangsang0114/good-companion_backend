package org.sku.zero.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sku.zero.config.security.jwt.TokenProvider;
import org.sku.zero.domain.Member;
import org.sku.zero.dto.request.AddMemberRequest;
import org.sku.zero.dto.request.LoginMemberRequest;
import org.sku.zero.dto.response.LoginResponse;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.NotFoundException;
import org.sku.zero.exception.UnauthorizedException;
import org.sku.zero.infrastructure.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long register(AddMemberRequest request) {
        Member member = request.toEntity();
        return memberRepository.save(member).getId();
    }

    public LoginResponse login(LoginMemberRequest request) {
        String email = request.email();
        String password = request.password();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.LOGIN_FAIL));
        if (!password.equals(member.getPassword()))
            throw new UnauthorizedException(ErrorCode.LOGIN_FAIL);
        String accessToken = tokenProvider.generateToken(member, Duration.ofMinutes(20), "accessToken");
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
}
