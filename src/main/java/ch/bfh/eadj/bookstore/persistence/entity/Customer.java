package ch.bfh.eadj.bookstore.persistence.entity;

import ch.bfh.eadj.bookstore.persistence.dto.CustomerInfo;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NR")
	private Long nr;
	@NotNull
	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;
	@NotNull
	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;
	@NotNull
	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;
	@NotNull
	@Valid
	@Embedded
	private Address address;
	@NotNull
	@Valid
	@Embedded
	private CreditCard creditCard;

	public Customer() {
	}

	public Customer(String firstName, String lastName, String email, Address address, CreditCard creditCard) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.creditCard = creditCard;
	}

	public CustomerInfo info() {
		return new CustomerInfo(nr, firstName, lastName, email);
	}

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		if (address == null) {
			address = new Address();
		}
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public CreditCard getCreditCard() {
		if (creditCard == null) {
			creditCard = new CreditCard();
		}
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Override
	public String toString() {
		return "Customer{nr=" + nr + '}';
	}
}
