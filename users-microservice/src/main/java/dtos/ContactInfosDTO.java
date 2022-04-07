package dtos;


import com.bt.dev.kodemy.users.model.ContactInfos;
import lombok.Data;

@Data
public class ContactInfosDTO implements java.io.Serializable {

    public ContactInfosDTO() {
        // empty constructor
    }

    public ContactInfosDTO(ContactInfos contactInfos) {
        if (contactInfos != null) {
            this.email = contactInfos.getEmail();
            this.phone = contactInfos.getPhone();
            this.skype = contactInfos.getSkype();
            this.facebook = contactInfos.getFacebook();
            this.linkedin = contactInfos.getLinkedin();
            this.website = contactInfos.getWebsite();
            this.contactNote = contactInfos.getNote();
        }
    }

    private String email;
    private String phone;
    private String skype;
    private String facebook;
    private String linkedin;
    private String website;
    private String contactNote;

}
