package com.library.loans.datalayer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PatronIdentifier {

    private String patronId;

    public PatronIdentifier() {
        this.patronId = UUID.randomUUID().toString();
    }
}
