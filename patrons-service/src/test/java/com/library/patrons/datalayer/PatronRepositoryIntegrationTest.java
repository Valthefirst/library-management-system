package com.library.patrons.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatronRepositoryIntegrationTest {

    @Autowired
    private PatronRepository patronRepository;

    @BeforeEach
    public void setUp() {
        patronRepository.deleteAll();
    }

    // positive test case
    @Test
    public void whenPatronExists_ReturnPatronByPatronId() {
        // arrange
        var homePhone = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(homePhone);

        Patron patron1 = new Patron("Vilma", "Chawner", "vchawner0@phoca.cz",
                ContactMethodPreference.MAIL, new Address("8452 Anhalt Park", "Chambly",
                "Québec", "Canada", "J3L 5Y6"), phoneNumbers);
        patronRepository.save(patron1);

        // act
        Patron savedPatron = patronRepository.findByPatronIdentifier_PatronId(patron1.getPatronIdentifier().getPatronId());

        // assert
        assertNotNull(savedPatron);
        assertEquals(savedPatron.getPatronIdentifier(), patron1.getPatronIdentifier());
        assertEquals(savedPatron.getFirstName(), patron1.getFirstName());
        assertEquals(savedPatron.getLastName(), patron1.getLastName());
        assertEquals(savedPatron.getEmailAddress(), patron1.getEmailAddress());
        assertEquals(savedPatron.getContactMethodPreference(), patron1.getContactMethodPreference());
        assertEquals(savedPatron.getAddress(), patron1.getAddress());
        assertEquals(savedPatron.getPhoneNumbers(), patron1.getPhoneNumbers());
    }

    // negative test case
    @Test
    public void whenPatronDoesNotExist_ReturnNull() {
        // arrange
        var homePhone = new PhoneNumber(PhoneType.HOME, "514-555-5555");
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(homePhone);

        Patron patron1 = new Patron("Vilma", "Chawner", "vchawner0@phoca.cz",
                ContactMethodPreference.MAIL, new Address("8452 Anhalt Park", "Chambly",
                "Québec", "Canada", "J3L 5Y6"), phoneNumbers);
        patronRepository.save(patron1);

        // act
        Patron nonExistentPatron = patronRepository.findByPatronIdentifier_PatronId("non-existent-id");

        // assert
        assertNull(nonExistentPatron);
    }

}