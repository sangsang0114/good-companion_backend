package org.sku.zero.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "id_generator")
@Entity
@Getter
public class IdGenerator {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "generated_id")
    private Long generatedId;

    public Long generateNewId() {
        return this.generatedId++;
    }
}