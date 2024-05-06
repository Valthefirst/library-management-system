package com.library.loans.businesslayer;

import com.library.loans.datalayer.Loan;
import com.library.loans.datalayer.LoanIdentifier;
import com.library.loans.datalayer.LoanRepository;
import com.library.loans.datalayer.LoanStatus;
import com.library.loans.datamapperlayer.LoanResponseMapper;
import com.library.loans.domainclientlayer.catalogs.Author;
import com.library.loans.domainclientlayer.catalogs.BookModel;
import com.library.loans.domainclientlayer.catalogs.CatalogServiceClient;
import com.library.loans.domainclientlayer.catalogs.Status;
import com.library.loans.domainclientlayer.fines.FineModel;
import com.library.loans.domainclientlayer.fines.FineServiceClient;
import com.library.loans.domainclientlayer.patrons.PatronModel;
import com.library.loans.domainclientlayer.patrons.PatronServiceClient;
import com.library.loans.presentationlayer.LoanRequestModel;
import com.library.loans.presentationlayer.LoanResponseModel;
import com.library.loans.utils.exceptions.NotFoundException;
import com.library.loans.utils.exceptions.UnavailableBookException;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude = org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
@ActiveProfiles("test")
class LoanServiceUnitTest {

    @Autowired
    LoanService loanService;

    @MockBean
    CatalogServiceClient catalogServiceClient;

    @MockBean
    FineServiceClient fineServiceClient;

    @MockBean
    PatronServiceClient patronServiceClient;

    @MockBean
    LoanRepository loanRepository;

    @SpyBean
    LoanResponseMapper loanResponseMapper;
    private final String NOT_FOUND_PATRON_ID = "e5913a79-9b1e-4516-9ffd-06578e7af260";
    private final String NOT_FOUND_LOAN_ID = "d846a5a7-2e1c-4c79-809c-4f3f471e826c";

    // positive path
    @Test
    public void whenValidPatronId_thenProcessGetAll() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
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
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1))
                .thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2))
                .thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);

        //act
        List<LoanResponseModel> loanResponseModels = loanService.getAllLoansForPatron("e5913a79-9b1e-4516-9ffd-06578e7af261");

        //assert
        assertNotNull(loanResponseModels);
        verify(loanResponseMapper, times(1)).entityListToResponseModelList(anyList());

        for (LoanResponseModel responseModel : loanResponseModels) {
            assertNotNull(responseModel);
            assertNotNull(responseModel.getLoanId());
            assertNotNull(responseModel.getPatronId());
        }
    }

    // negative path
    @Test
    public void whenInvalidPatronIdGETALL_thenThrowNotFound() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
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
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1))
                .thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2))
                .thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(loanRepository.findLoansByPatronModel_PatronId(NOT_FOUND_PATRON_ID)).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.getAllLoansForPatron(NOT_FOUND_PATRON_ID));
        verify(patronServiceClient, times(1)).getPatronByPatronId(NOT_FOUND_PATRON_ID);
    }

    // positive path
    @Test
    public void whenValidPatronId_LoanId_thenProcessGet() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
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
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier("id");
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1))
                .thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2))
                .thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(loan1);

        //act
        LoanResponseModel loanResponseModel = loanService.getLoanForPatron("e5913a79-9b1e-4516-9ffd-06578e7af261",
                loan1.getLoanIdentifier().getLoanId());

        //assert
        assertNotNull(loanResponseModel);
        verify(loanResponseMapper, times(1)).entityToResponseModel(updatedLoan);

        assertNotNull(loanResponseModel);
        assertNotNull(loanResponseModel.getLoanId());
        assertNotNull(loanResponseModel.getPatronId());
    }

    // negative path
    @Test
    public void whenInvalidPatronIdGET_thenThrowNotFound() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
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
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier("id");
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1))
                .thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2))
                .thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(patronServiceClient.getPatronByPatronId(NOT_FOUND_PATRON_ID)).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.getLoanForPatron(NOT_FOUND_PATRON_ID,
                loan1.getLoanIdentifier().getLoanId()));
        verify(patronServiceClient, times(1)).getPatronByPatronId(NOT_FOUND_PATRON_ID);
    }

