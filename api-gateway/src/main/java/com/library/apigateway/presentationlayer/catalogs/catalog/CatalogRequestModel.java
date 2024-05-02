package com.library.apigateway.presentationlayer.catalogs.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogRequestModel {

    private String type;
    private Integer size;
}
