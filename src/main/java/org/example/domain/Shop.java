package org.example.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode
public class Shop extends BaseTime implements Persistable<String> {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "sector_id")
    private String shopSector;

    @Column(name = "region_id")
    private String shopRegion;

    @Column(name = "boast")
    private String boast;

    @Column(name = "business_hours")
    private String businessHours;

    @Column(name = "info")
    private String info;

    @Column(name = "phone")
    private String phone;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "is_available")
    private Integer isAvailable;

    @Column(name = "recommend")
    private Long recommend;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "is_local_franchise")
    private Integer isLocalFranchise = 0;

    @Builder
    public Shop(String id, String name, String address, String shopSector,
                String shopRegion, String boast, String info, String phone,
                Long recommend, String businessHours, Integer isLocalFranchise, String zipcode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.shopSector = shopSector;
        this.shopRegion = shopRegion;
        this.boast = boast;
        this.info = info;
        this.phone = phone;
        this.recommend = recommend;
        this.businessHours = businessHours;
        this.isLocalFranchise = isLocalFranchise;
        this.zipcode = zipcode;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }

    public void disableShop() {
        this.isAvailable = 0;
    }

    public void enableShop() {
        this.isAvailable = 1;
    }

    public void setRateAvg(Double rate) {
        this.rate = rate;
    }

    public void editPhone(String phone) {
        this.phone = phone;
    }

    public void editInfo(String info) {
        this.info = info;
    }

    public void editBoast(String boast) {
        this.boast = boast;
    }

    public void editBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }
}
