package com.bt.dev.kodemy.users.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;


    @Column(name = "password")
    private String password;

    @Enumerated
    @Column(columnDefinition = "tinyint")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_secured")
    private boolean isSecured;

    @Basic
    private LocalDateTime dateCreated;

    @Basic
    private LocalDateTime dateUpdated;

    @Basic
    private LocalDateTime dateOfLastLogin;

    @Column(name = "note")
    private String note;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ContactInfos contactInfos;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AddressInfos addressInfos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
             name = "users_roles",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles= new HashSet<>();

    public void addRole(Role role){
         this.roles.add(role);
    }


    public List<Permission> getPermissions() {
        return this.roles.isEmpty() ? new ArrayList<>() : getRoles().stream()
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}


