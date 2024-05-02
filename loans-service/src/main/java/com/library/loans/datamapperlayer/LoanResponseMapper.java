package com.library.loans.datamapperlayer;

import com.library.loans.datalayer.Loan;
import com.library.loans.presentationlayer.LoanResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanResponseMapper {

//    PatronRepository patronRepository;

    @Mapping(expression = "java(loan.getLoanIdentifier().getLoanId())", target = "loanId")
    @Mapping(expression = "java(loan.getPatronModel().getPatronId())", target = "patronId")
    @Mapping(expression = "java(loan.getPatronModel().getFirstName())", target = "patronFirstName")
    @Mapping(expression = "java(loan.getPatronModel().getLastName())", target = "patronLastName")
//    @Mapping(expression = "java(loan.getBookModel())", target = "books")
    @Mapping(expression = "java(loan.getFineModel() != null ? loan.getFineModel().getFineId() : null)",
            target = "fineId")
//    @Mapping(expression = "java(patronRepository.findByPatronIdentifier_PatronId(loan.getPatronIdentifier().getPatronId()).getFirstName())", target = "patronFirstName")
//    @Mapping(expression = "java(patronRepository.findByPatronIdentifier_PatronId(loan.getPatronIdentifier().getPatronId()).getLastName())", target = "patronLastName")
    LoanResponseModel entityToResponseModel(Loan loan);

    List<LoanResponseModel> entityListToResponseModelList(List<Loan> books);

//    @AfterMapping
//    default void addLinks(@MappingTarget LoanResponseModel model, Loan loan) {
//        //self link
//        Link selfLink = linkTo(methodOn(PatronLoansController.class)
//                .getLoanForPatron(model.getPatronId(), model.getLoanId()))
//                .withSelfRel();
//        model.add(selfLink);
//
//        // all loans link
//        Link booksLink = linkTo(methodOn(PatronLoansController.class)
//                .getAllLoansForPatron(model.getPatronId()))
//                .withRel("loans for this patron");
//        model.add(booksLink);
////
////        //get patron by id
////        Link catalogueId = linkTo(methodOn(PatronLoansController.class)
////                .getLoanForPatron(model.getCatalogId()))
////                .withRel("catalog");
////        model.add(catalogueId);
//    }
}
