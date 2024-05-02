package com.library.apigateway.presentationlayer.catalogs.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogResponseModel extends RepresentationModel<CatalogResponseModel> {

    private String catalogId;
    private String type;
    private Integer size;
}
