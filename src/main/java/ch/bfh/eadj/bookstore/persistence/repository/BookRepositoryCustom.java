package ch.bfh.eadj.bookstore.persistence.repository;

import ch.bfh.eadj.bookstore.persistence.dto.BookInfo;

import java.util.List;

public interface BookRepositoryCustom {

	public List<BookInfo> findByKeywords(String keywords);
}
