package com.library.catalog.presentationlayer.books;

import com.library.catalog.businesslayer.books.BookService;
import com.library.catalog.datalayer.books.Author;
import com.library.catalog.datalayer.books.Status;
import com.library.catalog.utils.exceptions.InvalidISBNException;
import com.library.catalog.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.library.catalog.datalayer.books.Status.DAMAGED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BookController.class)
class BookControllerUnitTest {

    private final String FOUND_CATALOG_ID = "d846a5a7-2e1c-4c79-809c-4f3f471e826d";
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
    private BookController bookController;

    @MockBean
    private BookService bookService;

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

    // positive test case
    @Test
    public void whenBookExists_thenReturnBook() {
        // arrange
        BookResponseModel bookResponseModel = buildBookResponseModel();

        when(bookService.getBook(FOUND_BOOK_ISBN)).thenReturn(bookResponseModel);

        // act
        ResponseEntity<BookResponseModel> responseEntity = bookController.getBook(FOUND_BOOK_ISBN);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(bookService, times(1)).getBook(FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenISBNInvalid_thenReturnInvalidISBN() {
        // arrange
        when(bookService.getBook(INVALID_BOOK_ISBN))
                .thenThrow(new InvalidISBNException("ISBN must be 10 or 13 digits long."));

        // act
        InvalidISBNException exception = assertThrowsExactly(InvalidISBNException.class, () -> {
            bookController.getBook(INVALID_BOOK_ISBN);
        });

        // assert
        assertEquals("ISBN must be 10 or 13 digits long.", exception.getMessage());
        verify(bookService, times(1)).getBook(INVALID_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenBookDoesNotExist_thenReturnNotFound() {
        // arrange
        when(bookService.getBook(NOT_FOUND_BOOK_ISBN))
                .thenThrow(new NotFoundException("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            bookController.getBook(NOT_FOUND_BOOK_ISBN);
        });

        // assert
        assertEquals("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN, exception.getMessage());
        verify(bookService, times(1)).getBook(NOT_FOUND_BOOK_ISBN);
    }

    // positive test case
    @Test
    public void whenBookPatched_thenReturnBook() {
        // arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        BookResponseModel bookResponseModel = buildBookResponseModel();

        when(bookService.patchBook(bookRequestModel.getIsbn(), bookRequestModel)).thenReturn(bookResponseModel);

        // act
        ResponseEntity<BookResponseModel> responseEntity = bookController.patchBook(bookRequestModel,
                bookRequestModel.getIsbn());

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(bookService, times(1)).patchBook(bookRequestModel.getIsbn(), bookRequestModel);
    }

    // negative test case
    @Test
    public void whenISBNInvalidPATCH_thenReturnInvalidISBN() {
        // arrange
        BookRequestModel badBookRequestModel = buildBadBookRequestModel(INVALID_BOOK_ISBN);
        when(bookService.patchBook(badBookRequestModel.getIsbn(), badBookRequestModel))
                .thenThrow(new InvalidISBNException("ISBN must be 10 or 13 digits long."));

        // act
        InvalidISBNException exception = assertThrowsExactly(InvalidISBNException.class, () -> {
            bookController.patchBook(badBookRequestModel, badBookRequestModel.getIsbn());
        });

        // assert
        assertEquals("ISBN must be 10 or 13 digits long.", exception.getMessage());
        verify(bookService, times(1))
                .patchBook(badBookRequestModel.getIsbn(), badBookRequestModel);
    }

    // negative test case
    @Test
    public void whenBookDoesNotExistPATCH_thenReturnNotFound() {
        // arrange
        BookRequestModel badBookRequestModel = buildBadBookRequestModel(NOT_FOUND_BOOK_ISBN);
        when(bookService.patchBook(badBookRequestModel.getIsbn(), badBookRequestModel))
                .thenThrow(new NotFoundException("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            bookController.patchBook(badBookRequestModel, badBookRequestModel.getIsbn());
        });

        // assert
        assertEquals("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN, exception.getMessage());
        verify(bookService, times(1)).patchBook(badBookRequestModel.getIsbn(),
                badBookRequestModel);
    }

}