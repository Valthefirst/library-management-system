package com.library.catalog.presentationlayer.books;

import com.library.catalog.businesslayer.books.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(produces = "application/json", value = "{isbn}")
    public ResponseEntity<BookResponseModel> getBook(@PathVariable Long isbn) {
        return ResponseEntity.ok().body(bookService.getBook(isbn));
    }

    @PatchMapping(consumes = "application/json", value = "{isbn}", produces = "application/json")
    public ResponseEntity<BookResponseModel> patchBook(@RequestBody BookRequestModel bookRequestModel,
                                                       @PathVariable Long isbn) {
        return ResponseEntity.ok().body(bookService.patchBook(isbn, bookRequestModel));
    }
}
