import java.util.List;

public class BookService {
	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Response<Book> createBook(Book book) {
		return bookRepository.create(book);
	}

	public Response<Book> findBookByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	public Response<List<Book>> findAllBooks(int page, int limit) {
		return bookRepository.findAll(page, limit);
	}

	public Response<Long> getTotalBookCount() {
		return bookRepository.getTotalCount();
	}

	public Response<Book> updateBook(String isbn, Book book) {
		return bookRepository.update(isbn, book);
	}

	public Response<Void> deleteBook(String isbn) {
		return bookRepository.delete(isbn);
	}
}
