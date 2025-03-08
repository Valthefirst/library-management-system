package com.library.apigateway.businesslayer.loans;

import com.library.apigateway.domainclientlayer.loans.*;
import com.library.apigateway.mapperlayer.loans.LoanResponseMapper;
import com.library.apigateway.presentationlayer.fines.FineResponseModel;
import com.library.apigateway.presentationlayer.loans.LoanRequestModel;
import com.library.apigateway.presentationlayer.loans.LoanResponseModel;
import com.library.apigateway.presentationlayer.patrons.PatronResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude = org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
@ActiveProfiles("test")
class LoanServiceUnitTest {

    @Autowired
    LoanService loanService;

    @MockBean
    LoanServiceClient loanServiceClient;

    @SpyBean
    LoanResponseMapper loanResponseMapper;

    // positive test
    @Test
    public void whenValidPatronId_thenProcessGetAll() {
        // arrange
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

        var patronModel = PatronResponseModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineResponseModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loan1 = LoanResponseModel.builder()
                .loanId("loanIdentifier1")
                .patronId("patronId")
                .patronFirstName(patronModel.getFirstName())
                .patronLastName(patronModel.getLastName())
                .fineId(fineModel.getFineId())
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .books(bookModelList)
                .build();

        List<LoanResponseModel> loanList = new ArrayList<>();
        loanList.add(loan1);

        when(loanServiceClient.getAllLoans("patronId")).thenReturn(List.of(loan1));
        when(loanResponseMapper.responseModelListToResponseModelList(loanList)).thenReturn(loanList);

        // act
        List<LoanResponseModel> loans = loanService.getAllLoansForPatron("patronId");

        // assert
        assertEquals(1, loans.size());
        assertEquals("loanIdentifier1", loans.get(0).getLoanId());
        assertEquals("patronId", loans.get(0).getPatronId());
        assertEquals("Vilma", loans.get(0).getPatronFirstName());
        assertEquals("Chawner", loans.get(0).getPatronLastName());
        assertEquals("ef23ab6e-d614-47b9-95d0-d66167ae5080", loans.get(0).getFineId());
        assertEquals(LoanStatus.ACTIVE, loans.get(0).getStatus());
        assertEquals(LocalDate.now(), loans.get(0).getBorrowedDate());
        assertEquals(LocalDate.now().plusDays(21), loans.get(0).getDueDate());
        assertEquals(bookModelList, loans.get(0).getBooks());
        verify(loanServiceClient, times(1)).getAllLoans("patronId");
    }

//    // negative test
//    @Test
//    public void whenInvalidPatronId_thenProcessGetAll() {
//        // arrange
//        when(loanServiceClient.getAllLoans("patronId")).thenReturn(new ArrayList<>());
//        when(loanResponseMapper.responseModelListToResponseModelList(new ArrayList<>())).thenReturn(new ArrayList<>());
//
//        // act
//        List<LoanResponseModel> loans = loanService.getAllLoansForPatron("patronId");
//
//        // assert
//        assertEquals(0, loans.size());
//    }

