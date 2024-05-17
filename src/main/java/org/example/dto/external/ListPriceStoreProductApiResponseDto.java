package org.example.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.Product;

@Getter
@NoArgsConstructor
public class ListPriceStoreProductApiResponseDto {
    @JsonProperty("ListPriceModelStoreProductService")
    private ListPriceModelStoreProductService listPriceModelStoreProductService;

    @Getter
    public static class ListPriceModelStoreProductService {
        @JsonProperty("list_total_count")
        private Integer listTotalCount;

        @JsonProperty("RESULT")
        private Result result;

        @JsonProperty("row")
        private ListPriceModelStoreProductInfo[] row;
    }

    @Getter
    public static class Result {
        @JsonProperty("CODE")
        private String code;

        @JsonProperty("MESSAGE")
        private String message;
    }

    @Getter
    public static class ListPriceModelStoreProductInfo {
        @JsonProperty("SH_ID")
        private String shopid;

        @JsonProperty("IM_NAME")
        private String name;

        @JsonProperty("IM_PRICE")
        private Integer price;

        public Product toEntity() {
            return Product.builder()
                    .shopId(shopid)
                    .name(name)
                    .price(price)
                    .build();
        }
    }
}
