# Convenciones y documentación para desarrolladores - NombreDelProyecto
## Los commits y sus mensajes
* [README principal](../../README.md)
* [README documentación](../README.md)
* [README desarrollo](README.md)

---

### Resumen: Las 3 Reglas de Oro
1. **Un solo cambio por commit:** Si arreglaste un bug y también cambiaste el color de un botón, haz **dos** commits diferentes.
2. **Usa el infinitivo:** Escribe "agregar feature" en lugar de "agregada feature" o "agregando feature". Es como darle una orden al código.
3. **Estructura fija:** Usa siempre el formato `tipo: descripción`.

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

❌ **Malos (Evítalos):**
* `fix: ya funciona` (¿Qué funciona?)
* `cambios` (Muy vago y falta el tipo)
* `feat: arregle el login y cambie el footer y borré un logo` (Demasiadas cosas juntas)

✅ **Buenos (Cópialos):**
* `feat: conectar api de productos`
* `fix: corregir error de tipeo en el nombre de usuario`
* `docs: actualizar instrucciones de instalación en README.md`
* `style: ajustar intentación en codigo.java`

