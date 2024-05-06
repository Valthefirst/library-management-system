package com.library.loans.presentationlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.loans.datalayer.Loan;
import com.library.loans.datalayer.LoanRepository;
import com.library.loans.domainclientlayer.patrons.PatronModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class PatronLoansControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoanRepository loanRepository;

    private MockRestServiceServer mockRestServiceServer;
    private ObjectMapper mapper = new ObjectMapper();
    private final String PATRON_BASE_URL = "http://localhost:7003/api/v1/patrons";
    private final String LOAN_BASE_URL = "api/v1/patrons";

    @BeforeEach
    public void init() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    // positive path
    @Test
    public void whenGetAllLoansForPatron_thenReturnList() throws URISyntaxException, JsonProcessingException{
        // arrange
        PatronModel patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(PATRON_BASE_URL + "/e5913a79-9b1e-4516-9ffd-06578e7af261")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(patronModel))
                );

        // find all loans for the patron
//        List<Loan> loans = loanRepository.findLoansByPatronModel_PatronId(patronModel.getPatronId());

        List<Loan> loans = loanRepository.findAll();
        Loan loan = loans.stream()
                .filter(l -> l.getPatronModel().getPatronId().equals(patronModel.getPatronId()))
                .findFirst()
                .get();

//        assertNotNull(loans);
        assertNotNull(loan);

        String url = LOAN_BASE_URL + "/" + patronModel.getPatronId() + "/loans";

        // act & assert
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(LoanResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() > 0);
                });
    }

    // negative path
//    @Test
//    public void whenGetAllLoansForNotExistentPatron_thenReturnNotFound() throws URISyntaxException, JsonProcessingException{
//        // arrange
//        PatronModel patronModel = PatronModel.builder()
//                .patronId("non-existent-patron-id")
//                .firstName("Vilma")
//                .lastName("Chawner")
//                .build();
//        mockRestServiceServer.expect(ExpectedCount.once(),
//                        requestTo(new URI(PATRON_BASE_URL + "/" + patronModel.getPatronId())))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.NOT_FOUND)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(patronModel))
//                );
//
//        // find all loans for the patron
//                List<Loan> loans = loanRepository.findAll();
//                Loan loan = loans.stream()
//                        .filter(l -> l.getPatronModel().getPatronId().equals(patronModel.getPatronId()))
//                        .findFirst()
//                        .get();
//
//                assertEquals(loan.getPatronModel().getPatronId(), patronModel.getPatronId());
//
//        String url = LOAN_BASE_URL + "/" + patronModel.getPatronId() + "/loans";
//
//        // act & assert
////        webTestClient.get()
////                .uri(url)
////                .accept(MediaType.APPLICATION_JSON)
////                .exchange()
////                .expectStatus().isOk()
////                .expectHeader().contentType(MediaType.APPLICATION_JSON)
////                .expectBodyList(LoanResponseModel.class)
////                .value((list) -> {
////                    assertNotNull(list);
////                    assertTrue(list.size() > 0);
////                });
//    }

    // positive path
    @Test
    public void whenGetLoanForPatron_thenReturnLoan() throws URISyntaxException, JsonProcessingException{

        // arrange
        PatronModel patronModel = PatronModel.builder()
                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
                .firstName("Vilma")
                .lastName("Chawner")
                .build();
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(PATRON_BASE_URL + "/e5913a79-9b1e-4516-9ffd-06578e7af261")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(patronModel))
                );

        // find the loanId
        List<Loan> loans = loanRepository.findLoansByPatronModel_PatronId(patronModel.getPatronId());
        Loan loan = loans.stream()
                .filter(l -> l.getBooks().get(0).getIsbn().equals(9789390183522L) &&
                        l.getBooks().get(1).getIsbn().equals(9780132350882L))
                .findFirst()
                .get();

        assertNotNull(loan);

        String url = LOAN_BASE_URL + "/" + patronModel.getPatronId() + "/loans/" + loan.getLoanIdentifier().getLoanId();

        // act & assert
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoanResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(loan.getLoanIdentifier().getLoanId(), response.getLoanId());
                    assertEquals(loan.getBooks().get(0).getIsbn(), response.getBooks().get(0).getIsbn());
                });
    }

    // positive path
//    @Test
//    public void whenValidLoan_thenCreateLoan() throws URISyntaxException, JsonProcessingException {
//        // arrange
//        PatronModel patronModel = PatronModel.builder()
//                .patronId("e5913a79-9b1e-4516-9ffd-06578e7af261")
//                .firstName("Vilma")
//                .lastName("Chawner")
//                .build();
//
//        LoanRequestModel loanRequestModel = new LoanRequestModel(LoanStatus.ACTIVE,
//                List.of(9780545791328L, 9780765308481L));
//
//        mockRestServiceServer.expect(ExpectedCount.once(),
//                        requestTo(new URI(PATRON_BASE_URL + "/e5913a79-9b1e-4516-9ffd-06578e7af261/loans")))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.CREATED)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(patronModel))
//                );
//        mockRestServiceServer.expect(ExpectedCount.once(),
//                        requestTo(new URI(PATRON_BASE_URL + "/e5913a79-9b1e-4516-9ffd-06578e7af261/loans")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(patronModel))
//                );
//
//        // act & assert
//        webTestClient.post()
//                .uri(LOAN_BASE_URL + "/" + patronModel.getPatronId() + "/loans")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .bodyValue(loanRequestModel)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody(LoanResponseModel.class)
//                .value((loanResponse) -> {
//                    assertNotNull(loanResponse);
//                    assertEquals(loanResponse.getStatus(), LoanStatus.ACTIVE);
//                    assertEquals(loanResponse.getBooks().get(0).getIsbn(), 9780545791328L);
//                    assertEquals(loanResponse.getBooks().get(1).getIsbn(), 9780765308481L);
//                });
//    }

}