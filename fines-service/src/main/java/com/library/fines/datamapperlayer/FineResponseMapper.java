package com.library.fines.datamapperlayer;

import com.library.fines.datalayer.Fine;
import com.library.fines.presentationlayer.FineResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FineResponseMapper {

    @Mapping(expression = "java(fine.getFineIdentifier() != null ? fine.getFineIdentifier().getFineId() : null)",
            target = "fineId")
//    @Mapping(expression = "java(patron.getPatronIdentifier().getPatronId())", target = "patronId")
    FineResponseModel entityToResponseModel(Fine fine);

    List<FineResponseModel> entityListToResponseModelList(List<Fine> fines);

//    @AfterMapping
//    default void addLinks(@MappingTarget FineResponseModel model, Fine fine) {
//        //self link
//        Link selfLink = linkTo(methodOn(FineController.class)
//                .getFine(model.getFineId()))
//                .withSelfRel();
//        model.add(selfLink);
//
//        // all patrons link
//        Link finesLink = linkTo(methodOn(FineController.class)
//                .getAllFines())
//                .withRel("fines");
//        model.add(finesLink);
//    }
}
