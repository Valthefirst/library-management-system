package com.library.apigateway.domainclientlayer.fines;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.apigateway.presentationlayer.fines.FineRequestModel;
import com.library.apigateway.presentationlayer.fines.FineResponseModel;
import com.library.apigateway.utils.HttpErrorInfo;
import com.library.apigateway.utils.exceptions.InvalidAmountException;
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
public class FineServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String FINE_SERVICE_BASE_URL;

    private FineServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                              @Value("${app.fines-service.host}") String fineServiceHost,
                              @Value("${app.fines-service.port}") String fineServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        FINE_SERVICE_BASE_URL  = "http://" + fineServiceHost + ":" + fineServicePort + "/api/v1/fines";
    }

    public List<FineResponseModel> getAllFines() {
        try {
            FineResponseModel[] fineResponseModels = restTemplate.getForObject(FINE_SERVICE_BASE_URL,
                    FineResponseModel[].class);

            return Arrays.asList(fineResponseModels);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public FineResponseModel getFineByFineId(String fineId) {
        try {
            String url = FINE_SERVICE_BASE_URL + "/" + fineId;

            return restTemplate.getForObject(url, FineResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public FineResponseModel postFine(FineRequestModel fineRequestModel) {
        try {
            return restTemplate.postForObject(FINE_SERVICE_BASE_URL, fineRequestModel, FineResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public FineResponseModel putFineByFineId(String fineId, FineRequestModel fineRequestModel) {
        try {
            String url = FINE_SERVICE_BASE_URL + "/" + fineId;

            restTemplate.put(url, fineRequestModel);
            return getFineByFineId(fineId);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteFineByFineId(String fineId) {
        try {
            String url = FINE_SERVICE_BASE_URL + "/" + fineId;

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
            return new InvalidAmountException(getErrorMessage(ex));
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
