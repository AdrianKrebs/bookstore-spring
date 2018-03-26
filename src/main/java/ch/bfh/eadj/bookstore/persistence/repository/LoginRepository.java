package ch.bfh.eadj.bookstore.persistence.repository;


import ch.bfh.eadj.bookstore.persistence.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, String> {

	public Login findByUsername(String username);
}
