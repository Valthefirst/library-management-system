package com.library.fines.businesslayer;


import com.library.fines.presentationlayer.FineRequestModel;
import com.library.fines.presentationlayer.FineResponseModel;

import java.util.List;

public interface FineService {
    List<FineResponseModel> getAllFines();

    FineResponseModel getFine(String fineId);

    FineResponseModel addFine(FineRequestModel fineRequestModel);

    FineResponseModel updateFine(FineRequestModel fineRequestModel, String fineId);

    void deleteFine(String fineId);
}
