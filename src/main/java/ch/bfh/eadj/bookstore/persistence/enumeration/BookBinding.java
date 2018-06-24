package ch.bfh.eadj.bookstore.persistence.enumeration;

public enum BookBinding {

	HARDCOVER, PAPERBACK, EBOOK, UNKNOWN;


	public static BookBinding getBinding(String value) {
		try {
			return BookBinding.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			return BookBinding.UNKNOWN;
		}
	}
}
