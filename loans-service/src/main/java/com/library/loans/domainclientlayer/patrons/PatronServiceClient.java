package com.library.loans.domainclientlayer.patrons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.loans.utils.HttpErrorInfo;
import com.library.loans.utils.exceptions.InvalidAmountException;
import com.library.loans.utils.exceptions.InvalidEmailException;
import com.library.loans.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class PatronServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String PATRON_SERVICE_BASE_URL;

    private PatronServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                @Value("${app.patrons-service.host}") String patronsServiceHost,
                                @Value("${app.patrons-service.port}") String patronsServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        PATRON_SERVICE_BASE_URL  = "http://" + patronsServiceHost + ":" + patronsServicePort + "/api/v1/patrons";
    }

    public PatronModel getPatronByPatronId(String patronId) {
        try {
            String url = PATRON_SERVICE_BASE_URL + "/" + patronId;

            return restTemplate.getForObject(url, PatronModel.class);
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
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidEmailException(getErrorMessage(ex));
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
