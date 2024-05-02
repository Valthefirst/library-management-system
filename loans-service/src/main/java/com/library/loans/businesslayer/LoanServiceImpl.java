package com.library.loans.businesslayer;

import com.library.loans.datalayer.Loan;
import com.library.loans.datalayer.LoanRepository;
import com.library.loans.datamapperlayer.LoanRequestMapper;
import com.library.loans.datamapperlayer.LoanResponseMapper;
import com.library.loans.domainclientlayer.catalogs.BookModel;
import com.library.loans.domainclientlayer.catalogs.CatalogServiceClient;
import com.library.loans.domainclientlayer.catalogs.Status;
import com.library.loans.domainclientlayer.fines.FineServiceClient;
import com.library.loans.domainclientlayer.patrons.PatronServiceClient;
import com.library.loans.presentationlayer.LoanRequestModel;
import com.library.loans.presentationlayer.LoanResponseModel;
import com.library.loans.utils.exceptions.NotFoundException;
import com.library.loans.utils.exceptions.UnavailableBookException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;
    private final PatronServiceClient patronServiceClient;
    private final CatalogServiceClient catalogServiceClient;
    private final FineServiceClient fineServiceClient;
    private final LoanResponseMapper loanResponseMapper;
    private final LoanRequestMapper loanRequestMapper;

    public LoanServiceImpl(LoanRepository loanRepository, PatronServiceClient patronServiceClient, CatalogServiceClient catalogServiceClient, FineServiceClient fineServiceClient, LoanResponseMapper loanResponseMapper, LoanRequestMapper loanRequestMapper) {
        this.loanRepository = loanRepository;
        this.patronServiceClient = patronServiceClient;
        this.catalogServiceClient = catalogServiceClient;
        this.fineServiceClient = fineServiceClient;
        this.loanResponseMapper = loanResponseMapper;
        this.loanRequestMapper = loanRequestMapper;
    }

    @Override
    public List<LoanResponseModel> getAllLoansForPatron(String patronId) {
        if (patronServiceClient.getPatronByPatronId(patronId) == null) {
            throw new NotFoundException("Invalid patronId: " + patronId);
        }
        return loanResponseMapper.entityListToResponseModelList(
                loanRepository.findLoansByPatronModel_PatronId(patronId));
    }

    @Override
    public LoanResponseModel getLoanForPatron(String patronId, String loanId) {
        if (patronServiceClient.getPatronByPatronId(patronId) == null) {
            throw new NotFoundException("Invalid patronId: " + patronId);
        }

        Loan loan = loanRepository.findLoanByLoanIdentifier_LoanId(loanId);
        if (loan == null) {
            throw new NotFoundException("Invalid loanId: " + loanId);
        }

        if (loan.getFineModel().getFineId() == null) {
            loan.getFineModel().setFineId(null);
        }
        return loanResponseMapper.entityToResponseModel(
                loanRepository.findLoanByLoanIdentifier_LoanId(loanId));
    }

//    @Override
//    public LoanResponseModel addLoanForPatron(String patronId, LoanRequestModel loanRequestModel) {
//        if (patronServiceClient.getPatronByPatronId(patronId) == null) {
//            throw new NotFoundException("Invalid patronId: " + patronId);
//        }
////        String catalogId;
//
//        loanRequestModel.getBookISBN().forEach(isbn -> {
//            String catalogId = catalogServiceClient.getBookByCatalogIdAndIsbn();
//            String catalog = book.getCatalog();
//            BookModel bookObject = catalogServiceClient.getBookByCatalogIdAndIsbn(catalog, isbn);
//
//            if (bookObject == null) {
//                throw new NotFoundException("Invalid ISBN: " + isbn);
//            }
//
//            switch (bookObject.getStatus()) {
//                case BORROWED:
//                    throw new UnavailableBookException("Book with ISBN: " + isbn + " is already borrowed");
//                case LOST:
//                    throw new UnavailableBookException("Book with ISBN: " + isbn + " is lost");
//                case DAMAGED:
//                    throw new UnavailableBookException("Book with ISBN: " + isbn + " is damaged");
//            }
//
//            bookObject.setStatus(Status.BORROWED);
//        });
//
//        Loan loan = loanRequestMapper.requestModelToEntity(loanRequestModel,
//                new LoanIdentifier(),
//                new PatronIdentifier(patronId), null);
//        loan.setBorrowedDate(LocalDate.now());
//        loan.setDueDate(LocalDate.now().plusDays(21));
//        return loanResponseMapper.entityToResponseModel(loanRepository.save(loan));
//    }

//    @Override
//    public LoanResponseModel updateLoanForPatron(String patronId, LoanRequestModel loanRequestModel, String loanId) {
//        if (patronRepository.findByPatronIdentifier_PatronId(patronId) == null) {
//            throw new NotFoundException("Invalid patronId: " + patronId);
//        }
//        Loan loan = loanRepository.findByLoanIdentifier_LoanId(loanId);
//        if (loan == null) {
//            throw new NotFoundException("Invalid loanId: " + loanId);
//        }
//
//        Loan updatedLoan;
//        if (loanRequestModel.getFineId() == null) {
//            updatedLoan = loanRequestMapper.requestModelToEntity(loanRequestModel,
//                    loan.getLoanIdentifier(), new PatronIdentifier(patronId), null);
//        }
//        else
//        {
//            updatedLoan = loanRequestMapper.requestModelToEntity(loanRequestModel,
//                    loan.getLoanIdentifier(), new PatronIdentifier(patronId), new FineIdentifier(loanRequestModel.getFineId()));
//        }
//        updatedLoan.setId(loan.getId());
//        updatedLoan.setBorrowedDate(loan.getBorrowedDate());
//        if (loanRequestModel.getDueDate() == null)
//            updatedLoan.setDueDate(loan.getDueDate());
//        else
//            updatedLoan.setDueDate(loanRequestModel.getDueDate());
//        updatedLoan.setReturnedDate(loan.getReturnedDate());
//
//        LoanStatus previousStatus = loan.getStatus();
//        if (previousStatus != LoanStatus.RETURNED && loanRequestModel.getStatus() == LoanStatus.RETURNED) {
//            updatedLoan.getBooks().forEach(book -> {
//                Long isbn = book.getIsbn();
//                Book bookObject = bookRepository.findByIsbn_Isbn(isbn);
//                bookObject.setStatus(Status.AVAILABLE);
//            });
//            if (loan.getReturnedDate() == null) {
//                updatedLoan.setReturnedDate(LocalDate.now());
//            }
//            long days = ChronoUnit.DAYS.between(loan.getDueDate(), updatedLoan.getReturnedDate());
//            int numBooks = updatedLoan.getBooks().size();
//            if (days > 0) {
//                Fine lateFine = new Fine(BigDecimal.valueOf(0.25 * days * numBooks), "Late return", false);
//                updatedLoan.setFineIdentifier(lateFine.getFineIdentifier());
//                fineRepository.save(lateFine);
//            }
//        }
//        return loanResponseMapper.entityToResponseModel(loanRepository.save(updatedLoan));
//    }

    @Override
    public void deleteLoanForPatron(String patronId, String loanId) {
        if (patronServiceClient.getPatronByPatronId(patronId) == null) {
            throw new NotFoundException("Invalid patronId: " + patronId);
        }
        Loan loan = loanRepository.findLoanByLoanIdentifier_LoanId(loanId);
        if (loan == null) {
            throw new NotFoundException("Invalid loanId: " + loanId);
        }
        loanRepository.delete(loan);
    }
}
