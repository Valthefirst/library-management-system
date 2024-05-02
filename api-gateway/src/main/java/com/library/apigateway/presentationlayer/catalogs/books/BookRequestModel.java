package com.library.apigateway.presentationlayer.catalogs.books;

import com.library.apigateway.domainclientlayer.catalogs.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestModel {

    private Long isbn;
    private String title;
    private String collection;
    private String edition;
    private String publisher;
    private String synopsis;
    private String language;
    private String status;
    private Author author;
}
