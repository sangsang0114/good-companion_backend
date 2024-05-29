package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;

@Table(name = "shop_location")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShopLocation extends BaseTime implements Persistable<String> {
    @Id
    @Column(name = "shop_id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    private Shop shop;

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

    @Builder
    public ShopLocation(String shopId, BigDecimal latitude, BigDecimal longitude) {
        this.id = shopId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }
}
