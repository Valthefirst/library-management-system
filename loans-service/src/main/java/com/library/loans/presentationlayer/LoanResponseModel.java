package com.library.loans.presentationlayer;

import com.library.loans.datalayer.LoanStatus;
import com.library.loans.domainclientlayer.catalogs.BookModel;
import com.library.loans.domainclientlayer.catalogs.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseModel {

    private String loanId;
    private String patronId;
    private String patronFirstName;
    private String patronLastName;
    private String fineId;
    private LoanStatus status;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private List<BookModel> books;
}
