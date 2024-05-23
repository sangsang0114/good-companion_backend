package org.example.infrastructure.repository;

import org.example.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByIsAvailableOrderByIdDesc(Pageable pageable, Integer isAvailable);
}