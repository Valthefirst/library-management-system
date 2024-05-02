package com.library.loans.presentationlayer;

import com.library.loans.datalayer.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRequestModel {

    private String fineId;
    private LoanStatus status;
    private List<Long> bookISBN;
}
