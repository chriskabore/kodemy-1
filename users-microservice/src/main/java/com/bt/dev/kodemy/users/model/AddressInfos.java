package com.bt.dev.kodemy.users.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AddressInfos {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name="address")
    private String address;

    @Column(name="address2")
    private String address2;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="zip_code")
    private String zipCode;

    @OneToOne
    @MapsId
    private User user;

    public AddressInfos(String address, String address2, String city, String country, String zipCode) {
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
    }
}
