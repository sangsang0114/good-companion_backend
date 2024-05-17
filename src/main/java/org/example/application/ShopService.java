package org.example.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.domain.Shop;
import org.example.dto.external.ListPriceStoreApiResponseDto;
import org.example.dto.response.NearbyShopInfoResponse;
import org.example.infrastructure.repository.ShopLocationRepository;
import org.example.infrastructure.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final WebClient.Builder webclientBuilder;
    private final ShopLocationRepository shopLocationRepository;

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088";

    @Value("${seoul.key}")
    private String KEY;
    private final String RETURN_TYPE = "json";
    private final String SERVICE_NAME = "ListPriceModelStoreService";

    public List<ListPriceStoreApiResponseDto.ListPriceStoreApiInfo> loadShopInfo() {
        int startIndex = 1;
        int endIndex = 1000;
        int maxIndex = 2000;
        List<ListPriceStoreApiResponseDto.ListPriceStoreApiInfo> results = new ArrayList<>();

        while (startIndex < maxIndex) {
            String requestUrl = UriComponentsBuilder
                    .fromUriString(BASE_URL)
                    .pathSegment(KEY, RETURN_TYPE, SERVICE_NAME, startIndex + "", (startIndex + endIndex - 1) + "")
                    .build()
                    .toUriString();

            WebClient webClient = webclientBuilder
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(30 * 1024 * 1024)) //10MB
                    .baseUrl(requestUrl).build();

            ListPriceStoreApiResponseDto responseDto = webClient.get()
                    .retrieve()
                    .bodyToMono(ListPriceStoreApiResponseDto.class)
                    .block();

            results.addAll(Arrays.stream(responseDto.getListPriceModelStoreService().getRow()).toList());
            startIndex += 1000;
        }
        return results;
    }

    public String extractBusinessHours(String info) {
        String regex = "\\b(\\d{1,2}(?::\\d{2})?)\\s*(?:시)?(?:\\s*(?:~|-)\\s*)\\b(\\d{1,2}(?::\\d{2})?)\\s*(?:시)?\\b";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(info);
        if (matcher.find()) {
            String startTime = matcher.group(1);
            String endTime = matcher.group(2);

            startTime = convertTo24HoursFormat(startTime);
            endTime = convertTo24HoursFormat(endTime);
            if (startTime == null || endTime == null)
                return "";
            return String.format("%s - %s", startTime, endTime);
        } else {
            if (info.contains("24시"))
                return String.format("00:00 - 24:00");
            else
                return "";
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

    @Transactional
    public Shop save(Shop entity) {
        return shopRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Shop> getShopsByRegionCode(String regionCode) {
        return shopRepository.findShopsByShopRegion(regionCode);
    }

    @Transactional(readOnly = true)
    public Shop getShopById(String shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(EntityNotFoundException::new);
        return shop;
    }

    @Transactional(readOnly = true)
    public List<NearbyShopInfoResponse> getShopsByCoordinate(String latitude, String longitude, String radius) {
        List<NearbyShopInfoResponse> responses = shopLocationRepository.findNearbyShopLocations(
                Double.valueOf(latitude), Double.valueOf(longitude), Double.parseDouble(radius));
        return responses;
    }
}