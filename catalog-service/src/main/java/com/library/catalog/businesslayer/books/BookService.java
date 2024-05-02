package com.library.catalog.businesslayer.books;

import com.library.catalog.presentationlayer.books.BookRequestModel;
import com.library.catalog.presentationlayer.books.BookResponseModel;

public interface BookService {
    BookResponseModel getBook(Long isbn);

    BookResponseModel patchBook(Long isbn, BookRequestModel bookRequestModel);
}
