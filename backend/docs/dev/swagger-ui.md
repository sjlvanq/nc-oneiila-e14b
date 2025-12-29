##  ChurnCheck - Backend 
# Documentación de desarrollo: Swagger UI

* [Backend - README.md](../README.md)
* [Backend - Documentación de desarrollo](dev/README.md)

-----

* **URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* Obtener un Token mediante el endpoint `/login (auth-controller)`
  1. El botón `Try it out` habilita la edición del `Request body`
  2. Reemplazar los valores (por defecto "string") de las claves "email" y "password"
```
{
  "email": "admin@demo.com",
  "password": "123456"
}
```
  3. Enviar la solicitud con el botón `Execute`.
  4. Si los datos enviados corresponden a un usuario del sistema se recibirá una respuesta de código 200 con el token, algo similar a "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJDaHVybkNoZWNrQVBJIiwiZXhwIjoxNzY2OTk2NDUxLCJzdWIiOiJhZG1pbkBkZW1vLmNvbSJ9.wNArKPj-yZUx9mffL-TupWfZAdofOF7kYsavAZy6ORw". Copiarlo.
* Abrir el cuadro de diálogo `Available authorizations` con el botón `Authorize` ubicado en la parte superior de la página.
* Pegar el token (sin comillas) en el input `Value`. Guardarlo con `Authorize` y cerrar el diálogo con `Close`.
* Probar la conexión en el endpoint `/test (test-controller)`

¡Todo muy bien!