import java.time.format.DateTimeParseException;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import spark.Spark;

public class ApiMain {

	public static void main(String[] args) {

		String connectionString = "mongodb://localhost:27017";
		MongoClient mongoClient = MongoClients.create(connectionString);
		String databaseName = "books_database";
		BookRepository bookRepository = new MongoBookRepository(mongoClient, databaseName);
		BookService bookService = new BookService(bookRepository);

		Spark.port(8080);

		Spark.post("/api/books", (req, res) -> {

			Book newBook;
			try {
				newBook = Book.deserializeFromJson(req.body());
			} catch (IllegalArgumentException e) {
				res.status(400);
				return e.getMessage();
			} catch (DateTimeParseException e) {
				res.status(400);
				return "Error parsing date. Correct format: 2023-09-01T12:00:00";
			}

			Response<Book> retrievedBook = bookService.findBookByIsbn(newBook.getIsbn());

			if (!retrievedBook.isSuccess()) {

				Response<Book> createResponse = bookService.createBook(newBook);
				if (createResponse.isSuccess()) {
					res.status(201);
					res.type("application/json");
					return Book.serializeToJson(newBook);
				} else {
					res.status(409);
					return createResponse.getMessage();
				}
			} else {
				res.status(409);
				return "Book with ISBN " + newBook.getIsbn() + " already exist.";
			}

		});

		Spark.get("/api/books", (req, res) -> {

			int page = Integer.parseInt(req.queryParamOrDefault("page", "1"));
			int limit = Integer.parseInt(req.queryParamOrDefault("limit", "10"));

			// Get all books
			Response<List<Book>> allBooks = bookService.findAllBooks(page, limit);
			if (allBooks.isSuccess()) {

				Response<Long> totalItems = bookService.getTotalBookCount();

				res.type("application/json");
				return Book.serializeToJson(allBooks.getData(), totalItems.getData(), limit, page);
			} else {
				res.status(409);
				return allBooks.getMessage();
			}

			// return new Gson().toJson(allBooks);
		});

		Spark.get("/api/books/:isbn", (req, res) -> {
			String isbn = req.params(":isbn");

			// Read a book
			Response<Book> retrievedBook = bookService.findBookByIsbn(isbn);

			if (retrievedBook.isSuccess()) {
				res.type("application/json");
				// return new Gson().toJson(retrievedBook);
				return Book.serializeToJson(retrievedBook.getData());

			} else {
				res.status(404);
				return "Book not found";
			}

		});

		Spark.put("/api/books/:isbn", (req, res) -> {

			String isbn = req.params(":isbn");

			Book putBook;
			try {
				putBook = Book.deserializeFromJson(req.body());
			} catch (IllegalArgumentException e) {
				res.status(400);
				return e.getMessage();
			} catch (DateTimeParseException e) {
				res.status(400);
				return "Error parsing date. Correct format: 2023-09-01T12:00:00";
			}

			// Read a book
			Response<Book> retrievedBook = bookService.findBookByIsbn(isbn);

			if (retrievedBook.isSuccess()) {
				// Update the book

				try {

					if (putBook.getTitle() != null) {
						retrievedBook.getData().setTitle(putBook.getTitle());
					}
					if (putBook.getAuthor() != null) {
						retrievedBook.getData().setAuthor(putBook.getAuthor());
					}
					if (putBook.getPrice() != 0.0) {
						retrievedBook.getData().setPrice(putBook.getPrice());
					}
					if (putBook.getPublicationDate() != null) {
						retrievedBook.getData().setPublicationDate(putBook.getPublicationDate());
					}
					if (putBook.getGenre() != null) {
						retrievedBook.getData().setGenre(putBook.getGenre());
					}

					Response<Book> updatedBook = bookService.updateBook(isbn, retrievedBook.getData());
					res.status(200);
					return Book.serializeToJson(updatedBook.getData());

				} catch (IllegalArgumentException e) {
					res.status(400);
					return e.getMessage();
				}

			} else {
				res.status(404);
				return retrievedBook.getMessage() + isbn;
			}

		});

		Spark.delete("/api/books/:isbn", (req, res) -> {
			String isbn = req.params(":isbn");

			// Delete the book

			Response<Book> retrievedBook = bookService.findBookByIsbn(isbn);

			if (retrievedBook.isSuccess()) {

				Response<Void> deletedBook = bookService.deleteBook(isbn);
				if (deletedBook.isSuccess()) {
					res.status(200);
					return "Book with ISBN " + isbn + " deleted.";
				} else {
					res.status(400);
					return deletedBook.getMessage();
				}

			} else {
				res.status(404);
				return "Book not found.";
			}

		});
	}

}
