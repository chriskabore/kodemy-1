package dtos;


import com.bt.dev.kodemy.users.model.Permission;
import com.bt.dev.kodemy.users.model.Role;
import com.bt.dev.kodemy.users.model.User;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    public UserDTO() {
        // empty constructor
        roles = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public UserDTO(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.gender = user.getGender().name();

            this.birthDate = user.getBirthDate();

            this.enabled = user.isEnabled();

            this.note = user.getNote();

            this.dateCreated = user.getDateCreated();
            this.dateUpdated = user.getDateUpdated();
            this.dateOfLastLogin = user.getDateOfLastLogin();

            this.secured = user.isSecured();

            // contact, if set
            if (user.getContactInfos() != null) {
                this.contactInfosDTO = new ContactInfosDTO(user.getContactInfos());
            }

            // address, if set
            if (user.getAddressInfos() != null) {
                this.addressInfosDTO = new AddressInfosDTO(user.getAddressInfos());
            }

            // Because the permissions can be associated to more than one roles i'm creating two String arrays
            // with the distinct keys of roles and permissions.
            roles = new ArrayList<>();
            permissions = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roles.add(role.getRoleName());
                for (Permission p : role.getPermissions()) {
                    String key = p.getPermission();
                    if ((!permissions.contains(key)) && (p.isEnabled())) {
                        // add the permission only if enabled
                        permissions.add(key);
                    }
                }
            }

        }
    }

    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private java.time.LocalDate birthDate;

    private boolean enabled;

    private String note;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private LocalDateTime dateOfLastLogin;

    private boolean secured;

    private ContactInfosDTO contactInfosDTO;
    private AddressInfosDTO addressInfosDTO;

    // permissions and roles list
    private List<String> roles;
    private List<String> permissions;

}
