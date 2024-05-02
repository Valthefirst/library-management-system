package com.library.catalog.datalayer.catalog;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
public class CatalogIdentifier {

    private String catalogId;

    public CatalogIdentifier() {
        this.catalogId = UUID.randomUUID().toString();
    }
}
