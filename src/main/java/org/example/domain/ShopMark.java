package org.example.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;

@Entity
@Table(name = "shop_mark")
public class ShopMark extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}
