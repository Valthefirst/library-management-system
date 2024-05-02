package com.library.apigateway.presentationlayer.catalogs.catalogbooks;

import com.library.apigateway.businesslayer.catalogs.CatalogBooksService;
import com.library.apigateway.presentationlayer.catalogs.books.BookRequestModel;
import com.library.apigateway.presentationlayer.catalogs.books.BookResponseModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogRequestModel;
import com.library.apigateway.presentationlayer.catalogs.catalog.CatalogResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/catalogs")
public class CatalogBooksController {

    private final CatalogBooksService catalogBooksService;

    public CatalogBooksController(CatalogBooksService catalogBooksService) {
        this.catalogBooksService = catalogBooksService;
    }

    // Methods for the catalogs

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CatalogResponseModel>> getAllCatalogs() {
        return ResponseEntity.ok().body(catalogBooksService.getAllCatalogs());
    }

    @GetMapping(value = "{catalogId}", produces = "application/json")
    public ResponseEntity<CatalogResponseModel> getCatalog(@PathVariable String catalogId) {
        return ResponseEntity.ok().body(catalogBooksService.getCatalog(catalogId));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CatalogResponseModel> addCatalog(@RequestBody CatalogRequestModel catalogRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogBooksService.addCatalog(catalogRequestModel));
    }

    @PutMapping(consumes = "application/json", value = "{catalogId}", produces = "application/json")
    public ResponseEntity<CatalogResponseModel> updateCatalog(@RequestBody CatalogRequestModel catalogRequestModel,
                                                              @PathVariable String catalogId) {
        return ResponseEntity.ok().body(catalogBooksService.updateCatalog(catalogRequestModel, catalogId));
    }

    @DeleteMapping("{catalogId}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable String catalogId) {
        catalogBooksService.deleteCatalog(catalogId);
        return ResponseEntity.noContent().build();
    }

    // Methods for the books
    @GetMapping(value = "{catalogId}/books", produces = "application/json")
    public ResponseEntity<List<BookResponseModel>> getAllBooks(@PathVariable String catalogId) {
        return ResponseEntity.ok().body(catalogBooksService.getAllBooksInCatalog(catalogId));
    }

    @GetMapping(value = "{catalogId}/books/{isbn}", produces = "application/json")
    public ResponseEntity<BookResponseModel> getBook(@PathVariable String catalogId, @PathVariable Long isbn) {
        return ResponseEntity.ok().body(catalogBooksService.getBookInCatalog(catalogId, isbn));
    }

    @PostMapping(value = "{catalogId}/books", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BookResponseModel> addBook(@RequestBody BookRequestModel bookRequestModel,
                                                     @PathVariable String catalogId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogBooksService.addBookInCatalog(catalogId, bookRequestModel));
    }

    @PutMapping(consumes = "application/json", value = "{catalogId}/books/{isbn}", produces = "application/json")
    public ResponseEntity<BookResponseModel> updateBook(@RequestBody BookRequestModel bookRequestModel,
                                                        @PathVariable Long isbn, @PathVariable String catalogId) {
        BookResponseModel updatedMovie = catalogBooksService.updateBookInCatalog(catalogId, isbn, bookRequestModel);
        return ResponseEntity.ok().body(updatedMovie);
    }

    @PatchMapping(consumes = "application/json", value = "{catalogId}/books/{isbn}", produces = "application/json")
    public ResponseEntity<BookResponseModel> patchBook(@RequestBody BookRequestModel bookRequestModel,
                                                        @PathVariable Long isbn, @PathVariable String catalogId) {
        BookResponseModel updatedMovie = catalogBooksService.patchBookInCatalog(catalogId, isbn, bookRequestModel);
        return ResponseEntity.ok().body(updatedMovie);
    }

    @DeleteMapping("{catalogId}/books/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long isbn, @PathVariable String catalogId) {
        catalogBooksService.deleteBookInCatalog(catalogId, isbn);
        return ResponseEntity.noContent().build();
    }
}
