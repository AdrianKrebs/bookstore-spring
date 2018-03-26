package ch.bfh.eadj.bookstore.persistence.dto;

import ch.bfh.eadj.bookstore.persistence.enumeration.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class OrderInfo implements Serializable {

	private Long nr;
	private Date date;
	private BigDecimal amount;
	private OrderStatus status;

	public OrderInfo() {
	}

	public OrderInfo(Long nr, Date date, BigDecimal amount, OrderStatus status) {
		this.nr = nr;
		this.date = date;
		this.amount = amount;
		this.status = status;
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
}
