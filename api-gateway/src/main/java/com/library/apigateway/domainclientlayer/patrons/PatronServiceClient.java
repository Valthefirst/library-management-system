package com.library.apigateway.domainclientlayer.patrons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.apigateway.presentationlayer.patrons.PatronRequestModel;
import com.library.apigateway.presentationlayer.patrons.PatronResponseModel;
import com.library.apigateway.utils.HttpErrorInfo;
import com.library.apigateway.utils.exceptions.InvalidEmailException;
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

    public List<PatronResponseModel> getAllPatrons() {
        try {
            PatronResponseModel [] patronResponseModels = restTemplate.getForObject(PATRON_SERVICE_BASE_URL,
                    PatronResponseModel[].class);

            return Arrays.asList(patronResponseModels);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PatronResponseModel getPatronByPatronId(String patronId) {
        try {
            String url = PATRON_SERVICE_BASE_URL + "/" + patronId;

            return restTemplate.getForObject(url, PatronResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PatronResponseModel postPatron(PatronRequestModel patronRequestModel) {
        try {
            return restTemplate.postForObject(PATRON_SERVICE_BASE_URL, patronRequestModel, PatronResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PatronResponseModel putPatronByPatronId(String patronId, PatronRequestModel patronRequestModel) {
        try {
            String url = PATRON_SERVICE_BASE_URL + "/" + patronId;

            restTemplate.put(url, patronRequestModel);
            return getPatronByPatronId(patronId);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deletePatronByPatronId(String patronId) {
        try {
            String url = PATRON_SERVICE_BASE_URL + "/" + patronId;

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
