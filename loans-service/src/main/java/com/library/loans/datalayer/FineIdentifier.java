package com.library.loans.datalayer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FineIdentifier {

    private String fineId;

    public FineIdentifier() {
        this.fineId = UUID.randomUUID().toString();
    }
}
