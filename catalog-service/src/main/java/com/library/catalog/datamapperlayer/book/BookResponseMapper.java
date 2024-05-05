package com.library.catalog.datamapperlayer.book;

import com.library.catalog.datalayer.books.Book;
import com.library.catalog.presentationlayer.books.BookResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookResponseMapper {

    @Mapping(expression = "java(book.getIsbn().getIsbn())", target = "isbn")
    @Mapping(expression = "java(book.getCatalogIdentifier().getCatalogId())", target = "catalogId")
    BookResponseModel entityToResponseModel(Book book);

    List<BookResponseModel> entityListToResponseModelList(List<Book> books);
}
