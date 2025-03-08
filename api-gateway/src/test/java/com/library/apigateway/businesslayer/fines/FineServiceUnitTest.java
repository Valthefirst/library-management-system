package com.library.apigateway.businesslayer.fines;

import com.library.apigateway.domainclientlayer.fines.FineServiceClient;
import com.library.apigateway.mapperlayer.fines.FineResponseMapper;
import com.library.apigateway.presentationlayer.fines.FineRequestModel;
import com.library.apigateway.presentationlayer.fines.FineResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude = org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
@ActiveProfiles("test")
class FineServiceUnitTest {

    @Autowired
    FineService fineService;

    @MockBean
    FineServiceClient fineServiceClient;

    @SpyBean
    FineResponseMapper fineResponseMapper;

    // positive test
    @Test
    public void whenFinesExists_thenReturnFineList() {
        // arrange
        List<FineResponseModel> fineResponseModelList = List.of(
                FineResponseModel.builder()
                        .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                        .amount(BigDecimal.valueOf(0.50))
                        .reason("Late return")
                        .isPaid(true)
                        .build(),
                FineResponseModel.builder()
                        .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5082")
                        .amount(BigDecimal.valueOf(0.75))
                        .reason("Overdue books")
                        .isPaid(false)
                        .build()
        );

        when(fineServiceClient.getAllFines()).thenReturn(fineResponseModelList);

        // act
        List<FineResponseModel> result = fineService.getAllFines();

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fineServiceClient, times(1)).getAllFines();
    }

    // negative test
    @Test
    public void whenNoFineExists_thenReturnEmptyList() {
        // arrange
        when(fineServiceClient.getAllFines()).thenReturn(List.of());

        // act
        List<FineResponseModel> result = fineService.getAllFines();

        // assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(fineServiceClient, times(1)).getAllFines();
    }

    // positive test
    @Test
    public void whenFineExists_thenReturnFine() {
        // arrange
        FineResponseModel fineResponseModel = FineResponseModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .amount(BigDecimal.valueOf(0.50))
                .reason("Late return")
                .isPaid(true)
                .build();

        when(fineServiceClient.getFineByFineId("ef23ab6e-d614-47b9-95d0-d66167ae5081")).thenReturn(fineResponseModel);

        // act
        FineResponseModel result = fineService.getFine("ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        assertNotNull(result);
        assertEquals("ef23ab6e-d614-47b9-95d0-d66167ae5081", result.getFineId());
        verify(fineServiceClient, times(1))
                .getFineByFineId("ef23ab6e-d614-47b9-95d0-d66167ae5081");
    }

//    // negative test
//    @Test
//    public void whenFineDoesNotExist_thenThrowException() {
//        // arrange
//        when(fineServiceClient.getFineByFineId("ef23ab6e-d614-47b9-95d0-d66167ae5081")).thenReturn(null);
//
//        // act and assert
//        assertThrows(IllegalArgumentException.class, () -> fineService.getFine("ef23ab6e-d614-47b9-95d0-d66167ae5081"));
//        verify(fineServiceClient, times(1)).getFineByFineId("ef23ab6e-d614-47b9-95d0-d66167ae5081");
//    }

    // positive test
    @Test
    public void whenFineCreated_thenReturnFine() {
        // arrange
        FineRequestModel fineRequestModel = FineRequestModel.builder()
                .amount(BigDecimal.valueOf(0.50))
                .reason("Late return")
                .isPaid(true)
                .build();

        FineResponseModel fineResponseModelCreated = FineResponseModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .amount(BigDecimal.valueOf(0.50))
                .reason("Late return")
                .isPaid(true)
                .build();

        when(fineServiceClient.postFine(fineRequestModel)).thenReturn(fineResponseModelCreated);

        // act
        FineResponseModel result = fineService.addFine(fineRequestModel);

        // assert
        assertNotNull(result);
        assertEquals("ef23ab6e-d614-47b9-95d0-d66167ae5081", result.getFineId());
        assertEquals(BigDecimal.valueOf(0.50), result.getAmount());
        assertEquals("Late return", result.getReason());
        assertTrue(result.getIsPaid());
        verify(fineServiceClient, times(1)).postFine(fineRequestModel);
    }

    // positive test
    @Test
    public void whenFineExists_thenUpdateFine() {
        // arrange
        FineRequestModel fineRequestModel = FineRequestModel.builder()
                .amount(BigDecimal.valueOf(0.50))
                .reason("Late return")
                .isPaid(true)
                .build();

        FineResponseModel fineResponseModelUpdated = FineResponseModel.builder()
                .fineId("ef23ab6e-d614-47b9-95d0-d66167ae5081")
                .amount(BigDecimal.valueOf(0.75))
                .reason("Overdue books")
                .isPaid(false)
                .build();

        when(fineServiceClient.putFineByFineId("ef23ab6e-d614-47b9-95d0-d66167ae5081", fineRequestModel))
                .thenReturn(fineResponseModelUpdated);

        // act
        FineResponseModel result = fineService.updateFine(fineRequestModel, "ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        assertNotNull(result);
        assertEquals("ef23ab6e-d614-47b9-95d0-d66167ae5081", result.getFineId());
        assertEquals(BigDecimal.valueOf(0.75), result.getAmount());
        assertEquals("Overdue books", result.getReason());
        assertFalse(result.getIsPaid());
        verify(fineServiceClient, times(1))
                .putFineByFineId("ef23ab6e-d614-47b9-95d0-d66167ae5081", fineRequestModel);
    }

    // positive test
    @Test
    public void whenFineExists_thenDeleteFine() {
        // act
        fineService.deleteFine("ef23ab6e-d614-47b9-95d0-d66167ae5081");

        // assert
        verify(fineServiceClient, times(1))
                .deleteFineByFineId("ef23ab6e-d614-47b9-95d0-d66167ae5081");
    }

}