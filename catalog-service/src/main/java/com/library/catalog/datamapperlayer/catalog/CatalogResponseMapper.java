package com.library.catalog.datamapperlayer.catalog;

import com.library.catalog.datalayer.catalog.Catalog;
import com.library.catalog.presentationlayer.catalog.CatalogResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CatalogResponseMapper {

    @Mapping(expression = "java(catalog.getCatalogIdentifier().getCatalogId())", target = "catalogId")
    CatalogResponseModel entityToResponseModel(Catalog catalog);

    List<CatalogResponseModel> entityListToResponseModelList(List<Catalog> catalogs);
}
