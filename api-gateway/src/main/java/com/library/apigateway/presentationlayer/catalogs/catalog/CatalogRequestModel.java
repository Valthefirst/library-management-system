package com.library.apigateway.presentationlayer.catalogs.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogRequestModel {

    private String type;
    private Integer size;
}
