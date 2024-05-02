package com.library.catalog.businesslayer.catalogbooks;

import com.library.catalog.presentationlayer.books.BookRequestModel;
import com.library.catalog.presentationlayer.books.BookResponseModel;
import com.library.catalog.presentationlayer.catalog.CatalogRequestModel;
import com.library.catalog.presentationlayer.catalog.CatalogResponseModel;

import java.util.List;

public interface CatalogBooksService {

    // Methods for the catalogs
    List<CatalogResponseModel> getAllCatalogs();
    CatalogResponseModel getCatalog(String catalogId);
    CatalogResponseModel addCatalog(CatalogRequestModel catalogRequestModel);
    CatalogResponseModel updateCatalog(CatalogRequestModel catalogRequestModel, String catalogId);
    void deleteCatalog(String catalogId);

    // Methods for the books
    List<BookResponseModel> getAllBooksInCatalog(String catalogId);

    BookResponseModel getBookInCatalog(String catalogId, Long isbn);

    BookResponseModel addBookInCatalog(String catalogId, BookRequestModel bookRequestModel);

    BookResponseModel updateBookInCatalog(String catalogId, Long isbn, BookRequestModel bookRequestModel);

    void deleteBookInCatalog(String catalogId, Long isbn);
}
