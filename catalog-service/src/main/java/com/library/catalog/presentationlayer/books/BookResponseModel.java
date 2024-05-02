package com.library.catalog.presentationlayer.books;

import com.library.catalog.datalayer.books.Author;
import com.library.catalog.datalayer.books.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseModel {

    private Long isbn;
    private String catalogId;
    private String title;
    private String collection;
    private String edition;
    private String publisher;
    private String synopsis;
    private String language;
    private Status status;
    private Author author;
}
