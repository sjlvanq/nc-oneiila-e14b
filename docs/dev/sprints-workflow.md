* [README.md principal](../../README.md)
* [CONTRIBUTING.md](../CONTRIBUTING.md)

## Convenciones y documentación para desarrolladores - ChurnCheck
# Workflow Git para sprints

#### Resumen:

* Cada desarrollador a quien se le ha asignado una lista de tareas en el sprint creará una rama con el formato `(data|backend)/sprintN/devX`
* Al finalizar el sprint o concluir las tareas solicitará fusionar sus cambios a la rama principal con un Pull Request.
* Una vez fusionada, la rama de trabajo será eliminada.

### 1. Actualización de la copia local
Traer los últimos cambios de la rama principal `dev`
```shell
git checkout dev
git pull
```

### 2. Creación de una nueva rama 
Crear una nueva rama con el formato `(data|backend)/sprintN/devA` reemplazando `N` por el número de sprint y `A` por el identificador del desarrollador.

```shell
# Suponiendo que el desarrollador A, del equipo de Data Science, comienza su trabajo para el sprint 2:
git checkout -B data/sprint2/devA
```

### 3. Hacer los commits requeridos

```shell
git add .
# O indicando archivos específicos para el commit:
# git add archivo-editado1 archivo-editado2

git commit -m "tipo: descripción del commit"
```

Importante: seguir [convención de mensajes de commit](commits.md).

### 4. Actualizar la rama en remoto
```shell
# Suponiendo que el desarrollador A, del equipo de Data Science, trabaja en sus tareas para el sprint 2:
git push --set-upstream origin data/sprint2/devA
```
