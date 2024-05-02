package com.library.catalog.datamapperlayer.catalog;

import com.library.catalog.datalayer.catalog.Catalog;
import com.library.catalog.presentationlayer.catalog.CatalogResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CatalogResponseMapper {

    @Mapping(expression = "java(catalog.getCatalogIdentifier().getCatalogId())", target = "catalogId")
    CatalogResponseModel entityToResponseModel(Catalog catalog);

    List<CatalogResponseModel> entityListToResponseModelList(List<Catalog> catalogs);

//    @AfterMapping
//    default void addLinks(@MappingTarget CatalogResponseModel model, Catalog catalog) {
//        //self link
//        Link selfLink = linkTo(methodOn(CatalogBooksController.class)
//                .getCatalog(model.getCatalogId()))
//                .withSelfRel();
//        model.add(selfLink);
//
//        //all catalogues
//        Link cataloguesLink = linkTo(methodOn(CatalogBooksController.class)
//                .getAllCatalogs())
//                .withRel("catalogs");
//        model.add(cataloguesLink);
//    }
}
