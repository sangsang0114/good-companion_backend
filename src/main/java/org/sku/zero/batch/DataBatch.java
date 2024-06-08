package org.sku.zero.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.application.FranchiseService;
import org.sku.zero.application.ProductService;
import org.sku.zero.application.ShopLocationService;
import org.sku.zero.application.ShopService;
import org.sku.zero.domain.Shop;
import org.sku.zero.dto.external.GeoCoderResultDto;
import org.sku.zero.dto.external.ListPriceStoreApiResponseDto;
import org.sku.zero.dto.external.ListPriceStoreProductApiResponseDto;
import org.sku.zero.infrastructure.repository.ProductRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataBatch {
    private final ShopService shopService;
    private final ShopLocationService shopLocationService;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final FranchiseService franchiseService;

    @Bean
    public Job loadShopJob(JobRepository jobRepository, Step loadShopStep) {
        return new JobBuilder("loadShopJob", jobRepository)
                .start(loadShopStep)
                .build();
    }

    @Bean
    public Step loadShopStep(JobRepository jobRepository, Tasklet loadShopTasklet,
                             PlatformTransactionManager transactionManager) {
        return new StepBuilder("loadShopStep", jobRepository)
                .tasklet(loadShopTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet loadshopTasklet(JobRepository jobRepository) {
        Set<String> validIds = new HashSet<>();
        return ((contribution, chunkContext) -> {
            List<Shop> shops = shopService.findAllShops();
            shops.forEach(shop -> validIds.add(shop.getId()));
            List<ListPriceStoreApiResponseDto.ListPriceStoreApiInfo> dto = shopService.loadShopInfo();
            for (ListPriceStoreApiResponseDto.ListPriceStoreApiInfo info : dto) {
                //이미 등록되어있는 가게의 경우 스킵
                if (validIds.contains(info.getId())) continue;
                GeoCoderResultDto geoCoderResultDto = shopLocationService.getCoordinateAndRegionId(info.getAddress());
                // 유효하지 않은 주소의 경우 별도 처리
                if (geoCoderResultDto == null) {
                    continue;
                }
                validIds.add(info.getId());

                String x = geoCoderResultDto.longitude();
                String y = geoCoderResultDto.latitude();
                String zipCode = shopLocationService.getZipcode(x, y);
                Integer isFranchise = franchiseService.isFranchise(zipCode, info.getName()) ? 1 : 0;

                String regionId = geoCoderResultDto.regionId().substring(0, 5);
                String refinedAddress = geoCoderResultDto.refinedAddress();
                String businessHours = shopService.extractBusinessHours(info.getInfo());
                Shop shop = shopService.save(info.toShopEntity(regionId, refinedAddress, businessHours, zipCode, isFranchise));
                shopLocationService.save(geoCoderResultDto.toEntity(shop.getId()));
            }

            List<ListPriceStoreProductApiResponseDto.ListPriceModelStoreProductInfo> results = productService.loadShopProductInfo();
            for (ListPriceStoreProductApiResponseDto.ListPriceModelStoreProductInfo info : results)
                if (validIds.contains(info.getShopid()))
                    productRepository.saveOrUpdateProduct(info.toEntity());
            return RepeatStatus.FINISHED;
        });
    }
}