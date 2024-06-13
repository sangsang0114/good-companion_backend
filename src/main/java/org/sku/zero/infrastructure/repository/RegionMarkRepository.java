package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegionMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionMarkRepository extends JpaRepository<RegionMark, Long> {
    Optional<RegionMark> findRegionMarkByMemberIdAndRegionId(Long memberId, String regionId);
    List<RegionMark> findRegionMarksByMember(Member member);
}
