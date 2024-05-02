package com.library.catalog.presentationlayer.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogResponseModel {

    private String catalogId;
    private String type;
    private Integer size;
}
