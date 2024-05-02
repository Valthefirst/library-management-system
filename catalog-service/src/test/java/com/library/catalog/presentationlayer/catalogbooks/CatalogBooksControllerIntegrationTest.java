package com.library.catalog.presentationlayer.catalogbooks;

import com.library.catalog.datalayer.books.Author;
import com.library.catalog.datalayer.books.Book;
import com.library.catalog.datalayer.books.BookRepository;
import com.library.catalog.datalayer.books.Status;
import com.library.catalog.datalayer.catalog.Catalog;
import com.library.catalog.datalayer.catalog.CatalogRepository;
import com.library.catalog.presentationlayer.books.BookRequestModel;
import com.library.catalog.presentationlayer.books.BookResponseModel;
import com.library.catalog.presentationlayer.catalog.CatalogRequestModel;
import com.library.catalog.presentationlayer.catalog.CatalogResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static com.library.catalog.datalayer.books.Status.DAMAGED;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class CatalogBooksControllerIntegrationTest {

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
    private final Status FOUND_BOOK_STATUS = DAMAGED;
    private final String FOUND_BOOK_AUTHOR_FNAME = "F. Scott";
    private final String FOUND_BOOK_AUTHOR_LNAME = "Fitzgerald";
    private final Long NOT_FOUND_BOOK_ISBN = 9789390183520L;
    private final Long INVALID_BOOK_ISBN = 978939018352L;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebTestClient webTestClient;

    // CATALOGS

    // positive path
    @Test
    public void whenGetCatalogs_thenReturnAllCatalogs() {

        // arrange
        long sizeDB = catalogRepository.count();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CatalogResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                });
    }

    // negative path
    // empty database, no catalogs, empty list returned
    @Test
    public void whenGetCatalogsEmptyDB_thenReturnEmptyList() {

        // arrange
        catalogRepository.deleteAll();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CatalogResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.isEmpty());
                });
    }

    // positive test case
    @Test
    public void whenGetCatalogExists_thenReturnCatalogByCatalogId() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CatalogResponseModel.class)
                .value((catalog) -> {
                    assertNotNull(catalog);
                    assertEquals(catalog.getCatalogId(), FOUND_CATALOG_ID);
                    assertEquals(catalog.getType(), FOUND_CATALOG_TYPE);
                    assertEquals(catalog.getSize(), FOUND_CATALOG_SIZE);
                });
    }
    // negative test case
    @Test
    public void whenGetCatalogDoesNotExist_thenReturnNotFound() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);
    }

    // positive test case
    @Test
    public void whenValidCatalog_thenCreateCatalog() {

        // arrange
        long sizeDB = catalogRepository.count();
        CatalogRequestModel catalogRequestModel = new CatalogRequestModel("Kidz", 0);

        webTestClient.post()
                .uri(BASE_URI_CATALOG)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(catalogRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CatalogResponseModel.class)
                .value((catalogResponseModel) -> {
                    assertNotNull(catalogResponseModel);
                    assertEquals(catalogRequestModel.getType(), catalogResponseModel.getType());
                    assertEquals(catalogRequestModel.getSize(), catalogResponseModel.getSize());
                });

        // assert
        assertEquals(sizeDB + 1, catalogRepository.count());
    }

    // positive test case
    @Test
    public void whenValidCatalog_thenUpdateCatalog() {

        // arrange
        CatalogRequestModel catalogRequestModel = new CatalogRequestModel("Kidz", 0);

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(catalogRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CatalogResponseModel.class)
                .value((catalogResponseModel) -> {
                    assertNotNull(catalogResponseModel);
                    assertEquals(catalogRequestModel.getType(), catalogResponseModel.getType());
                    assertEquals(catalogRequestModel.getSize(), catalogResponseModel.getSize());
                });
    }

    // negative test case
    @Test
    public void whenInvalidCatalogIdForUpdate_thenThrowException() {

        // arrange
        CatalogRequestModel catalogRequestModel = new CatalogRequestModel("Kidz", 0);

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(catalogRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);
    }

    // positive test case
    @Test
    public void whenValidCatalog_thenDeleteCatalog() {

        // arrange
        long sizeDB = catalogRepository.count();
        List<Book> bookList = bookRepository.findAllByCatalogIdentifier_CatalogId(FOUND_CATALOG_ID);
        List<Integer> ids = new ArrayList<>();
        bookList.forEach(book -> {
            ids.add(book.getId());
        });
        bookRepository.deleteAllByIdInBatch(ids);
        Catalog cat = catalogRepository.findByCatalogIdentifier_CatalogId(FOUND_CATALOG_ID);
        cat.setSize(0);
        catalogRepository.save(cat);

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertEquals(sizeDB - 1, catalogRepository.count());
    }

    // negative test case
    @Test
    public void whenInvalidCatalogIdForDelete_thenThrowException() {

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);
    }

    // negative test case
    @Test
    public void whenCatalogWithBooksForDelete_thenThrowException() {

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("Catalog has books and cannot be deleted");
    }

    //                                                          BOOKS

    // positive path
    @Test
    public void whenGetBooks_thenReturnAllBooks() {

        // arrange
        List<Book> bookList = bookRepository.findAllByCatalogIdentifier_CatalogId(FOUND_CATALOG_ID);
        long sizeCatalog = bookList.size();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeCatalog);
                });
    }

    // negative path
    // empty database, no books, empty list returned
    @Test
    public void whenGetBooksEmptyDB_thenReturnEmptyList() {

        // arrange
        bookRepository.deleteAll();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.isEmpty());
                });
    }

    // negative path
    @Test
    public void whenGetBooksForUnknownCatalog_thenThrowException() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID + "/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);
    }

    // positive test case
    @Test
    public void whenGetBookExists_thenReturnBookByISBN() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value((book) -> {
                    assertNotNull(book);
                    assertEquals(book.getIsbn(), FOUND_BOOK_ISBN);
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
    public void whenGetBookForUnknownCatalog_thenThrowException() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);
    }

    // negative test case
    @Test
    public void whenGetBookDoesNotExist_thenReturnNotFound() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + NOT_FOUND_BOOK_ISBN)
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
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + INVALID_BOOK_ISBN)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("ISBN must be 10 or 13 digits long.");
    }

    // positive test case
    @Test
    public void whenValidBook_thenCreateBook() {

        // arrange
        long sizeDB = bookRepository.count();
        BookRequestModel bookRequestModel = new BookRequestModel(1234567890123L, "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

        webTestClient.post()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value((bookResponseModel) -> {
                    assertNotNull(bookResponseModel);
                    assertEquals(bookRequestModel.getTitle(), bookResponseModel.getTitle());
                    assertEquals(bookRequestModel.getCollection(), bookResponseModel.getCollection());
                    assertEquals(bookRequestModel.getEdition(), bookResponseModel.getEdition());
                    assertEquals(bookRequestModel.getPublisher(), bookResponseModel.getPublisher());
                    assertEquals(bookRequestModel.getSynopsis(), bookResponseModel.getSynopsis());
                    assertEquals(bookRequestModel.getLanguage(), bookResponseModel.getLanguage());
                    assertEquals(bookRequestModel.getStatus(), bookResponseModel.getStatus().toString());
                    assertEquals(bookRequestModel.getAuthor().getFirstName(), bookResponseModel.getAuthor().getFirstName());
                    assertEquals(bookRequestModel.getAuthor().getLastName(), bookResponseModel.getAuthor().getLastName());
                });

        // assert
        assertEquals(sizeDB + 1, bookRepository.count());
    }

    // negative test case
    @Test
    public void whenValidBookForUnknownCatalogPOST_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();
        BookRequestModel bookRequestModel = new BookRequestModel(1234567890123L, "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

        webTestClient.post()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID + "/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);

        // assert
        assertEquals(sizeDB, bookRepository.count());
    }

    // negative test case
    @Test
    public void whenInvalidISBNForPost_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();
        BookRequestModel bookRequestModel = new BookRequestModel(INVALID_BOOK_ISBN, "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

        webTestClient.post()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books")
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

    // negative test case
    @Test
    public void whenAlreadyExistingISBNForPost_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();
        BookRequestModel bookRequestModel = new BookRequestModel(FOUND_BOOK_ISBN, "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

//        webTestClient.post()
//                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .bodyValue(bookRequestModel)
//                .exchange()
//                .expectStatus().isCreated();

        webTestClient.post()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("The catalog already contains a book with isbn: "
                        + FOUND_BOOK_ISBN);

        // assert
        assertEquals(sizeDB, bookRepository.count());
    }

    // positive test case
    @Test
    public void whenValidBook_thenUpdateBook() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(1234567890123L, "New Book",
                "The magnum opus trilogy", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value((bookResponseModel) -> {
                    assertNotNull(bookResponseModel);
                    assertEquals(bookRequestModel.getTitle(), bookResponseModel.getTitle());
                    assertEquals(bookRequestModel.getCollection(), bookResponseModel.getCollection());
                    assertEquals(bookRequestModel.getEdition(), bookResponseModel.getEdition());
                    assertEquals(bookRequestModel.getPublisher(), bookResponseModel.getPublisher());
                    assertEquals(bookRequestModel.getSynopsis(), bookResponseModel.getSynopsis());
                    assertEquals(bookRequestModel.getLanguage(), bookResponseModel.getLanguage());
                    assertEquals(bookRequestModel.getStatus(), bookResponseModel.getStatus().toString());
                    assertEquals(bookRequestModel.getAuthor().getFirstName(), bookResponseModel.getAuthor().getFirstName());
                    assertEquals(bookRequestModel.getAuthor().getLastName(), bookResponseModel.getAuthor().getLastName());
                });
    }

    // negative test case
    @Test
    public void whenValidBookForUnknownCatalogPUT_thenThrowException() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(1234567890123L, "New Book",
                "The magnum opus trilogy", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);
    }

    // negative test case
    @Test
    public void whenUnknownISBNForUpdate_thenThrowException() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(1234567890123L, "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + NOT_FOUND_BOOK_ISBN)
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
    public void whenInvalidISBNForUpdate_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();
        BookRequestModel bookRequestModel = new BookRequestModel(INVALID_BOOK_ISBN, "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", "AVAILABLE", new Author("Val", "Chase"));

        webTestClient.put()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + INVALID_BOOK_ISBN)
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

    // positive test case
    @Test
    public void whenValidBook_thenPatchBook() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(null, null,
                null, null, null,
                null, null, "BORROWED", null);

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
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
    public void whenValidBookForUnknownCatalogPATCH_thenThrowException() {

        // arrange
        BookRequestModel bookRequestModel = new BookRequestModel(null, null,
                null, null, null,
                null, null, "BORROWED", null);

        // act & assert
        webTestClient.patch()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);
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
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + NOT_FOUND_BOOK_ISBN)
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
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + INVALID_BOOK_ISBN)
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

    // positive test case
    @Test
    public void whenValidBook_thenDeleteBook() {

        // arrange
        long sizeDB = bookRepository.count();

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertEquals(sizeDB - 1, bookRepository.count());
    }

    // negative test case
    @Test
    public void whenValidBookForUnknownCatalogDELETE_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_CATALOG + "/" + NOT_FOUND_CATALOG_ID + "/books/" + FOUND_BOOK_ISBN)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown catalogId provided: "
                        + NOT_FOUND_CATALOG_ID);

        // assert
        assertEquals(sizeDB, bookRepository.count());
    }

    // negative test case
    @Test
    public void whenUnknownISBNForDelete_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();

        // act
        webTestClient.delete()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + NOT_FOUND_BOOK_ISBN)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown ISBN provided: " + NOT_FOUND_BOOK_ISBN);

        // assert
        assertEquals(sizeDB, bookRepository.count());
    }

    // negative test case
    @Test
    public void whenInvalidISBNForDelete_thenThrowException() {

        // arrange
        long sizeDB = bookRepository.count();

        // act
        webTestClient.delete()
                .uri(BASE_URI_CATALOG + "/" + FOUND_CATALOG_ID + "/books/" + INVALID_BOOK_ISBN)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("ISBN must be 10 or 13 digits long.");

        // assert
        assertEquals(sizeDB, bookRepository.count());
    }

}