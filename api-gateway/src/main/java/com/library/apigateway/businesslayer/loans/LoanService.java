package com.library.apigateway.businesslayer.loans;

import com.library.apigateway.presentationlayer.loans.LoanRequestModel;
import com.library.apigateway.presentationlayer.loans.LoanResponseModel;

import java.util.List;

public interface LoanService {
    List<LoanResponseModel> getAllLoansForPatron(String patronId);

    LoanResponseModel getLoanForPatron(String patronId, String loanId);

    LoanResponseModel addLoanForPatron(String patronId, LoanRequestModel loanRequestModel);

    LoanResponseModel updateLoanForPatron(String patronId, String loanId, LoanRequestModel loanRequestModel);

    void deleteLoanForPatron(String patronId, String loanId);
}
