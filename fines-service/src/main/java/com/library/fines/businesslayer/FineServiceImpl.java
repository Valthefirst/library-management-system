package com.library.fines.businesslayer;

import com.library.fines.datalayer.Fine;
import com.library.fines.datalayer.FineIdentifier;
import com.library.fines.datalayer.FineRepository;
import com.library.fines.datamapperlayer.FineRequestMapper;
import com.library.fines.datamapperlayer.FineResponseMapper;
import com.library.fines.presentationlayer.FineRequestModel;
import com.library.fines.presentationlayer.FineResponseModel;
import com.library.fines.utils.exceptions.InvalidAmountException;
import com.library.fines.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FineServiceImpl implements FineService{

    private final FineRepository fineRepository;
    private final FineResponseMapper fineResponseMapper;
    private final FineRequestMapper fineRequestMapper;

    public FineServiceImpl(FineRepository fineRepository, FineResponseMapper fineResponseMapper, FineRequestMapper fineRequestMapper) {
        this.fineRepository = fineRepository;
        this.fineResponseMapper = fineResponseMapper;
        this.fineRequestMapper = fineRequestMapper;
    }

    @Override
    public List<FineResponseModel> getAllFines() {
        return fineResponseMapper.entityListToResponseModelList(fineRepository.findAll());
    }

    @Override
    public FineResponseModel getFine(String fineId) {
        Fine fine = fineRepository.findByFineIdentifier_FineId(fineId);
        if (fine == null)
            throw new NotFoundException("Unknown fineId: " + fineId);
        return fineResponseMapper.entityToResponseModel(fine);
    }

    @Override
    public FineResponseModel addFine(FineRequestModel fineRequestModel) {
        if (fineRequestModel.getAmount().compareTo(new BigDecimal("0.00")) < 0)
            throw new InvalidAmountException("The fine must have a positive value");

        Fine fine = fineRequestMapper.requestModelToEntity(fineRequestModel, new FineIdentifier());
        return fineResponseMapper.entityToResponseModel(fineRepository.save(fine));
    }

    @Override
    public FineResponseModel updateFine(FineRequestModel fineRequestModel, String fineId) {
        Fine existingFine = fineRepository.findByFineIdentifier_FineId(fineId);
        if (existingFine == null)
            throw new NotFoundException("Unknown fineId: " + fineId);
        if (fineRequestModel.getAmount().compareTo(new BigDecimal("0.00")) < 0)
            throw new InvalidAmountException("The fine must have a positive value");
        Fine updatedFine = fineRequestMapper.requestModelToEntity(fineRequestModel, existingFine.getFineIdentifier());
        updatedFine.setId(existingFine.getId());
        return fineResponseMapper.entityToResponseModel(fineRepository.save(updatedFine));
    }

    @Override
    public void deleteFine(String fineId) {
        Fine fine = fineRepository.findByFineIdentifier_FineId(fineId);
        if (fine == null)
            throw new NotFoundException("Unknown fineId: " + fineId);
        fineRepository.delete(fine);
    }
}
