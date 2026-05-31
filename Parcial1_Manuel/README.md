# Parcial 2 - Programacion III

## Descripcion breve
Aplicacion de consola en Java con JPA (sin Spring Data) para gestionar ABM de categorias y productos con baja logica y consulta JPQL de productos por categoria.
El menu de uso es por consola.

## Instrucciones para ejecutar
Requisitos:
- Java 17
- Gradle Wrapper (incluido en el proyecto)

Comandos (PowerShell):
- .\gradlew.bat clean build
- .\gradlew.bat run

Comandos (Git Bash):
- ./gradlew.bat clean build
- ./gradlew.bat run

La base se configura con H2 file desde persistence.xml usando la unidad miUnidad.

## Funcionalidades implementadas
- Repositorio base generico con:
  - guardar (persistencia y actualizacion con merge)
  - buscarPorId
  - listarActivos (eliminado = false)
  - eliminarLogico (eliminado = true)
- Repositorio de categorias (hereda comportamiento base)
- Repositorio de productos con:
  - listar activos con categoria cargada
  - consulta JPQL buscarPorCategoria con TypedQuery y parametro nombrado
- Menu de consola con:
  - Gestion de categorias: alta, baja logica, modificacion y listado
  - Gestion de productos: alta, baja logica, modificacion y listado
  - Reporte de productos por categoria
- Validaciones de entrada:
  - nombre no vacio
  - precio mayor a 0
  - stock mayor o igual a 0
  - manejo de entradas invalidas sin romper ejecucion

## Checklist HU-01 a HU-09
- HU-01: BaseRepository<T> con CRUD generico, transacciones, Optional y cierre de EntityManager.
- HU-02: CategoriaRepository y ProductoRepository, super(Class<T>) y buscarPorCategoria con JPQL.
- HU-03: Alta de categoria.
- HU-04: Modificacion de categoria.
- HU-05: Baja logica de categoria.
- HU-06: Alta de producto.
- HU-07: Modificacion de producto.
- HU-08: Baja logica de producto.
- HU-09: Consulta JPQL productos por categoria.
