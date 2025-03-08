package com.library.apigateway.businesslayer.catalogs;

import com.library.apigateway.domainclientlayer.catalogs.Author;
import com.library.apigateway.domainclientlayer.catalogs.CatalogServiceClient;
import com.library.apigateway.domainclientlayer.catalogs.Status;
import com.library.apigateway.mapperlayer.catalogs.book.BookResponseMapper;
import com.library.apigateway.mapperlayer.catalogs.catalog.CatalogResponseMapper;
import com.library.apigateway.presentationlayer.catalogs.books.BookRequestModel;
import com.library.apigateway.presentationlayer.catalogs.books.BookResponseModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogRequestModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude = org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
@ActiveProfiles("test")
class CatalogBooksServiceUnitTest {

    @Autowired
    CatalogBooksService catalogBooksService;

    @MockBean
    CatalogServiceClient catalogServiceClient;

    @MockBean
    CatalogResponseMapper catalogResponseMapper;

    @MockBean
    BookResponseMapper bookResponseMapper;

    // positive test
    @Test
    public void whenCatalogsExists_thenReturnCatalogList() {
        // arrange
        List<CatalogResponseModel> catalogResponseModelList = List.of(
                CatalogResponseModel.builder()
                        .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                        .type("Fiction")
                        .size(20)
                        .build(),
                CatalogResponseModel.builder()
                        .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5082")
                        .type("Non-Fiction")
                        .size(50)
                        .build()
        );

        when(catalogServiceClient.getAllCatalogs()).thenReturn(catalogResponseModelList);
        when(catalogResponseMapper.responseModelListToResponseModelList(catalogResponseModelList))
                .thenReturn(catalogResponseModelList);

        // act
        List<CatalogResponseModel> result = catalogBooksService.getAllCatalogs();

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(catalogResponseModelList, result);
        verify(catalogServiceClient, times(1)).getAllCatalogs();
        verify(catalogResponseMapper, times(1)).responseModelListToResponseModelList(catalogResponseModelList);
    }

    // negative test
    @Test
    public void whenCatalogsNotExists_thenReturnEmptyList() {
        // arrange
        List<CatalogResponseModel> catalogResponseModelList = List.of();

        when(catalogServiceClient.getAllCatalogs()).thenReturn(catalogResponseModelList);
        when(catalogResponseMapper.responseModelListToResponseModelList(catalogResponseModelList))
                .thenReturn(catalogResponseModelList);

        // act
        List<CatalogResponseModel> result = catalogBooksService.getAllCatalogs();

        // assert
        assertNotNull(result);
        assertEquals(catalogResponseModelList, result);
        verify(catalogServiceClient, times(1)).getAllCatalogs();
        verify(catalogResponseMapper, times(1)).responseModelListToResponseModelList(catalogResponseModelList);
    }

