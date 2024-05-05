package com.library.patrons.presentationlayer;

import com.library.patrons.businesslayer.PatronService;
import com.library.patrons.datalayer.PhoneNumber;
import com.library.patrons.datalayer.PhoneType;
import com.library.patrons.utils.exceptions.InvalidEmailException;
import com.library.patrons.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.library.patrons.datalayer.ContactMethodPreference.MAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PatronController.class)
class PatronControllerUnitTest {

    private final String FOUND_PATRON_ID = "e5913a79-9b1e-4516-9ffd-06578e7af261";
    private final String FOUND_PATRON_EMAIL = "vchawner0@phoca.cz";
    private final String NOT_FOUND_PATRON_ID = "ef23ab6e-d614-47b9-95d0-d66167ae5082";
    private final String INVALID_PATRON_EMAIL = "HVgjhVHV,";

    @Autowired
    private PatronController patronController;

    @MockBean
    private PatronService patronService;

    private PatronRequestModel buildPatronRequestModel() {
        var phoneNumber1 = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        var phoneNumber2 = new PhoneNumber(PhoneType.MOBILE, "514-555-4444");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(0, phoneNumber1);
        phoneNumbers.add(1, phoneNumber2);

        return PatronRequestModel.builder()
                .firstName("Vilma")
                .lastName("Chawner")
                .emailAddress(FOUND_PATRON_EMAIL)
                .contactMethodPreference("MAIL")
                .streetAddress("8452 Anhalt Park")
                .city("Chambly")
                .province("Québec")
                .country("Canada")
                .postalCode("J3L 5Y6")
                .phoneNumbers(phoneNumbers)
                .build();
    }

    private PatronRequestModel buildBadPatronRequestModel() {
        var phoneNumber1 = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        var phoneNumber2 = new PhoneNumber(PhoneType.MOBILE, "514-555-4444");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(0, phoneNumber1);
        phoneNumbers.add(1, phoneNumber2);

        return PatronRequestModel.builder()
                .firstName("Vilma")
                .lastName("Chawner")
                .emailAddress(INVALID_PATRON_EMAIL)
                .contactMethodPreference("MAIL")
                .streetAddress("8452 Anhalt Park")
                .city("Chambly")
                .province("Québec")
                .country("Canada")
                .postalCode("J3L 5Y6")
                .phoneNumbers(phoneNumbers)
                .build();
    }

    private PatronResponseModel buildPatronResponseModel() {
        var phoneNumber1 = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        var phoneNumber2 = new PhoneNumber(PhoneType.MOBILE, "514-555-4444");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(0, phoneNumber1);
        phoneNumbers.add(1, phoneNumber2);

        return PatronResponseModel.builder()
                .patronId(FOUND_PATRON_ID)
                .firstName("Vilma")
                .lastName("Chawner")
                .emailAddress(FOUND_PATRON_EMAIL)
                .contactMethodPreference(MAIL)
                .streetAddress("8452 Anhalt Park")
                .city("Chambly")
                .province("Québec")
                .country("Canada")
                .postalCode("J3L 5Y6")
                .phoneNumbers(phoneNumbers)
                .build();
    }

    // positive test case
    @Test
    public void whenPatronsExists_thenReturnPatronList() {
        // arrange
        PatronResponseModel patronResponseModel = buildPatronResponseModel();

        when(patronService.getAllPatrons()).thenReturn(Collections.singletonList(patronResponseModel));

        // act
        ResponseEntity<List<PatronResponseModel>> responseEntity = patronController.getAllPatrons();

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(patronResponseModel, responseEntity.getBody().get(0));
        verify(patronService, times(1)).getAllPatrons();
    }

    // negative test case
    @Test
    public void whenNoPatronExists_thenReturnEmptyList() {
        // arrange
        when(patronService.getAllPatrons()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<PatronResponseModel>> responseEntity = patronController.getAllPatrons();

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());
        verify(patronService, times(1)).getAllPatrons();
    }

