package com.library.loans.datalayer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoanIdentifier {

    private String loanId;

    public LoanIdentifier() {
        this.loanId = UUID.randomUUID().toString();
    }
}
