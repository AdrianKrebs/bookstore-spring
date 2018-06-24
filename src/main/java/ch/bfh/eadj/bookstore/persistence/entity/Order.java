package ch.bfh.eadj.bookstore.persistence.entity;


import ch.bfh.eadj.bookstore.persistence.dto.OrderInfo;
import ch.bfh.eadj.bookstore.persistence.enumeration.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOOK_ORDER")
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NR")
	private Long nr;
	@Column(name = "ORDER_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	@Column(name = "AMOUNT", precision = 7, scale = 2, nullable = false)
	private BigDecimal amount;
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CUSTOMER_NR")
	private Customer customer;
	@Embedded
	private Address address;
	@Embedded
	private CreditCard creditCard;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ORDER_NR")
	private List<OrderItem> items;

	public Order() {
	}

	public Order(Date date, BigDecimal amount, OrderStatus status,
			Customer customer, Address address, CreditCard creditCard, List<OrderItem> items) {
		this.date = date;
		this.amount = amount;
		this.status = status;
		this.customer = customer;
		this.address = address;
		this.creditCard = creditCard;
		this.items = items;
	}

	public OrderInfo info() {
		return new OrderInfo(nr, date, amount, status);
	}

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard card) {
		this.creditCard = card;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Order{nr=" + nr + '}';
	}
}
