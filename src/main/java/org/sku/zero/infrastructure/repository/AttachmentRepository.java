package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAttachmentsByServiceNameAndServiceTarget(String serviceName, Long ServiceTarget);
}
