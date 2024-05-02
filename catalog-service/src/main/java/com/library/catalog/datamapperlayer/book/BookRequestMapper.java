package com.library.catalog.datamapperlayer.book;

import com.library.catalog.datalayer.books.Book;
import com.library.catalog.datalayer.books.ISBN;
import com.library.catalog.datalayer.catalog.CatalogIdentifier;
import com.library.catalog.presentationlayer.books.BookRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(expression = "java(catalogIdentifier)", target = "catalogIdentifier")
    Book requestModelToEntity(BookRequestModel requestModel,
                              ISBN isbn/*, Author author*/, CatalogIdentifier catalogIdentifier);
}
