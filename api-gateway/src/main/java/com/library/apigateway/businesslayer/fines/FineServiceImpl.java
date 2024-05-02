package com.library.apigateway.businesslayer.fines;

import com.library.apigateway.domainclientlayer.fines.FineServiceClient;
import com.library.apigateway.mapperlayer.fines.FineResponseMapper;
import com.library.apigateway.presentationlayer.fines.FineRequestModel;
import com.library.apigateway.presentationlayer.fines.FineResponseModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FineServiceImpl implements FineService{

    private final FineServiceClient fineServiceClient;
    private final FineResponseMapper fineResponseMapper;

    public FineServiceImpl(FineServiceClient fineServiceClient, FineResponseMapper fineResponseMapper) {
        this.fineServiceClient = fineServiceClient;
        this.fineResponseMapper = fineResponseMapper;
    }

    @Override
    public List<FineResponseModel> getAllFines() {
        return fineResponseMapper.responseModelListToResponseModelList(fineServiceClient.getAllFines());
    }

    @Override
    public FineResponseModel getFine(String fineId) {
        return fineResponseMapper.responseModelToResponseModel(fineServiceClient.getFineByFineId(fineId));
    }

    @Override
    public FineResponseModel addFine(FineRequestModel fineRequestModel) {
        return fineResponseMapper.responseModelToResponseModel(fineServiceClient.postFine(fineRequestModel));
    }

    @Override
    public FineResponseModel updateFine(FineRequestModel fineRequestModel, String fineId) {
        return fineResponseMapper.responseModelToResponseModel(fineServiceClient.putFineByFineId(fineId,
                fineRequestModel));
    }

    @Override
    public void deleteFine(String fineId) {
        fineServiceClient.deleteFineByFineId(fineId);
    }
}
