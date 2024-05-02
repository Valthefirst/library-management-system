package com.library.apigateway.domainclientlayer.catalogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.apigateway.presentationlayer.catalogs.books.BookRequestModel;
import com.library.apigateway.presentationlayer.catalogs.books.BookResponseModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogRequestModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogResponseModel;
import com.library.apigateway.utils.HttpErrorInfo;
import com.library.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@Slf4j
public class CatalogServiceClient {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String BOOK_SERVICE_BASE_URL;

    private CatalogServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                 @Value("${app.catalog-service.host}") String catalogServiceHost,
                                 @Value("${app.catalog-service.port}") String catalogServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        BOOK_SERVICE_BASE_URL  = "http://" + catalogServiceHost + ":" + catalogServicePort + "/api/v1/catalogs";
    }

    public List<CatalogResponseModel> getAllCatalogs() {
        try {
            CatalogResponseModel [] catalogResponseModels = restTemplate.getForObject(BOOK_SERVICE_BASE_URL,
                    CatalogResponseModel[].class);

            return Arrays.asList(catalogResponseModels);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public CatalogResponseModel getCatalogByCatalogId(String catalogId) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId;

            return restTemplate.getForObject(url, CatalogResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public CatalogResponseModel postCatalog(CatalogRequestModel catalogRequestModel) {
        try {
            return restTemplate.postForObject(BOOK_SERVICE_BASE_URL, catalogRequestModel, CatalogResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public CatalogResponseModel putCatalogByCatalogId(String catalogId, CatalogRequestModel catalogRequestModel) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId;

            restTemplate.put(url, catalogRequestModel);
            return getCatalogByCatalogId(catalogId);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteCatalogByCatalogId(String catalogId) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId;

            restTemplate.delete(url);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<BookResponseModel> getBooksInCatalog(String catalogId) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId + "/books";

            BookResponseModel [] bookResponseModels = restTemplate.getForObject(url, BookResponseModel[].class);

            return Arrays.asList(bookResponseModels);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public BookResponseModel getBookByCatalogIdAndIsbn(String catalogId, Long isbn) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId + "/books/" + isbn;

            return restTemplate.getForObject(url, BookResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public BookResponseModel postBookByCatalogId(String catalogId, BookRequestModel bookRequestModel) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId + "/books";

            return restTemplate.postForObject(url, bookRequestModel, BookResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public BookResponseModel putBookByCatalogIdAndIsbn(String catalogId, Long isbn, BookRequestModel bookRequestModel) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId + "/books/" + isbn;

            restTemplate.put(url, bookRequestModel, BookResponseModel.class);
            return getBookByCatalogIdAndIsbn(catalogId, isbn);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

//    public BookResponseModel patchBookByCatalogIdAndIsbn(String catalogId, Long isbn, BookRequestModel bookRequestModel) {
//        try {
//            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId + "/books/" + isbn;
//
//            return restTemplate.patchForObject(url, bookRequestModel, BookResponseModel.class);
//        }
//        catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
//    }

    public void deleteBookByCatalogIdAndIsbn(String catalogId, Long isbn) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId + "/books/" + isbn;

            restTemplate.delete(url);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public RuntimeException handleHttpClientException(HttpClientErrorException ex) {

        //include all possible responses from the client
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }

    public String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }
}