    // positive test case
    @Test
    public void whenPatronExists_thenReturnPatron() {
        // arrange
        PatronResponseModel patronResponseModel = buildPatronResponseModel();

        when(patronService.getPatron(FOUND_PATRON_ID)).thenReturn(patronResponseModel);

        // act
        ResponseEntity<PatronResponseModel> responseEntity = patronController.getPatron(FOUND_PATRON_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(patronResponseModel, responseEntity.getBody());
        verify(patronService, times(1)).getPatron(FOUND_PATRON_ID);
    }

    // negative test case
    @Test
    public void whenPatronDoesNotExist_thenReturnNotFound() {
        // arrange
        when(patronService.getPatron(NOT_FOUND_PATRON_ID)).thenThrow(new NotFoundException("Unknown patronId: " +
                NOT_FOUND_PATRON_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            patronController.getPatron(NOT_FOUND_PATRON_ID);
        });

        // assert
        assertEquals("Unknown patronId: " + NOT_FOUND_PATRON_ID, exception.getMessage());
        verify(patronService, times(1)).getPatron(NOT_FOUND_PATRON_ID);
    }

    // positive test case
    @Test
    public void whenPatronCreated_thenReturnPatron() {
        // arrange
        PatronRequestModel patronRequestModel = buildPatronRequestModel();
        PatronResponseModel patronResponseModel = buildPatronResponseModel();

        when(patronService.addPatron(patronRequestModel)).thenReturn(patronResponseModel);

        // act
        ResponseEntity<PatronResponseModel> responseEntity = patronController.addPatron(patronRequestModel);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(patronResponseModel, responseEntity.getBody());
        verify(patronService, times(1)).addPatron(patronRequestModel);
    }

    // negative test case
    @Test
    public void whenInvalidPatron_thenThrowException() {
        // arrange
        PatronRequestModel patronRequestModel = buildBadPatronRequestModel();
        when(patronService.addPatron(patronRequestModel)).thenThrow(new InvalidEmailException("Email address is invalid"));

        // act
        InvalidEmailException exception = assertThrowsExactly(InvalidEmailException.class, () -> {
            patronController.addPatron(patronRequestModel);
        });

        // assert
        assertEquals("Email address is invalid", exception.getMessage());
        verify(patronService, times(1)).addPatron(patronRequestModel);
    }

    // positive test case
    @Test
    public void whenPatronUpdated_thenReturnPatron() {
        // arrange
        PatronRequestModel patronRequestModel = buildPatronRequestModel();
        PatronResponseModel patronResponseModel = buildPatronResponseModel();

        when(patronService.updatePatron(patronRequestModel, FOUND_PATRON_ID)).thenReturn(patronResponseModel);

        // act
        ResponseEntity<PatronResponseModel> responseEntity = patronController.updatePatron(FOUND_PATRON_ID, patronRequestModel);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(patronResponseModel, responseEntity.getBody());
        verify(patronService, times(1)).updatePatron(patronRequestModel, FOUND_PATRON_ID);
    }

    // negative test case
    @Test
    public void whenInvalidPatronIdForUpdate_thenReturnNotFound() {
        // arrange
        PatronRequestModel patronRequestModel = buildPatronRequestModel();
        when(patronService.updatePatron(patronRequestModel, NOT_FOUND_PATRON_ID))
                .thenThrow(new NotFoundException("Unknown patronId: " + NOT_FOUND_PATRON_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            patronController.updatePatron(NOT_FOUND_PATRON_ID, patronRequestModel);
        });

        // assert
        assertEquals("Unknown patronId: " + NOT_FOUND_PATRON_ID, exception.getMessage());
        verify(patronService, times(1)).updatePatron(patronRequestModel, NOT_FOUND_PATRON_ID);
    }

    // positive test case
    @Test
    public void whenPatronDeleted_thenNoContent() {
        // arrange
        doNothing().when(patronService).removePatron(FOUND_PATRON_ID);

        // act
        ResponseEntity<Void> responseEntity = patronController.removePatron(FOUND_PATRON_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(patronService, times(1)).removePatron(FOUND_PATRON_ID);
    }

    // negative test case
    @Test
    public void whenPatronDoesNotExist_thenNotFound() {
        // arrange
        doThrow(new NotFoundException("Unknown patronId: " + NOT_FOUND_PATRON_ID)).when(patronService).removePatron(NOT_FOUND_PATRON_ID);

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            patronController.removePatron(NOT_FOUND_PATRON_ID);
        });

        // assert
        assertEquals("Unknown patronId: " + NOT_FOUND_PATRON_ID, exception.getMessage());
        verify(patronService, times(1)).removePatron(NOT_FOUND_PATRON_ID);
    }

}