package org.example.infrastructure.repository;

import org.example.domain.RegistrationProposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationProposalRepository extends JpaRepository<RegistrationProposal, Long> {
}
