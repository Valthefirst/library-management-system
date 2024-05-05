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
}
