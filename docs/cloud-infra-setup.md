# Infraestructura en la nube (OCI)

## ¿Qué se utilizó?

Se utilizó **Oracle Cloud Infrastructure (OCI)**, que es una plataforma de computación en la nube. En lugar de correr todo en una laptop personal, se crea una **instancia** (un servidor virtual en internet) que queda disponible para instalar herramientas, guardar archivos del proyecto y ejecutar servicios del proyecto de forma remota.

### ¿Por qué se eligió OCI?

* Permite tener un servidor **24/7** accesible desde cualquier lugar.
* Es una opción común en proyectos académicos porque tiene modalidad **Free Tier** (recursos gratuitos con limitaciones).
* Facilita que varias personas del equipo trabajen sobre el mismo entorno.

### ¿Para qué se usó la instancia?

* Tener un entorno centralizado para el proyecto.
* Subir archivos del equipo (modelos, configuraciones, etc.).
* Preparar el servidor para que más adelante se puedan hacer pruebas del proyecto desde fuera.

---

# Complicaciones encontradas

## 1) Disponibilidad limitada en Free Tier

**Una complicación importante** fue que no siempre hay disponibilidad para crear instancias gratuitas (Free Tier), ya que los recursos se llenan dependiendo de la demanda en la región.

---

# Configuración de la instancia

## Versión del sistema operativo y por qué

Se usó **Ubuntu 22.04 (LTS)**.

**Razones:**

* Es una versión estable y ampliamente usada en servidores.
* “LTS” significa *Long Term Support*, es decir, tiene soporte y actualizaciones por varios años.
* Tiene buena compatibilidad con herramientas comunes de ciencia de datos y despliegue.

---

# Accesos y seguridad

## Actualización inicial del sistema

Al iniciar el servidor, se hizo la actualización básica del sistema para asegurar que el entorno estuviera al día (parches, seguridad y compatibilidad).

## Creación y configuración de usuarios

Se configuraron usuarios separados para organizar el acceso y evitar que todo se haga con una sola cuenta.

**Ejemplo de propósito:**

* **Usuario “backend”**: pensado para tareas relacionadas con el servidor y despliegue.
* **Usuario “ds” (data science)**: pensado para tareas de análisis/modelos.

Esto ayuda a separar responsabilidades, minimizar errores accidentales y mantener orden cuando el equipo crece.

## Acceso por claves SSH

El acceso remoto se configuró con **SSH**, que es una forma segura de conectarse al servidor desde otra computadora.

Se usaron **claves SSH** (llave pública/privada) porque:

* Es más seguro que usar solo contraseñas.
* Evita intentos fáciles de acceso no autorizado.
* Permite dar acceso a varios miembros del equipo con sus propias llaves.

---

# Resultado

Al finalizar esta etapa:

* La instancia quedó creada y funcional en OCI.
* El sistema operativo quedó definido (Ubuntu 22.04 LTS).
* Se dejaron accesos y usuarios listos para el trabajo en equipo.
