package org.example.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.ProductService;
import org.example.application.ShopImageService;
import org.example.application.ShopLocationService;
import org.example.application.ShopService;
import org.example.domain.Shop;
import org.example.dto.external.GeoCoderResultDto;
import org.example.dto.external.ListPriceStoreApiResponseDto;
import org.example.dto.external.ListPriceStoreProductApiResponseDto;
import org.example.infrastructure.repository.ProductRepository;
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
    private final ShopImageService shopImageService;
    private final ProductRepository productRepository;
    private final ProductService productService;

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
            List<ListPriceStoreApiResponseDto.ListPriceStoreApiInfo> dto = shopService.loadShopInfo();
            for (ListPriceStoreApiResponseDto.ListPriceStoreApiInfo info : dto) {
                GeoCoderResultDto geoCoderResultDto = shopLocationService.getCoordinateAndRegionId(info.getAddress());

                // 유효하지 않은 주소의 경우 별도 처리
                if (geoCoderResultDto == null) {
                    continue;
                }
                validIds.add(info.getId());

                String regionId = geoCoderResultDto.regionId().substring(0, 5);
                String refinedAddress = geoCoderResultDto.refinedAddress();
                String businessHours = shopService.extractBusinessHours(info.getInfo());
                Shop shop = shopService.save(info.toShopEntity(regionId, refinedAddress, businessHours));
                shopImageService.save(info.toShopImageEntity());
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