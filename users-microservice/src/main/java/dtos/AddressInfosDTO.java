package dtos;


import com.bt.dev.kodemy.users.model.AddressInfos;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressInfosDTO implements Serializable {

    public AddressInfosDTO() {
        // empty constructor
    }

    public AddressInfosDTO(AddressInfos addressInfos) {
        if (addressInfos != null) {
            this.address = addressInfos.getAddress();
            this.address2 = addressInfos.getAddress2();
            this.city = addressInfos.getCity();
            this.country = addressInfos.getCountry();
            this.zipCode = addressInfos.getZipCode();
        }
    }

    private String address;
    private String address2;
    private String city;
    private String country;
    private String zipCode;

}
