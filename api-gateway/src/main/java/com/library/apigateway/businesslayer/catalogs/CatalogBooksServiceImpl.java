package com.library.apigateway.businesslayer.catalogs;

import com.library.apigateway.domainclientlayer.catalogs.CatalogServiceClient;
import com.library.apigateway.mapperlayer.catalogs.book.BookResponseMapper;
import com.library.apigateway.mapperlayer.catalogs.catalog.CatalogResponseMapper;
import com.library.apigateway.presentationlayer.catalogs.books.BookRequestModel;
import com.library.apigateway.presentationlayer.catalogs.books.BookResponseModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogRequestModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogBooksServiceImpl implements CatalogBooksService {

    private final CatalogServiceClient catalogServiceClient;
    private final CatalogResponseMapper catalogResponseMapper;
    private final BookResponseMapper bookResponseMapper;

    public CatalogBooksServiceImpl(CatalogServiceClient catalogServiceClient, CatalogResponseMapper catalogResponseMapper, BookResponseMapper bookResponseMapper) {
        this.catalogServiceClient = catalogServiceClient;
        this.catalogResponseMapper = catalogResponseMapper;
        this.bookResponseMapper = bookResponseMapper;
    }

    // Methods for the catalogs
    @Override
    public List<CatalogResponseModel> getAllCatalogs() {
        return catalogResponseMapper.responseModelListToResponseModelList(catalogServiceClient.getAllCatalogs());
    }

    @Override
    public CatalogResponseModel getCatalog(String catalogId) {
        return catalogResponseMapper.responseModelToResponseModel(catalogServiceClient
                .getCatalogByCatalogId(catalogId));
    }

    @Override
    public CatalogResponseModel addCatalog(CatalogRequestModel catalogRequestModel) {
        return catalogResponseMapper.responseModelToResponseModel(catalogServiceClient
                .postCatalog(catalogRequestModel));
    }

    @Override
    public CatalogResponseModel updateCatalog(CatalogRequestModel catalogRequestModel, String catalogId) {
        return catalogResponseMapper.responseModelToResponseModel(catalogServiceClient.putCatalogByCatalogId(catalogId,
                catalogRequestModel));
    }

    @Override
    public void deleteCatalog(String catalogId) {
        catalogServiceClient.deleteCatalogByCatalogId(catalogId);
    }


    // Methods for the books
    @Override
    public List<BookResponseModel> getAllBooksInCatalog(String catalogId) {
        return bookResponseMapper.responseModelListToResponseModelList(
                catalogServiceClient.getBooksInCatalog(catalogId));
    }

    @Override
    public BookResponseModel getBookInCatalog(String catalogId, Long isbn) {
        return bookResponseMapper.responseModelToResponseModel(catalogServiceClient
                .getBookByCatalogIdAndIsbn(catalogId, isbn));
    }

    @Override
    public BookResponseModel addBookInCatalog(String catalogId, BookRequestModel bookRequestModel) {
        return bookResponseMapper.responseModelToResponseModel(catalogServiceClient
                .postBookByCatalogId(catalogId, bookRequestModel));
    }

    @Override
    public BookResponseModel updateBookInCatalog(String catalogId, Long isbn, BookRequestModel bookRequestModel) {
        return bookResponseMapper.responseModelToResponseModel(catalogServiceClient
                .putBookByCatalogIdAndIsbn(catalogId, isbn, bookRequestModel));
    }

//    @Override
//    public BookResponseModel patchBookInCatalog(String catalogId, Long isbn, BookRequestModel bookRequestModel) {
//        return bookResponseMapper.responseModelToResponseModel(catalogServiceClient
//                .patchBookByCatalogIdAndIsbn(catalogId, isbn, bookRequestModel));
//    }

    @Override
    public void deleteBookInCatalog(String catalogId, Long isbn) {
        catalogServiceClient.deleteBookByCatalogIdAndIsbn(catalogId, isbn);
    }
}
