package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "franchise")
@Getter
public class Franchise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shopname")
    private String shopName;

    @Column(name = "zipcode")
    private String zipcode;
}