package org.sku.zero.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NoticeAddedEvent extends ApplicationEvent {
    private final String noticeTitle;
    private final String noticeUrl;

    public NoticeAddedEvent(Object source, String noticeTitle, String noticeUrl) {
        super(source);
        this.noticeTitle = noticeTitle;
        this.noticeUrl = noticeUrl;
    }
}
