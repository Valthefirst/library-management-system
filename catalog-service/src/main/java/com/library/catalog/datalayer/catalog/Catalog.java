package com.library.catalog.datalayer.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="catalogs")
@Data
@NoArgsConstructor
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private CatalogIdentifier catalogIdentifier;
    private String type;
    private Integer size;

    public Catalog(@NotNull CatalogIdentifier catalogIdentifier, @NotNull String type, @NotNull Integer size) {
        this.catalogIdentifier = catalogIdentifier;
        this.type = type;
        this.size = size;
    }
}
