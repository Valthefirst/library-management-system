package com.library.patrons.presentationlayer;

import com.library.patrons.datalayer.ContactMethodPreference;
import com.library.patrons.datalayer.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronResponseModel {

    private String patronId;
    private String firstName;
    private String lastName;
    private String emailAddress;

    private ContactMethodPreference contactMethodPreference;

    private String streetAddress;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private List<PhoneNumber> phoneNumbers;
}
