package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByIsAvailableOrderByIdDesc(Pageable pageable, Integer isAvailable);
}