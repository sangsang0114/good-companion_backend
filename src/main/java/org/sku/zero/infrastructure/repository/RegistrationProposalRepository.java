package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.RegistrationProposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationProposalRepository extends JpaRepository<RegistrationProposal, Long> {
}
