package com.library.patrons.datalayer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Address {

    private String streetAddress;
    private String city;
    private String province;
    private String country;
    private String postalCode;

    public Address(@NotNull String streetAddress, @NotNull String city, @NotNull String province, @NotNull String country, @NotNull String postalCode) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
    }
}
