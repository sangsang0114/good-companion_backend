package org.example.infrastructure.repository;

import org.example.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAttachmentsByServiceNameAndServiceTarget(String serviceName, Long ServiceTarget);
}
