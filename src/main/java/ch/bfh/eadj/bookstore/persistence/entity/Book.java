package ch.bfh.eadj.bookstore.persistence.entity;


import ch.bfh.eadj.bookstore.persistence.dto.BookInfo;
import ch.bfh.eadj.bookstore.persistence.enumeration.BookBinding;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

	@Id
	@NotNull
	@Column(name = "ISBN")
	private String isbn;
	@NotNull
	@Column(name = "AUTHORS", nullable = false)
	private String authors;
	@NotNull
	@Column(name = "TITLE", nullable = false)
	private String title;
	@NotNull
	@Column(name = "PRICE", precision = 7, scale = 2, nullable = false)
	private BigDecimal price;
	@Column(name = "PUBLISHER")
	private String publisher;
	@Column(name = "PUB_YEAR")
	private Integer publicationYear;
	@Column(name = "BINDING")
	@Enumerated(EnumType.STRING)
	private BookBinding binding;
	@Column(name = "NUM_PAGES")
	private Integer numberOfPages;
	@Column(name = "DESCRIPTION", length = 5000)
	private String description;
	@Column(name = "IMAGE_URL")
	private String imageUrl;

	public Book() {
	}

	public Book(String isbn, String authors, String title, BigDecimal price) {
		this.isbn = isbn;
		this.authors = authors;
		this.title = title;
		this.price = price;
	}

	public Book(String isbn, String authors, String title, BigDecimal price, String publisher, Integer publicationYear,
			BookBinding binding, Integer numberOfPages, String description, String imageUrl) {
		this.isbn = isbn;
		this.authors = authors;
		this.title = title;
		this.price = price;
		this.publisher = publisher;
		this.publicationYear = publicationYear;
		this.binding = binding;
		this.numberOfPages = numberOfPages;
		this.description = description;
		this.imageUrl = imageUrl;
	}

	public BookInfo info() {
		return new BookInfo(isbn, authors, title, price);
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public BookBinding getBinding() {
		return binding;
	}

	public void setBinding(BookBinding binding) {
		this.binding = binding;
	}

	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "Book{isbn=" + isbn + '}';
	}
}
