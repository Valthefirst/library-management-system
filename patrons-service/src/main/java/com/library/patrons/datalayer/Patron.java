package com.library.patrons.datalayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="patrons")
@Data
@NoArgsConstructor
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private PatronIdentifier patronIdentifier;
    private String firstName;
    private String lastName;
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    private ContactMethodPreference contactMethodPreference;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "patron_phonenumbers", joinColumns = @JoinColumn(name="patron_id"))
    private List<PhoneNumber> phoneNumbers;

    public Patron(@NotNull String firstName, @NotNull String lastName, @NotNull String emailAddress,
                  @NotNull ContactMethodPreference contactMethodPreference, @NotNull Address address,
                  @NotNull List<PhoneNumber> phoneNumbers) {
        this.patronIdentifier = new PatronIdentifier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.contactMethodPreference = contactMethodPreference;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
    }
}
