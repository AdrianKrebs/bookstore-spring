package ch.bfh.eadj.bookstore.persistence.repository;


import ch.bfh.eadj.bookstore.persistence.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String>, BookRepositoryCustom {

	public Book findByIsbn(String isbn);
}
