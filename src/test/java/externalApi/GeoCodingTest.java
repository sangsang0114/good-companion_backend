package externalApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sku.zero.dto.external.GeoCoderApiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class GeoCodingTest {
    private String BASE_URL = "https://api.vworld.kr/req/address";
    private String REQUEST = "getcoord";
    private String TYPE = "ROAD";
    private String SERVICE = "address";

    @Value("${geocoder.key}")
    private String KEY;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        Resource resource = new ClassPathResource("application-API.yaml");
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(resource);
        Properties properties = yamlFactory.getObject();
        if (properties != null) {
            properties.forEach((key, value) -> registry.add((String) key, () -> value));
        }
    }

    private String getRequestUrl(String address) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("service", SERVICE)
                .queryParam("request", REQUEST)
                .queryParam("address", address)
                .queryParam("key", KEY)
                .queryParam("type", TYPE)
                .build()
                .toUriString();
    }

    @Test
    @DisplayName("존재하지 않는 주소일 경우, NOT_FOUND 응답")
    public void notExistAddressThenReturnErrorTest() {
        //given
        final String notExistAddress = "서울특별시 일산동구 백석로 123";
        String requestUrl = getRequestUrl(notExistAddress);

        //when
        WebClient webClient = WebClient.builder().baseUrl(requestUrl).build();
        GeoCoderApiResponseDto response = webClient.get()
                .retrieve()
                .bodyToMono(GeoCoderApiResponseDto.class)
                .block();

        //then
        String status = response.getResponse().getStatus();
        assertEquals("NOT_FOUND", status);
    }

    @Test
    @DisplayName("존재하는 주소일 경우, OK 응답")
    public void existAddressThenReturnOkTest() {
        //given
        final String existAddress = "서울특별시 서울 중랑구 봉화산로56길 153, 지하2층";
        String requestUrl = getRequestUrl(existAddress);

        //when
        WebClient webClient = WebClient.builder().baseUrl(requestUrl).build();
        GeoCoderApiResponseDto response = webClient.get()
                .retrieve()
                .bodyToMono(GeoCoderApiResponseDto.class)
                .block();

        //then
        String status = response.getResponse().getStatus();
        assertEquals("OK", status);
    }
}
