package com.library.loans.domainclientlayer.catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class BookModel {

    Long isbn;
    String catalogId;
    String title;
    String collection;
    Status status;
    Author author;
}
