package com.library.catalog.datalayer.books;

import com.library.catalog.datalayer.catalog.CatalogIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    public void whenFineExists_ReturnFineByFineId() {
        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"), "The Da Vinci Code", "", "1", "Doubleday", "x", "english", AVAILABLE, new Author("Dan", "Brown"));
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
    public void whenFineDoesNotExist_ReturnNull() {
        // arrange
        Book book1 = new Book(new ISBN(9783161484100L), new CatalogIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d"), "The Da Vinci Code", "", "1", "Doubleday", "x", "english", AVAILABLE, new Author("Dan", "Brown"));
        bookRepository.save(book1);

        // act
        Book nonExistentBook = bookRepository.findByIsbn_Isbn(978316148410L);

        // assert
        assertNull(nonExistentBook);
    }

}