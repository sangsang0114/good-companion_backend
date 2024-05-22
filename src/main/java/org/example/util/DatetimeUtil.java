package org.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatetimeUtil {
    public DatetimeUtil() {
        throw new RuntimeException("Util 클래스는 인스턴스를 생성할 수 없습니다");
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss").format(localDateTime);
    }

    public static String formatDate(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(localDateTime);
    }

    public static String formatTime(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("HH:mm:ss").format(localDateTime);
    }
}
