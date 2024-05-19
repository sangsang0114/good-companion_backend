package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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