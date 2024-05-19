package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/")
    public List<Long> getAttachmentIdsByServiceNameAndTarget(@RequestParam String serviceName, @RequestParam Long serviceTarget) {
        return attachmentService.getFileIndicesByServiceNameAndTarget(serviceName, serviceTarget);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getAttachmentById(@PathVariable Long id) throws IOException {
        System.out.println(id);
        HttpHeaders headers = new HttpHeaders();
        Resource resource = attachmentService.getAttachmentById(id, headers);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(resource);
    }


}
