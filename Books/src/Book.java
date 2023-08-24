import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Book implements Comparable<Book> {

	private final String isbn;
	private String title;
	private String author;
	private Double price;
	private LocalDateTime publicationDate;
	private String genre;

	// Static TreeSet to store all books
	private static TreeSet<Book> bookDatabase = new TreeSet<>();

	public Book(String isbn, String title, String author, Double price, LocalDateTime publicationDate, String genre) {
		System.out.println("New book generated.");
		if (isValidIsbn(isbn) && isValidTitle(title) && isValidAuthor(author) && isValidPrice(price)
				&& isValidPublicationDate(publicationDate) && isValidGenre(genre)) {

			this.isbn = isbn;
			this.title = title;
			this.author = author;
			this.price = price;
			this.publicationDate = publicationDate;
			this.genre = genre;

		} else {
			throw new IllegalArgumentException("Invalid attributes provided");
		}

	}

	private Book(String isbn) {
		System.out.println("New book generated.");
		if (isValidIsbn(isbn)) {
			this.isbn = isbn;
		} else {
			throw new IllegalArgumentException("Invalid attributes provided");
		}

	}

	public static Book search(String isbnTarget) {
		// Using floor to find the exact book with matching ISBN
		Book targetBook = new Book(isbnTarget);
		Book floorBook = bookDatabase.floor(targetBook);
		if (floorBook != null && floorBook.equals(targetBook)) {
			System.out.println("Found book with ISBN: " + floorBook.toString());
			return floorBook;
		} else {
			throw new IllegalArgumentException("No book found with matching ISBN");
		}
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (isValidTitle(title)) {
			this.title = title;
		} else {
			throw new IllegalArgumentException("Invalid title");
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if (isValidAuthor(author)) {
			this.author = author;
		} else {
			throw new IllegalArgumentException("Invalid author");
		}
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		if (isValidPrice(price)) {
			this.price = price;
		} else {
			throw new IllegalArgumentException("Invalid price");
		}
	}

	public LocalDateTime getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(LocalDateTime publicationDate) {
		if (isValidPublicationDate(publicationDate)) {
			this.publicationDate = publicationDate;
		} else {
			throw new IllegalArgumentException("Invalid publication date");
		}
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		if (isValidGenre(genre)) {
			this.genre = genre;
		} else {
			throw new IllegalArgumentException("Invalid genre");
		}
	}

	private boolean isValidIsbn(String isbn) {
		return isbn != null && !isbn.isEmpty();
	}

	private boolean isValidTitle(String title) {
		return title != null && !title.isEmpty();
	}

	private boolean isValidAuthor(String author) {
		return author != null && !author.isEmpty();
	}

	private boolean isValidPrice(Double price) {
		return price != null && price >= 0;
	}

	private boolean isValidPublicationDate(LocalDateTime date) {
		return date != null; // Example validation: Date is in the past
	}

	private boolean isValidGenre(String genre) {
		Set<String> validGenres = new HashSet<>();
		validGenres.add("Misterio");
		validGenres.add("Ciencia ficción");
		validGenres.add("Romance");
		validGenres.add("Drama");
		validGenres.add("Aventura");
		validGenres.add("Acción");
		validGenres.add("Terror");
		validGenres.add("Histórico");
		validGenres.add("Biografía");
		validGenres.add("Filosofía");
		validGenres.add("Política");
		validGenres.add("Negocios");
		validGenres.add("Autoayuda");

		return validGenres.contains(genre);
	}

	public void create() {
		System.out.println("create");

		if (bookDatabase.contains(this)) {
			throw new IllegalArgumentException("Book with ISBN already exists.");
		} else if (!(isValidIsbn(isbn) && isValidTitle(title) && isValidAuthor(author) && isValidPrice(price)
				&& isValidPublicationDate(publicationDate) && isValidGenre(genre))) {
			throw new IllegalArgumentException("Invalid attributes provided");
		}

		else {

			bookDatabase.add(this);
			System.out.println("New book added: " + this.toString());

		}

	}

	public String update(String title, String author, Double price, LocalDateTime publicationDate, String genre) {
		System.out.println("update");

		if (!(isValidTitle(title) && isValidAuthor(author) && isValidPrice(price)
				&& isValidPublicationDate(publicationDate) && isValidGenre(genre))) {
			throw new IllegalArgumentException("Invalid attributes provided");
		} else {
			this.setTitle(title);
			this.setAuthor(author);
			this.setPrice(price);
			this.setPublicationDate(publicationDate);
			this.setGenre(genre);

			return "Book updated.";
		}
	}

	public static String delete(String isbnTarget) {
		System.out.println("delete");
		Book bookTarget = new Book(isbnTarget);

		if (bookDatabase.contains(bookTarget)) {
			bookDatabase.remove(bookTarget);
			return "Book deleted.";
		} else {
			return "Book does not exist in book database.";
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(isbn, other.isbn);
	}

	@Override
	public String toString() {
		return "{\"isbn\":\"" + isbn + "\", \"title\":\"" + title + "\", \"author\":\"" + author + "\", \"price\":\""
				+ price + "\", \"publicationDate\":\"" + publicationDate + "\", \"genre\":\"" + genre + "\"}";
	}

	@Override
	public int compareTo(Book otherBook) {
		return this.isbn.compareTo(otherBook.isbn);
	}

	// Static method to access the book database
	public static TreeSet<Book> getBookDatabase() {
		return bookDatabase;
	}

}
