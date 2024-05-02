package com.library.catalog.presentationlayer.catalogbooks;

import com.library.catalog.businesslayer.catalogbooks.CatalogBooksService;
import com.library.catalog.datalayer.books.Author;
import com.library.catalog.datalayer.books.Status;
import com.library.catalog.presentationlayer.books.BookRequestModel;
import com.library.catalog.presentationlayer.books.BookResponseModel;
import com.library.catalog.presentationlayer.catalog.CatalogRequestModel;
import com.library.catalog.presentationlayer.catalog.CatalogResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.library.catalog.datalayer.books.Status.DAMAGED;

@SpringBootTest(classes = CatalogBooksController.class)
class CatalogBooksControllerUnitTest {

    private final String BASE_URI_CATALOG = "api/v1/catalogs";
    private final String FOUND_CATALOG_ID = "d846a5a7-2e1c-4c79-809c-4f3f471e826d";
    private final String FOUND_CATALOG_TYPE = "Adult";
    private final int FOUND_CATALOG_SIZE = 4;
    private final String NOT_FOUND_CATALOG_ID = "ef23ab6e-d614-47b9-95d0-d66167ae5082";
    private final Long FOUND_BOOK_ISBN = 9789390183524L;
    private final String FOUND_BOOK_TITLE = "The Great Gatsby";
    private final String FOUND_BOOK_COLLECTION = "F. Scott Fitzgerald";
    private final String FOUND_BOOK_EDITION = "";
    private final String FOUND_BOOK_PUBLISHER = "Scribner";
    private final String FOUND_BOOK_SYNOPSIS = "The Great Gatsby is a novel written by American author F. Scott " +
            "Fitzgerald that follows a cast of characters living in the fictional towns of West Egg and East Egg on " +
            "prosperous Long Island in the summer of 1922. The story primarily concerns the young and mysterious " +
            "millionaire Jay Gatsby and his quixotic passion and obsession with the beautiful former debutante Daisy Buchanan.";
    private final String FOUND_BOOK_LANGUAGE = "English";
    private final String FOUND_BOOK_STATUS_STRING = "DAMAGED";
    private final Status FOUND_BOOK_STATUS = DAMAGED;
    private final String FOUND_BOOK_AUTHOR_FNAME = "F. Scott";
    private final String FOUND_BOOK_AUTHOR_LNAME = "Fitzgerald";
    private final Long INVALID_BOOK_ISBN = 97893901835L;

    @Autowired
    private CatalogBooksController catalogBooksController;

    @MockBean
    private CatalogBooksService catalogBooksService;

    private CatalogRequestModel buildCatalogRequestModel() {
        return CatalogRequestModel.builder()
                .type(FOUND_CATALOG_TYPE)
                .size(FOUND_CATALOG_SIZE)
                .build();
    }

//    private CatalogRequestModel buildBadCatalogRequestModel() {
//        return CatalogRequestModel.builder()
//                .type(FOUND_CATALOG_TYPE)
//                .size(10)
//                .build();
//    }

    private CatalogResponseModel buildCatalogResponseModel() {
        return CatalogResponseModel.builder()
                .catalogId(FOUND_CATALOG_ID)
                .type(FOUND_CATALOG_TYPE)
                .size(FOUND_CATALOG_SIZE)
                .build();
    }

    private BookRequestModel buildBookRequestModel() {
        return BookRequestModel.builder()
                .isbn(FOUND_BOOK_ISBN)
                .title(FOUND_BOOK_TITLE)
                .collection(FOUND_BOOK_COLLECTION)
                .edition(FOUND_BOOK_EDITION)
                .publisher(FOUND_BOOK_PUBLISHER)
                .synopsis(FOUND_BOOK_SYNOPSIS)
                .language(FOUND_BOOK_LANGUAGE)
                .status(FOUND_BOOK_STATUS_STRING)
                .author(new Author(FOUND_BOOK_AUTHOR_FNAME, FOUND_BOOK_AUTHOR_LNAME))
                .build();
    }

    private BookRequestModel buildBadBookRequestModel() {
        return BookRequestModel.builder()
                .isbn(INVALID_BOOK_ISBN)
                .title(FOUND_BOOK_TITLE)
                .collection(FOUND_BOOK_COLLECTION)
                .edition(FOUND_BOOK_EDITION)
                .publisher(FOUND_BOOK_PUBLISHER)
                .synopsis(FOUND_BOOK_SYNOPSIS)
                .language(FOUND_BOOK_LANGUAGE)
                .status(FOUND_BOOK_STATUS_STRING)
                .author(new Author(FOUND_BOOK_AUTHOR_FNAME, FOUND_BOOK_AUTHOR_LNAME))
                .build();
    }

    private BookResponseModel buildFineBookResponseModel() {
        return BookResponseModel.builder()
                .isbn(FOUND_BOOK_ISBN)
                .catalogId(FOUND_CATALOG_ID)
                .title(FOUND_BOOK_TITLE)
                .collection(FOUND_BOOK_COLLECTION)
                .edition(FOUND_BOOK_EDITION)
                .publisher(FOUND_BOOK_PUBLISHER)
                .synopsis(FOUND_BOOK_SYNOPSIS)
                .language(FOUND_BOOK_LANGUAGE)
                .status(FOUND_BOOK_STATUS)
                .author(new Author(FOUND_BOOK_AUTHOR_FNAME, FOUND_BOOK_AUTHOR_LNAME))
                .build();
    }

}