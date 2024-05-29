package org.sku.zero.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "region")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;
}
