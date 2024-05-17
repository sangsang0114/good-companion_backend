package org.example.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeoCoderApiResponseDto {
    private Response response;

    @Getter
    public static class Response {
        private String status;
        private Result result;
        private Refined refined;
        private Error error;
    }

    @Getter
    public static class Refined {
        private String text;
        private Structure structure;
    }

    @Getter
    public static class Structure {
        private String level0;
        private String level1;
        private String level2;
        private String level3;
        private String level4L;
        private String level4LC;
        private String level4A;
        private String level4AC;
        private String level5;
        private String detail;
    }

    @Data
    public static class Result {
        private String crs;
        private Point point;
    }

    @Getter
    public static class Point {
        private String x;
        private String y;
    }

    @Getter
    public static class Error {
        private String level;
        private String code;
        private String text;
    }
}
