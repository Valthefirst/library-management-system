package com.library.fines.datalayer;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
public class FineIdentifier {

    private String fineId;

    public FineIdentifier() {
        this.fineId = UUID.randomUUID().toString();
    }
}
