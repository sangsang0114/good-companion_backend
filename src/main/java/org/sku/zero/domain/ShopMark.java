package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_mark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopMark extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    private Shop shop;

    @Column(name = "shop_id")
    private String shopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @Builder
    public ShopMark(String shopId, Long memberId) {
        this.shopId = shopId;
        this.memberId = memberId;
    }
}
