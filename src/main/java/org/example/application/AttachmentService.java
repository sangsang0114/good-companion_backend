package org.example.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Attachment;
import org.example.infrastructure.repository.AttachmentRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final String PATH = "C:\\DEV\\";

    @Transactional
    public String uploadFile(List<MultipartFile> fileList, String serviceName, Long serviceTarget) {
        StringBuilder imgUrl = new StringBuilder("http://localhost:8080/api/v1/attachment/");
        int idx = 0;
        if (fileList == null || fileList.isEmpty()) return null;
        try {
            for (MultipartFile multipartFile : fileList) {
                String originalFileName = multipartFile.getOriginalFilename();
                if (originalFileName == null || originalFileName.isEmpty()) continue;
                String ext = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
                String uuid = UUID.randomUUID().toString();
                String savedFileName = uuid + "." + ext;

                File file = new File(PATH + savedFileName);
                multipartFile.transferTo(file);
                file.setReadable(true);
                file.setWritable(true);

                Attachment attachment = Attachment.builder()
                        .originalFilename(originalFileName)
                        .storedFilename(savedFileName)
                        .serviceName(serviceName)
                        .serviceTarget(serviceTarget)
                        .build();
                Long id = attachmentRepository.save(attachment).getId();
                if (idx == 0) imgUrl.append(id);
                ++idx;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return imgUrl.toString();
    }

    @Transactional(readOnly = true)
    public List<Long> getFileIndicesByServiceNameAndTarget(String serviceName, Long serviceTarget) {
        List<Attachment> attachments = attachmentRepository.findAttachmentsByServiceNameAndServiceTarget(serviceName, serviceTarget);
        List<Long> indices = attachments.stream().map(Attachment::getId).sorted().toList();
        return indices;
    }

    @Transactional(readOnly = true)
    public Resource getImageByAttachmentId(Long attachmentId, HttpHeaders headers) throws IOException {
        String savedFileName = attachmentRepository.findById(attachmentId)
                .orElseThrow(EntityNotFoundException::new).getStoredFilename();
        FileSystemResource resource = new FileSystemResource(PATH + savedFileName);
        Path filePath = Paths.get(savedFileName);
        headers.add("Content-Type", Files.probeContentType(filePath));
        return resource;
    }

    @Transactional
    public void removeAttachmentById(Long attacmentId) throws IOException{
        return;
    }
}
