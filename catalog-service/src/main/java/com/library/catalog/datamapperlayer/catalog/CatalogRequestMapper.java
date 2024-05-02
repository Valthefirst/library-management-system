package com.library.catalog.datamapperlayer.catalog;

import com.library.catalog.datalayer.catalog.Catalog;
import com.library.catalog.datalayer.catalog.CatalogIdentifier;
import com.library.catalog.presentationlayer.catalog.CatalogRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatalogRequestMapper {

    @Mapping(target = "id", ignore = true)
    Catalog requestModelToEntity(CatalogRequestModel requestModel, CatalogIdentifier catalogIdentifier);
}
