package ch.bfh.eadj.bookstore.application.service;

import ch.bfh.eadj.bookstore.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.bookstore.application.exception.BookNotFoundException;
import ch.bfh.eadj.bookstore.persistence.dto.BookInfo;
import ch.bfh.eadj.bookstore.persistence.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //ganzer container fährt hoch
@ExtendWith(SpringExtension.class)
@Transactional
class CatalogServiceIT {


    @Autowired
    private  CatalogService catalogService;


    private String isbn = "0099590085";

    @BeforeAll
    static void setUp() throws Exception {

    }

    @Test
    public void shouldFailAddBook() throws BookAlreadyExistsException {
        //when
        Book book = createBook("test", Integer.toString(new Random().nextInt(10000)), "max muster");
        catalogService.addBook(book);

        Executable executable = () -> catalogService.addBook(book);
        assertThrows(BookAlreadyExistsException.class, executable);
    }

    @Test
    public void shouldFindBook() throws BookNotFoundException, BookAlreadyExistsException {

        String isbn = Integer.toString(new Random().nextInt(10000));
        createBook("test", isbn, "max muster");

        //when
        Book book = catalogService.findBook(isbn);

        //then
        assertEquals("test", book.getTitle());
        assertEquals(isbn, book.getIsbn());
    }

    @Test
    public void shouldFindBookByKeywordsManyResults() {

        //when
        List<BookInfo> books = catalogService.searchBooks("sapiens");

        //then
        assertThat(books.size(), is(greaterThan(5)));
        BookInfo first = books.get(0);
        assertNotNull(first.getTitle());
        assertNotNull(first.getAuthors());
        assertNotNull(first.getIsbn());
        assertNotNull(first.getPrice());
    }

    @Test
    public void shouldFindBookByKeywordsFewResults() {

        //when
        List<BookInfo> books = catalogService.searchBooks("Sapiens: A Brief History of Humankind Yuval Noah Harari");

        //then
        assertThat(books.size(), is(greaterThan(5)));
        BookInfo first = books.get(0);
        assertNotNull(first.getTitle());
        assertNotNull(first.getAuthors());
        assertNotNull(first.getIsbn());
        assertNotNull(first.getPrice());
    }


    @Test
    public void shouldNotFindNonExistingBookWithMultipleKeywords() throws BookNotFoundException {


        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("max sven");

        //then
        assertFalse(booksFromDb.isEmpty());
    }


    @Test
    public void shouldNotFindBookByKeywords() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("Manikrz");

        //then
        assertTrue(booksFromDb.isEmpty());
    }

    @Test
    public void shouldUpdateBook() throws BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(isbn);
        bookFromDb.setAuthors("Adrian Krebs");

        catalogService.updateBook(bookFromDb);

        //then
        Book afterUpdate = catalogService.findBook(isbn);
        assertEquals("Adrian Krebs", afterUpdate.getAuthors());
    }

    @Test
    public void shouldFailUpdateBook() throws BookNotFoundException, BookAlreadyExistsException {
        //given
        Book book = createBook("test", "12345", "max muster");
        book.setIsbn("1231231321");
        //when
        Executable executable = () -> catalogService.updateBook(book);
        assertThrows(BookNotFoundException.class, executable);
    }


    Book createBook(String title, String isbn, String author) throws BookAlreadyExistsException {
        Book b = new Book();
        b.setTitle(title);
        b.setIsbn(isbn);
        b.setAuthors(author);
        b.setPrice(new BigDecimal("100.14"));
        catalogService.addBook(b);
        return b;
    }


}