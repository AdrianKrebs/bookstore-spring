package ch.bfh.eadj.bookstore.application.service;


import ch.bfh.eadj.bookstore.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.bookstore.application.exception.BookNotFoundException;
import ch.bfh.eadj.bookstore.persistence.dto.BookInfo;
import ch.bfh.eadj.bookstore.persistence.entity.Book;
import ch.bfh.eadj.bookstore.persistence.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
@Transactional(rollbackFor = {BookAlreadyExistsException.class})
public class CatalogService {




    @Autowired
    BookRepository bookRepo;

   @Autowired
   private AmazonCatalog amazonCatalog;

    public Book findBookOnAmazon(String isbn) throws BookNotFoundException {
        Book bookByIsbn = amazonCatalog.findBook(isbn);
        if (bookByIsbn == null) {
            throw new BookNotFoundException();
        }
        return bookByIsbn;
    }

    public Book findBook(String isbn) {
        return bookRepo.findByIsbn(isbn);
    }


    public void addBook(Book book) throws BookAlreadyExistsException {
        Book bookFromDB = bookRepo.findByIsbn(book.getIsbn());
        if (bookFromDB != null) {
            throw new BookAlreadyExistsException();
        }
        bookRepo.save(book);
    }

    public void removeBook(Book book) {
        bookRepo.delete(book);
    }

    public List<BookInfo> searchBooks(String keywords) {
        if (keywords != null && keywords.length() > 0) {
            return amazonCatalog.searchBooks(keywords);
        } else {
            return Collections.emptyList();
        }
    }

    public void updateBook(Book book) throws BookNotFoundException {
        bookRepo.save(book);
    }
}
