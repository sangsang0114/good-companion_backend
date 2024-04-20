package org.example.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Table(name = "product")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseTime implements Persistable<Long> {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id")
    private String shopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    private Shop shop;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "recommend")
    private Integer recommend;

    @Column(name = "is_available")
    private Integer isAvailable;

    @Builder
    public Product(String shopId, String name, Integer price) {
        this.shopId = shopId;
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
