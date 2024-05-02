package com.library.patrons.datamapperlayer;

import com.library.patrons.datalayer.Address;
import com.library.patrons.datalayer.Patron;
import com.library.patrons.datalayer.PatronIdentifier;
import com.library.patrons.presentationlayer.PatronRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatronRequestMapper {

    @Mapping(target = "id", ignore = true)
    Patron requestModelToEntity(PatronRequestModel patronRequestModel,
                                PatronIdentifier patronIdentifier,
                                Address address);
}
