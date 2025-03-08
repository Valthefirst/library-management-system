package com.library.apigateway.businesslayer.patrons;

import com.library.apigateway.domainclientlayer.patrons.ContactMethodPreference;
import com.library.apigateway.domainclientlayer.patrons.PatronServiceClient;
import com.library.apigateway.domainclientlayer.patrons.PhoneNumber;
import com.library.apigateway.domainclientlayer.patrons.PhoneType;
import com.library.apigateway.mapperlayer.patrons.PatronResponseMapper;
import com.library.apigateway.presentationlayer.patrons.PatronRequestModel;
import com.library.apigateway.presentationlayer.patrons.PatronResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude = org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
@ActiveProfiles("test")
class PatronServiceUnitTest {

    @Autowired
    PatronService patronService;

    @MockBean
    PatronServiceClient patronServiceClient;

    @SpyBean
    PatronResponseMapper patronResponseMapper;

    private final String PATRON_ID = "e5913a79-9b1e-4516-9ffd-06578e7af261";
    private final String PATRON_EMAIL = "vchawner0@phoca.cz";

    private PatronRequestModel buildPatronRequestModel() {
        var phoneNumber1 = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        var phoneNumber2 = new PhoneNumber(PhoneType.MOBILE, "514-555-4444");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(0, phoneNumber1);
        phoneNumbers.add(1, phoneNumber2);

        return PatronRequestModel.builder()
                .firstName("Vilma")
                .lastName("Chawner")
                .emailAddress(PATRON_EMAIL)
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
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .emailAddress(PATRON_EMAIL)
                .contactMethodPreference(ContactMethodPreference.MAIL)
                .streetAddress("8452 Anhalt Park")
                .city("Chambly")
                .province("Québec")
                .country("Canada")
                .postalCode("J3L 5Y6")
                .phoneNumbers(phoneNumbers)
                .build();
    }

    // positive test
    @Test
    public void whenPatronsExists_thenReturnPatronList() {
        // arrange
        var phoneNumber1 = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        var phoneNumber2 = new PhoneNumber(PhoneType.MOBILE, "514-555-4444");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(0, phoneNumber1);
        phoneNumbers.add(1, phoneNumber2);

        var patronModel1 = PatronResponseModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .emailAddress(PATRON_EMAIL)
                .contactMethodPreference(ContactMethodPreference.MAIL)
                .streetAddress("8452 Anhalt Park")
                .city("Chambly")
                .province("Québec")
                .country("Canada")
                .postalCode("J3L 5Y6")
                .phoneNumbers(phoneNumbers)
                .build();

        var phoneNumber3 = new PhoneNumber(PhoneType.HOME, "438-555-5555");
        var phoneNumber4 = new PhoneNumber(PhoneType.MOBILE, "438-555-4444");
        List<PhoneNumber> phoneNumbers2 = new ArrayList<>();
        phoneNumbers2.add(0, phoneNumber3);
        phoneNumbers2.add(1, phoneNumber4);

        var patronModel2 = PatronResponseModel.builder()
                .patronId("c3540a89-cb47-4c96-888e-ff96708db4d8")
                .firstName("Alick")
                .lastName("Ucceli")
                .emailAddress("aucceli0@dot.gov")
                .contactMethodPreference(ContactMethodPreference.EMAIL)
                .streetAddress("73 Shoshone Road")
                .city("Barraute")
                .province("Québec")
                .country("Canada")
                .postalCode("P0M 2T6")
                .phoneNumbers(phoneNumbers2)
                .build();

        when(patronServiceClient.getAllPatrons()).thenReturn(List.of(patronModel1, patronModel2));

        // act
        List<PatronResponseModel> patronResponseModels = patronService.getAllPatrons();

        // assert
        assertNotNull(patronResponseModels);
        assertEquals(2, patronResponseModels.size());
        verify(patronServiceClient, times(1)).getAllPatrons();

        for (PatronResponseModel patronResponseModel : patronResponseModels) {
            assertNotNull(patronResponseModel.getPatronId());
            assertNotNull(patronResponseModel.getFirstName());
            assertNotNull(patronResponseModel.getLastName());
            assertNotNull(patronResponseModel.getEmailAddress());
            assertNotNull(patronResponseModel.getContactMethodPreference());
            assertNotNull(patronResponseModel.getStreetAddress());
            assertNotNull(patronResponseModel.getCity());
            assertNotNull(patronResponseModel.getProvince());
            assertNotNull(patronResponseModel.getCountry());
            assertNotNull(patronResponseModel.getPostalCode());
            assertNotNull(patronResponseModel.getPhoneNumbers());
        }
    }

    // negative test
    @Test
    public void whenNoPatronExists_thenReturnEmptyList() {
        // arrange
        when(patronServiceClient.getAllPatrons()).thenReturn(new ArrayList<>());

        // act
        List<PatronResponseModel> patronResponseModels = patronService.getAllPatrons();

        // assert
        assertNotNull(patronResponseModels);
        assertEquals(0, patronResponseModels.size());
        verify(patronServiceClient, times(1)).getAllPatrons();
    }

