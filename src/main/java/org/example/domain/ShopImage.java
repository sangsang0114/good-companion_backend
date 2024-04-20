package org.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "shop_image")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShopImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    private Shop shop;

    @Column(name = "shop_id")
    private String shopId;

    @Builder
    public ShopImage(String url, String shopId) {
        this.url = url;
        this.shopId = shopId;
    }
}
