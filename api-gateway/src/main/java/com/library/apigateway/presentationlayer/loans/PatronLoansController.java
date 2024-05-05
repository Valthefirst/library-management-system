package com.library.apigateway.presentationlayer.loans;

import com.library.apigateway.businesslayer.loans.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/patrons/{patronId}/loans")
public class PatronLoansController {

    private final LoanService loanService;

    public PatronLoansController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<LoanResponseModel>> getAllLoansForPatron(@PathVariable String patronId) {
        return ResponseEntity.ok().body(loanService.getAllLoansForPatron(patronId));
    }

    @GetMapping(value = "{loanId}", produces = "application/json")
    public ResponseEntity<LoanResponseModel> getLoanForPatron(@PathVariable String patronId,
                                                              @PathVariable String loanId) {
        return ResponseEntity.ok().body(loanService.getLoanForPatron(patronId, loanId));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoanResponseModel> addLoanForPatron(@PathVariable String patronId,
                                                              @RequestBody LoanRequestModel loanRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.addLoanForPatron(patronId, loanRequestModel));
    }

    @PutMapping(consumes = "application/json", value = "{loanId}", produces = "application/json")
    public ResponseEntity<LoanResponseModel> updateLoanForPatron(@PathVariable String patronId,
                                                                 @RequestBody LoanRequestModel loanRequestModel,
                                                               @PathVariable String loanId) {
        return ResponseEntity.ok().body(loanService.updateLoanForPatron(patronId, loanId, loanRequestModel));
    }

    @DeleteMapping("{loanId}")
    public ResponseEntity<Void> deleteLoanForPatron(@PathVariable String patronId, @PathVariable String loanId) {
        loanService.deleteLoanForPatron(patronId, loanId);
        return ResponseEntity.noContent().build();
    }
}
