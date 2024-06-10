package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Table(name = "review")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Review extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    private Shop shop;

    @Column(name = "shop_id")
    private String shopId;

    @Column(name = "score")
    private Double score;

    @Column(name = "comment")
    private String comment;

    @Builder
    public Review(Long memberId, String shopId, Double score, String comment) {
        this.memberId = memberId;
        this.shopId = shopId;
        this.score = score;
        this.comment = comment;
    }

    public void editReview(String comment, Double score) {
        this.comment = comment;
        this.score = score;
    }
}
