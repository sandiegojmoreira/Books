# Books

El repositorio se levanta con eclipse y se consume con una API HTTP.

Las requests de la API para el CRUD de libros son las siguientes:

Create
http://localhost:8080/create?isbn=27&title=27&author=27&price=27&publicationDate=2023-12-12%2012:00:00&genre=Misterio

Read
http://localhost:8080/read?isbn=27

Update
http://localhost:8080/update?isbn=27&title=myTitle&author=Joaquin&price=10811&publicationDate=2012-10-16%2012:00:00&genre=Misterio

Delete
http://localhost:8080/delete?isbn=27

