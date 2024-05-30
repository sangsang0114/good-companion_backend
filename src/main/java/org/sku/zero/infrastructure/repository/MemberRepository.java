package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);

    List<Member> findMembersByFcmTokenIsNotNull();
}
