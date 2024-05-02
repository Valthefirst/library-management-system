package com.library.loans.datalayer;

import com.library.loans.domainclientlayer.catalogs.BookModel;
import com.library.loans.domainclientlayer.fines.FineModel;
import com.library.loans.domainclientlayer.patrons.PatronModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Document(collection = "loans")
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    private String id;

    @Indexed(unique = true)
    private LoanIdentifier loanIdentifier;
    private PatronModel patronModel;
    private List<BookModel> books;
    private FineModel fineModel;
    private LoanStatus status;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
}
