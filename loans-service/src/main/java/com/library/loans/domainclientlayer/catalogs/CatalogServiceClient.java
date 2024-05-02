package com.library.loans.domainclientlayer.catalogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.loans.utils.HttpErrorInfo;
import com.library.loans.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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

//        BOOK_SERVICE_BASE_URL  = "http://" + catalogServiceHost + ":" + catalogServicePort + "/api/v1/catalogs";

        BOOK_SERVICE_BASE_URL  = "http://" + catalogServiceHost + ":" + catalogServicePort + "/api/v1/books";
    }

//    public BookModel getBookByCatalogIdAndIsbn(String catalogId, String isbn) {
//        try {
//            String url = BOOK_SERVICE_BASE_URL + "/" + catalogId + "/books/" + isbn;
//
//            BookModel bookModel = restTemplate.getForObject(url, BookModel.class);
//
//            return bookModel;
//        }
//        catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
//    }

    public BookModel getBookByIsbn(Long isbn) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + isbn;

            return restTemplate.getForObject(url, BookModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public BookModel patchBookByIsbn(Long isbn, BookModel bookModel) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + isbn;

            return restTemplate.patchForObject(url, bookModel, BookModel.class);
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
