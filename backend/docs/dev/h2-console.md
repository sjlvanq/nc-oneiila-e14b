##  ChurnCheck - Backend 
# Documentación de desarrollo: H2 Console

* [Backend - README.md](../README.md)
* [Backend - Documentación de desarrollo](dev/README.md)

-----

* **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
1. Al correr la aplicación identificar la línea que contenga la cadena de conexión y el usuario 
```
2025-12-...  INFO 5140 --- [demo] [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2025-12-...  INFO 5140 --- [demo] [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:testdb user=SA
2025-12-...  INFO 5140 --- [demo] [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
```
2. Usar la cadena de conexión y el usuario como valor de `JDBC URL` y `User Name` respectivamente
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: (vacío por defecto)
```
**Dichos valores también pueden consultarse en el `application.yaml`**

¡Todo muy bien!