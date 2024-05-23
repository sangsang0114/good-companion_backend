package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.domain.Product;
import org.example.domain.Shop;
import org.example.dto.external.ListPriceStoreProductApiResponseDto;
import org.example.dto.request.AddProductRequest;
import org.example.dto.response.ProductResponse;
import org.example.exception.ErrorCode;
import org.example.exception.NotFoundException;
import org.example.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final WebClient.Builder webClientBuilder;
    private final ShopService shopService;
    private final ProductRepository productRepository;

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088";
    private final AttachmentService attachmentService;
    @Value("${seoul.key}")
    private String KEY;
    private final String RETURN_TYPE = "json";
    private final String SERVICE_NAME = "ListPriceModelStoreProductService";

    public List<ListPriceStoreProductApiResponseDto.ListPriceModelStoreProductInfo> loadShopProductInfo() {
        List<ListPriceStoreProductApiResponseDto.ListPriceModelStoreProductInfo> results = new ArrayList<>();
        int startIndex = 1;
        int endIndex = 1000;
        int maxIndex = 4000;
        while (startIndex <= maxIndex) {
            String requestUrl = UriComponentsBuilder
                    .fromUriString(BASE_URL)
                    .pathSegment(KEY, RETURN_TYPE, SERVICE_NAME, startIndex + "", (startIndex + endIndex - 1) + "")
                    .build().toUriString();

            WebClient webClient = webClientBuilder
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(30 * 1024 * 1024)) //10MB
                    .baseUrl(requestUrl).build();
            ListPriceStoreProductApiResponseDto responseDto = webClient.get()
                    .retrieve()
                    .bodyToMono(ListPriceStoreProductApiResponseDto.class)
                    .block();
            results.addAll(Arrays.stream(responseDto.getListPriceModelStoreProductService().getRow()).toList());
            startIndex += 1000;
        }
        return results;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByShopId(String shopId) {
        Shop shop = shopService.getShopById(shopId);
        List<Product> products = productRepository.findShopProductsByShop(shop);
        List<ProductResponse> responses = products.stream().map(product -> {
            List<Long> indices = attachmentService.getFileIndicesByServiceNameAndTarget("Product", product.getId());
            String imgUrl;
            if (indices.isEmpty()) imgUrl = null;
            else imgUrl = "http://localhost:8080/api/v1/attachment/" + indices.get(0);
            return ProductResponse.toDto(product, imgUrl);
        }).toList();
        return responses;
    }

    @Transactional
    public Long addProduct(AddProductRequest addProductRequest) {
        Shop shop = shopService.getShopById(addProductRequest.shopId());
        Product product = productRepository.save(addProductRequest.toEntity());
        return product.getId();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));
        return product;
    }

    @Transactional
    public Long editProduct(Long id, Integer price) {
        Product product = getProductById(id);
        product.editPrice(price);
        return product.getId();
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.delete();
    }
}
