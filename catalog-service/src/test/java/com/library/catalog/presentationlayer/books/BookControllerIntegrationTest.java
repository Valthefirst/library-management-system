package com.library.catalog.presentationlayer.books;

import com.library.catalog.datalayer.books.BookRepository;
import com.library.catalog.datalayer.books.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.library.catalog.datalayer.books.Status.DAMAGED;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class BookControllerIntegrationTest {

    private final String BASE_URI_BOOK = "api/v1/books/";
    private final String FOUND_CATALOG_ID = "d846a5a7-2e1c-4c79-809c-4f3f471e826d";
    private final Long FOUND_BOOK_ISBN13 = 9789390183524L;
    private final Long FOUND_BOOK_ISBN10 = 1234567890L;
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
    private final Status FOUND_BOOK_STATUS = DAMAGED;
    private final String FOUND_BOOK_AUTHOR_FNAME = "F. Scott";
    private final String FOUND_BOOK_AUTHOR_LNAME = "Fitzgerald";
    private final Long NOT_FOUND_BOOK_ISBN = 9789390183520L;
    private final Long INVALID_BOOK_ISBN = 978939018352L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebTestClient webTestClient;

    // positive test case
    @Test
    public void whenGetBookExists_thenReturnBookByISBN13() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_BOOK + FOUND_BOOK_ISBN13)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value((book) -> {
                    assertNotNull(book);
                    assertEquals(book.getIsbn(), FOUND_BOOK_ISBN13);
                    assertEquals(book.getCatalogId(), FOUND_CATALOG_ID);
                    assertEquals(book.getTitle(), FOUND_BOOK_TITLE);
                    assertEquals(book.getCollection(), FOUND_BOOK_COLLECTION);
                    assertEquals(book.getEdition(), FOUND_BOOK_EDITION);
                    assertEquals(book.getPublisher(), FOUND_BOOK_PUBLISHER);
                    assertEquals(book.getSynopsis(), FOUND_BOOK_SYNOPSIS);
                    assertEquals(book.getLanguage(), FOUND_BOOK_LANGUAGE);
                    assertEquals(book.getStatus(), FOUND_BOOK_STATUS);
                    assertEquals(book.getAuthor().getFirstName(), FOUND_BOOK_AUTHOR_FNAME);
                    assertEquals(book.getAuthor().getLastName(), FOUND_BOOK_AUTHOR_LNAME);
                });
    }

    // positive test case
    @Test
    public void whenGetBookExists_thenReturnBookByISBN10() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_BOOK + FOUND_BOOK_ISBN10)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value((book) -> {
                    assertNotNull(book);
                    assertEquals(book.getIsbn(), FOUND_BOOK_ISBN10);
                    assertEquals(book.getCatalogId(), FOUND_CATALOG_ID);
                    assertEquals(book.getTitle(), FOUND_BOOK_TITLE);
                    assertEquals(book.getCollection(), FOUND_BOOK_COLLECTION);
                    assertEquals(book.getEdition(), FOUND_BOOK_EDITION);
                    assertEquals(book.getPublisher(), FOUND_BOOK_PUBLISHER);
                    assertEquals(book.getSynopsis(), FOUND_BOOK_SYNOPSIS);
                    assertEquals(book.getLanguage(), FOUND_BOOK_LANGUAGE);
                    assertEquals(book.getStatus(), FOUND_BOOK_STATUS);
                    assertEquals(book.getAuthor().getFirstName(), FOUND_BOOK_AUTHOR_FNAME);
                    assertEquals(book.getAuthor().getLastName(), FOUND_BOOK_AUTHOR_LNAME);
                });
    }

    // negative test case
    @Test
    public void whenGetBookDoesNotExist_thenReturnNotFound() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_BOOK + NOT_FOUND_BOOK_ISBN)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown ISBN provided: "
                        + NOT_FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenGetBookWithInvalidISBN_thenThrowException() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_BOOK + INVALID_BOOK_ISBN)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("ISBN must be 10 or 13 digits long.");
    }

    // positive test case
    @Test
    public void whenValidBook_thenPatchBook13() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(null, null,
                null, null, null,
                null, null, "BORROWED", null);

        // act & assert
        webTestClient.patch()
                .uri(BASE_URI_BOOK + FOUND_BOOK_ISBN13)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value((bookResponseModel) -> {
                    assertNotNull(bookResponseModel);
                    assertEquals(bookRequestModel.getStatus(), bookResponseModel.getStatus().toString());
                });
    }

    // positive test case
    @Test
    public void whenValidBook_thenPatchBook10() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(null, null,
                null, null, null,
                null, null, "BORROWED", null);

        // act & assert
        webTestClient.patch()
                .uri(BASE_URI_BOOK + FOUND_BOOK_ISBN10)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value((bookResponseModel) -> {
                    assertNotNull(bookResponseModel);
                    assertEquals(bookRequestModel.getStatus(), bookResponseModel.getStatus().toString());
                });
    }

    // negative test case
    @Test
    public void whenUnknownISBNForPatch_thenThrowException() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(null, null,
                null, null, null,
                null, null, "BORROWED", null);

        // act & assert
        webTestClient.patch()
                .uri(BASE_URI_BOOK + NOT_FOUND_BOOK_ISBN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN);
    }

    // negative test case
    @Test
    public void whenInvalidISBNForPatch_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();
        BookRequestModel bookRequestModel = new BookRequestModel(null, null,
                null, null, null,
                null, null, "BORROWED", null);

        webTestClient.patch()
                .uri(BASE_URI_BOOK + INVALID_BOOK_ISBN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("ISBN must be 10 or 13 digits long.");

        // assert
        assertEquals(sizeDB, bookRepository.count());
    }

}