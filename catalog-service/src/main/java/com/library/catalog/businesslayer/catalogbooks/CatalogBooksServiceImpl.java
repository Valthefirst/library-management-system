package com.library.catalog.businesslayer.catalogbooks;

import com.library.catalog.datalayer.books.Book;
import com.library.catalog.datalayer.books.BookRepository;
import com.library.catalog.datalayer.books.ISBN;
import com.library.catalog.datalayer.catalog.Catalog;
import com.library.catalog.datalayer.catalog.CatalogIdentifier;
import com.library.catalog.datalayer.catalog.CatalogRepository;
import com.library.catalog.datamapperlayer.book.BookRequestMapper;
import com.library.catalog.datamapperlayer.book.BookResponseMapper;
import com.library.catalog.datamapperlayer.catalog.CatalogRequestMapper;
import com.library.catalog.datamapperlayer.catalog.CatalogResponseMapper;
import com.library.catalog.presentationlayer.books.BookRequestModel;
import com.library.catalog.presentationlayer.books.BookResponseModel;
import com.library.catalog.presentationlayer.catalog.CatalogRequestModel;
import com.library.catalog.presentationlayer.catalog.CatalogResponseModel;
import com.library.catalog.utils.exceptions.DuplicateISBNException;
import com.library.catalog.utils.exceptions.InUseException;
import com.library.catalog.utils.exceptions.InvalidISBNException;
import com.library.catalog.utils.exceptions.NotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogBooksServiceImpl implements CatalogBooksService {

    private final CatalogRepository catalogRepository;
    private final BookRepository bookRepository;
    private final CatalogResponseMapper catalogResponseMapper;
    private final CatalogRequestMapper catalogRequestMapper;
    private final BookResponseMapper bookResponseMapper;
    private final BookRequestMapper bookRequestMapper;

    public CatalogBooksServiceImpl(CatalogRepository catalogRepository, BookRepository bookRepository, CatalogResponseMapper catalogResponseMapper, CatalogRequestMapper catalogRequestMapper, BookResponseMapper bookResponseMapper, BookRequestMapper bookRequestMapper) {
        this.catalogRepository = catalogRepository;
        this.bookRepository = bookRepository;
        this.catalogResponseMapper = catalogResponseMapper;
        this.catalogRequestMapper = catalogRequestMapper;
        this.bookResponseMapper = bookResponseMapper;
        this.bookRequestMapper = bookRequestMapper;
    }

    // Methods for the catalogs
    @Override
    public List<CatalogResponseModel> getAllCatalogs() {
        List<Catalog> catalogs = catalogRepository.findAll();
        return catalogResponseMapper.entityListToResponseModelList(catalogs);
    }

    @Override
    public CatalogResponseModel getCatalog(String catalogId) {
        Catalog catalog = catalogRepository.findByCatalogIdentifier_CatalogId(catalogId);
        if (catalog == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);
        return catalogResponseMapper.entityToResponseModel(catalog);
    }

    @Override
    public CatalogResponseModel addCatalog(CatalogRequestModel catalogRequestModel) {
        return catalogResponseMapper.entityToResponseModel(catalogRepository
                .save(catalogRequestMapper.requestModelToEntity(catalogRequestModel, new CatalogIdentifier())));
    }

    @Override
    public CatalogResponseModel updateCatalog(CatalogRequestModel catalogRequestModel, String catalogId) {
        Catalog existingCatalog = catalogRepository.findByCatalogIdentifier_CatalogId(catalogId);
        if (existingCatalog == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);

        Catalog updatedCatalog = catalogRequestMapper.requestModelToEntity(catalogRequestModel, existingCatalog.getCatalogIdentifier());
        updatedCatalog.setId(existingCatalog.getId());
        return catalogResponseMapper.entityToResponseModel(catalogRepository.save(updatedCatalog));
    }

    @Override
    public void deleteCatalog(String catalogId) {
        Catalog existingCatalog = catalogRepository.findByCatalogIdentifier_CatalogId(catalogId);
        if (existingCatalog == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);

        if (existingCatalog.getSize() > 0)
            throw new InUseException("Catalog has books and cannot be deleted");

        catalogRepository.delete(existingCatalog);
    }


    // Methods for the books
    @Override
    public List<BookResponseModel> getAllBooksInCatalog(String catalogId) {
        if (catalogRepository.findByCatalogIdentifier_CatalogId(catalogId) == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);

        //add query params

        return bookResponseMapper.entityListToResponseModelList(
                bookRepository.findAllByCatalogIdentifier_CatalogId(catalogId));
    }

    @Override
    public BookResponseModel getBookInCatalog(String catalogId, Long isbn) {
        if (catalogRepository.findByCatalogIdentifier_CatalogId(catalogId) == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);

        if (isbn.toString().length() != 10 && isbn.toString().length() != 13) {
            throw new InvalidISBNException("ISBN must be 10 or 13 digits long.");
        }

        Book book = bookRepository.findByIsbn_Isbn(isbn);
        if (book == null) {
            throw new NotFoundException("Unknown ISBN provided: " + isbn);
        }

        book = bookRepository.findByCatalogIdentifier_CatalogIdAndIsbn_Isbn(catalogId, isbn);
        if (book == null) {
            throw new NotFoundException("Book is not in the catalog");
        }

        return bookResponseMapper.entityToResponseModel(book);
    }

    @Override
    public BookResponseModel addBookInCatalog(String catalogId, BookRequestModel bookRequestModel) {
        if (catalogRepository.findByCatalogIdentifier_CatalogId(catalogId) == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);

        if (bookRequestModel.getIsbn().toString().length() != 10 && bookRequestModel.getIsbn().toString().length() != 13) {
            throw new InvalidISBNException("ISBN must be 10 or 13 digits long.");
        }

        Book book = bookRequestMapper.requestModelToEntity(bookRequestModel, new ISBN(bookRequestModel.getIsbn()),
                new CatalogIdentifier(catalogId));

        // To increment the number of books in the catalog
        Catalog catalog = catalogRepository.findByCatalogIdentifier_CatalogId(catalogId);
        catalog.setSize(catalog.getSize() + 1);

        try {
            return bookResponseMapper.entityToResponseModel(bookRepository.save(book));
        }
        catch (DataAccessException e) {
            if(e.getMessage().contains("constraint [")) {
                catalog.setSize(catalog.getSize() - 1);
                throw new DuplicateISBNException("The catalog already contains a book with isbn: " + bookRequestModel.getIsbn());
            }
            throw new InvalidISBNException("Could not save the new book.");
        }
    }

    @Override
    public BookResponseModel updateBookInCatalog(String catalogId, Long isbn, BookRequestModel bookRequestModel) {
        if (catalogRepository.findByCatalogIdentifier_CatalogId(catalogId) == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);

        if (isbn.toString().length() != 10 && isbn.toString().length() != 13) {
            throw new InvalidISBNException("ISBN must be 10 or 13 digits long.");
        }

        Book existingBook = bookRepository.findByIsbn_Isbn(isbn);
        if (existingBook == null) {
            throw new NotFoundException("Unknown ISBN provided: " + isbn);
        }

        existingBook = bookRepository.findByCatalogIdentifier_CatalogIdAndIsbn_Isbn(catalogId, isbn);
        if (existingBook == null) {
            throw new NotFoundException("Book is not in the catalog");
        }

        Book updatedBook = bookRequestMapper.requestModelToEntity(bookRequestModel, existingBook.getIsbn(),
                existingBook.getCatalogIdentifier());
        updatedBook.setId(existingBook.getId());

        Book response = bookRepository.save(updatedBook);
        return bookResponseMapper.entityToResponseModel(response);
    }

    @Override
    public void deleteBookInCatalog(String catalogId, Long isbn) {
        if (catalogRepository.findByCatalogIdentifier_CatalogId(catalogId) == null)
            throw new NotFoundException("Unknown catalogId provided: " + catalogId);

        if (isbn.toString().length() != 10 && isbn.toString().length() != 13) {
            throw new InvalidISBNException("ISBN must be 10 or 13 digits long.");
        }

        Book existingBook = bookRepository.findByIsbn_Isbn(isbn);
        if (existingBook == null) {
            throw new NotFoundException("Unknown ISBN provided: " + isbn);
        }

        existingBook = bookRepository.findByCatalogIdentifier_CatalogIdAndIsbn_Isbn(catalogId, isbn);
        if (existingBook == null) {
            throw new NotFoundException("Book is not in the catalog");
        }

        // To decrement the number of books in the catalogue
        Catalog catalog = catalogRepository.findByCatalogIdentifier_CatalogId(catalogId);
        catalog.setSize(catalog.getSize() - 1);

        bookRepository.delete(existingBook);
    }
}
