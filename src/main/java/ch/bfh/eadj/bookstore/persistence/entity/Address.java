package ch.bfh.eadj.bookstore.persistence.entity;


import ch.bfh.eadj.bookstore.persistence.enumeration.Country;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {

	@NotNull
	@Column(name = "ADDR_STREET", nullable = false)
	private String street;
	@NotNull
	@Column(name = "ADDR_CITY", nullable = false)
	private String city;
	@NotNull
	@Column(name = "ADDR_POSTAL_CODE", nullable = false)
	private String postalCode;
	@NotNull
	@Column(name = "ADDR_COUNTRY", nullable = false)
	@Enumerated(EnumType.STRING)
	private Country country;

	public Address() {
	}

	public Address(String street, String city, String postalCode, Country country) {
		this.street = street;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
