package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegistrationProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistrationProposalRepository extends JpaRepository<RegistrationProposal, Long> {
    @Query("SELECT rp FROM RegistrationProposal rp JOIN FETCH rp.member WHERE rp.id =:id")
    Optional<RegistrationProposal> findRegistrationProposalById(Long id);

    List<RegistrationProposal> findRegistrationProposalsByMember(Member member);
}
