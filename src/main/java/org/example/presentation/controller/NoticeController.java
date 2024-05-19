package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.NoticeService;
import org.example.domain.Notice;
import org.example.dto.request.AddNoticeRequest;
import org.example.dto.response.ListNoticeResponse;
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
    public ResponseEntity<List<ListNoticeResponse>> listNotices(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        Page<Notice> notices = noticeService.listNotices(page, size);
        List<ListNoticeResponse> response = notices.stream().map(ListNoticeResponse::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/new")
    public ResponseEntity<Long> addNotice(
            @RequestBody AddNoticeRequest request,
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
