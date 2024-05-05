package com.library.patrons.datamapperlayer;

import com.library.patrons.datalayer.Patron;
import com.library.patrons.presentationlayer.PatronResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatronResponseMapper {

    @Mapping(expression = "java(patron.getPatronIdentifier().getPatronId())", target = "patronId")
    @Mapping(expression = "java(patron.getAddress().getStreetAddress())", target = "streetAddress")
    @Mapping(expression = "java(patron.getAddress().getCity())", target = "city")
    @Mapping(expression = "java(patron.getAddress().getProvince())", target = "province")
    @Mapping(expression = "java(patron.getAddress().getCountry())", target = "country")
    @Mapping(expression = "java(patron.getAddress().getPostalCode())", target = "postalCode")
    PatronResponseModel entityToResponseModel(Patron patron);

    List<PatronResponseModel> entityListToResponseModelList(List<Patron> patrons);
}
