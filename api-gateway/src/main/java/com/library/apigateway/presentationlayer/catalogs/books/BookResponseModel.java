package com.library.apigateway.presentationlayer.catalogs.books;

import com.library.apigateway.domainclientlayer.catalogs.Author;
import com.library.apigateway.domainclientlayer.catalogs.Status;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseModel extends RepresentationModel<BookResponseModel> {

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