    // positive test
    @Test
    public void whenValidPatronId_LoanId_thenProcessGet() {
        // arrange
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

        var patronModel = PatronResponseModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineResponseModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loan1 = LoanResponseModel.builder()
                .loanId("loanIdentifier1")
                .patronId("patronId")
                .patronFirstName(patronModel.getFirstName())
                .patronLastName(patronModel.getLastName())
                .fineId(fineModel.getFineId())
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .books(bookModelList)
                .build();

        when(loanServiceClient.getLoanByLoanId("patronId", "loanId")).thenReturn(loan1);
        when(loanResponseMapper.responseModelToResponseModel(loan1)).thenReturn(loan1);

        // act
        LoanResponseModel loan = loanService.getLoanForPatron("patronId", "loanId");

        // assert
        assertEquals("loanIdentifier1", loan.getLoanId());
        assertEquals("patronId", loan.getPatronId());
        assertEquals("Vilma", loan.getPatronFirstName());
        assertEquals("Chawner", loan.getPatronLastName());
        assertEquals("ef23ab6e-d614-47b9-95d0-d66167ae5080", loan.getFineId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals(LocalDate.now(), loan.getBorrowedDate());
        assertEquals(LocalDate.now().plusDays(21), loan.getDueDate());
        assertEquals(bookModelList, loan.getBooks());
        verify(loanServiceClient, times(1)).getLoanByLoanId("patronId", "loanId");
    }

    // positive test
    @Test
    public void whenValidPatronId_Loan_thenCreateLoan() {
        // arrange
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

        List<Long> bookISBNs = new ArrayList<>();
        bookISBNs.add(0, bookModel1.getIsbn());
        bookISBNs.add(1, bookModel2.getIsbn());

        var patronModel = PatronResponseModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineResponseModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanRequest1 = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNs)
                .build();

        var loanResponse1 = LoanResponseModel.builder()
                .loanId("loanIdentifier1")
                .patronId("patronId")
                .patronFirstName(patronModel.getFirstName())
                .patronLastName(patronModel.getLastName())
                .fineId(fineModel.getFineId())
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .books(bookModelList)
                .build();

        when(loanServiceClient.postLoan("patronId", loanRequest1)).thenReturn(loanResponse1);
        when(loanResponseMapper.responseModelToResponseModel(loanResponse1)).thenReturn(loanResponse1);

        // act
        LoanResponseModel loan = loanService.addLoanForPatron("patronId", loanRequest1);

        // assert
        assertEquals("loanIdentifier1", loan.getLoanId());
        assertEquals("patronId", loan.getPatronId());
        assertEquals("Vilma", loan.getPatronFirstName());
        assertEquals("Chawner", loan.getPatronLastName());
        assertEquals("ef23ab6e-d614-47b9-95d0-d66167ae5080", loan.getFineId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals(LocalDate.now(), loan.getBorrowedDate());
        assertEquals(LocalDate.now().plusDays(21), loan.getDueDate());
        assertEquals(bookModelList, loan.getBooks());
        verify(loanServiceClient, times(1)).postLoan("patronId", loanRequest1);
    }

    // positive test
    @Test
    public void whenValidPatronId_Loan_thenUpdateLoan() {
        // arrange
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

        List<Long> bookISBNs = new ArrayList<>();
        bookISBNs.add(0, bookModel1.getIsbn());
        bookISBNs.add(1, bookModel2.getIsbn());

        var patronModel = PatronResponseModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineResponseModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanRequest1 = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNs)
                .build();

        var loanResponse1 = LoanResponseModel.builder()
                .loanId("loanIdentifier1")
                .patronId("patronId")
                .patronFirstName(patronModel.getFirstName())
                .patronLastName(patronModel.getLastName())
                .fineId(fineModel.getFineId())
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .books(bookModelList)
                .build();

        when(loanServiceClient.putLoanByLoanId("patronId", "loanId", loanRequest1))
                .thenReturn(loanResponse1);
        when(loanResponseMapper.responseModelToResponseModel(loanResponse1)).thenReturn(loanResponse1);

        // act
        LoanResponseModel loan = loanService.updateLoanForPatron("patronId", "loanId", loanRequest1);

        // assert
        assertEquals("loanIdentifier1", loan.getLoanId());
        assertEquals("patronId", loan.getPatronId());
        assertEquals("Vilma", loan.getPatronFirstName());
        assertEquals("Chawner", loan.getPatronLastName());
        assertEquals("ef23ab6e-d614-47b9-95d0-d66167ae5080", loan.getFineId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals(LocalDate.now(), loan.getBorrowedDate());
        assertEquals(LocalDate.now().plusDays(21), loan.getDueDate());
        assertEquals(bookModelList, loan.getBooks());
        verify(loanServiceClient, times(1)).putLoanByLoanId("patronId", "loanId"
                , loanRequest1);
    }

    // positive test
    @Test
    public void whenValidPatronId_LoanId_thenDeleteLoan() {
        // arrange
        doNothing().when(loanServiceClient).deleteLoanByLoanId("patronId", "loanId");

        // act
        loanService.deleteLoanForPatron("patronId", "loanId");

        // assert
        verify(loanServiceClient, times(1)).deleteLoanByLoanId("patronId", "loanId");
    }

}