//     negative path
    @Test
    public void whenInvalidLoanIdGET_thenThrowNotFound() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
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
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier("id");
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1))
                .thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2))
                .thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.getLoanForPatron("e5913a79-9b1e-4516-9ffd-06578e7af261",
                loan1.getLoanIdentifier().getLoanId()));
    }

    // positive path
    @Test
    public void whenValidPatronId_Loan_thenCreateLoan() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);

        //act
        LoanResponseModel loanResponseModel = loanService.addLoanForPatron("e5913a79-9b1e-4516-9ffd-06578e7af261", loanRequestModel);

        //assert
        assertNotNull(loanResponseModel);
        assertNotNull(loanResponseModel.getLoanId());
        verify(loanResponseMapper, times(1)).entityToResponseModel(updatedLoan);
    }

    // negative path
    @Test
    public void whenInvalidPatronIdPOST_thenThrowException() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(patronServiceClient.getPatronByPatronId(NOT_FOUND_PATRON_ID)).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.addLoanForPatron(NOT_FOUND_PATRON_ID, loanRequestModel));
        verify(patronServiceClient, times(1)).getPatronByPatronId(NOT_FOUND_PATRON_ID);
    }

    // negative path
    @Test
    public void whenInvalidLoanPOST_thenThrowException1() {
        //arrange
        var bookModel1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);

        //act and assert
        assertThrows(UnavailableBookException.class, () -> loanService.addLoanForPatron(patronModel.getPatronId(), loanRequestModel));
        verify(patronServiceClient, times(1)).getPatronByPatronId(patronModel.getPatronId());
    }

    // negative path
    @Test
    public void whenInvalidLoanPOST_thenThrowException2() {
        //arrange
        var bookModel1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.LOST)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);

        //act and assert
        assertThrows(UnavailableBookException.class, () -> loanService.addLoanForPatron(patronModel.getPatronId(), loanRequestModel));
        verify(patronServiceClient, times(1)).getPatronByPatronId(patronModel.getPatronId());
    }

    // negative path
    @Test
    public void whenInvalidLoanPOST_thenThrowException3() {
        //arrange
        var bookModel1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.DAMAGED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);

        //act and assert
        assertThrows(UnavailableBookException.class, () -> loanService.addLoanForPatron(patronModel.getPatronId(), loanRequestModel));
        verify(patronServiceClient, times(1)).getPatronByPatronId(patronModel.getPatronId());
    }

    // positive path
    @Test
    public void whenValidPatronId_Loan_thenUpdateLoan() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.RETURNED)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(loan1);

        //act
        LoanResponseModel loanResponseModel = loanService.updateLoanForPatron("e5913a79-9b1e-4516-9ffd-06578e7af261",
                loanRequestModel, loan1.getLoanIdentifier().getLoanId());

        //assert
        assertNotNull(loanResponseModel);
        assertNotNull(loanResponseModel.getLoanId());
        verify(loanResponseMapper, times(1)).entityToResponseModel(updatedLoan);
    }

    // negative path
    @Test
    public void whenInvalidLoanPUT_thenThrowException1() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, loan1);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(loan1);

        //act and assert
        assertThrows(UnavailableBookException.class, () -> loanService.updateLoanForPatron(patronModel.getPatronId(), loanRequestModel, loan1.getLoanIdentifier().getLoanId()));
        verify(patronServiceClient, times(1)).getPatronByPatronId(patronModel.getPatronId());
    }

    // negative path
    @Test
    public void whenInvalidLoanPUT_thenThrowException2() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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
                .status(Status.DAMAGED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, loan1);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(loan1);

        //act and assert
        assertThrows(UnavailableBookException.class, () -> loanService.updateLoanForPatron(patronModel.getPatronId(), loanRequestModel, loan1.getLoanIdentifier().getLoanId()));
        verify(patronServiceClient, times(1)).getPatronByPatronId(patronModel.getPatronId());
    }

    // negative path
    @Test
    public void whenInvalidLoanPUT_thenThrowException3() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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
                .status(Status.LOST)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.ACTIVE)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, loan1);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(loan1);

        //act and assert
        assertThrows(UnavailableBookException.class, () -> loanService.updateLoanForPatron(patronModel.getPatronId(), loanRequestModel, loan1.getLoanIdentifier().getLoanId()));
        verify(patronServiceClient, times(1)).getPatronByPatronId(patronModel.getPatronId());
    }

    // positive path
    @Test
    public void whenValidPatronId_LoanWithLateFees_thenUpdateLoan() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.RETURNED)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.of(2023,3,3))
                .dueDate(LocalDate.of(2023,3,3).plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.of(2023,3,3))
                .dueDate(LocalDate.of(2023,3,3).plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(loan1);
        when(fineServiceClient.putFine(loan1.getFineModel(), loan1.getFineModel().getFineId())).thenReturn(loan1.getFineModel());

        //act
        LoanResponseModel loanResponseModel = loanService.updateLoanForPatron("e5913a79-9b1e-4516-9ffd-06578e7af261",
                loanRequestModel, loan1.getLoanIdentifier().getLoanId());

        //assert
        assertNotNull(loanResponseModel);
        assertNotNull(loanResponseModel.getLoanId());
        verify(loanResponseMapper, times(1)).entityToResponseModel(updatedLoan);
        verify(fineServiceClient, times(1)).putFine(loan1.getFineModel(), loan1.getFineModel().getFineId());
    }

    // negative path
    @Test
    public void whenInvalidPatronId_PUT_thenThrowNotFound() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.RETURNED)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(patronServiceClient.getPatronByPatronId(NOT_FOUND_PATRON_ID)).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.updateLoanForPatron(NOT_FOUND_PATRON_ID, loanRequestModel, loanIdentifier1.getLoanId()));
        verify(patronServiceClient, times(1)).getPatronByPatronId(NOT_FOUND_PATRON_ID);
    }

    // negative path
    @Test
    public void whenValidPatronId_InvalidLoanIdPUT_thenThrowNotFound() {
        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        var loanRequestModel = LoanRequestModel.builder()
                .status(LoanStatus.RETURNED)
                .bookISBN(bookISBNList)
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.updateLoanForPatron("e5913a79-9b1e-4516-9ffd-06578e7af261",
                loanRequestModel, loan1.getLoanIdentifier().getLoanId()));
        verify(loanRepository, times(1)).findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId());
    }

    // positive path
    @Test
    public void whenValidPatronIdAndLoanId_DELETE_thenDeleteLoan() {

        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(loan1);

        //act
        loanService.deleteLoanForPatron(patronModel.getPatronId(), loan1.getLoanIdentifier().getLoanId());

        //assert
        verify(loanRepository, times(1)).delete(loan1);
    }

    // negative path
    @Test
     public void whenInvalidPatronId_DELETE_thenThrowNotFound() {

        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
                .author(Author.builder()
                        .firstName("Robert")
                        .lastName("Martin")
                        .build())
                .build();

        List<BookModel> bookModelList = new ArrayList<>();
        bookModelList.add(0, bookModel1);
        bookModelList.add(1, bookModel2);

        List<Long> bookISBNList = new ArrayList<>();
        bookISBNList.add(0, bookModel1.getIsbn());
        bookISBNList.add(1, bookModel2.getIsbn());

        var patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();

        var fineModel = FineModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5080")
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(patronServiceClient.getPatronByPatronId(NOT_FOUND_PATRON_ID)).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.deleteLoanForPatron(NOT_FOUND_PATRON_ID,
                loan1.getLoanIdentifier().getLoanId()));
        verify(patronServiceClient, times(1)).getPatronByPatronId(NOT_FOUND_PATRON_ID);
        verify(loanRepository, times(0)).delete(loan1);
    }

    // negative path
    @Test
    public void whenInvalidLoanId_DELETE_thenThrowNotFound() {

        //arrange
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

        var bookModelUpdate1 = BookModel.builder()
                .isbn(9789390183522L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("The Great Gatsby")
                .collection("F. Scott Fitzgerald")
                .status(Status.BORROWED)
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

        var bookModelUpdate2 = BookModel.builder()
                .isbn(9780132350882L)
                .catalogId("d846a5a7-2e1c-4c79-809c-4f3f471e826d")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .collection("Software Development")
                .status(Status.BORROWED)
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
                .amount(new BigDecimal("0.00"))
                .build();

        // loan objects
        var loanIdentifier1 = new LoanIdentifier();
        var loan1 = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();


        var updatedLoan = Loan.builder()
                .loanIdentifier(loanIdentifier1)
                .patronModel(patronModel)
                .fineModel(fineModel)
                .books(bookModelList)
                .status(LoanStatus.ACTIVE)
                .borrowedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(21))
                .build();

        //define mock behaviors
        when(patronServiceClient.getPatronByPatronId(patronModel.getPatronId())).thenReturn(patronModel);
        when(fineServiceClient.getFineByFineId(fineModel.getFineId())).thenReturn(fineModel);
        when(catalogServiceClient.getBookByIsbn(bookModel1.getIsbn())).thenReturn(bookModel1);
        when(catalogServiceClient.getBookByIsbn(bookModel2.getIsbn())).thenReturn(bookModel2);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate1.getIsbn(), bookModelUpdate1)).thenReturn(bookModelUpdate1);
        when(catalogServiceClient.patchBookByIsbn(bookModelUpdate2.getIsbn(), bookModelUpdate2)).thenReturn(bookModelUpdate2);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan1, updatedLoan);
        when(loanRepository.findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId())).thenReturn(null);

        //act and assert
        assertThrows(NotFoundException.class, () -> loanService.deleteLoanForPatron(patronModel.getPatronId(),
                loan1.getLoanIdentifier().getLoanId()));
        verify(loanRepository, times(1)).findLoanByLoanIdentifier_LoanId(loan1.getLoanIdentifier().getLoanId());
        verify(loanRepository, times(0)).delete(loan1);
    }

}