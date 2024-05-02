package com.library.patrons.businesslayer;

import com.library.patrons.presentationlayer.PatronRequestModel;
import com.library.patrons.presentationlayer.PatronResponseModel;

import java.util.List;

public interface PatronService {

    List<PatronResponseModel> getAllPatrons();
    PatronResponseModel getPatron(String patronId);
    PatronResponseModel addPatron(PatronRequestModel patronRequestModel);
    PatronResponseModel updatePatron(PatronRequestModel patronRequestModel, String patronId);
    void removePatron(String patronId);
}
