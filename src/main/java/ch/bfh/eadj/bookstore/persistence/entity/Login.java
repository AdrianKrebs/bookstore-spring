package ch.bfh.eadj.bookstore.persistence.entity;

import ch.bfh.eadj.bookstore.persistence.enumeration.UserGroup;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "LOGIN")
public class Login implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nr;
	@Column(name = "USER_NAME", nullable = false, unique = true)
	private String username;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "USER_GROUP")
	@Enumerated(EnumType.STRING)
	private UserGroup group;

	public Login() {
	}

	public Login(String username, String password, UserGroup group) {
		this.username = username;
		this.password = password;
		this.group = group;
	}

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Login{username=" + username + '}';
	}
}
