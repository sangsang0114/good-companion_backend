package org.example.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.dto.request.FcmSendTestRequest;
import org.example.dto.external.FcmMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {
    @Value("${fcm.key.path}")
    private String SERVICE_ACCOUNT_JSON;
    @Value("${fcm.api.url}")
    private String FCM_API_URL;

    private final ObjectMapper mapper;

    @Async
    public void sendMessageTo(FcmSendTestRequest dto) throws IOException {
        String message = makeMessage(dto);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(FCM_API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        log.info(response.body().string());
    }

    private String makeMessage(FcmSendTestRequest dto) throws JsonProcessingException {
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(dto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(dto.getTitle())
                                .body(dto.getBody())
                                .image("https://cdn.pixabay.com/photo/2022/01/05/05/55/man-6916436_1280.jpg")
                                .build()
                        )
                        .build()
                )
                .validateOnly(false)
                .build();
        return mapper.writeValueAsString(fcmMessageDto);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = SERVICE_ACCOUNT_JSON;
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
