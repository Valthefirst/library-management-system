package com.library.loans.datalayer;

import com.library.loans.domainclientlayer.catalogs.Author;
import com.library.loans.domainclientlayer.catalogs.BookModel;
import com.library.loans.domainclientlayer.catalogs.Status;
import com.library.loans.domainclientlayer.fines.FineModel;
import com.library.loans.domainclientlayer.patrons.PatronModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class LoanRepositoryIntegrationTest {

    @Autowired
    LoanRepository loanRepository;

    @BeforeEach
    public void setUp() {
        loanRepository.deleteAll();
    }

    @Test
    public void whenPatronIsValid_ThenReturnLoans() {
        // arrange
        var loanIdentifier1 = new LoanIdentifier();
        var bookModel1 = BookModel.builder()
                .isbn(9780395193952L)
                .catalogId("448b5ee1-4445-4213-84cf-0dc9150d82e9")
                .title("The Lord Of The Rings")
                .collection("The Lord Of The Rings")
                .status(Status.AVAILABLE)
                .author(Author.builder()
                        .firstName("J.R.R.")
                        .lastName("Tolkien")
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

        var patronModel = PatronModel.builder()
                .patronId("c3540a89-cb47-4c96-888e-ff96708db4d8")
                .firstName("Alick")
                .lastName("Ucceli")
                .build();

        var fineModel1 = FineModel.builder()
                .fineId(new FineIdentifier().getFineId())
                .amount(new BigDecimal("0.00"))
                .reason(null)
                .isPaid(null)
                .build();

        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .books(List.of(bookModel1, bookModel2))
                .fineModel(fineModel1)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .returnedDate(null)
                .build();

        loanRepository.save(loan1);

        // act
        var savedLoan = loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId());

        // assert
        assertNotNull(savedLoan);
        assertEquals(savedLoan.getLoanIdentifier().getLoanId(), loan1.getLoanIdentifier().getLoanId());
        assertEquals(savedLoan.getFineModel(), loan1.getFineModel());
        assertEquals(savedLoan.getPatronModel(), loan1.getPatronModel());
        assertEquals(savedLoan.getBooks().size(), loan1.getBooks().size());
        assertEquals(savedLoan.getStatus(), loan1.getStatus());
        assertEquals(savedLoan.getBorrowedDate(), loan1.getBorrowedDate());
        assertEquals(savedLoan.getDueDate(), loan1.getDueDate());
        assertEquals(savedLoan.getReturnedDate(), loan1.getReturnedDate());
    }

    @Test
    public void whenLoanDoesNotExist_ThenReturnNull() {
        // arrange
        var loanIdentifier1 = new LoanIdentifier();
        var bookModel1 = BookModel.builder()
                .isbn(9780395193952L)
                .catalogId("448b5ee1-4445-4213-84cf-0dc9150d82e9")
                .title("The Lord Of The Rings")
                .collection("The Lord Of The Rings")
                .status(Status.AVAILABLE)
                .author(Author.builder()
                        .firstName("J.R.R.")
                        .lastName("Tolkien")
                        .build())
                .build();

        var patronModel = PatronModel.builder()
                .patronId("c3540a89-cb47-4c96-888e-ff96708db4d8")
                .firstName("Alick")
                .lastName("Ucceli")
                .build();

        var fineModel1 = FineModel.builder()
                .fineId(new FineIdentifier().getFineId())
                .amount(new BigDecimal("0.00"))
                .reason(null)
                .isPaid(null)
                .build();

        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .books(List.of(bookModel1))
                .fineModel(fineModel1)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .returnedDate(null)
                .build();

        loanRepository.save(loan1);

        // act
        var savedLoan = loanRepository.findLoansByPatronModel_PatronId("non-existent-id");

        // assert
        assertNotNull(savedLoan);
        assertEquals(0, savedLoan.size());
        assertArrayEquals(new Loan[]{}, savedLoan.toArray());
    }

    @Test
    public void whenPatronHasLoans_ThenReturnListOfLoans() {
        // arrange
        var loanIdentifier1 = new LoanIdentifier();
        var loanIdentifier2 = new LoanIdentifier();
        var bookModel1 = BookModel.builder()
                .isbn(9780395193952L)
                .catalogId("448b5ee1-4445-4213-84cf-0dc9150d82e9")
                .title("The Lord Of The Rings")
                .collection("The Lord Of The Rings")
                .status(Status.AVAILABLE)
                .author(Author.builder()
                        .firstName("J.R.R.")
                        .lastName("Tolkien")
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

        var patronModel = PatronModel.builder()
                .patronId("c3540a89-cb47-4c96-888e-ff96708db4d8")
                .firstName("Alick")
                .lastName("Ucceli")
                .build();

        var fineModel1 = FineModel.builder()
                .fineId(new FineIdentifier().getFineId())
                .amount(new BigDecimal("0.00"))
                .reason(null)
                .isPaid(null)
                .build();

        var fineModel2 = FineModel.builder()
                .fineId(new FineIdentifier().getFineId())
                .amount(new BigDecimal("0.00"))
                .reason(null)
                .isPaid(null)
                .build();

        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .books(List.of(bookModel1))
                .fineModel(fineModel1)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .returnedDate(null)
                .build();

        var loan2 = Loan.builder()
                .loanIdentifier(loanIdentifier2)
                .patronModel(patronModel)
                .books(List.of(bookModel2))
                .fineModel(fineModel2)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .returnedDate(null)
                .build();

        loanRepository.save(loan1);
        loanRepository.save(loan2);

        // act
        var savedLoans = loanRepository.findLoansByPatronModel_PatronId("c3540a89-cb47-4c96-888e-ff96708db4d8");

        // assert
        assertNotNull(savedLoans);
        assertEquals(2, savedLoans.size());
    }

    @Test
    public void whenPatronHasNoLoans_ThenReturnNull() {
        // arrange
        var loanIdentifier1 = new LoanIdentifier();
        var loanIdentifier2 = new LoanIdentifier();
        var bookModel1 = BookModel.builder()
                .isbn(9780395193952L)
                .catalogId("448b5ee1-4445-4213-84cf-0dc9150d82e9")
                .title("The Lord Of The Rings")
                .collection("The Lord Of The Rings")
                .status(Status.AVAILABLE)
                .author(Author.builder()
                        .firstName("J.R.R.")
                        .lastName("Tolkien")
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

        var patronModel = PatronModel.builder()
                .patronId("c3540a89-cb47-4c96-888e-ff96708db4d8")
                .firstName("Alick")
                .lastName("Ucceli")
                .build();

        var fineModel1 = FineModel.builder()
                .fineId(new FineIdentifier().getFineId())
                .amount(new BigDecimal("0.00"))
                .reason(null)
                .isPaid(null)
                .build();

        var fineModel2 = FineModel.builder()
                .fineId(new FineIdentifier().getFineId())
                .amount(new BigDecimal("0.00"))
                .reason(null)
                .isPaid(null)
                .build();

        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .books(List.of(bookModel1))
                .fineModel(fineModel1)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .returnedDate(null)
                .build();

        var loan2 = Loan.builder()
                .loanIdentifier(loanIdentifier2)
                .patronModel(patronModel)
                .books(List.of(bookModel2))
                .fineModel(fineModel2)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .returnedDate(null)
                .build();

        loanRepository.save(loan1);
        loanRepository.save(loan2);

        // act
        var savedLoan = loanRepository.findLoansByPatronModel_PatronId("cc9c2c7f-afc9-46fb-8119-17158e54d02f");

        // assert
        assertNotNull(savedLoan);
        assertEquals(0, savedLoan.size());
        assertArrayEquals(new Loan[]{}, savedLoan.toArray());
    }

}