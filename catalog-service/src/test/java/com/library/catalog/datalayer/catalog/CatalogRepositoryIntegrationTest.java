package com.library.catalog.datalayer.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CatalogRepositoryIntegrationTest {

    @Autowired
    private CatalogRepository catalogRepository;

    @BeforeEach
    public void setUp() {
        catalogRepository.deleteAll();
    }

    // positive test case
    @Test
    public void whenBookExists_ReturnBookByISBN() {
        // arrange
        Catalog catalog1 = new Catalog(new CatalogIdentifier("12"), "kids", 0);
        catalogRepository.save(catalog1);

        // act
        Catalog savedCatalog = catalogRepository.findByCatalogIdentifier_CatalogId(
                catalog1.getCatalogIdentifier().getCatalogId());

        // assert
        assertNotNull(savedCatalog);
        assertEquals(savedCatalog.getCatalogIdentifier(), catalog1.getCatalogIdentifier());
        assertEquals(savedCatalog.getType(), catalog1.getType());
        assertEquals(savedCatalog.getSize(), catalog1.getSize());
    }

    // negative test case
    @Test
    public void whenBookDoesNotExist_ReturnNull1() {
        // arrange
        Catalog catalog1 = new Catalog(new CatalogIdentifier("12"), "kids", 0);
        catalogRepository.save(catalog1);

        // act
        Catalog nonExistentCatalog = catalogRepository.findByCatalogIdentifier_CatalogId("x");

        // assert
        assertNull(nonExistentCatalog);
    }

}