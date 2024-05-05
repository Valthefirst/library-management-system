package com.library.catalog.presentationlayer.catalogbooks;

import com.library.catalog.businesslayer.catalogbooks.CatalogBooksService;
import com.library.catalog.datalayer.books.Author;
import com.library.catalog.datalayer.books.Status;
import com.library.catalog.presentationlayer.books.BookRequestModel;
import com.library.catalog.presentationlayer.books.BookResponseModel;
import com.library.catalog.presentationlayer.catalog.CatalogRequestModel;
import com.library.catalog.presentationlayer.catalog.CatalogResponseModel;
import com.library.catalog.utils.exceptions.DuplicateISBNException;
import com.library.catalog.utils.exceptions.InvalidISBNException;
import com.library.catalog.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static com.library.catalog.datalayer.books.Status.DAMAGED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CatalogBooksController.class)
class CatalogBooksControllerUnitTest {

    private final String FOUND_CATALOG_ID = "d846a5a7-2e1c-4c79-809c-4f3f471e826d";
    private final String OTHER_CATALOG_ID = "e125b033-591e-464d-91de-beaf360c3d48";
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
            "millionaire Jay Gatsby and his quixotic passion and obsession with the beautiful former debutante " +
            "Daisy Buchanan.";
    private final String FOUND_BOOK_LANGUAGE = "English";
    private final String FOUND_BOOK_STATUS_STRING = "DAMAGED";
    private final Status FOUND_BOOK_STATUS = DAMAGED;
    private final String FOUND_BOOK_AUTHOR_FNAME = "F. Scott";
    private final String FOUND_BOOK_AUTHOR_LNAME = "Fitzgerald";
    private final Long NOT_FOUND_BOOK_ISBN = 9789390183520L;
    private final Long INVALID_BOOK_ISBN = 978939018352L;

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

