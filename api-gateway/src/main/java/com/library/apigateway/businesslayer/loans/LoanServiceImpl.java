package com.library.apigateway.businesslayer.loans;

import com.library.apigateway.domainclientlayer.loans.LoanServiceClient;
import com.library.apigateway.mapperlayer.loans.LoanResponseMapper;
import com.library.apigateway.presentationlayer.loans.LoanRequestModel;
import com.library.apigateway.presentationlayer.loans.LoanResponseModel;
import com.library.apigateway.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanServiceClient loanServiceClient;
    private final LoanResponseMapper loanResponseMapper;

    public LoanServiceImpl(LoanServiceClient loanServiceClient, LoanResponseMapper loanResponseMapper) {
        this.loanServiceClient = loanServiceClient;
        this.loanResponseMapper = loanResponseMapper;
    }

    @Override
    public List<LoanResponseModel> getAllLoansForPatron(String patronId) {
        return loanResponseMapper.responseModelListToResponseModelList(loanServiceClient.getAllLoans(patronId));
    }

    @Override
    public LoanResponseModel getLoanForPatron(String patronId, String loanId) {
        return loanResponseMapper.responseModelToResponseModel(loanServiceClient.getLoanByLoanId(patronId, loanId));
    }

    @Override
    public LoanResponseModel addLoanForPatron(String patronId, LoanRequestModel loanRequestModel) {
        return loanResponseMapper.responseModelToResponseModel(loanServiceClient.postLoan(patronId, loanRequestModel));
    }

    @Override
    public LoanResponseModel updateLoanForPatron(String patronId, String loanId, LoanRequestModel loanRequestModel) {
        return loanResponseMapper.responseModelToResponseModel(loanServiceClient
                .putLoanByLoanId(patronId, loanId, loanRequestModel));
    }

    @Override
    public void deleteLoanForPatron(String patronId, String loanId) {
        loanServiceClient.deleteLoanByLoanId(patronId, loanId);
    }
}
