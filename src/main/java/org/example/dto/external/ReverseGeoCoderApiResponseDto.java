package org.example.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReverseGeoCoderApiResponseDto {
    @JsonProperty("response")
    private Response response;

    @Getter
    public static class Response {
        private String status;
        @JsonProperty("result")
        private List<Result> result;

        @JsonProperty("error")
        private Error error;
    }

    @Getter
    public static class Result {
        private String zipcode;
        private String text;
        private Structure structure;

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
    }

    @Getter
    public static class Error {
        private String level;
        private String code;
        private String text;
    }
}
