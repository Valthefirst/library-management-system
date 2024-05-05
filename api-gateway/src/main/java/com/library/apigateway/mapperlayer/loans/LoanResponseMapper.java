package com.library.apigateway.mapperlayer.loans;

import com.library.apigateway.presentationlayer.fines.FineController;
import com.library.apigateway.presentationlayer.loans.LoanResponseModel;
import com.library.apigateway.presentationlayer.loans.PatronLoansController;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface LoanResponseMapper {

    LoanResponseModel responseModelToResponseModel(LoanResponseModel loanResponseModel);

    List<LoanResponseModel> responseModelListToResponseModelList(List<LoanResponseModel> loanResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget LoanResponseModel loanResponseModel) {
        //self link
        Link selfLink = linkTo(methodOn(PatronLoansController.class)
                .getLoanForPatron(loanResponseModel.getPatronId(), loanResponseModel.getLoanId()))
                .withSelfRel();
        loanResponseModel.add(selfLink);

        // all loans link
        Link booksLink = linkTo(methodOn(PatronLoansController.class)
                .getAllLoansForPatron(loanResponseModel.getPatronId()))
                .withRel("Loans for this patron");
        loanResponseModel.add(booksLink);

        // all loans link
        Link fineLink = linkTo(methodOn(FineController.class)
                .getFine(loanResponseModel.getFineId()))
                .withRel("Fine for this loan");
        loanResponseModel.add(fineLink);
    }
}
