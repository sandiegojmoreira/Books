import java.util.List;

public interface BookRepository {
	Response<Book> create(Book book);

	Response<Book> findByIsbn(String isbn);

	Response<List<Book>> findAll(int page, int limit);

	Response<Long> getTotalCount();

	Response<Book> update(String isbn, Book book);

	Response<Void> delete(String isbn);
}
