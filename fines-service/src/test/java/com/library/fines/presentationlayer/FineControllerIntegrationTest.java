package com.library.fines.presentationlayer;

import com.library.fines.datalayer.FineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class FineControllerIntegrationTest {

    private final String BASE_URI_FINES = "api/v1/fines";
    private final String FOUND_FINE_ID = "ef23ab6e-d614-47b9-95d0-d66167ae5081";
    private final BigDecimal FOUND_FINE_AMOUNT = new BigDecimal("0.50");
    private final String FOUND_FINE_REASON = "Late return";
    private final boolean FOUND_FINE_ISPAID = true;
    private final String NOT_FOUND_FINE_ID = "ef23ab6e-d614-47b9-95d0-d66167ae5082";

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private WebTestClient webTestClient;

    // positive path
    @Test
    public void whenGetFines_thenReturnAllFines() {

        // arrange
        long sizeDB = fineRepository.count();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_FINES)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(FineResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                });
    }

    // negative path
    // empty database, no fines empty list returned
    @Test
    public void whenGetFinesEmptyDB_thenReturnEmptyList() {

        // arrange
        fineRepository.deleteAll();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_FINES)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(FineResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.isEmpty());
                });
    }

    // positive test case
    @Test
    public void whenGetFineExists_thenReturnFineByFineId() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_FINES + "/" + FOUND_FINE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FineResponseModel.class)
                .value((fine) -> {
                    assertNotNull(fine);
                    assertEquals(fine.getFineId(), FOUND_FINE_ID);
                    assertEquals(fine.getAmount(), FOUND_FINE_AMOUNT);
                    assertEquals(fine.getReason(), FOUND_FINE_REASON);
                    assertEquals(fine.getIsPaid(), FOUND_FINE_ISPAID);
                });
    }

    // negative test case
    @Test
    public void whenGetFineDoesNotExist_thenReturnNotFound() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_FINES + "/" + NOT_FOUND_FINE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown fineId: " + NOT_FOUND_FINE_ID);
    }

    // positive test case
    @Test
    public void whenValidFine_thenCreateFine() {

        // arrange
        long sizeDB = fineRepository.count();
        FineRequestModel fineRequestModel = new FineRequestModel(new BigDecimal("0.50"), "Late return", false);

        webTestClient.post()
                .uri(BASE_URI_FINES)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(fineRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FineResponseModel.class)
                .value((fineResponseModel) -> {
                    assertNotNull(fineResponseModel);
                    assertEquals(fineRequestModel.getAmount(), fineResponseModel.getAmount());
                    assertEquals(fineRequestModel.getReason(), fineResponseModel.getReason());
                    assertEquals(fineRequestModel.getIsPaid(), fineResponseModel.getIsPaid());
                });

        // assert
        assertEquals(sizeDB + 1, fineRepository.count());
    }

    // negative test case
    @Test
    public void whenInvalidFine_thenThrowException() {

        // arrange
        FineRequestModel fineRequestModel = new FineRequestModel(new BigDecimal("0.00"), "Late return", false);

        // act & assert
        webTestClient.post()
                .uri(BASE_URI_FINES)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(fineRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("The fine must have a value");
    }

    // positive test case
    @Test
    public void whenValidFine_thenUpdateFine() {

        // arrange
        FineRequestModel fineRequestModel = new FineRequestModel(new BigDecimal("0.50"), "Late return", false);

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_FINES + "/" + FOUND_FINE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(fineRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FineResponseModel.class)
                .value((fineResponseModel) -> {
                    assertNotNull(fineResponseModel);
                    assertEquals(fineRequestModel.getAmount(), fineResponseModel.getAmount());
                    assertEquals(fineRequestModel.getReason(), fineResponseModel.getReason());
                    assertEquals(fineRequestModel.getIsPaid(), fineResponseModel.getIsPaid());
                });
    }

    // negative test case
    @Test
    public void whenInvalidFineIdForUpdate_thenThrowException() {

        // arrange
        FineRequestModel fineRequestModel = new FineRequestModel(new BigDecimal("0.00"), "Late return", false);

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_FINES + "/" + NOT_FOUND_FINE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(fineRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown fineId: " + NOT_FOUND_FINE_ID);
    }

    // negative test case
    @Test
    public void whenInvalidFineForUpdate_thenThrowException() {

        // arrange
        FineRequestModel fineRequestModel = new FineRequestModel(new BigDecimal("0.00"), "Late return", false);

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_FINES + "/" + FOUND_FINE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(fineRequestModel)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("The fine must have a value");
    }

    // positive test case
    @Test
    public void whenValidFine_thenDeleteFine() {

        // arrange
        long sizeDB = fineRepository.count();

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_FINES + "/" + FOUND_FINE_ID)
                .exchange()
                .expectStatus().isNoContent();

        // assert
        assertEquals(sizeDB - 1, fineRepository.count());
    }

    // negative test case
    @Test
    public void whenInvalidFineIdForDelete_thenThrowException() {

        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_FINES + "/" + NOT_FOUND_FINE_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown fineId: " + NOT_FOUND_FINE_ID);
    }

}