    // positive test
    @Test
    public void whenCatalogExists_thenReturnCatalog() {
        // arrange
        CatalogResponseModel catalogResponseModel = CatalogResponseModel.builder()
                .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .type("Fiction")
                .size(20)
                .build();

        when(catalogServiceClient.getCatalogByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081"))
                .thenReturn(catalogResponseModel);
        when(catalogResponseMapper.responseModelToResponseModel(catalogResponseModel))
                .thenReturn(catalogResponseModel);

        // act
        CatalogResponseModel result = catalogBooksService.getCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        assertEquals(catalogResponseModel, result);
        verify(catalogServiceClient, times(1)).getCatalogByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081");
        verify(catalogResponseMapper, times(1)).responseModelToResponseModel(catalogResponseModel);
    }

    // negative test
    @Test
    public void whenCatalogNotExists_thenReturnNull() {
        // arrange
        when(catalogServiceClient.getCatalogByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")).thenReturn(null);
        when(catalogResponseMapper.responseModelToResponseModel(null)).thenReturn(null);

        // act
        CatalogResponseModel result = catalogBooksService.getCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        assertNull(result);
        verify(catalogServiceClient, times(1)).getCatalogByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081");
        verify(catalogResponseMapper, times(1)).responseModelToResponseModel(null);
    }

    // positive test
    @Test
    public void whenCatalogAdded_thenReturnCatalog() {
        // arrange
        CatalogRequestModel catalogRequestModel = CatalogRequestModel.builder()
                .type("Fiction")
                .size(20)
                .build();

        CatalogResponseModel catalogResponseModel = CatalogResponseModel.builder()
                .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .type("Fiction")
                .size(20)
                .build();

        when(catalogServiceClient.postCatalog(catalogRequestModel)).thenReturn(catalogResponseModel);
        when(catalogResponseMapper.responseModelToResponseModel(catalogResponseModel)).thenReturn(catalogResponseModel);

        // act
        CatalogResponseModel result = catalogBooksService.addCatalog(catalogRequestModel);

        // assert
        assertEquals(catalogResponseModel, result);
        verify(catalogServiceClient, times(1)).postCatalog(catalogRequestModel);
        verify(catalogResponseMapper, times(1)).responseModelToResponseModel(catalogResponseModel);
    }

    // positive test
    @Test
    public void whenCatalogUpdated_thenReturnCatalog() {
        // arrange
        CatalogRequestModel catalogRequestModel = CatalogRequestModel.builder()
                .type("Fiction")
                .size(20)
                .build();

        CatalogResponseModel catalogResponseModel = CatalogResponseModel.builder()
                .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .type("Fiction")
                .size(20)
                .build();

        when(catalogServiceClient.putCatalogByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081", catalogRequestModel))
                .thenReturn(catalogResponseModel);
        when(catalogResponseMapper.responseModelToResponseModel(catalogResponseModel)).thenReturn(catalogResponseModel);

        // act
        CatalogResponseModel result = catalogBooksService
                .updateCatalog(catalogRequestModel, "ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        assertEquals(catalogResponseModel, result);
        verify(catalogServiceClient, times(1))
                .putCatalogByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081", catalogRequestModel);
        verify(catalogResponseMapper, times(1)).responseModelToResponseModel(catalogResponseModel);
    }

    // positive test
    @Test
    public void whenCatalogDeleted_thenVerify() {
        // act
        catalogBooksService.deleteCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        verify(catalogServiceClient, times(1)).deleteCatalogByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081");
    }

    // positive test
    @Test
    public void whenBooksExists_thenReturnBookList() {
        // arrange
        List<BookResponseModel> bookResponseModelList = List.of(
                BookResponseModel.builder()
                        .isbn(1234567890L)
                        .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                        .title("Book1")
                        .collection("Collection1")
                        .edition("Edition1")
                        .publisher("Publisher1")
                        .synopsis("Synopsis1")
                        .language("Language1")
                        .status(Status.AVAILABLE)
                        .author(new Author("James", "Jervis"))
                        .build(),
                BookResponseModel.builder()
                        .isbn(1234567891L)
                        .title("Book2")
                        .collection("Collection2")
                        .edition("Edition2")
                        .publisher("Publisher2")
                        .synopsis("Synopsis2")
                        .language("Language2")
                        .status(Status.BORROWED)
                        .author(new Author("John", "Doe"))
                        .build()
        );

        when(catalogServiceClient.getBooksInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081"))
                .thenReturn(bookResponseModelList);
        when(bookResponseMapper.responseModelListToResponseModelList(bookResponseModelList))
                .thenReturn(bookResponseModelList);

        // act
        List<BookResponseModel> result = catalogBooksService
                .getAllBooksInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        assertEquals(bookResponseModelList, result);
        verify(catalogServiceClient, times(1)).getBooksInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081");
        verify(bookResponseMapper, times(1)).responseModelListToResponseModelList(bookResponseModelList);
    }

    // negative test
    @Test
    public void whenBooksNotExists_thenReturnEmptyList() {
        // arrange
        List<BookResponseModel> bookResponseModelList = List.of();

        when(catalogServiceClient.getBooksInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081"))
                .thenReturn(bookResponseModelList);
        when(bookResponseMapper.responseModelListToResponseModelList(bookResponseModelList))
                .thenReturn(bookResponseModelList);

        // act
        List<BookResponseModel> result = catalogBooksService
                .getAllBooksInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        assertEquals(bookResponseModelList, result);
        verify(catalogServiceClient, times(1)).getBooksInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081");
        verify(bookResponseMapper, times(1)).responseModelListToResponseModelList(bookResponseModelList);
    }

    // positive test
    @Test
    public void whenBookExists_thenReturnBook() {
        // arrange
        BookResponseModel bookResponseModel = BookResponseModel.builder()
                .isbn(1234567890L)
                .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .title("Book1")
                .collection("Collection1")
                .edition("Edition1")
                .publisher("Publisher1")
                .synopsis("Synopsis1")
                .language("Language1")
                .status(Status.AVAILABLE)
                .author(new Author("James", "Jervis"))
                .build();

        when(catalogServiceClient.getBookByCatalogIdAndIsbn("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L))
                .thenReturn(bookResponseModel);
        when(bookResponseMapper.responseModelToResponseModel(bookResponseModel))
                .thenReturn(bookResponseModel);

        // act
        BookResponseModel result = catalogBooksService.getBookInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L);

        // assert
        assertEquals(bookResponseModel, result);
        verify(catalogServiceClient, times(1))
                .getBookByCatalogIdAndIsbn("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L);
        verify(bookResponseMapper, times(1)).responseModelToResponseModel(bookResponseModel);
    }

    // negative test
    @Test
    public void whenBookNotExists_thenReturnNull() {
        // arrange
        when(catalogServiceClient.getBookByCatalogIdAndIsbn("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L))
                .thenReturn(null);
        when(bookResponseMapper.responseModelToResponseModel(null)).thenReturn(null);

        // act
        BookResponseModel result = catalogBooksService.getBookInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L);

        // assert
        assertNull(result);
        verify(catalogServiceClient, times(1))
                .getBookByCatalogIdAndIsbn("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L);
        verify(bookResponseMapper, times(1)).responseModelToResponseModel(null);
    }

    // positive test
    @Test
    public void whenBookAdded_thenReturnBook() {
        // arrange
        BookRequestModel bookRequestModel = BookRequestModel.builder()
                .title("Book1")
                .collection("Collection1")
                .edition("Edition1")
                .publisher("Publisher1")
                .synopsis("Synopsis1")
                .language("Language1")
                .status("AVAILABLE")
                .author(new Author("James", "Jervis"))
                .build();

        BookResponseModel bookResponseModel = BookResponseModel.builder()
                .isbn(1234567890L)
                .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .title("Book1")
                .collection("Collection1")
                .edition("Edition1")
                .publisher("Publisher1")
                .synopsis("Synopsis1")
                .language("Language1")
                .status(Status.AVAILABLE)
                .author(new Author("James", "Jervis"))
                .build();

        when(catalogServiceClient.postBookByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081", bookRequestModel))
                .thenReturn(bookResponseModel);
        when(bookResponseMapper.responseModelToResponseModel(bookResponseModel)).thenReturn(bookResponseModel);

        // act
        BookResponseModel result = catalogBooksService
                .addBookInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081", bookRequestModel);

        // assert
        assertEquals(bookResponseModel, result);
        verify(catalogServiceClient, times(1))
                .postBookByCatalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081", bookRequestModel);
        verify(bookResponseMapper, times(1)).responseModelToResponseModel(bookResponseModel);
    }

    // positive test
    @Test
    public void whenBookUpdated_thenReturnBook() {
        // arrange
        BookRequestModel bookRequestModel = BookRequestModel.builder()
                .title("Book1")
                .collection("Collection1")
                .edition("Edition1")
                .publisher("Publisher1")
                .synopsis("Synopsis1")
                .language("Language1")
                .status("AVAILABLE")
                .author(new Author("James", "Jervis"))
                .build();

        BookResponseModel bookResponseModel = BookResponseModel.builder()
                .isbn(1234567890L)
                .catalogId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .title("Book1")
                .collection("Collection1")
                .edition("Edition1")
                .publisher("Publisher1")
                .synopsis("Synopsis1")
                .language("Language1")
                .status(Status.AVAILABLE)
                .author(new Author("James", "Jervis"))
                .build();

        when(catalogServiceClient.putBookByCatalogIdAndIsbn("ef23ab6e-d614-47b9-95d0-d66167ae5081",
                1234567890L, bookRequestModel)).thenReturn(bookResponseModel);
        when(bookResponseMapper.responseModelToResponseModel(bookResponseModel)).thenReturn(bookResponseModel);

        // act
        BookResponseModel result = catalogBooksService
                .updateBookInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L, bookRequestModel);

        // assert
        assertEquals(bookResponseModel, result);
        verify(catalogServiceClient, times(1))
                .putBookByCatalogIdAndIsbn("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L, bookRequestModel);
        verify(bookResponseMapper, times(1)).responseModelToResponseModel(bookResponseModel);
    }

    // positive test
    @Test
    public void whenBookDeleted_thenVerify() {
        // act
        catalogBooksService.deleteBookInCatalog("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L);

        // assert
        verify(catalogServiceClient, times(1))
                .deleteBookByCatalogIdAndIsbn("ef23ab6e-d614-47b9-95d0-d66167ae5081", 1234567890L);
    }

}