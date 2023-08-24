import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Application {

	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

		server.createContext("/create", new CreateBookHandler());
		server.createContext("/read", new ReadBookHandler());
		server.createContext("/update", new UpdateBookHandler());
		server.createContext("/delete", new DeleteBookHandler());

		server.setExecutor(null);
		server.start();
	}

	private static LocalDateTime dateTime(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
		return dateTime;
	}

}

class CreateBookHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Parse query parameters from the request URL
		String query = exchange.getRequestURI().getQuery();
		Map<String, List<String>> queryParams = HttpUtils.parseQuery(query);

		// Access and process the parameters
		String isbn = queryParams.getOrDefault("isbn", List.of("Unknown")).get(0);
		String title = queryParams.getOrDefault("title", List.of("Unknown")).get(0);
		String author = queryParams.getOrDefault("author", List.of("Unknown")).get(0);
		String priceString = queryParams.getOrDefault("price", List.of("Unknown")).get(0);
		String publicationDateString = queryParams.getOrDefault("publicationDate", List.of("Unknown")).get(0);
		String genre = queryParams.getOrDefault("genre", List.of("Unknown")).get(0);

		boolean isValid = true;

		double priceDouble = 0;
		try {
			priceDouble = Double.parseDouble(priceString);
		} catch (NumberFormatException e) {
			System.out.println("Invalid price.");
			isValid = false;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime publicationDate = null;
		try {
			publicationDate = LocalDateTime.parse(publicationDateString, formatter);

		} catch (DateTimeParseException e) {
			System.out.println("Invalid date string or format");
			isValid = false;
		}

		String response;
		if (isValid) {
			try {
				new Book(isbn, title, author, priceDouble, publicationDate, genre).create();
				response = "Book created";
			} catch (IllegalArgumentException e) {
				response = e.getMessage();
				isValid = false;
			}

		} else {
			// Construct the response
			response = "Invalid parameters.";
		}
		if (isValid) {
			exchange.sendResponseHeaders(200, response.length());

		} else {
			exchange.sendResponseHeaders(400, response.length());

		}
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response.getBytes());
		}
	}

}

class ReadBookHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Parse query parameters from the request URL
		String query = exchange.getRequestURI().getQuery();
		Map<String, List<String>> queryParams = HttpUtils.parseQuery(query);

		// Access and process the parameters
		String isbn = queryParams.getOrDefault("isbn", List.of("Unknown")).get(0);

		String response;
		try {
			// Construct the response
			response = Book.search(isbn).toString();
			exchange.sendResponseHeaders(200, response.length());
		} catch (IllegalArgumentException e) {
			response = e.getMessage();
			exchange.sendResponseHeaders(400, response.length());
		}

		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response.getBytes());
		}
	}
}

class UpdateBookHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Parse query parameters from the request URL
		String query = exchange.getRequestURI().getQuery();
		Map<String, List<String>> queryParams = HttpUtils.parseQuery(query);

		// Access and process the parameters
		String isbn = queryParams.getOrDefault("isbn", List.of("Unknown")).get(0);
		String title = queryParams.getOrDefault("title", List.of("Unknown")).get(0);
		String author = queryParams.getOrDefault("author", List.of("Unknown")).get(0);
		String priceString = queryParams.getOrDefault("price", List.of("Unknown")).get(0);
		String publicationDateString = queryParams.getOrDefault("publicationDate", List.of("Unknown")).get(0);
		String genre = queryParams.getOrDefault("genre", List.of("Unknown")).get(0);

		boolean isValid = true;

		double priceDouble = 0;
		try {
			priceDouble = Double.parseDouble(priceString);
		} catch (NumberFormatException e) {
			System.out.println("Invalid price.");
			isValid = false;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime publicationDate = null;
		try {
			publicationDate = LocalDateTime.parse(publicationDateString, formatter);
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date string or format");
			isValid = false;
		}

		String response;
		if (isValid) {
			try {
				Book.search(isbn).update(title, author, priceDouble, publicationDate, genre);
				response = "Book updated succesfully.";
			} catch (IllegalArgumentException e) {
				response = e.getMessage();
				isValid = false;
			}

		} else {
			// Construct the response
			response = "Invalid parameters.";
		}
		if (isValid) {
			exchange.sendResponseHeaders(200, response.length());

		} else {
			exchange.sendResponseHeaders(400, response.length());

		}
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response.getBytes());
		}
	}
}

class DeleteBookHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Parse query parameters from the request URL
		String query = exchange.getRequestURI().getQuery();
		Map<String, List<String>> queryParams = HttpUtils.parseQuery(query);

		// Access and process the parameters
		String isbn = queryParams.getOrDefault("isbn", List.of("Unknown")).get(0);

		// Construct the response
		String response = Book.delete(isbn);

		exchange.sendResponseHeaders(200, response.length());
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response.getBytes());
		}
	}
}
