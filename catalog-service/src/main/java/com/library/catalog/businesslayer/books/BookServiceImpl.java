package com.library.catalog.businesslayer.books;

import com.library.catalog.datalayer.books.Book;
import com.library.catalog.datalayer.books.BookRepository;
import com.library.catalog.datalayer.books.Status;
import com.library.catalog.datamapperlayer.book.BookResponseMapper;
import com.library.catalog.presentationlayer.books.BookRequestModel;
import com.library.catalog.presentationlayer.books.BookResponseModel;
import com.library.catalog.utils.exceptions.InvalidISBNException;
import com.library.catalog.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final BookResponseMapper bookResponseMapper;

    public BookServiceImpl(BookRepository bookRepository, BookResponseMapper bookResponseMapper) {
        this.bookRepository = bookRepository;
        this.bookResponseMapper = bookResponseMapper;
    }

    @Override
    public BookResponseModel getBook(Long isbn) {
        if (isbn.toString().length() != 10 && isbn.toString().length() != 13) {
            throw new InvalidISBNException("ISBN must be 10 or 13 digits long.");
        }

        Book existingBook = bookRepository.findByIsbn_Isbn(isbn);
        if (existingBook == null) {
            throw new NotFoundException("Unknown ISBN provided: " + isbn);
        }
        return bookResponseMapper.entityToResponseModel(bookRepository.findByIsbn_Isbn(isbn));
    }

    @Override
    public BookResponseModel patchBook(Long isbn, BookRequestModel bookRequestModel) {
        if (isbn.toString().length() != 10 && isbn.toString().length() != 13) {
            throw new InvalidISBNException("ISBN must be 10 or 13 digits long.");
        }

        Book existingBook = bookRepository.findByIsbn_Isbn(isbn);
        if (existingBook == null) {
            throw new NotFoundException("Unknown ISBN provided: " + isbn);
        }

        existingBook.setStatus(Status.valueOf(bookRequestModel.getStatus()));

        Book response = bookRepository.save(existingBook);
        return bookResponseMapper.entityToResponseModel(response);
    }
}
