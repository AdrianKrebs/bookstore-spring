package ch.bfh.eadj.bookstore.persistence.entity;


import ch.bfh.eadj.bookstore.persistence.enumeration.CreditCardType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class CreditCard implements Serializable {

	@NotNull
	@Column(name = "CC_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private CreditCardType type;
	@NotNull
	@Column(name = "CC_NUMBER", nullable = false)
	private String number;
	@NotNull
	@Column(name = "CC_EXP_MONTH", nullable = false)
	private Integer expirationMonth;
	@NotNull
	@Column(name = "CC_EXP_YEAR", nullable = false)
	private Integer expirationYear;

	public CreditCard() {
	}

	public CreditCard(CreditCardType type, String number, Integer expirationMonth, Integer expirationYear) {
		this.type = type;
		this.number = number;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
	}

	public CreditCardType getType() {
		return type;
	}

	public void setType(CreditCardType type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}
}
