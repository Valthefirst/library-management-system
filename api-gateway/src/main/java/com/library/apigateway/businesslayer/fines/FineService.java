package com.library.apigateway.businesslayer.fines;

import com.library.apigateway.presentationlayer.fines.FineRequestModel;
import com.library.apigateway.presentationlayer.fines.FineResponseModel;

import java.util.List;

public interface FineService {
    List<FineResponseModel> getAllFines();

    FineResponseModel getFine(String fineId);

    FineResponseModel addFine(FineRequestModel fineRequestModel);

    FineResponseModel updateFine(FineRequestModel fineRequestModel, String fineId);

    void deleteFine(String fineId);
}
