package com.library.catalog.datalayer.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Integer>{

    Catalog findByCatalogIdentifier_CatalogId(String catalogId);
}
