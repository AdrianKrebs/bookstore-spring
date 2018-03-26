package ch.bfh.eadj.bookstore.persistence.repository;


import ch.bfh.eadj.bookstore.persistence.dto.BookInfo;
import ch.bfh.eadj.bookstore.persistence.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<BookInfo> findByKeywords(String keywords) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<BookInfo> query = cb.createQuery(BookInfo.class);
		Root<Book> root = query.from(Book.class);
		query.select(cb.construct(BookInfo.class, root.get("isbn"), root.get("authors"), root.get("title"), root.get("price")));
		query.where(containsKeywords(cb, root, keywords));
		return entityManager.createQuery(query).getResultList();
	}

	private Predicate containsKeywords(CriteriaBuilder cb, Root<Book> root, String keywords) {
		List<Predicate> predicates = new ArrayList<>();
		for (String keyword : keywords.toLowerCase().split("\\s+")) {
			predicates.add(containsKeyword(cb, root, keyword));
		}
		return cb.and(predicates.toArray(new Predicate[0]));
	}

	private Predicate containsKeyword(CriteriaBuilder cb, Root<Book> root, String keyword) {
		return cb.or(containsKeyword(cb, root, "isbn", keyword),
				containsKeyword(cb, root, "authors", keyword),
				containsKeyword(cb, root, "title", keyword),
				containsKeyword(cb, root, "publisher", keyword));
	}

	private Predicate containsKeyword(CriteriaBuilder cb, Root<Book> root, String attribute, String keyword) {
		return cb.like(cb.lower(root.<String>get(attribute)), "%" + keyword + "%");
	}
}
