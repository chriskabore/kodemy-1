package dtos;


import com.bt.dev.kodemy.users.model.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PermissionDTO implements java.io.Serializable {

    private Long permissionId;
    private String permission;
    private boolean enabled;
    private String note;

    public PermissionDTO(Permission permission) {
        this.permissionId = permission.getPermissionId();
        this.permission = permission.getPermission();
        this.enabled = permission.isEnabled();
        this.note = permission.getNote();
    }

}
