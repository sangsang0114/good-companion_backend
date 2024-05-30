package org.sku.zero.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.application.FcmService;
import org.sku.zero.application.MemberService;
import org.sku.zero.domain.Member;
import org.sku.zero.dto.request.FcmSendTestRequest;
import org.springframework.boot.autoconfigure.web.embedded.TomcatVirtualThreadsWebServerFactoryCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NoticeAddedListener implements ApplicationListener<NoticeAddedEvent> {
    private final FcmService fcmService;
    private final MemberService memberService;

    @Override
    public void onApplicationEvent(NoticeAddedEvent event) {
        String title = event.getNoticeTitle();
        String url = event.getNoticeUrl();

        List<Member> members = memberService.findMembersByFcmFlagIsTrue();
        members.forEach(member -> {
            try {
                fcmService.sendMessageTo(FcmSendTestRequest.builder()
                        .token(member.getFcmToken())
                        .title("중요 공지 알림")
                        .body(title)
                        .build()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}