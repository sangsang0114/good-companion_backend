package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopManager extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(name = "is_available")
    private Integer isAvailable;

    @Builder
    public ShopManager(Member member, Shop shop) {
        this.member = member;
        this.shop = shop;
    }
}
