package com.library.apigateway.mapperlayer.catalogs.catalog;

import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogResponseModel;
import com.library.apigateway.presentationlayer.catalogs.catalogbooks.CatalogBooksController;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CatalogResponseMapper {

    CatalogResponseModel responseModelToResponseModel(CatalogResponseModel catalogResponseModel);

    List<CatalogResponseModel> responseModelListToResponseModelList(List<CatalogResponseModel> catalogResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget CatalogResponseModel catalogResponseModel) {
        //self link
        Link selfLink = linkTo(methodOn(CatalogBooksController.class)
                .getCatalog(catalogResponseModel.getCatalogId()))
                .withSelfRel();
        catalogResponseModel.add(selfLink);

        //all catalogues
        Link cataloguesLink = linkTo(methodOn(CatalogBooksController.class)
                .getAllCatalogs())
                .withRel("All catalogs");
        catalogResponseModel.add(cataloguesLink);
    }
}
