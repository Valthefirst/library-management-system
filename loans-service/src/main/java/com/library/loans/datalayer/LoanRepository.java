package com.library.loans.datalayer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoanRepository extends MongoRepository<Loan, String> {

    Loan findLoanByLoanIdentifier_LoanId(String loanId);
    List<Loan> findLoansByPatronModel_PatronId(String patronId);
}
