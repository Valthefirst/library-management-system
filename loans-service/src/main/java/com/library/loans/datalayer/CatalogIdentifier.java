package com.library.loans.datalayer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CatalogIdentifier {

    private String catalogId;

    public CatalogIdentifier() {
        this.catalogId = UUID.randomUUID().toString();
    }
}
