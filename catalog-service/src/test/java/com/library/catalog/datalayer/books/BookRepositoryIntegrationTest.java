package com.library.catalog.datalayer.books;

import com.library.catalog.datalayer.catalog.CatalogIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.library.catalog.datalayer.books.Status.AVAILABLE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
    }

    // positive test case
    @Test
    public void whenBookExists_ReturnBookByISBN() {
        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"),
                "The Da Vinci Code", "", "1", "Doubleday", "x",
                "english", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);

        // act
        Book savedBook = bookRepository.findByIsbn_Isbn(book1.getIsbn().getIsbn());

        // assert
        assertNotNull(savedBook);
        assertEquals(savedBook.getIsbn(), book1.getIsbn());
        assertEquals(savedBook.getCatalogIdentifier(), book1.getCatalogIdentifier());
        assertEquals(savedBook.getTitle(), book1.getTitle());
        assertEquals(savedBook.getCollection(), book1.getCollection());
        assertEquals(savedBook.getEdition(), book1.getEdition());
        assertEquals(savedBook.getPublisher(), book1.getPublisher());
        assertEquals(savedBook.getSynopsis(), book1.getSynopsis());
        assertEquals(savedBook.getLanguage(), book1.getLanguage());
        assertEquals(savedBook.getStatus(), book1.getStatus());
        assertEquals(savedBook.getAuthor(), book1.getAuthor());
    }

    // negative test case
    @Test
    public void whenBookDoesNotExist_ReturnNull1() {
        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"),
                "The Da Vinci Code", "", "1", "Doubleday", "x",
                "english", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);

        // act
        Book nonExistentBook = bookRepository.findByIsbn_Isbn(978316148410L);

        // assert
        assertNull(nonExistentBook);
    }

    // positive test case
    @Test
    public void whenBookExists_ReturnBookByCatalogIdAndISBN() {
        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"),
                "The Da Vinci Code", "", "1", "Doubleday", "x",
                "english", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);

        // act
        Book savedBook = bookRepository.findByCatalogIdentifier_CatalogIdAndIsbn_Isbn(
                book1.getCatalogIdentifier().getCatalogId(), book1.getIsbn().getIsbn());

        // assert
        assertNotNull(savedBook);
        assertEquals(savedBook.getIsbn(), book1.getIsbn());
        assertEquals(savedBook.getCatalogIdentifier(), book1.getCatalogIdentifier());
        assertEquals(savedBook.getTitle(), book1.getTitle());
        assertEquals(savedBook.getCollection(), book1.getCollection());
        assertEquals(savedBook.getEdition(), book1.getEdition());
        assertEquals(savedBook.getPublisher(), book1.getPublisher());
        assertEquals(savedBook.getSynopsis(), book1.getSynopsis());
        assertEquals(savedBook.getLanguage(), book1.getLanguage());
        assertEquals(savedBook.getStatus(), book1.getStatus());
        assertEquals(savedBook.getAuthor(), book1.getAuthor());
    }

    // negative test case
    @Test
    public void whenBookDoesNotExist_ReturnNull2() {
        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"),
                "The Da Vinci Code", "", "1", "Doubleday", "x",
                "english", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);

        // act
        Book nonExistentBook = bookRepository.findByCatalogIdentifier_CatalogIdAndIsbn_Isbn(
                "d846a5a7-2e1c-4c79-809c-4f3f471e826d", 978316148410L);

        // assert
        assertNull(nonExistentBook);
    }
    @Test
    public void whenBookDoesNotExist_ReturnNull3() {
        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"),
                "The Da Vinci Code", "", "1", "Doubleday", "x",
                "english", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);

        // act
        Book nonExistentBook = bookRepository.findByCatalogIdentifier_CatalogIdAndIsbn_Isbn(
                "d846a5a7-2e1c-4c79-809c-4f3f471e826c", 9783161484100L);

        // assert
        assertNull(nonExistentBook);
    }

    // positive path
    @Test
    public void whenBooksExist_thenReturnAllBooks() {

        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"),
                "The Da Vinci Code", "", "1", "Doubleday", "x",
                "English", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);
        Book book2 = new Book(new ISBN(1234567890123L),new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"), "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", AVAILABLE, new Author("Val", "Chase"));
        bookRepository.save(book2);

        // act
        List<Book> savedBooks = bookRepository.findAllByCatalogIdentifier_CatalogId(
                book1.getCatalogIdentifier().getCatalogId());

        // assert
        assertNotNull(savedBooks);
        for (Book book : savedBooks) {
            assertNotNull(book);
            assertEquals(book.getCatalogIdentifier().getCatalogId(), book1.getCatalogIdentifier().getCatalogId());
            assertEquals(book.getStatus(), book1.getStatus());
            assertEquals(book.getLanguage(), book1.getLanguage());
        }
    }

    // negative path
    @Test
    public void whenCatalogDoesNotExist_thenReturnEmptyList() {

        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"),
                "The Da Vinci Code", "", "1", "Doubleday", "x",
                "English", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);
        Book book2 = new Book(new ISBN(1234567890123L),new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"), "New Book",
                "New Collection", "1st edition", "Neji Publications",
                "Val Chase's magnum opus", "English", AVAILABLE, new Author("Val", "Chase"));
        bookRepository.save(book2);

        // act
        List<Book> nonExistentBooks = bookRepository.findAllByCatalogIdentifier_CatalogId("12");

        // assert
        assertNotNull(nonExistentBooks);
        for (Book book : nonExistentBooks) {
            assertNull(book);
        }
    }

}