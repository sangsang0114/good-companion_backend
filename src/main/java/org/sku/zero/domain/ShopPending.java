package org.sku.zero.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.domain.Persistable;

@Table(name = "shop_pending")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicInsert
public class ShopPending extends BaseTime implements Persistable<String> {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;


    @Column(name = "sector_id")
    private String shopSector;

    @Column(name = "boast")
    private String boast;

    @Column(name = "info")
    private String info;

    @Column(name = "phone")
    private String phone;

    @Column(name = "img_url_public")
    private String imgUrlPublic;

    @Column(name = "error_info")
    private String errorInfo;

    @Column(name = "status")
    private String status;

    @Column(name = "memo")
    private String memo;

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }

    @Builder
    public ShopPending(String id, String name, String address, String shopSector,
                       String boast, String info, String phone, String imgUrlPublic, String errorInfo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.shopSector = shopSector;
        this.boast = boast;
        this.info = info;
        this.phone = phone;
        this.imgUrlPublic = imgUrlPublic;
        this.errorInfo = errorInfo;
    }

    public void editMemo(String memo) {
        this.memo = memo;
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
