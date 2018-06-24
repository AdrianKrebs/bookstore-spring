package ch.bfh.eadj.bookstore.persistence.repository;


import ch.bfh.eadj.bookstore.persistence.dto.OrderInfo;
import ch.bfh.eadj.bookstore.persistence.entity.Customer;
import ch.bfh.eadj.bookstore.persistence.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

	public Order findByNr(long nr);

	@Query("SELECT NEW ch.bfh.eadj.bookstore.persistence.dto.OrderInfo(o.nr, o.date, o.amount, o.status) "
			+ "FROM Order o WHERE o.customer = :customer AND YEAR(o.date) = :year")
	public List<OrderInfo> findByCustomerAndYear(@Param("customer") Customer customer, @Param("year") Integer year);
}
