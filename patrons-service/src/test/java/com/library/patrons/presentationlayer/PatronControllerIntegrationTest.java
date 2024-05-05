package com.library.patrons.presentationlayer;

import com.library.patrons.datalayer.ContactMethodPreference;
import com.library.patrons.datalayer.PatronRepository;
import com.library.patrons.datalayer.PhoneNumber;
import com.library.patrons.datalayer.PhoneType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static com.library.patrons.datalayer.ContactMethodPreference.MAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class PatronControllerIntegrationTest {

    private final String BASE_URI_PATRONS = "api/v1/patrons";
    private final String FOUND_PATRON_ID = "e5913a79-9b1e-4516-9ffd-06578e7af261";
    private final String FOUND_PATRON_FNAME = "Vilma";
    private final String FOUND_PATRON_LNAME = "Chawner";
    private final String FOUND_PATRON_EMAIL = "vchawner0@phoca.cz";
    private final ContactMethodPreference FOUND_PATRON_CONTACT_METHOD = MAIL;
    private final String FOUND_PATRON_STREET = "8452 Anhalt Park";
    private final String FOUND_PATRON_CITY = "Chambly";
    private final String FOUND_PATRON_PROVINCE = "Quebec";
    private final String FOUND_PATRON_COUNTRY = "Canada";
    private final String FOUND_PATRON_POSTAL_CODE = "J3L 5Y6";
    private final String FOUND_PATRON_PHONE_NUMBER1 = "515-555-5555";
    private final String FOUND_PATRON_PHONE_NUMBER2 = "515-555-4444";
    private final String NOT_FOUND_PATRON_ID = "ef23ab6e-d614-47b9-95d0-d66167ae5082";
    private final String INVALID_PATRON_EMAIL = "HVgjhVHV,";

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private WebTestClient webTestClient;

    // positive path
    @Test
    public void whenGetPatrons_thenReturnAllPatrons() {

        // arrange
        long sizeDB = patronRepository.count();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_PATRONS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PatronResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                });
    }

    // negative path
    // empty database, no patrons empty list returned
    @Test
    public void whenGetPatronsEmptyDB_thenReturnEmptyList() {

        // arrange
        patronRepository.deleteAll();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_PATRONS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PatronResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.isEmpty());
                });
    }

    // positive test case
    @Test
    public void whenGetPatronExists_thenReturnPatronByPatronId() {

        // arrange
        var phoneNumber1 = new PhoneNumber(PhoneType.HOME, FOUND_PATRON_PHONE_NUMBER1);
        var phoneNumber2 = new PhoneNumber(PhoneType.MOBILE, FOUND_PATRON_PHONE_NUMBER2);
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(0, phoneNumber1);
        phoneNumbers.add(1, phoneNumber2);

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_PATRONS + "/" + FOUND_PATRON_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PatronResponseModel.class)
                .value((patron) -> {
                    assertNotNull(patron);
                    assertEquals(patron.getPatronId(), FOUND_PATRON_ID);
                    assertEquals(patron.getFirstName(), FOUND_PATRON_FNAME);
                    assertEquals(patron.getLastName(), FOUND_PATRON_LNAME);
                    assertEquals(patron.getEmailAddress(), FOUND_PATRON_EMAIL);
                    assertEquals(patron.getContactMethodPreference(), FOUND_PATRON_CONTACT_METHOD);
                    assertEquals(patron.getStreetAddress(), FOUND_PATRON_STREET);
                    assertEquals(patron.getCity(), FOUND_PATRON_CITY);
                    assertEquals(patron.getProvince(), FOUND_PATRON_PROVINCE);
                    assertEquals(patron.getCountry(), FOUND_PATRON_COUNTRY);
                    assertEquals(patron.getPostalCode(), FOUND_PATRON_POSTAL_CODE);
                    assertEquals(patron.getPhoneNumbers().size(), phoneNumbers.size());
                });
    }

    // negative test case
    @Test
    public void whenGetPatronDoesNotExist_thenReturnNotFound() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_PATRONS + "/" + NOT_FOUND_PATRON_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown patronId: " + NOT_FOUND_PATRON_ID);
    }

    // positive test case
    @Test
    public void whenValidPatron_thenCreatePatron() {

        // arrange
        long sizeDB = patronRepository.count();
        PatronRequestModel patronRequestModel = new PatronRequestModel("Val", "Chase", "val.chase@gmail.com",
                "MAIL", "1234 Elm St", "Des Moines", "Iowa", "USA", "50309",
                List.of(new PhoneNumber(PhoneType.HOME, "515-555-5555"), new PhoneNumber(PhoneType.MOBILE, "515-555-4444")));

        webTestClient.post()
                .uri(BASE_URI_PATRONS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(patronRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PatronResponseModel.class)
                .value((patronResponseModel) -> {
                    assertNotNull(patronResponseModel);
                    assertEquals(patronRequestModel.getFirstName(), patronResponseModel.getFirstName());
                    assertEquals(patronRequestModel.getLastName(), patronResponseModel.getLastName());
                    assertEquals(patronRequestModel.getEmailAddress(), patronResponseModel.getEmailAddress());
                    assertEquals(patronRequestModel.getContactMethodPreference(), patronResponseModel
                            .getContactMethodPreference().toString());
                    assertEquals(patronRequestModel.getStreetAddress(), patronResponseModel.getStreetAddress());
                    assertEquals(patronRequestModel.getCity(), patronResponseModel.getCity());
                    assertEquals(patronRequestModel.getProvince(), patronResponseModel.getProvince());
                    assertEquals(patronRequestModel.getCountry(), patronResponseModel.getCountry());
                    assertEquals(patronRequestModel.getPostalCode(), patronResponseModel.getPostalCode());
                    assertEquals(patronRequestModel.getPhoneNumbers().size(), patronResponseModel.getPhoneNumbers().size());
                });

        // assert
        assertEquals(sizeDB + 1, patronRepository.count());
    }

    // negative test case
    @Test
    public void whenInvalidPatron_thenThrowException() {

        // arrange
        PatronRequestModel patronRequestModel = new PatronRequestModel("Val", "Chase", INVALID_PATRON_EMAIL,
                "MAIL", "1234 Elm St", "Des Moines", "Iowa", "USA", "50309",
                List.of(new PhoneNumber(PhoneType.HOME, "515-555-5555"), new PhoneNumber(PhoneType.MOBILE, "515-555-4444")));

        // act & assert
        webTestClient.post()
                .uri(BASE_URI_PATRONS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(patronRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("Email address is invalid");
    }

    // positive test case
    @Test
    public void whenValidPatron_thenUpdatePatron() {

        // arrange
        PatronRequestModel patronRequestModel = new PatronRequestModel("Val", "Chase", "val.chase@gmail.com",
                "MAIL", "1234 Elm St", "Des Moines", "Iowa", "USA", "50309",
                List.of(new PhoneNumber(PhoneType.HOME, "515-555-5555"), new PhoneNumber(PhoneType.MOBILE, "515-555-4444")));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_PATRONS + "/" + FOUND_PATRON_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(patronRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PatronResponseModel.class)
                .value((patronResponseModel) -> {
                    assertNotNull(patronResponseModel);
                    assertEquals(patronRequestModel.getFirstName(), patronResponseModel.getFirstName());
                    assertEquals(patronRequestModel.getLastName(), patronResponseModel.getLastName());
                    assertEquals(patronRequestModel.getEmailAddress(), patronResponseModel.getEmailAddress());
                    assertEquals(patronRequestModel.getContactMethodPreference(), patronResponseModel
                            .getContactMethodPreference().toString());
                    assertEquals(patronRequestModel.getStreetAddress(), patronResponseModel.getStreetAddress());
                    assertEquals(patronRequestModel.getCity(), patronResponseModel.getCity());
                    assertEquals(patronRequestModel.getProvince(), patronResponseModel.getProvince());
                    assertEquals(patronRequestModel.getCountry(), patronResponseModel.getCountry());
                    assertEquals(patronRequestModel.getPostalCode(), patronResponseModel.getPostalCode());
                    assertEquals(patronRequestModel.getPhoneNumbers().size(), patronResponseModel.getPhoneNumbers().size());
                });
    }

    // negative test case
    @Test
    public void whenInvalidPatronIdForUpdate_thenThrowException() {

        // arrange
        PatronRequestModel patronRequestModel = new PatronRequestModel("Val", "Chase", "val.chase@gmail.com",
                "MAIL", "1234 Elm St", "Des Moines", "Iowa", "USA", "50309",
                List.of(new PhoneNumber(PhoneType.HOME, "515-555-5555"), new PhoneNumber(PhoneType.MOBILE, "515-555-4444")));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_PATRONS + "/" + NOT_FOUND_PATRON_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(patronRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown patronId: " + NOT_FOUND_PATRON_ID);
    }

    // positive test case
    @Test
    public void whenValidPatron_thenDeletePatron() {

        // arrange
        long sizeDB = patronRepository.count();

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_PATRONS + "/" + FOUND_PATRON_ID)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertEquals(sizeDB - 1, patronRepository.count());
    }

    // negative test case
    @Test
    public void whenInvalidPatronIdForDelete_thenThrowException() {

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_PATRONS + "/" + NOT_FOUND_PATRON_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown patronId: " + NOT_FOUND_PATRON_ID);
    }

}