package org.example.presentation.controller;


import lombok.RequiredArgsConstructor;
import org.example.application.FcmService;
import org.example.dto.request.FcmSendTestRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/test")
    public void test(@RequestBody FcmSendTestRequest sendDto) throws Exception {
        fcmService.sendMessageTo(sendDto);
    }

    @PostMapping("/register")
    public void registerFcmToken(@RequestParam String fcmToken) {
        //FCM 등록
    }

    @DeleteMapping("/delete")
    public void deleteDcmToken() {
        //FCM 삭제
    }
}
