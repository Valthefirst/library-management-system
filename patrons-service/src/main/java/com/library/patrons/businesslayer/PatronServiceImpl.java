package com.library.patrons.businesslayer;

import com.library.patrons.datalayer.Address;
import com.library.patrons.datalayer.Patron;
import com.library.patrons.datalayer.PatronIdentifier;
import com.library.patrons.datalayer.PatronRepository;
import com.library.patrons.datamapperlayer.PatronRequestMapper;
import com.library.patrons.datamapperlayer.PatronResponseMapper;
import com.library.patrons.presentationlayer.PatronRequestModel;
import com.library.patrons.presentationlayer.PatronResponseModel;
import com.library.patrons.utils.exceptions.InvalidEmailException;
import com.library.patrons.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PatronServiceImpl implements PatronService{

    private final PatronRepository patronRepository;
    private final PatronResponseMapper patronResponseMapper;
    private final PatronRequestMapper patronRequestMapper;

    public PatronServiceImpl(PatronRepository patronRepository, PatronResponseMapper patronResponseMapper, PatronRequestMapper patronRequestMapper) {
        this.patronRepository = patronRepository;
        this.patronResponseMapper = patronResponseMapper;
        this.patronRequestMapper = patronRequestMapper;
    }

    @Override
    public List<PatronResponseModel> getAllPatrons() {
        List<Patron> patronList = patronRepository.findAll();
        return patronResponseMapper.entityListToResponseModelList(patronList);
    }

    @Override
    public PatronResponseModel getPatron(String patronId) {
        Patron foundPatron = patronRepository.findByPatronIdentifier_PatronId(patronId);
        if (foundPatron == null) {
            throw new NotFoundException("Unknown patronId: " + patronId);
        }
        return patronResponseMapper.entityToResponseModel(foundPatron);
    }

    @Override
    public PatronResponseModel addPatron(PatronRequestModel patronRequestModel) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(patronRequestModel.getEmailAddress());
        if (!matcher.matches()) {
            throw new InvalidEmailException("Email address is invalid");
        }

        Address address = new Address(patronRequestModel.getStreetAddress(), patronRequestModel.getCity(),
                patronRequestModel.getProvince(), patronRequestModel.getCountry(), patronRequestModel.getPostalCode());

        Patron patron = patronRequestMapper.requestModelToEntity(patronRequestModel, new PatronIdentifier(), address);
        patron.setAddress(address);
        return patronResponseMapper.entityToResponseModel(patronRepository.save(patron));
    }

    @Override
    public PatronResponseModel updatePatron(PatronRequestModel patronRequestModel, String patronId) {
        Patron foundPatron = patronRepository.findByPatronIdentifier_PatronId(patronId);
        if (foundPatron == null) {
            throw new NotFoundException("Unknown patronId: " + patronId);
        }

        Address address = new Address(patronRequestModel.getStreetAddress(), patronRequestModel.getCity(),
                patronRequestModel.getProvince(), patronRequestModel.getCountry(), patronRequestModel.getPostalCode());

        Patron updatedPatron = patronRequestMapper.requestModelToEntity(patronRequestModel,
                foundPatron.getPatronIdentifier(), address);
        updatedPatron.setId(foundPatron.getId());
        return patronResponseMapper.entityToResponseModel(patronRepository.save(updatedPatron));
    }

    @Override
    public void removePatron(String patronId) {
        Patron foundPatron = patronRepository.findByPatronIdentifier_PatronId(patronId);
        if (foundPatron == null) {
            throw new NotFoundException("Unknown patronId: " + patronId);
        }
        patronRepository.delete(foundPatron);
    }
}
