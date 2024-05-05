package com.library.apigateway.domainclientlayer.loans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.apigateway.presentationlayer.loans.LoanRequestModel;
import com.library.apigateway.presentationlayer.loans.LoanResponseModel;
import com.library.apigateway.utils.HttpErrorInfo;
import com.library.apigateway.utils.exceptions.NotFoundException;
import com.library.apigateway.utils.exceptions.UnavailableBookException;
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
public class LoanServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String LOAN_SERVICE_BASE_URL;

    private LoanServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                              @Value("${app.loans-service.host}") String loanServiceHost,
                              @Value("${app.loans-service.port}") String loanServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        LOAN_SERVICE_BASE_URL  = "http://" + loanServiceHost + ":" + loanServicePort + "/api/v1/patrons";
    }

    public List<LoanResponseModel> getAllLoans(String patronId) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + patronId + "/loans";
            LoanResponseModel[] loanResponseModels = restTemplate.getForObject(url, LoanResponseModel[].class);
            return Arrays.asList(loanResponseModels);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public LoanResponseModel getLoanByLoanId(String patronId, String loanId) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + patronId + "/loans/" + loanId;
            return restTemplate.getForObject(url, LoanResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public LoanResponseModel postLoan(String patronId, LoanRequestModel loanRequestModel) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + patronId + "/loans";
            return restTemplate.postForObject(url, loanRequestModel, LoanResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public LoanResponseModel putLoanByLoanId(String patronId, String loanId, LoanRequestModel loanRequestModel) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + patronId + "/loans/" + loanId;
            restTemplate.put(url, loanRequestModel);
            return getLoanByLoanId(patronId, loanId);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteLoanByLoanId(String patronId, String loanId) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + patronId + "/loans/" + loanId;
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
            return new UnavailableBookException(getErrorMessage(ex));
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
