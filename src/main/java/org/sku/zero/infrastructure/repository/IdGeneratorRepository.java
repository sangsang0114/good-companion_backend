package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.IdGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdGeneratorRepository extends JpaRepository<IdGenerator, Long> {
}
