package ch.bfh.eadj.bookstore.persistence.repository;


import ch.bfh.eadj.bookstore.persistence.dto.CustomerInfo;
import ch.bfh.eadj.bookstore.persistence.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public Customer findByNr(Long nr);

	@Query("SELECT c.nr FROM Customer c WHERE LOWER(c.email) = LOWER(:email)")
	public Long findNrByEmail(@Param("email") String email);

	@Query("SELECT NEW ch.bfh.eadj.bookstore.persistence.dto.CustomerInfo(c.nr, c.firstName, c.lastName, c.email) "
			+ "FROM Customer c WHERE "
			+ "LOWER(c.firstName) LIKE CONCAT('%', LOWER(:name), '%') OR "
			+ "LOWER(c.lastName) LIKE CONCAT('%', LOWER(:name), '%')")
	public List<CustomerInfo> findByName(@Param("name") String name);
}
