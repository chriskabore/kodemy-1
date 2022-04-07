package com.bt.dev.kodemy.users.model;

import jdk.jfr.DataAmount;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    public static final long USER = 1;
    public static final long ADMINISTRATOR = 2;

    public Role(String roleName){
        this.roleName = roleName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permissions",
            joinColumns=@JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    public List<Permission> permissions = new ArrayList<>();

}