    // positive test
    @Test
    public void whenPatronExists_thenReturnPatron() {
        // arrange
        var phoneNumber1 = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        var phoneNumber2 = new PhoneNumber(PhoneType.MOBILE, "514-555-4444");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(0, phoneNumber1);
        phoneNumbers.add(1, phoneNumber2);

        var patronModel = PatronResponseModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .emailAddress(PATRON_EMAIL)
                .contactMethodPreference(ContactMethodPreference.MAIL)
                .streetAddress("8452 Anhalt Park")
                .city("Chambly")
                .province("Québec")
                .country("Canada")
                .postalCode("J3L 5Y6")
                .phoneNumbers(phoneNumbers)
                .build();

        when(patronServiceClient.getPatronByPatronId(PATRON_ID)).thenReturn(patronModel);

        // act
        PatronResponseModel patronResponseModel = patronService.getPatron(PATRON_ID);

        // assert
        assertNotNull(patronResponseModel);
        assertEquals(PATRON_ID, patronResponseModel.getPatronId());
        assertEquals("Vilma", patronResponseModel.getFirstName());
        assertEquals("Chawner", patronResponseModel.getLastName());
        assertEquals(PATRON_EMAIL, patronResponseModel.getEmailAddress());
        assertEquals(ContactMethodPreference.MAIL, patronResponseModel.getContactMethodPreference());
        assertEquals("8452 Anhalt Park", patronResponseModel.getStreetAddress());
        assertEquals("Chambly", patronResponseModel.getCity());
        assertEquals("Québec", patronResponseModel.getProvince());
        assertEquals("Canada", patronResponseModel.getCountry());
        assertEquals("J3L 5Y6", patronResponseModel.getPostalCode());
        assertEquals("514-555-5555", patronResponseModel.getPhoneNumbers().get(0).getNumber());
        assertEquals("514-555-4444", patronResponseModel.getPhoneNumbers().get(1).getNumber());
        verify(patronServiceClient, times(1)).getPatronByPatronId(PATRON_ID);
    }

//    @SpyBean
//    PatronResponseMapper patronResponseMapper;
//
//    // Example usage in a test
//    @Test
//    public void testMapping() {
//        // Arrange
//        PatronRequestModel requestModel = buildPatronRequestModel();
//        PatronResponseModel responseModel = buildPatronResponseModel();
//
//        // Act
//        PatronResponseModel mappedResponse = patronResponseMapper.toResponseModel(requestModel);
//
//        // Assert
//        assertNotNull(mappedResponse);
//        verify(patronResponseMapper, times(1)).toResponseModel(requestModel);
//    }

    // negative test
    @Test
    public void whenNoPatronExists_thenReturnNull() {
        // arrange
        when(patronServiceClient.getPatronByPatronId(PATRON_ID)).thenReturn(null);

        // act
        PatronResponseModel patronResponseModel = patronService.getPatron(PATRON_ID);

        // assert
        assertNull(patronResponseModel);
        verify(patronServiceClient, times(1)).getPatronByPatronId(PATRON_ID);
    }

    // positive test
    @Test
    public void whenPatronCreated_thenReturnPatron() {
        // arrange
        PatronResponseModel responseModel = new PatronResponseModel();

        when(patronServiceClient.postPatron(any(PatronRequestModel.class))).thenReturn(buildPatronResponseModel());

        // act
        PatronResponseModel patronResponseModel = patronService.addPatron(buildPatronRequestModel());

        // assert
        assertNotNull(patronResponseModel);
        assertNotNull(patronResponseModel.getPatronId());
        assertEquals(buildPatronRequestModel().getFirstName(), patronResponseModel.getFirstName());
        assertEquals(buildPatronRequestModel().getLastName(), patronResponseModel.getLastName());
        assertEquals(buildPatronRequestModel().getEmailAddress(), patronResponseModel.getEmailAddress());
        assertEquals(buildPatronRequestModel().getContactMethodPreference(), patronResponseModel
                .getContactMethodPreference().toString());
        assertEquals(buildPatronRequestModel().getStreetAddress(), patronResponseModel.getStreetAddress());
        assertEquals(buildPatronRequestModel().getCity(), patronResponseModel.getCity());
        assertEquals(buildPatronRequestModel().getProvince(), patronResponseModel.getProvince());
        assertEquals(buildPatronRequestModel().getCountry(), patronResponseModel.getCountry());
        assertEquals(buildPatronRequestModel().getPostalCode(), patronResponseModel.getPostalCode());
        assertEquals(buildPatronRequestModel().getPhoneNumbers().get(0).getNumber(), patronResponseModel
                .getPhoneNumbers().get(0).getNumber());
        assertEquals(buildPatronRequestModel().getPhoneNumbers().get(1).getNumber(), patronResponseModel
                .getPhoneNumbers().get(1).getNumber());
        verify(patronServiceClient, times(1)).postPatron(any(PatronRequestModel.class));
    }

    // positive test
    @Test
    public void whenPatronUpdated_thenReturnPatron() {
        // arrange
        PatronResponseModel responseModel = new PatronResponseModel();

        when(patronServiceClient.putPatronByPatronId(anyString(), any(PatronRequestModel.class)))
                .thenReturn(buildPatronResponseModel());

        // act
        PatronResponseModel patronResponseModel = patronService.updatePatron(buildPatronRequestModel(), PATRON_ID);

        // assert
        assertNotNull(patronResponseModel);
        verify(patronServiceClient, times(1)).putPatronByPatronId(anyString(),
                any(PatronRequestModel.class));
    }

    // positive test
    @Test
    public void whenPatronDeleted_thenNoContent() {
        // arrange
        doNothing().when(patronServiceClient).deletePatronByPatronId(PATRON_ID);

        // act
        patronService.removePatron(PATRON_ID);

        // assert
        verify(patronServiceClient, times(1)).deletePatronByPatronId(PATRON_ID);
    }

}