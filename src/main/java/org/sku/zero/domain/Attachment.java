package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attachment")
@Getter
@Entity
@SQLDelete(sql = "UPDATE shopdb.attachment SET is_available = 0 WHERE id = ?")
@SQLRestriction("is_available = 1")
@DynamicInsert
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

    @Column(name = "is_available")
    private Integer isAvailable;

    @Builder
    public Attachment(String serviceName, Long serviceTarget, String originalFilename, String storedFilename) {
        this.serviceName = serviceName;
        this.serviceTarget = serviceTarget;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
    }
}
