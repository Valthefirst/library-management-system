package com.library.fines.datamapperlayer;

import com.library.fines.datalayer.Fine;
import com.library.fines.datalayer.FineIdentifier;
import com.library.fines.presentationlayer.FineRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FineRequestMapper {

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "fineId", source = "fineIdentifier")
//    @Mapping(expression = "java(patronIdentifier)", target = "patronIdentifier")
    Fine requestModelToEntity(FineRequestModel fineRequestModel,
                              FineIdentifier fineIdentifier);
}
