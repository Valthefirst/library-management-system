package com.library.apigateway.mapperlayer.catalogs.book;

import com.library.apigateway.presentationlayer.catalogs.books.BookResponseModel;
import com.library.apigateway.presentationlayer.catalogs.catalogbooks.CatalogBooksController;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface BookResponseMapper {

    BookResponseModel responseModelToResponseModel(BookResponseModel bookResponseModel);

    List<BookResponseModel> responseModelListToResponseModelList(List<BookResponseModel> bookResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget BookResponseModel bookResponseModel) {
        //self link
        Link selfLink = linkTo(methodOn(CatalogBooksController.class)
                .getBook(bookResponseModel.getCatalogId(), bookResponseModel.getIsbn()))
                .withSelfRel();
        bookResponseModel.add(selfLink);

        // all books link
        Link booksLink = linkTo(methodOn(CatalogBooksController.class)
                .getAllBooks(bookResponseModel.getCatalogId()))
                .withRel("books in this catalog");
        bookResponseModel.add(booksLink);

        //get catalogue by id
        Link catalogueId = linkTo(methodOn(CatalogBooksController.class)
                .getCatalog(bookResponseModel.getCatalogId()))
                .withRel("catalog");
        bookResponseModel.add(catalogueId);
    }
}
