package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.NoticeService;
import org.sku.zero.domain.Notice;
import org.sku.zero.dto.request.AddNoticeRequest;
import org.sku.zero.dto.response.ListNoticeResponse;
import org.sku.zero.dto.response.NoticeDetailResponse;
import org.sku.zero.dto.response.NoticePageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/")
    public ResponseEntity<NoticePageResponse> listNotices(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        Page<Notice> noticePage = noticeService.listNotices(page, size);
        List<ListNoticeResponse> noticeList = noticePage.stream().map(ListNoticeResponse::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(NoticePageResponse.toDto(noticeList, noticePage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> getNoticeById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(noticeService.getNoticeById(Long.parseLong(id)));
    }

    @PostMapping("/new")
    public ResponseEntity<Long> addNotice(
            @ModelAttribute AddNoticeRequest request,
            Principal principal) {
        Long id = noticeService.addNotice(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Long> updateNotice(
            @PathVariable Long id,
            @RequestBody Notice notice,
            Principal principal) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteNotice(
            @PathVariable Long id,
            Principal principal) {
        return null;
    }
}
