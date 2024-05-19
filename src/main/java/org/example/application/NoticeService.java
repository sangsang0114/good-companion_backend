package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.ErrorCode;
import org.example.domain.Member;
import org.example.domain.Notice;
import org.example.exception.NotFoundException;
import org.example.infrastructure.repository.MemberRepository;
import org.example.infrastructure.repository.NoticeRepository;
import org.example.dto.request.AddNoticeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public Page<Notice> listNotices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> notices = noticeRepository.findAll(pageable);
        return notices;
    }

    public Notice getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SERVER_ERROR));
        return notice;
    }

    @Transactional
    public Long addNotice(AddNoticeRequest request, Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SERVER_ERROR));
        Notice notice = noticeRepository.save(request.toEntity(member.getId()));
        return notice.getId();
    }

    @Transactional
    public Long modifyNotice() {
        return null;
    }

    @Transactional
    public Boolean removeNotice(Long id) {
        return null;
    }
}
