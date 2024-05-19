package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.FileSystemResource;

@Getter
@Setter
@Builder
public class LoadAttachmentResponse {
    private Long attachmentId;
    private FileSystemResource resource;

    public static LoadAttachmentResponse toDto(Long attachmentId, FileSystemResource resource) {
        return LoadAttachmentResponse.builder()
                .attachmentId(attachmentId)
                .resource(resource)
                .build();
    }
}
