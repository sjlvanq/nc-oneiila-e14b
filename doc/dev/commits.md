# Convenciones y documentación para desarrolladores - NombreDelProyecto
## Los commits y sus mensajes
* [README principal](../../README.md)
* [README documentación](../README.md)
* [README desarrollo](README.md)

---

### Resumen: Reglas de Oro
1. **Un solo cambio por commit:** Si arreglaste un bug y también cambiaste el color de un botón, haz **dos** commits diferentes.
2. **En inglés:** Todos los mensajes de commits estarán en inglés y comenzarán en minúsculas.
3. **Usa el infinitivo (imperativo en inglés):** Escribe "uncomment method" en lugar de "uncommented" o "uncommenting". Es como darle una orden al código.
4. **Estructura fija:** Usa siempre el formato `tipo: descripción`.

---

### Estructura del Mensaje
Todos tus commits deben seguir este formato:

`tipo: descripción corta en minúsculas`

*Ejemplo: `feat: agregar validación al formulario de registro`*

---

### Tipos de Commit

| Tipo | Cuándo usarlo |
| :--- | :--- |
| **feat** | Una nueva funcionalidad (ej. un nuevo endpoint). |
| **fix** | Solución de un error o bug. |
| **docs** | Cambios sólo en la documentación (README, comentarios). |
| **style** | Cambios visuales o de formato (espacios, comas) que no afectan la lógica. |
| **refactor** | Código cambiado que ni arregla un bug ni añade una función. |
| **test** | Añadir o corregir pruebas unitarias. |

---

### Ejemplos

**❌ Malos (Evítalos):**

* fix: it works now (Vago y no describe la solución).
* cambios (Falta el tipo y está en español).
* feat: fix login and change footer and delete logo (Demasiadas responsabilidades en un solo commit).

**✅ Buenos:**

* feat: connect products api
* fix: resolve typo in username field
* docs: update installation instructions in README.md
* style: fix indentation in auth controller
