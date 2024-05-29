package org.sku.zero.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.domain.ShopLocation;
import org.sku.zero.dto.external.GeoCoderApiResponseDto;
import org.sku.zero.dto.external.GeoCoderResultDto;
import org.sku.zero.dto.external.ReverseGeoCoderApiResponseDto;
import org.sku.zero.dto.response.ShopLocationResponse;
import org.sku.zero.infrastructure.repository.ShopLocationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopLocationService {
    private String BASE_URL = "https://api.vworld.kr/req/address";
    private String TYPE = "ROAD";
    private String SERVICE = "address";
    private String REQUEST = "getcoord";

    @Value("${geocoder.key}")
    private String KEY;
    private final ShopLocationRepository shopLocationRepository;

    @Transactional(readOnly = true)
    public ShopLocationResponse findByShopId(String shopId) {
        ShopLocation shopLocation = shopLocationRepository.findById(shopId)
                .orElseThrow(EntityNotFoundException::new);
        return ShopLocationResponse.toDto(shopLocation);
    }

    @Transactional
    public ShopLocation save(ShopLocation shopLocation) {
        return shopLocationRepository.save(shopLocation);
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

    public GeoCoderResultDto getCoordinateAndRegionId(String address) {
        String requestUrl = getRequestUrl(address);
        WebClient webClient = WebClient.builder()
                .baseUrl(requestUrl)
                .build();
        GeoCoderApiResponseDto responseDto = webClient.get()
                .retrieve()
                .bodyToMono(GeoCoderApiResponseDto.class)
                .block();

        if (responseDto != null) {
            String status = responseDto.getResponse().getStatus();
            if ("ERROR".equals(status)) {
                GeoCoderApiResponseDto.Error error = responseDto.getResponse().getError();
                log.error("Error: {}", error.getText());
                return null;
            } else if ("NOT_FOUND".equals(status)) {
//                log.warn("Warning: {}", "존재하지 않는 주소입니다.");
                return null;
            } else {
                GeoCoderResultDto resultDto = GeoCoderResultDto.toDto(responseDto);
                return resultDto;
            }
        }
        return null;
    }

    private String getRequestUrl2(String point) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("service", SERVICE)
                .queryParam("request", "getAddress")
                .queryParam("version", "2.0")
                .queryParam("crs", "epsg:4326")
                .queryParam("point", point)
                .queryParam("zipcode", "true")
                .queryParam("simple", "true")
                .queryParam("key", KEY)
                .queryParam("type", "road")
                .build()
                .toUriString();
    }

    public String getZipcode(String x, String y) {
        String requestUrl = getRequestUrl2(x + "," + y);
        WebClient webClient = WebClient.builder()
                .baseUrl(requestUrl)
                .build();
        ReverseGeoCoderApiResponseDto responseDto = webClient.get()
                .retrieve()
                .bodyToMono(ReverseGeoCoderApiResponseDto.class)
                .block();

        if (responseDto != null) {
            String status = responseDto.getResponse().getStatus();
            if ("ERROR".equals(status)) {
                return null;
            } else if ("NOT_FOUND".equals(status)) {
                return null;
            } else {
                return responseDto.getResponse().getResult().get(0).getZipcode();
            }
        }
        return null;
    }
}