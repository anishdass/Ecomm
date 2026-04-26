package com.app.ecomm_application.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "adresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;

    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
