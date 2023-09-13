import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.Callable;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "BookCRUD", mixinStandardHelpOptions = true, version = "1.0", description = "CRUD operations for books")
public class CliMain implements Callable<Integer> {

	static BookService bookService;

	@Command(name = "create", description = "Create a new book")
	void create(@Option(names = { "-i", "--isbn" }, required = true) String isbn,
			@Option(names = { "-t", "--title" }, required = true) String title,
			@Option(names = { "-a", "--author" }, required = true) String author,
			@Option(names = { "-p", "--price" }, required = true) double price,
			@Option(names = { "-d", "--publicationDate" }, required = true) String publicationDate,
			@Option(names = { "-g", "--genre" }, required = true) String genre) {

		// Create new book
		Book newBook;
		try {
			newBook = new Book(isbn, title, author, price, LocalDateTime.parse(publicationDate), genre);
		} catch (IllegalArgumentException e) {

			System.out.println(e.getMessage());
			return;
		} catch (DateTimeParseException e) {

			System.out.println("Error parsing date. Correct format: 2023-09-01T12:00:00");
			return;
		}

		Response<Book> retrievedBook = bookService.findBookByIsbn(newBook.getIsbn());

		if (!retrievedBook.isSuccess()) {
			Response<Book> createResponse = bookService.createBook(newBook);
			if (createResponse.isSuccess()) {
				System.out.println(Book.serializeToJson(newBook));
			} else {
				System.out.println(createResponse.getMessage());
			}
		} else {
			System.out.println("Book with ISBN " + newBook.getIsbn() + " already exist.");
		}
	}

	@Command(name = "readall", description = "Read all books")
	void readall(@Option(names = { "-p", "--page" }, defaultValue = "1") int page,
			@Option(names = { "-l", "--limit" }, defaultValue = "10") int limit) {

		// Get all books
		Response<List<Book>> allBooks = bookService.findAllBooks(page, limit);
		if (allBooks.isSuccess()) {

			Response<Long> totalItems = bookService.getTotalBookCount();

			System.out.println(Book.serializeToJson(allBooks.getData(), totalItems.getData(), limit, page));
		} else {

			System.out.println(allBooks.getMessage());
		}
	}

	@Command(name = "read", description = "Read a book")
	void read(@Option(names = { "-i", "--isbn" }, required = true) String isbn) {

		// Read a book
		Response<Book> retrievedBook = bookService.findBookByIsbn(isbn);

		if (retrievedBook.isSuccess()) {
			System.out.println(Book.serializeToJson(retrievedBook.getData()));

		} else {
			System.out.println("Book not found");
		}
	}

	@Command(name = "update", description = "Update a book")
	void update(@Option(names = { "-i", "--isbn" }, required = true) String isbn,
			@Option(names = { "-t", "--title" }, required = true) String title,
			@Option(names = { "-a", "--author" }, required = true) String author,
			@Option(names = { "-p", "--price" }, required = true) double price,
			@Option(names = { "-d", "--publicationDate" }, required = true) String publicationDate,
			@Option(names = { "-g", "--genre" }, required = true) String genre) {

		Book putBook;
		try {
			putBook = new Book(isbn, title, author, price, LocalDateTime.parse(publicationDate), genre);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		} catch (DateTimeParseException e) {
			System.out.println("Error parsing date. Correct format: 2023-09-01T12:00:00");
			return;
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
				System.out.println(Book.serializeToJson(updatedBook.getData()));

			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}

		} else {
			System.out.println(retrievedBook.getMessage() + isbn);
		}
	}

	@Command(name = "delete", description = "Delete a book")
	void delete(@Option(names = { "-i", "--isbn" }, required = true) String isbn) {
		Response<Book> retrievedBook = bookService.findBookByIsbn(isbn);
		if (retrievedBook.isSuccess()) {
			Response<Void> deletedBook = bookService.deleteBook(isbn);
			if (deletedBook.isSuccess()) {
				System.out.println("Book with ISBN " + isbn + " deleted.");
			} else {
				System.out.println(deletedBook.getMessage());
			}
		} else {
			System.out.println("Book not found.");
		}
	}

	public static void main(String[] args) {

		String connectionString = "mongodb://localhost:27017";
		MongoClient mongoClient = MongoClients.create(connectionString);
		String databaseName = "books_database";
		BookRepository bookRepository = new MongoBookRepository(mongoClient, databaseName);
		bookService = new BookService(bookRepository);

		int exitCode = new CommandLine(new CliMain()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {
		return new CommandLine(this).execute();
	}
}
