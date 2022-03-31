package com.bt.dev.kodemy.users.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "permission", nullable = false)
    private String permission;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @Column(name = "note")
    private String note;

}
