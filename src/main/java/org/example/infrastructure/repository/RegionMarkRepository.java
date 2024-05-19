package org.example.infrastructure.repository;

import org.example.domain.RegionMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegionMarkRepository extends JpaRepository<RegionMark, Long> {
    Optional<RegionMark> findRegionMarkByMemberIdAndRegionId(Long memberId, String regionId);

    @Query("SELECT r.member.email FROM RegionMark r JOIN r.member m WHERE r.regionId= :regionId AND r.emailFlag = 1")
    List<String> findEmailsByRegionId(String regionId);

    @Query("SELECT r.member.fcmToken FROM RegionMark r JOIN r.member m WHERE r.regionId= :regionId AND r.fcmFlag = 1")
    List<String> findFcmTokensByRegionId(String regionId);
}
