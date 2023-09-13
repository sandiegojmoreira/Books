import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoBookRepository implements BookRepository {
	private final MongoCollection<Document> collection;

	public MongoBookRepository(MongoClient mongoClient, String databaseName) {
		MongoDatabase database = mongoClient.getDatabase(databaseName);
		this.collection = database.getCollection("books");
	}

	@Override
	public Response<Book> create(Book book) {
		try {
			Document bookDocument = new Document("isbn", book.getIsbn()).append("title", book.getTitle())
					.append("author", book.getAuthor()).append("price", book.getPrice())
					.append("publicationDate", book.getPublicationDate().toString()).append("genre", book.getGenre());

			collection.insertOne(bookDocument);
			return Response.success("Book created successfully", book);
		} catch (Exception e) {
			return Response.error("Failed to create the book");
		}
	}

	@Override
	public Response<Book> findByIsbn(String isbn) {
		try {
			Document document = collection.find(new Document("isbn", isbn)).first();
			if (document == null) {
				return Response.error("Book not found");
			}
			return Response.success("Book found", documentToBook(document));
		} catch (Exception e) {
			return Response.error("Failed to find the book");
		}
	}

	@Override
	public Response<List<Book>> findAll(int page, int limit) {
		try {
			int skip = (page - 1) * limit;
			FindIterable<Document> documents = collection.find().skip(skip).limit(limit);

			List<Book> books = new ArrayList<>();
			for (Document document : documents) {
				books.add(documentToBook(document));
			}

			return Response.success("Books retrieved successfully", books);
		} catch (Exception e) {
			return Response.error("Failed to retrieve books");
		}
	}

	@Override
	public Response<Book> update(String isbn, Book book) {
		try {
			Document bookDocument = new Document("isbn", book.getIsbn()).append("title", book.getTitle())
					.append("author", book.getAuthor()).append("price", book.getPrice())
					.append("publicationDate", book.getPublicationDate().toString()).append("genre", book.getGenre());

			collection.replaceOne(new Document("isbn", isbn), bookDocument);
			return Response.success("Book updated successfully", book);
		} catch (Exception e) {
			return Response.error("Failed to update the book");
		}
	}

	@Override
	public Response<Void> delete(String isbn) {
		try {
			collection.deleteOne(new Document("isbn", isbn));
			return Response.success("Book deleted successfully", null);
		} catch (Exception e) {
			return Response.error("Failed to delete the book");
		}
	}

	@Override
	public Response<Long> getTotalCount() {
		try {
			AggregateIterable<Document> aggregate = collection.aggregate(List
					.of(new Document("$group", new Document("_id", null).append("count", new Document("$sum", 1)))));

			Document result = aggregate.first();
			if (result != null) {
				long totalCount = result.getInteger("count");
				return Response.success("Total count retrieved successfully", totalCount);
			} else {
				return Response.success("Total count is 0", 0L);
			}
		} catch (Exception e) {
			return Response.error("Failed to retrieve total count");
		}
	}

	private Book documentToBook(Document document) {
		return new Book(document.getString("isbn"), document.getString("title"), document.getString("author"),
				document.getDouble("price"), LocalDateTime.parse(document.getString("publicationDate")),
				document.getString("genre"));
	}
}
