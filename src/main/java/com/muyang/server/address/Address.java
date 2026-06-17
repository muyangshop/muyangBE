package com.muyang.server.address;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String label;
    private String recipient;
    private String phone;
    private String zipcode;
    private String address1;
    private String address2;
    private boolean isDefault;
}
