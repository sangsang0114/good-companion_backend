package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attachment")
@Getter
@Entity
public class Attachment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_target")
    private Long serviceTarget;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "stored_filedname")
    private String storedFilename;

    @Builder
    public Attachment(String serviceName, Long serviceTarget, String originalFilename, String storedFilename) {
        this.serviceName = serviceName;
        this.serviceTarget = serviceTarget;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
    }
}
