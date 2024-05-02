package com.library.catalog.datalayer.books;

import com.library.catalog.datalayer.catalog.CatalogIdentifier;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="books")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ISBN isbn;

    @Embedded
    private CatalogIdentifier catalogIdentifier;
    private String title;
    private String collection;
    private String edition;
    private String publisher;
    private String synopsis;
    private String language;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private Author author;

    public Book(@NotNull ISBN isbn, @NotNull CatalogIdentifier catalogIdentifier, @NotNull String title, String collection, @NotNull String edition, @NotNull String publisher, @NotNull String synopsis, @NotNull String language, @NotNull Status status, @NotNull Author author) {
        this.isbn = isbn;
        this.catalogIdentifier = catalogIdentifier;
        this.title = title;
        this.collection = collection;
        this.edition = edition;
        this.publisher = publisher;
        this.synopsis = synopsis;
        this.language = language;
        this.status = status;
        this.author = author;
    }

}
