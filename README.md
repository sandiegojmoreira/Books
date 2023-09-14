# API RESTful de Libros

Este es un proyecto Java creado con Maven que implementa una API RESTful para gestionar una colección de libros. La API proporciona un único recurso llamado "books" que permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en libros.

## URL de la API

Cuando se levanta la API, puedes acceder a los libros a través de la siguiente URL:

http://localhost:8080/api/books

css
Copy code

## Formato del Cuerpo JSON

El contenido del cuerpo JSON para crear o actualizar un libro debe seguir el siguiente formato:

json
{
  "isbn": "abc",
  "title": "Sample Book",
  "author": "John Doe",
  "price": 19.99,
  "publicationDate": "2023-09-01T12:00:00",
  "genre": "Misterio"
}

Operaciones Disponibles
La API admite las siguientes operaciones:

GET /api/books: Obtiene el listado de libros. Puedes usar los parámetros adicionales page y limit para la paginación.

GET /api/books/{isbn}: Obtiene un libro específico por su ISBN.

POST /api/books: Crea un nuevo libro utilizando el formato de cuerpo JSON mencionado anteriormente.

PUT /api/books/{isbn}: Actualiza un libro existente por su ISBN utilizando el formato de cuerpo JSON mencionado anteriormente.

DELETE /api/books/{isbn}: Elimina un libro por su ISBN.

Ejemplo de Uso
Aquí hay un ejemplo de cómo obtener el listado de libros utilizando la opción GET con paginación:
GET /api/books?page=1&limit=10

Esto devolverá los primeros 10 libros de la colección.



## Interfaz de Línea de Comando (CLI)

Además de la API RESTful, este proyecto también ofrece una interfaz de línea de comando (CLI) para interactuar con la colección de libros. A continuación, se muestran los comandos disponibles y sus argumentos correspondientes:

### Comandos Disponibles

#### `create`

Crea un nuevo libro con la siguiente sintaxis:

- `-i, --isbn`: Especifica el ISBN del libro.
- `-t, --title`: Especifica el título del libro.
- `-a, --author`: Especifica el autor del libro.
- `-p, --price`: Especifica el precio del libro.
- `-d, --publicationDate`: Especifica la fecha de publicación del libro.
- `-g, --genre`: Especifica el género del libro.

Ejemplo de uso:
java -jar CliMain.jar create -i <isbn> -t <titulo> -a <autor> -p <precio> -d <fechaPublicacion> -g <genero>
java -jar CliMain.jar create -i abc -t "Sample Book" -a "John Doe" -p 19.99 -d "2023-09-01T12:00:00" -g "Misterio"



Este comando creará un nuevo libro con los detalles proporcionados.

#### `update`

Actualiza un libro existente con la siguiente sintaxis:

java -jar CliMain.jar update -i <isbn> -t <titulo> -a <autor> -p <precio> -d <fechaPublicacion> -g <genero>


Los argumentos son los mismos que para el comando `create`. Este comando actualizará un libro existente con los detalles proporcionados.

#### `read`

Lee un libro por su ISBN con la siguiente sintaxis:

java -jar CliMain.jar read -i <isbn>


- `-i, --isbn`: Especifica el ISBN del libro que deseas leer.

#### `delete`

Elimina un libro por su ISBN con la siguiente sintaxis:

java -jar CliMain.jar delete -i <isbn>

- `-i, --isbn`: Especifica el ISBN del libro que deseas eliminar.

#### `readall`

Obtiene el listado de libros con opciones de paginación:

java -jar CliMain.jar readall -p 1 -l 10

- `-p, --page`: Especifica la página de resultados que deseas obtener.
- `-l, --limit`: Especifica la cantidad máxima de resultados por página.

Ejemplo de uso:

java -jar CliMain.jar readall -p 1 -l 10

Este comando devolverá los primeros 10 libros de la colección en la página 1.




