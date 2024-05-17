import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExTest {
    @Test
    @DisplayName("정규식을 이용하여 처음 나타나는 시간 형식의 영업시간을 추출한다.")
    public void extractBusinessHours() throws IOException {
        ClassPathResource resource = new ClassPathResource("a.txt");
        byte[] byteData = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String[] strs = new String(byteData, StandardCharsets.UTF_8).split("\r\n");

        String regex = "\\b(\\d{1,2}(?::\\d{2})?)\\s*(?:시)?(?:\\s*(?:~|-)\\s*)\\b(\\d{1,2}(?::\\d{2})?)\\s*(?:시)?\\b";
        Pattern pattern = Pattern.compile(regex);
        for (String str : strs) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                String startTime = matcher.group(1);
                String endTime = matcher.group(2);

                startTime = convertTo24HoursFormat(startTime);
                endTime = convertTo24HoursFormat(endTime);
                if (startTime == null || endTime == null) continue;
//                System.out.printf("%s - %s\n", startTime, endTime);
            } else {
                if (str.contains("24시")) {
//                    System.out.printf("%s - %s\n", "00:00", "24:00");
                } else {
                    System.out.println(str);
                }
            }
        }
    }

    private String convertTo24HoursFormat(String time) {
        if (time.length() <= 2) {
            int temp = Integer.parseInt(time);
            if (temp <= 23) {
                return temp + ":00";
            } else {
                return null;
            }
        }
        if (time.contains("오전") || time.contains("AM")) {
            time = time.replaceAll("[^0-9:]", "");
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            if (hour == 12) {
                return "00:" + parts[1];
            } else if (hour < 10) {
                return "0" + hour + ":" + parts[1];
            } else {
                return time;
            }
        } else if (time.contains("오후") || time.contains("PM")) {
            time = time.replaceAll("[^0-9:]", "");
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            if (hour != 12) {
                hour += 12;
            }
            return hour + ":" + parts[1];
        }
        return time;
    }
}