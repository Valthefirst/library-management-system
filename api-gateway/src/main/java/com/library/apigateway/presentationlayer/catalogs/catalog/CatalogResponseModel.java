package com.library.apigateway.presentationlayer.catalogs.catalog;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogResponseModel extends RepresentationModel<CatalogResponseModel> {

    private String catalogId;
    private String type;
    private Integer size;
}
