import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Book implements Comparable<Book> {

	private final String isbn;
	private String title;
	private String author;
	private Double price;
	private LocalDateTime publicationDate;
	private String genre;

	public Book(String isbn, String title, String author, Double price, LocalDateTime publicationDate, String genre) {
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

	public Book(String title, String author, Double price, LocalDateTime publicationDate, String genre) {

		// JM - Constructor usado para deserializar.
		this.isbn = "";
		this.title = title;
		this.author = author;
		this.price = price;
		this.publicationDate = publicationDate;
		this.genre = genre;

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

	public static LocalDateTime changePublicationDateType(String publicationDateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime publicationDate = null;
		try {
			publicationDate = LocalDateTime.parse(publicationDateString, formatter);
			return publicationDate;
		} catch (DateTimeParseException e) {
			// System.out.println(e.getMessage());
			return null;
		}
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
		return "Book{" + "isbn='" + isbn + '\'' + ", title='" + title + '\'' + ", author='" + author + '\'' + ", price="
				+ price + ", publicationDate=" + publicationDate + ", genre='" + genre + '\'' + '}';
	}

	@Override
	public int compareTo(Book otherBook) {
		return this.isbn.compareTo(otherBook.isbn);
	}

	public static String serializeToJson(Book book) {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).create();
		return gson.toJson(book);
	}

	public static String serializeToJson(List<Book> books) {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).create();
		return gson.toJson(books);
	}

	public static String serializeToJson(List<Book> books, long totalItems, int limit, int page) {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).create();
		int totalPages = (int) Math.ceil((double) totalItems / limit);
		Map<String, Object> dataMap = new LinkedHashMap<>();
		dataMap.put("page", page);
		dataMap.put("totalItems", totalItems);
		dataMap.put("totalPages", totalPages);
		dataMap.put("data", books);

		return gson.toJson(dataMap);
	}

	public static Book deserializeFromJson(String json) throws DateTimeParseException {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
				.create();
		return gson.fromJson(json, Book.class);
	}

	private static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
		private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

		@Override
		public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
			return context.serialize(formatter.format(src));
		}
	}

	private static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
		private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

		@Override
		public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			String dateTimeString = json.getAsString();
			return LocalDateTime.parse(dateTimeString, formatter);
		}
	}

}
