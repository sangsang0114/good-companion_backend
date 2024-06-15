package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.MemberService;
import org.sku.zero.domain.Member;
import org.sku.zero.dto.request.*;
import org.sku.zero.dto.response.LoginResponse;
import org.sku.zero.dto.response.MemberPageResponse;
import org.sku.zero.dto.response.MemberResponse;
import org.sku.zero.dto.response.NotificationSettingResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api/v1/member")
@RestController
@RequiredArgsConstructor
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkExistsMemberById(@RequestParam String email) {
        boolean result = memberService.checkDuplicateByEmail(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkExistsMemberNickname(@RequestParam String nickname) {
        boolean result = memberService.checkDuplicateByNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> registerMember(@RequestBody AddMemberRequest request) {
        Long memberId = memberService.register(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginMember(@RequestBody LoginMemberRequest request) {
        LoginResponse loginResponse = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginResponse);
    }

    @PostMapping("/newAccessToken")
    public ResponseEntity<LoginResponse> regenerateLoginResponse(
            @RequestBody GetNewAccessTokenRequest request,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.regenerateAccessToken(principal.getName(), request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logoutMember(Principal principal) {
        memberService.logout(principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(true);
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<Boolean> sendVerificationCode(@RequestBody VerifyEmailRequest request) {
        Boolean result = memberService.verifyEmail(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyEmailCode(@RequestBody VerifyCodeRequest request) {
        Boolean result = memberService.verifyCode(request.email(), request.code());
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.forgotPassword(email));
    }

    @GetMapping("/validate-uuid/{email}/{uuid}")
    public ResponseEntity<Boolean> validateUuid(@PathVariable String email, @PathVariable String uuid) {
        Boolean result = memberService.validateUuid(email, uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Boolean> resetPassword(@RequestBody ResetPasswordRequest request) {
        Boolean result = memberService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordRequest request, Principal principal){
        Boolean result = memberService.changePassword(principal, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PatchMapping("/change-nickname")
    public ResponseEntity<Boolean> changeNickname(@RequestBody ChangeNicknameRequest request, Principal principal){
        Boolean result = memberService.changeNickname(principal, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PatchMapping("/change-notification-setting")
    public ResponseEntity<Boolean> changeNotificationSetting(@RequestBody ChangeNotificationSettingRequest request, Principal principal){
        Boolean result = memberService.changeNotificationSetting(principal, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/notification-setting")
    public ResponseEntity<NotificationSettingResponse> findNotificationSettingByEmail(Principal principal){
        NotificationSettingResponse response = memberService.getNotificationSettingByEmail(principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/info")
    public ResponseEntity<MemberResponse> findMemberById(@RequestParam Long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(MemberResponse.toDto(memberService.findById(memberId)));
    }

    @GetMapping("/list")
    public ResponseEntity<MemberPageResponse> listMembers(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        Page<Member> memberPage = memberService.listMembers(page, size);
        List<MemberResponse> memberResponseList = memberPage
                .stream().map(MemberResponse::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(MemberPageResponse.toDto(memberResponseList, memberPage));
    }

    @PatchMapping("/update-fcm-by-admin")
    public ResponseEntity<Boolean> updateFcmByAdmin(@RequestBody ModifyFcmTokenRequest request){
        Boolean result = memberService.updateFcmByAdmin(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}