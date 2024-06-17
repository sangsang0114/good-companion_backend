package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.sku.zero.domain.enums.ProposalStatus;

@Entity
@Table(name = "registration_proposal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicInsert
public class RegistrationProposal extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_address")
    private String shopAddress;

    @Column(name = "shop_phone")
    private String shopPhone;

    @Column(name = "business_hours")
    private String businessHours;

    @Column(name = "info")
    private String info;

    @Column(name = "reason")
    private String reason;

    @Column(name = "sector_id")
    private String sectorId;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    @Column(name = "memo")
    private String memo;

    @Builder
    public RegistrationProposal(Long memberId, String shopName, String shopAddress,
                                String shopPhone, String businessHours, String info,
                                String reason, String sectorId, String zipcode) {
        this.memberId = memberId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopPhone = shopPhone;
        this.businessHours = businessHours;
        this.info = info;
        this.reason = reason;
        this.sectorId = sectorId;
        this.zipcode = zipcode;
    }

    public void approve() {
        this.status = ProposalStatus.APPROVED;
    }

    public void reject() {
        this.status = ProposalStatus.REJECTED;
    }

    public void editMemo(String memo) {
        this.memo = memo;
    }
}
