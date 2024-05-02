package com.library.apigateway.businesslayer.patrons;

import com.library.apigateway.domainclientlayer.patrons.PatronServiceClient;
import com.library.apigateway.mapperlayer.patrons.PatronResponseMapper;
import com.library.apigateway.presentationlayer.patrons.PatronRequestModel;
import com.library.apigateway.presentationlayer.patrons.PatronResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatronServiceImpl implements PatronService{

    private final PatronServiceClient patronServiceClient;
    private final PatronResponseMapper patronResponseMapper;

    public PatronServiceImpl(PatronServiceClient patronServiceClient, PatronResponseMapper patronResponseMapper) {
        this.patronServiceClient = patronServiceClient;
        this.patronResponseMapper = patronResponseMapper;
    }

    @Override
    public List<PatronResponseModel> getAllPatrons() {
        return patronResponseMapper.responseModelListToResponseModelList(patronServiceClient.getAllPatrons());
    }

    @Override
    public PatronResponseModel getPatron(String patronId) {
        return patronResponseMapper.responseModelToResponseModel(patronServiceClient.getPatronByPatronId(patronId));
    }

    @Override
    public PatronResponseModel addPatron(PatronRequestModel patronRequestModel) {
        return patronResponseMapper.responseModelToResponseModel(patronServiceClient.postPatron(patronRequestModel));
    }

    @Override
    public PatronResponseModel updatePatron(PatronRequestModel patronRequestModel, String patronId) {
        return patronResponseMapper.responseModelToResponseModel(patronServiceClient.putPatronByPatronId(patronId,
                patronRequestModel));
    }

    @Override
    public void removePatron(String patronId) {
        patronServiceClient.deletePatronByPatronId(patronId);
    }
}
