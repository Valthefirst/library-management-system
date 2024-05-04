package com.library.loans.utils;

import com.library.loans.datalayer.*;
import com.library.loans.datalayer.LoanRepository;
import com.library.loans.domainclientlayer.catalogs.Author;
import com.library.loans.domainclientlayer.catalogs.BookModel;
import com.library.loans.domainclientlayer.catalogs.Status;
import com.library.loans.domainclientlayer.fines.FineModel;
import com.library.loans.domainclientlayer.patrons.PatronModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseLoaderService implements CommandLineRunner {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public void run(String... args) throws Exception {

        //first purchase order
        var loanIdentifier1 = new LoanIdentifier();

        var bookModel1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.AVAILABLE)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        var bookModel2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.AVAILABLE)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.50"))
                .reason("Late return")
                .isPaid(true)
                .build();

        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        loanRepository.save(loan1);
    }
}
