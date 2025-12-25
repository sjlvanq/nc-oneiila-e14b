* [README.md principal](../../README.md)
* [Desarrollo/README.md](../README.md)

## Convenciones y documentación para desarrolladores - Proyecto ChurnCheck
# Workflow de Git para sprints

0. Traer los últimos cambios de la rama principal `dev`
```shell
git checkout dev
git pull
```

1. Crear una nueva rama aislada con el formato `sprintN/devA` reemplazando `N` por el número de sprint y `A` por el identificador del desarrollador a quien se le ha asignado una lista de tareas por hacer.

```shell
# Suponiendo que el desarrollador A comienza su trabajo para el sprint 2:
git checkout -B sprint2/devA
```

2. Traer los ficheros requeridos del código base cuando este ha sido creado en una rama distinta

```shell
# Suponiendo que el código base está en la rama sprint1/basecode y se desea
# traer el archivo src/main/java/com/example/demo/DemoApplication.java:
git checkout sprint1/basecode -- src/main/java/com/example/demo/DemoApplication.java
```

3. Realizar los cambios necesarios separándolos en distintos commits

```shell
git add .
git commit -m "tipo: descripción del commit"
```

4. Actualizar la rama en remoto
```shell
# Suponiendo que el desarrollador A comienza su trabajo para el sprint 2:
git push --set-upstream origin sprint2/devA
```