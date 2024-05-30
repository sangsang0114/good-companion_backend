package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Notice;
import org.sku.zero.dto.request.AddNoticeRequest;
import org.sku.zero.dto.response.NoticeDetailResponse;
import org.sku.zero.event.NoticeAddedEvent;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.NotFoundException;
import org.sku.zero.infrastructure.repository.MemberRepository;
import org.sku.zero.infrastructure.repository.NoticeRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final AttachmentService attachmentService;
    private final MemberService memberService;
    private final ApplicationEventPublisher eventPublisher;

    public Page<Notice> listNotices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> notices = noticeRepository.findAllByIsAvailableOrderByIdDesc(pageable, 1);
        return notices;
    }

    @Transactional
    public Long addNotice(AddNoticeRequest request, Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SERVER_ERROR));
        Notice notice = noticeRepository.save(request.toEntity(member.getId()));
        List<MultipartFile> files = request.files();
        attachmentService.uploadFile(files, "Notice", notice.getId());
        
        if(request.isImportant()){
            eventPublisher.publishEvent(new NoticeAddedEvent(this, request.title(), null));
        }
        return notice.getId();
    }

    @Transactional
    public NoticeDetailResponse getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTICE_NOT_FOUND));

        Member member = memberService.findById(notice.getMemberId());
        List<Long> attachmentIndices = attachmentService.getFileIndicesByServiceNameAndTarget
                ("Notice", notice.getId());
        List<String> urls = attachmentIndices.stream().map(index -> "http://localhost:8080/api/v1/attachment/" + index).toList();
        NoticeDetailResponse response = NoticeDetailResponse.toDto(member, notice, urls);
        return response;
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
