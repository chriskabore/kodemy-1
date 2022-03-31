package com.bt.dev.kodemy.users.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ContactInfos {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="skype")
    private String skype;

    @Column(name="facebook")
    private String facebook;

    @Column(name="linkedin")
    private String linkedin;

    @Column(name = "website")
    private String website;

    @Column(name="note")
    private String note;

    @OneToOne
    @MapsId
    private User user;
}
