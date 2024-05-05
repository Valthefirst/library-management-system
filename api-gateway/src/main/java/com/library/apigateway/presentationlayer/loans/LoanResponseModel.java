package com.library.apigateway.presentationlayer.loans;

import com.library.apigateway.domainclientlayer.loans.BookModel;
import com.library.apigateway.domainclientlayer.loans.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseModel extends RepresentationModel<LoanResponseModel> {

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
