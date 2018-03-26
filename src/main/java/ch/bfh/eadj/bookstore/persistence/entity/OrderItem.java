package ch.bfh.eadj.bookstore.persistence.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nr;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "ISBN")
	private Book book;
	@Column(name = "QUANTITY", nullable = false)
	private Integer quantity;

	public OrderItem() {
	}

	public OrderItem(Book book, Integer quantity) {
		this.book = book;
		this.quantity = quantity;
	}

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderItem{nr=" + nr + '}';
	}
}
