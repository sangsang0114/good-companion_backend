package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.MemberService;
import org.example.dto.request.AddMemberRequest;
import org.example.dto.request.GetNewAccessTokenRequest;
import org.example.dto.request.LoginMemberRequest;
import org.example.dto.response.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
}