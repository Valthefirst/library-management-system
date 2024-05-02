package com.library.apigateway.mapperlayer.fines;

import com.library.apigateway.presentationlayer.fines.FineController;
import com.library.apigateway.presentationlayer.fines.FineResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface FineResponseMapper {

    FineResponseModel responseModelToResponseModel(FineResponseModel fineResponseModel);

    List<FineResponseModel> responseModelListToResponseModelList(List<FineResponseModel> fineResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget FineResponseModel fineResponseModel) {
        //self link
        Link selfLink = linkTo(methodOn(FineController.class)
                .getFine(fineResponseModel.getFineId()))
                .withSelfRel();
        fineResponseModel.add(selfLink);

        // all patrons link
        Link finesLink = linkTo(methodOn(FineController.class)
                .getAllFines())
                .withRel("All fines");
        fineResponseModel.add(finesLink);
    }
}
