package com.library.apigateway.mapperlayer.patrons;

import com.library.apigateway.presentationlayer.patrons.PatronController;
import com.library.apigateway.presentationlayer.patrons.PatronResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PatronResponseMapper {

    PatronResponseModel responseModelToResponseModel(PatronResponseModel patronResponseModel);

    List<PatronResponseModel> responseModelListToResponseModelList(List<PatronResponseModel> patronResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget PatronResponseModel patronResponseModel) {
        //self link
        Link selfLink = linkTo(methodOn(PatronController.class)
                .getPatron(patronResponseModel.getPatronId()))
                .withSelfRel();
        patronResponseModel.add(selfLink);

        // all patrons link
        Link patronsLink = linkTo(methodOn(PatronController.class)
                .getAllPatrons())
                .withRel("All patrons");
        patronResponseModel.add(patronsLink);
    }
}
