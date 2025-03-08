package com.library.apigateway.businesslayer.patrons;

import com.library.apigateway.presentationlayer.patrons.PatronRequestModel;
import com.library.apigateway.presentationlayer.patrons.PatronResponseModel;

import java.util.List;

public interface PatronService {

    List<PatronResponseModel> getAllPatrons();

    PatronResponseModel getPatron(String patronId);

    PatronResponseModel addPatron(PatronRequestModel patronRequestModel);

    PatronResponseModel updatePatron(PatronRequestModel patronRequestModel, String patronId);

    void removePatron(String patronId);
}
