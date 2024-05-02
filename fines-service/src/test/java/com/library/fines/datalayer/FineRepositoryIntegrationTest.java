package com.library.fines.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FineRepositoryIntegrationTest {

    @Autowired
    private FineRepository fineRepository;

    @BeforeEach
    public void setUp() {
        fineRepository.deleteAll();
    }

    // positive test case
    @Test
    public void whenFineExists_ReturnFineByFineId() {
        // arrange
        Fine fine1 = new Fine(new BigDecimal("9.75"), "Overdue books", false);
        fineRepository.save(fine1);

        // act
        Fine savedFine = fineRepository.findByFineIdentifier_FineId(fine1.getFineIdentifier().getFineId());

        // assert
        assertNotNull(savedFine);
        assertEquals(savedFine.getFineIdentifier(), fine1.getFineIdentifier());
        assertEquals(savedFine.getAmount(), fine1.getAmount());
        assertEquals(savedFine.getReason(), fine1.getReason());
        assertEquals(savedFine.getIsPaid(), fine1.getIsPaid());
    }

    // negative test case
    @Test
    public void whenFineDoesNotExist_ReturnNull() {
        // arrange
        Fine fine1 = new Fine(new BigDecimal("9.75"), "Overdue books", false);
        fineRepository.save(fine1);

        // act
        Fine nonExistentFine = fineRepository.findByFineIdentifier_FineId("non-existent-id");

        // assert
        assertNull(nonExistentFine);
    }

}