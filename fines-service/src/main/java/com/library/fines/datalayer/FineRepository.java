package com.library.fines.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Integer> {

    Fine findByFineIdentifier_FineId(String fineId);
}
