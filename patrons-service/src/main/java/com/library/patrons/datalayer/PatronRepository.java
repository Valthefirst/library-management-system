package com.library.patrons.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Integer> {

    Patron findByPatronIdentifier_PatronId(String patronId);
}