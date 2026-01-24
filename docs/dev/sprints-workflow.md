* [README.md principal](../../README.md)
* [CONTRIBUTING.md](../CONTRIBUTING.md)

## Convenciones y documentación para desarrolladores - ChurnCheck
# Workflow Git para sprints

#### Resumen:

* Cada desarrollador a quien se le ha asignado una lista de tareas en el sprint creará una rama con el formato 
  * `(área)/sprintN/devX`
  * `(área)/no-sprint/(tipo)/nombre-descriptivo
  * Las áreas válidas son `core`, `backend`, `frontend` o `datascience`.
  * Si la tarea pertenece a un sprint activo se usará `sprintN`; del lo contrario, se usará `no-sprint`.
  * Los tipos de rama comunes son: `feature`, `bugfix`, `refactor` o `experimental`.
* Al finalizar el sprint o concluir las tareas solicitará fusionar sus cambios a la rama principal con un Pull Request.
* Una vez fusionada, la rama de trabajo será eliminada.

> Durante el Sprint Planning, cada equipo identifica, desglosa y organiza las tareas por área o dominio. Estos conjuntos de tareas se categorizan mediante identificadores (ej. `dev1`, `dev2`, `devA`, `devB`) siguiendo las convenciones internas de cada célula de trabajo. La asignación final de un desarrollador a un grupo de tareas específico se realiza al cierre de la planificación. Cabe destacar que estos identificadores son temporales y dinámicos: el desarrollador responsable del grupo `dev3` en el sprint actual podrá asumir un identificador distinto en el siguiente ciclo, según la naturaleza de las tareas asignadas.


### 1. Actualización de la copia local
Traer los últimos cambios de la rama principal `dev`
```shell
git checkout dev
git pull
```

### 2. Creación de una nueva rama 

```shell
# Suponiendo que el desarrollador A, del equipo de Backend, comienza su trabajo para el sprint 2:
git checkout -B backend/sprint2/devA
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
# Suponiendo que el desarrollador A, del equipo de Backend, trabaja en sus tareas para el sprint 2:
git push --set-upstream origin backend/sprint2/devA
```
