package org.example.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "region_mark")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegionMark extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "id")
    private Long id;

    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "email_flag")
    private Integer emailFlag;

    @Column(name = "fcm_Flag")
    private Integer fcmFlag;

    @Builder
    public RegionMark(Long memberId, String regionId) {
        this.memberId = memberId;
        this.regionId = regionId;
        this.emailFlag = 1;
        this.fcmFlag = 1;
    }
}