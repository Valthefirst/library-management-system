package com.library.fines.presentationlayer;

import com.library.fines.businesslayer.FineService;
import com.library.fines.utils.exceptions.InvalidAmountException;
import com.library.fines.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = FineController.class)
class FineControllerUnitTest {

    private final String FOUND_FINE_ID = "ef23ab6e-d614-47b9-95d0-d66167ae5081";
    private final BigDecimal FOUND_FINE_AMOUNT = new BigDecimal("0.50");
    private final String FOUND_FINE_REASON = "Late return";
    private final boolean FOUND_FINE_ISPAID = true;
    private final String NOT_FOUND_FINE_ID = "ef23ab6e-d614-47b9-95d0-d66167ae5082";

    @Autowired
    private FineController fineController;

    @MockBean
    private FineService fineService;

    private FineRequestModel buildFineRequestModel() {
        return FineRequestModel.builder()
                .amount(new BigDecimal("0.75"))
                .reason("Overdue books")
                .isPaid(false)
                .build();
    }

    private FineRequestModel buildBadFineRequestModel() {
        return FineRequestModel.builder()
                .amount(new BigDecimal("0.00"))
                .reason("Overdue books")
                .isPaid(false)
                .build();
    }

    private FineResponseModel buildFineResponseModel() {
        return FineResponseModel.builder()
                .fineId(FOUND_FINE_ID)
                .amount(FOUND_FINE_AMOUNT)
                .reason(FOUND_FINE_REASON)
                .isPaid(FOUND_FINE_ISPAID)
                .build();
    }

    // positive test case
    @Test
    public void whenFinesExists_thenReturnFineList() {
        // arrange
        FineResponseModel fineResponseModel = buildFineResponseModel();

        when(fineService.getAllFines()).thenReturn(Collections.singletonList(fineResponseModel));

        // act
        ResponseEntity<List<FineResponseModel>> responseEntity = fineController.getAllFines();

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(fineResponseModel, responseEntity.getBody().get(0));
        verify(fineService, times(1)).getAllFines();
    }

    // negative test case
    @Test
    public void whenNoFineExists_thenReturnEmptyList() {
        // arrange
        when(fineService.getAllFines()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<FineResponseModel>> responseEntity = fineController.getAllFines();

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());
        verify(fineService, times(1)).getAllFines();
    }

    // positive test case
    @Test
    public void whenFineExists_thenReturnFine() {
        // arrange
        FineResponseModel fineResponseModel = buildFineResponseModel();

        when(fineService.getFine(FOUND_FINE_ID)).thenReturn(fineResponseModel);

        // act
        ResponseEntity<FineResponseModel> responseEntity = fineController.getFine(FOUND_FINE_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(fineResponseModel, responseEntity.getBody());
        verify(fineService, times(1)).getFine(FOUND_FINE_ID);
    }

    // negative test case
    @Test
    public void whenFineDoesNotExist_thenReturnNotFound() {
        // arrange
        when(fineService.getFine(NOT_FOUND_FINE_ID)).thenThrow(new NotFoundException("Unknown fineId: " +
                NOT_FOUND_FINE_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            fineController.getFine(NOT_FOUND_FINE_ID);
        });

        // assert
        assertEquals("Unknown fineId: " + NOT_FOUND_FINE_ID, exception.getMessage());
        verify(fineService, times(1)).getFine(NOT_FOUND_FINE_ID);
    }

    // positive test case
    @Test
    public void whenFineCreated_thenReturnFine() {
        // arrange
        FineRequestModel fineRequestModel = buildFineRequestModel();
        FineResponseModel fineResponseModel = buildFineResponseModel();

        when(fineService.addFine(fineRequestModel)).thenReturn(fineResponseModel);

        // act
        ResponseEntity<FineResponseModel> responseEntity = fineController.addFine(fineRequestModel);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(fineResponseModel, responseEntity.getBody());
        verify(fineService, times(1)).addFine(fineRequestModel);
    }

    // negative test case
    @Test
    public void whenInvalidFine_thenThrowException() {
        // arrange
        FineRequestModel fineRequestModel = buildBadFineRequestModel();
        when(fineService.addFine(fineRequestModel)).thenThrow(new InvalidAmountException("The fine must have a value"));

        // act
        InvalidAmountException exception = assertThrowsExactly(InvalidAmountException.class, () -> {
            fineController.addFine(fineRequestModel);
        });

        // assert
        assertEquals("The fine must have a value", exception.getMessage());
        verify(fineService, times(1)).addFine(fineRequestModel);
    }

    // positive test case
    @Test
    public void whenFineUpdated_thenReturnFine() {
        // arrange
        FineRequestModel fineRequestModel = buildFineRequestModel();
        FineResponseModel fineResponseModel = buildFineResponseModel();

        when(fineService.updateFine(fineRequestModel, FOUND_FINE_ID)).thenReturn(fineResponseModel);

        // act
        ResponseEntity<FineResponseModel> responseEntity = fineController.updateFine(fineRequestModel, FOUND_FINE_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(fineResponseModel, responseEntity.getBody());
        verify(fineService, times(1)).updateFine(fineRequestModel, FOUND_FINE_ID);
    }

    // negative test case
    @Test
    public void whenInvalidFineIdForUpdate_thenReturnNotFound() {
        // arrange
        FineRequestModel fineRequestModel = buildFineRequestModel();
        when(fineService.updateFine(fineRequestModel, NOT_FOUND_FINE_ID))
                .thenThrow(new NotFoundException("Unknown fineId: " + NOT_FOUND_FINE_ID));

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            fineController.updateFine(fineRequestModel, NOT_FOUND_FINE_ID);
        });

        // assert
        assertEquals("Unknown fineId: " + NOT_FOUND_FINE_ID, exception.getMessage());
        verify(fineService, times(1)).updateFine(fineRequestModel, NOT_FOUND_FINE_ID);
    }

    // negative test case
    @Test
    public void whenInvalidFineForUpdate_thenReturnNotFound() {
        // arrange
        FineRequestModel fineRequestModel = buildBadFineRequestModel();
        when(fineService.updateFine(fineRequestModel, FOUND_FINE_ID))
                .thenThrow(new InvalidAmountException("The fine must have a value"));

        // act
        InvalidAmountException exception = assertThrowsExactly(InvalidAmountException.class, () -> {
            fineController.updateFine(fineRequestModel, FOUND_FINE_ID);
        });

        // assert
        assertEquals("The fine must have a value", exception.getMessage());
        verify(fineService, times(1)).updateFine(fineRequestModel, FOUND_FINE_ID);
    }

    // positive test case
    @Test
    public void whenFineDeleted_thenNoContent() {
        // arrange
        doNothing().when(fineService).deleteFine(FOUND_FINE_ID);

        // act
        ResponseEntity<Void> responseEntity = fineController.deleteFine(FOUND_FINE_ID);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(fineService, times(1)).deleteFine(FOUND_FINE_ID);
    }

    // negative test case
    @Test
    public void whenFineDoesNotExist_thenNotFound() {
        // arrange
        doThrow(new NotFoundException("Unknown fineId: " + NOT_FOUND_FINE_ID)).when(fineService).deleteFine(NOT_FOUND_FINE_ID);

        // act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            fineController.deleteFine(NOT_FOUND_FINE_ID);
        });

        // assert
        assertEquals("Unknown fineId: " + NOT_FOUND_FINE_ID, exception.getMessage());
        verify(fineService, times(1)).deleteFine(NOT_FOUND_FINE_ID);
    }

}