    private BookRequestModel buildBadBookRequestModel(Long isbn) {
        return BookRequestModel.builder()
                .isbn(isbn)
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

    private BookResponseModel buildBookResponseModel() {
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

    //                                                      CATALOGS

    // positive test case
    @Test
    public void whenCatalogsExists_thenReturnCatalogList() {
        // arrange
        CatalogResponseModel catalogResponseModel = buildCatalogResponseModel();

        when(catalogBooksService.getAllCatalogs()).thenReturn(Collections.singletonList(catalogResponseModel));

        // act
        ResponseEntity<List<CatalogResponseModel>> responseEntity = catalogBooksController.getAllCatalogs();

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(catalogResponseModel, responseEntity.getBody().get(0));
        verify(catalogBooksService, times(1)).getAllCatalogs();
    }

    // negative test case
    @Test
    public void whenNoCatalogExists_thenReturnEmptyList() {
        // arrange
        when(catalogBooksService.getAllCatalogs()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<CatalogResponseModel>> responseEntity = catalogBooksController.getAllCatalogs();

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());
        verify(catalogBooksService, times(1)).getAllCatalogs();
    }

    // positive test case
    @Test
    public void whenCatalogExists_thenReturnCatalog() {
        // arrange
        CatalogResponseModel catalogResponseModel = buildCatalogResponseModel();

        when(catalogBooksService.getCatalog(FOUND_CATALOG_ID)).thenReturn(catalogResponseModel);

        // act
        ResponseEntity<CatalogResponseModel> responseEntity = catalogBooksController.getCatalog(FOUND_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(catalogResponseModel, responseEntity.getBody());
        verify(catalogBooksService, times(1)).getCatalog(FOUND_CATALOG_ID);
    }

    // negative test case
    @Test
    public void whenCatalogDoesNotExist_thenReturnNotFound() {
        // arrange
        when(catalogBooksService.getCatalog(NOT_FOUND_CATALOG_ID)).thenThrow(new NotFoundException(
                "Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.getCatalog(NOT_FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID, exception.getMessage());
        verify(catalogBooksService, times(1)).getCatalog(NOT_FOUND_CATALOG_ID);
    }

    // positive test case
    @Test
    public void whenCatalogCreated_thenReturnCatalog() {
        // arrange
        CatalogRequestModel catalogRequestModel = buildCatalogRequestModel();
        CatalogResponseModel catalogResponseModel = buildCatalogResponseModel();

        when(catalogBooksService.addCatalog(catalogRequestModel)).thenReturn(catalogResponseModel);

        // act
        ResponseEntity<CatalogResponseModel> responseEntity = catalogBooksController.addCatalog(catalogRequestModel);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(catalogResponseModel, responseEntity.getBody());
        verify(catalogBooksService, times(1)).addCatalog(catalogRequestModel);
    }

    // positive test case
    @Test
    public void whenCatalogUpdated_thenReturnCatalog() {
        // arrange
        CatalogRequestModel catalogRequestModel = buildCatalogRequestModel();
        CatalogResponseModel catalogResponseModel = buildCatalogResponseModel();

        when(catalogBooksService.updateCatalog(catalogRequestModel, FOUND_CATALOG_ID))
                .thenReturn(catalogResponseModel);

        // act
        ResponseEntity<CatalogResponseModel> responseEntity = catalogBooksController
                .updateCatalog(catalogRequestModel,FOUND_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(catalogResponseModel, responseEntity.getBody());
        verify(catalogBooksService, times(1)).updateCatalog(catalogRequestModel,
                FOUND_CATALOG_ID);
    }

    // negative test case
    @Test
    public void whenInvalidCatalogIdForUpdate_thenReturnNotFound() {
        // arrange
        CatalogRequestModel catalogRequestModel = buildCatalogRequestModel();
        when(catalogBooksService.updateCatalog(catalogRequestModel, NOT_FOUND_CATALOG_ID))
                .thenThrow(new NotFoundException("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.updateCatalog(catalogRequestModel, NOT_FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID, exception.getMessage());
        verify(catalogBooksService, times(1)).updateCatalog(catalogRequestModel,
                NOT_FOUND_CATALOG_ID);
    }

    // positive test case
    @Test
    public void whenCatalogDeleted_thenNoContent() {
        // arrange
        doNothing().when(catalogBooksService).deleteCatalog(FOUND_CATALOG_ID);

        // act
        ResponseEntity<Void> responseEntity = catalogBooksController.deleteCatalog(FOUND_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(catalogBooksService, times(1)).deleteCatalog(FOUND_CATALOG_ID);
    }

    // negative test case
    @Test
    public void whenCatalogDoesNotExist_thenNotFound() {
        // arrange
        doThrow(new NotFoundException("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID))
                .when(catalogBooksService).deleteCatalog(NOT_FOUND_CATALOG_ID);

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.deleteCatalog(NOT_FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID, exception.getMessage());
        verify(catalogBooksService, times(1)).deleteCatalog(NOT_FOUND_CATALOG_ID);
    }

    //                                                      BOOKS

    // positive test case
    @Test
    public void whenBooksExists_thenReturnBookList() {
        // arrange
        BookResponseModel bookResponseModel = buildBookResponseModel();

        when(catalogBooksService.getAllBooksInCatalog(FOUND_CATALOG_ID)).thenReturn(Collections
                .singletonList(bookResponseModel));

        // act
        ResponseEntity<List<BookResponseModel>> responseEntity = catalogBooksController
                .getAllBooks(FOUND_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(bookResponseModel, responseEntity.getBody().get(0));
        verify(catalogBooksService, times(1)).getAllBooksInCatalog(FOUND_CATALOG_ID);
    }

    // negative test case
    @Test
    public void whenNoBookExists_thenReturnEmptyList() {
        // arrange
        when(catalogBooksService.getAllBooksInCatalog(FOUND_CATALOG_ID)).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<BookResponseModel>> responseEntity = catalogBooksController
                .getAllBooks(FOUND_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());
        verify(catalogBooksService, times(1)).getAllBooksInCatalog(FOUND_CATALOG_ID);
    }

    // positive test case
    @Test
    public void whenBookExists_thenReturnBook() {
        // arrange
        BookResponseModel bookResponseModel = buildBookResponseModel();

        when(catalogBooksService.getBookInCatalog(FOUND_CATALOG_ID, FOUND_BOOK_ISBN))
                .thenReturn(bookResponseModel);

        // act
        ResponseEntity<BookResponseModel> responseEntity = catalogBooksController
                .getBook(FOUND_CATALOG_ID, FOUND_BOOK_ISBN);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(catalogBooksService, times(1))
                .getBookInCatalog(FOUND_CATALOG_ID, FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenBookExistButNotCatalog_thenReturnNotFound() {
        // arrange
        when(catalogBooksService.getBookInCatalog(NOT_FOUND_CATALOG_ID, FOUND_BOOK_ISBN))
                .thenThrow(new NotFoundException("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.getBook(NOT_FOUND_CATALOG_ID, FOUND_BOOK_ISBN);
        });

        // assert
        assertEquals("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID, exception.getMessage());
        verify(catalogBooksService, times(1))
                .getBookInCatalog(NOT_FOUND_CATALOG_ID, FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenISBNInvalid_thenReturnInvalidISBN() {
        // arrange
        when(catalogBooksService.getBookInCatalog(FOUND_CATALOG_ID, INVALID_BOOK_ISBN))
                .thenThrow(new InvalidISBNException("ISBN must be 10 or 13 digits long."));

        // act
        InvalidISBNException exception = assertThrowsExactly(InvalidISBNException.class, () -> {
            catalogBooksController.getBook(FOUND_CATALOG_ID, INVALID_BOOK_ISBN);
        });

        // assert
        assertEquals("ISBN must be 10 or 13 digits long.", exception.getMessage());
        verify(catalogBooksService, times(1))
                .getBookInCatalog(FOUND_CATALOG_ID, INVALID_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenBookDoesNotExist_thenReturnNotFound() {
        // arrange
        when(catalogBooksService.getBookInCatalog(NOT_FOUND_CATALOG_ID, NOT_FOUND_BOOK_ISBN))
                .thenThrow(new NotFoundException("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.getBook(NOT_FOUND_CATALOG_ID, NOT_FOUND_BOOK_ISBN);
        });

        // assert
        assertEquals("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN, exception.getMessage());
        verify(catalogBooksService, times(1))
                .getBookInCatalog(NOT_FOUND_CATALOG_ID, NOT_FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenBookExistInDifferentCatalog_thenReturnNotFound() {
        // arrange
        when(catalogBooksService.getBookInCatalog(OTHER_CATALOG_ID, FOUND_BOOK_ISBN))
                .thenThrow(new NotFoundException("Book is not in the catalog."));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.getBook(OTHER_CATALOG_ID, FOUND_BOOK_ISBN);
        });

        // assert
        assertEquals("Book is not in the catalog.", exception.getMessage());
        verify(catalogBooksService, times(1))
                .getBookInCatalog(OTHER_CATALOG_ID, FOUND_BOOK_ISBN);
    }

    // positive test case
    @Test
    public void whenBookCreated_thenReturnBook() {
        // arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        BookResponseModel bookResponseModel = buildBookResponseModel();

        when(catalogBooksService.addBookInCatalog(OTHER_CATALOG_ID, bookRequestModel)).thenReturn(bookResponseModel);

        // act
        ResponseEntity<BookResponseModel> responseEntity = catalogBooksController
                .addBook(bookRequestModel, OTHER_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(catalogBooksService, times(1))
                .addBookInCatalog(OTHER_CATALOG_ID, bookRequestModel);
    }

    // negative test case
    @Test
    public void whenBookValidButCatalogDoesNotExistPOST_thenReturnNotFound() {
        // arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        when(catalogBooksService.addBookInCatalog(NOT_FOUND_CATALOG_ID, bookRequestModel))
                .thenThrow(new NotFoundException("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.addBook(bookRequestModel, NOT_FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID, exception.getMessage());
        verify(catalogBooksService, times(1))
                .addBookInCatalog(NOT_FOUND_CATALOG_ID, bookRequestModel);
    }

    // negative test case
    @Test
    public void whenISBNInvalidPOST_thenReturnInvalidISBN() {
        // arrange
        BookRequestModel badBookRequestModel = buildBadBookRequestModel(INVALID_BOOK_ISBN);
        when(catalogBooksService.addBookInCatalog(FOUND_CATALOG_ID, badBookRequestModel))
                .thenThrow(new InvalidISBNException("ISBN must be 10 or 13 digits long."));

        // act
        InvalidISBNException exception = assertThrowsExactly(InvalidISBNException.class, () -> {
            catalogBooksController.addBook(badBookRequestModel, FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("ISBN must be 10 or 13 digits long.", exception.getMessage());
        verify(catalogBooksService, times(1))
                .addBookInCatalog(FOUND_CATALOG_ID, badBookRequestModel);
    }

    // negative test case
    @Test
    public void whenBookAlreadyExists_thenDuplicateISBN() {
        // arrange
        BookRequestModel badBookRequestModel = buildBadBookRequestModel(FOUND_BOOK_ISBN);
        when(catalogBooksService.addBookInCatalog(FOUND_CATALOG_ID, badBookRequestModel))
                .thenThrow(new DuplicateISBNException("The catalog already contains a book with isbn: " +
                        badBookRequestModel.getIsbn()));

        // act
        DuplicateISBNException exception = assertThrowsExactly(DuplicateISBNException.class, () -> {
            catalogBooksController.addBook(badBookRequestModel, FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("The catalog already contains a book with isbn: " + badBookRequestModel.getIsbn(),
                exception.getMessage());
        verify(catalogBooksService, times(1)).addBookInCatalog(FOUND_CATALOG_ID,
                badBookRequestModel);
    }

    // positive test case
    @Test
    public void whenBookUpdated_thenReturnBook() {
        // arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        BookResponseModel bookResponseModel = buildBookResponseModel();

        when(catalogBooksService.updateBookInCatalog(FOUND_CATALOG_ID, bookRequestModel.getIsbn(), bookRequestModel))
                .thenReturn(bookResponseModel);

        // act
        ResponseEntity<BookResponseModel> responseEntity = catalogBooksController.updateBook(bookRequestModel,
                bookRequestModel.getIsbn(), FOUND_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(catalogBooksService, times(1)).updateBookInCatalog(FOUND_CATALOG_ID,
                bookRequestModel.getIsbn(), bookRequestModel);
    }

    // negative test case
    @Test
    public void whenBookExistButNotCatalogPUT_thenReturnNotFound() {
        // arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        when(catalogBooksService.updateBookInCatalog(NOT_FOUND_CATALOG_ID, bookRequestModel.getIsbn(), bookRequestModel))
                .thenThrow(new NotFoundException("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.updateBook(bookRequestModel, bookRequestModel.getIsbn(), NOT_FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID, exception.getMessage());
        verify(catalogBooksService, times(1))
                .updateBookInCatalog(NOT_FOUND_CATALOG_ID, bookRequestModel.getIsbn(), bookRequestModel);
    }

    // negative test case
    @Test
    public void whenISBNInvalidPUT_thenReturnInvalidISBN() {
        // arrange
        BookRequestModel badBookRequestModel = buildBadBookRequestModel(INVALID_BOOK_ISBN);
        when(catalogBooksService.updateBookInCatalog(FOUND_CATALOG_ID, badBookRequestModel.getIsbn(), badBookRequestModel))
                .thenThrow(new InvalidISBNException("ISBN must be 10 or 13 digits long."));

        // act
        InvalidISBNException exception = assertThrowsExactly(InvalidISBNException.class, () -> {
            catalogBooksController.updateBook(badBookRequestModel, badBookRequestModel.getIsbn(), FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("ISBN must be 10 or 13 digits long.", exception.getMessage());
        verify(catalogBooksService, times(1))
                .updateBookInCatalog(FOUND_CATALOG_ID, badBookRequestModel.getIsbn(), badBookRequestModel);
    }

    // negative test case
    @Test
    public void whenBookDoesNotExistPUT_thenReturnNotFound() {
        // arrange
        BookRequestModel badBookRequestModel = buildBadBookRequestModel(NOT_FOUND_BOOK_ISBN);
        when(catalogBooksService.updateBookInCatalog(FOUND_CATALOG_ID, badBookRequestModel.getIsbn(), badBookRequestModel))
                .thenThrow(new NotFoundException("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.updateBook(badBookRequestModel, badBookRequestModel.getIsbn(), FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN, exception.getMessage());
        verify(catalogBooksService, times(1))
                .updateBookInCatalog(FOUND_CATALOG_ID, badBookRequestModel.getIsbn(), badBookRequestModel);
    }

    // negative test case
    @Test
    public void whenBookExistInDifferentCatalogPUT_thenReturnNotFound() {
        // arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        when(catalogBooksService.updateBookInCatalog(NOT_FOUND_CATALOG_ID, bookRequestModel.getIsbn(), bookRequestModel))
                .thenThrow(new NotFoundException("Book is not in the catalog."));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.updateBook(bookRequestModel, bookRequestModel.getIsbn(), NOT_FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Book is not in the catalog.", exception.getMessage());
        verify(catalogBooksService, times(1))
                .updateBookInCatalog(NOT_FOUND_CATALOG_ID, bookRequestModel.getIsbn(), bookRequestModel);
    }

    // positive test case
    @Test
    public void whenBookDeleted_thenNoContent() {
        // arrange
        doNothing().when(catalogBooksService).deleteBookInCatalog(FOUND_CATALOG_ID, FOUND_BOOK_ISBN);

        // act
        ResponseEntity<Void> responseEntity = catalogBooksController.deleteBook(FOUND_BOOK_ISBN, FOUND_CATALOG_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(catalogBooksService, times(1)).deleteBookInCatalog(FOUND_CATALOG_ID, FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenBookExistButNotCatalogDELETE_thenReturnNotFound() {
        // arrange
        doThrow(new NotFoundException("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID))
                .when(catalogBooksService).deleteBookInCatalog(NOT_FOUND_CATALOG_ID, FOUND_BOOK_ISBN);

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.deleteBook(FOUND_BOOK_ISBN, NOT_FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown catalogId provided: " + NOT_FOUND_CATALOG_ID, exception.getMessage());
        verify(catalogBooksService, times(1))
                .deleteBookInCatalog(NOT_FOUND_CATALOG_ID, FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenISBNInvalidDELETE_thenReturnInvalidISBN() {
        // arrange
        doThrow(new InvalidISBNException("ISBN must be 10 or 13 digits long."))
                .when(catalogBooksService).deleteBookInCatalog(FOUND_CATALOG_ID, INVALID_BOOK_ISBN);

        // act
        InvalidISBNException exception = assertThrowsExactly(InvalidISBNException.class, () -> {
            catalogBooksController.deleteBook(INVALID_BOOK_ISBN, FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("ISBN must be 10 or 13 digits long.", exception.getMessage());
        verify(catalogBooksService, times(1))
                .deleteBookInCatalog(FOUND_CATALOG_ID, INVALID_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenBookDoesNotExistDELETE_thenReturnNotFound() {
        // arrange
        doThrow(new NotFoundException("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN))
                .when(catalogBooksService).deleteBookInCatalog(FOUND_CATALOG_ID, NOT_FOUND_BOOK_ISBN);

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.deleteBook(NOT_FOUND_BOOK_ISBN, FOUND_CATALOG_ID);
        });

        // assert
        assertEquals("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN, exception.getMessage());
        verify(catalogBooksService, times(1))
                .deleteBookInCatalog(FOUND_CATALOG_ID, NOT_FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenBookExistInDifferentCatalogDELETE_thenReturnNotFound() {
        // arrange
        doThrow(new NotFoundException("Book is not in the catalog."))
                .when(catalogBooksService).deleteBookInCatalog(OTHER_CATALOG_ID, FOUND_BOOK_ISBN);

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            catalogBooksController.deleteBook(FOUND_BOOK_ISBN, OTHER_CATALOG_ID);
        });

        // assert
        assertEquals("Book is not in the catalog.", exception.getMessage());
        verify(catalogBooksService, times(1))
                .deleteBookInCatalog(OTHER_CATALOG_ID, FOUND_BOOK_ISBN);
    }

}