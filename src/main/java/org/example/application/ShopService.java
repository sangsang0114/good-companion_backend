package org.example.application;


import lombok.RequiredArgsConstructor;
import org.example.application.redis.DailyShopService;
import org.example.domain.Shop;
import org.example.domain.redis.DailyShop;
import org.example.dto.external.GeoCoderResultDto;
import org.example.dto.external.ListPriceStoreApiResponseDto;
import org.example.dto.request.AddShopRequest;
import org.example.dto.request.ModifyShopRequest;
import org.example.dto.response.BestShopResponse;
import org.example.dto.response.NearbyShopInfoResponse;
import org.example.dto.response.ShopDetailResponse;
import org.example.event.NewShopAddedEvent;
import org.example.exception.BadRequestException;
import org.example.exception.ErrorCode;
import org.example.exception.NotFoundException;
import org.example.infrastructure.repository.ShopLocationRepository;
import org.example.infrastructure.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
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
    private final ApplicationEventPublisher eventPublisher;

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088";
    private final ShopLocationService shopLocationService;
    private final AttachmentService attachmentService;
    private final ShopSectorService shopSectorService;
    private final DailyShopService dailyShopService;

    @Value("${seoul.key}")
    private String KEY;
    private final String RETURN_TYPE = "json";
    private final String SERVICE_NAME = "ListPriceModelStoreService";
    private final ShopLocationRepository shopLocationRepository;
    private final String DOMAIN = "http://localhost:8080/api/v1/attachment/";

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
    public List<NearbyShopInfoResponse> getShopsByCoordinate(String latitude, String longitude, String radius) {
        List<NearbyShopInfoResponse> responses = shopLocationRepository.findNearbyShopLocations(
                Double.valueOf(latitude), Double.valueOf(longitude), Double.parseDouble(radius)).stream().map(response -> {
            if (response.getImgUrl() == null) {
                List<Long> indicies = attachmentService.getFileIndicesByServiceNameAndTarget("Shop", Long.parseLong(response.getId()));
                if (!indicies.isEmpty()) response.setImgUrl("http://localhost:8080/api/v1/attachment/" + indicies.get(0));
            }
            return response;
        }).toList();
        return responses;
    }

    @Transactional
    public void addShop(AddShopRequest addShopRequest) {
        GeoCoderResultDto resultDto = shopLocationService.getCoordinateAndRegionId(addShopRequest.address());
        String shopId = addShopRequest.id();
        if (resultDto == null)
            throw new BadRequestException(ErrorCode.INVALID_ADDRESS);
        String refinedAddress = resultDto.refinedAddress();

        List<MultipartFile> files = addShopRequest.files();
        String firstImageUrl = attachmentService.uploadFile(files, "Shop", Long.parseLong(shopId));
        shopRepository.save(addShopRequest.toEntity(refinedAddress));
        shopLocationService.save(resultDto.toEntity(shopId));

        eventPublisher.publishEvent(new NewShopAddedEvent(this, addShopRequest, firstImageUrl));
    }

    @Transactional(readOnly = true)
    public List<Shop> getShopsByRegionCode(String regionCode) {
        return shopRepository.findShopsByShopRegion(regionCode);
    }

    @Transactional(readOnly = true)
    public Shop getShopById(String shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));
        return shop;
    }

    @Transactional(readOnly = true)
    public ShopDetailResponse getShopDetailByShopId(String shopId) {
        Shop shop = getShopById(shopId);

        List<String> imgUrls = new ArrayList<>();
        String sectorName = shopSectorService.getSectorNameByID(shop.getShopSector());
        if (shopId.startsWith("0")) { //공공데이터의 가게
            String imgUrl = shop.getImgUrlPublic();
            if (imgUrl != null)
                imgUrls.add(imgUrl);
        }
        //프로그램의 가게 + 공공데이터에 추가로 넣은 이미지
        List<Long> attachmentIds = attachmentService.getFileIndicesByServiceNameAndTarget(
                "Shop", Long.parseLong(shopId));
        attachmentIds.forEach(attachmentId -> imgUrls.add(DOMAIN + attachmentId));

        return ShopDetailResponse.toDto2(shop, sectorName, imgUrls);
    }

    @Transactional(readOnly = true)
    public List<BestShopResponse> findBestRecommendedShopPerSector() {
        List<Shop> shops = shopRepository.findBestRecommendedShopPerSector();
        return shops.stream().map(shop -> {
            if (shop.getId().startsWith("0")) {
                BestShopResponse bs = BestShopResponse.toDto(shop, shop.getImgUrlPublic());
                return bs;
            } else {
                List<Long> indicies = attachmentService.getFileIndicesByServiceNameAndTarget("Shop", Long.parseLong(shop.getId()));
                if (indicies.isEmpty()) return null;
                Long id = indicies.get(0);
                BestShopResponse bs = BestShopResponse.toDto(shop, "http://localhost:8080/api/v1/attachment/" + id);
                return bs;
            }
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<DailyShop> fetchDailyShop() {
        List<DailyShop> shops = shopRepository.getRandomThreeShops()
                .stream().map(shop -> {
                    String imgUrl = shop.getImgUrlPublic();
                    if (imgUrl == null) {
                        Long index = attachmentService.getFileIndicesByServiceNameAndTarget
                                        ("Shop", Long.parseLong(shop.getId()))
                                .get(0);
                        imgUrl = "http://localhost:8080/api/v1/attachment/" + index;
                    }
                    return DailyShop.builder()
                            .phone(shop.getPhone())
                            .name(shop.getName())
                            .address(shop.getAddress())
                            .shopId(shop.getId())
                            .imgUrl(imgUrl)
                            .build();
                }).toList();
        dailyShopService.deleteAllDailyShops();
        shops.forEach(dailyShopService::saveDailyShop);
        return shops;
    }

    public Page<Shop> findAllShops(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shop> shops = shopRepository.findAll(pageable);
        return shops;
    }

    public void editShop(ModifyShopRequest shopRequest) throws IOException {
        String shopId = shopRequest.shopId();
        Shop shop = getShopById(shopId);
        shop.editBoast(shopRequest.boast());
        shop.editInfo(shopRequest.info());
        shop.editPhone(shopRequest.phone());
        shop.editBusinessHours(shopRequest.businessHours());
        if (shopRequest.deletedFiles() != null && !shopRequest.deletedFiles().isEmpty()) {
            List<Long> attachmentIds = shopRequest.deletedFiles().stream()
                    .map(deletedFile -> Long.parseLong(deletedFile.replaceAll("http://localhost:8080/api/v1/attachment/", ""))).toList();

            for (Long attachmentId : attachmentIds) {
                attachmentService.removeAttachmentById(attachmentId);
            }
        }

        if (shopRequest.newFiles() != null && !shopRequest.newFiles().isEmpty()) {
            attachmentService.uploadFile(shopRequest.newFiles(), "Shop", Long.parseLong(shopId));
        }
    }
}