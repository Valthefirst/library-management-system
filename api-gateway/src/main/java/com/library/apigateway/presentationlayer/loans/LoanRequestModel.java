package com.library.apigateway.presentationlayer.loans;

import com.library.apigateway.domainclientlayer.loans.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRequestModel {

    private LoanStatus status;
    private List<Long> bookISBN;
}
