package com.utn;

import com.utn.entities.Categoria;
import com.utn.entities.Producto;
import com.utn.repository.CategoriaRepository;
import com.utn.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final CategoriaRepository categoriaRepository = new CategoriaRepository();
    private static final ProductoRepository productoRepository = new ProductoRepository();

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Gestion de Categorias");
            System.out.println("2. Gestion de Productos");
            System.out.println("3. Reportes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            if (!SCANNER.hasNextLine()) {
                System.out.println("Entrada finalizada. Cerrando aplicacion.");
                break;
            }
            String opcion = SCANNER.nextLine().trim();
            switch (opcion) {
                case "1" -> menuCategorias();
                case "2" -> menuProductos();
                case "3" -> menuReportes();
                case "0" -> {
                    salir = true;
                    System.out.println("Saliendo...");
                }
                default -> System.out.println("Opcion invalida.");
            }
        }

        SCANNER.close();
    }

    private static void menuCategorias() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- GESTION DE CATEGORIAS ---");
            System.out.println("1. Alta de categoria");
            System.out.println("2. Baja logica de categoria");
            System.out.println("3. Modificacion de categoria");
            System.out.println("4. Listado de categorias");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            String opcion = SCANNER.nextLine().trim();
            switch (opcion) {
                case "1" -> altaCategoria();
                case "2" -> bajaLogicaCategoria();
                case "3" -> modificarCategoria();
                case "4" -> listarCategoriasActivas();
                case "0" -> volver = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    private static void menuProductos() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- GESTION DE PRODUCTOS ---");
            System.out.println("1. Alta de producto");
            System.out.println("2. Baja logica de producto");
            System.out.println("3. Modificacion de producto");
            System.out.println("4. Listado de productos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            String opcion = SCANNER.nextLine().trim();
            switch (opcion) {
                case "1" -> altaProducto();
                case "2" -> bajaLogicaProducto();
                case "3" -> modificarProducto();
                case "4" -> listarProductosActivos();
                case "0" -> volver = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    private static void menuReportes() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Productos por categoria");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            String opcion = SCANNER.nextLine().trim();
            switch (opcion) {
                case "1" -> reporteProductosPorCategoria();
                case "0" -> volver = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    private static void altaCategoria() {
        System.out.println("\n--- ALTA DE CATEGORIA ---");
        System.out.print("Nombre: ");
        String nombre = SCANNER.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("Error: el nombre no puede estar vacio.");
            return;
        }

        System.out.print("Descripcion: ");
        String descripcion = SCANNER.nextLine().trim();

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        categoria.setEliminado(false);

        Categoria guardada = categoriaRepository.guardar(categoria);
        System.out.println("Categoria creada con ID: " + guardada.getId());
    }

    private static void bajaLogicaCategoria() {
        System.out.println("\n--- BAJA LOGICA DE CATEGORIA ---");
        List<Categoria> categorias = listarCategoriasActivas();
        if (categorias.isEmpty()) {
            return;
        }

        Long id = leerLongRequerido("ID de categoria a eliminar: ");
        if (id == null) {
            return;
        }

        Optional<Categoria> categoriaOpt = categoriaRepository.buscarPorId(id);
        if (categoriaOpt.isEmpty() || categoriaOpt.get().isEliminado()) {
            System.out.println("Error: la categoria no existe o ya fue eliminada.");
            return;
        }

        String nombreCategoria = categoriaOpt.get().getNombre();
        boolean eliminada = categoriaRepository.eliminarLogico(id);
        if (eliminada) {
            System.out.println("Categoria eliminada logicamente: " + nombreCategoria);
        } else {
            System.out.println("No se pudo eliminar la categoria.");
        }
    }

    private static void modificarCategoria() {
        System.out.println("\n--- MODIFICACION DE CATEGORIA ---");
        List<Categoria> categorias = listarCategoriasActivas();
        if (categorias.isEmpty()) {
            return;
        }

        Long id = leerLongRequerido("ID de categoria a modificar: ");
        if (id == null) {
            return;
        }

        Optional<Categoria> categoriaOpt = categoriaRepository.buscarPorId(id);
        if (categoriaOpt.isEmpty() || categoriaOpt.get().isEliminado()) {
            System.out.println("Error: la categoria no existe o fue eliminada.");
            return;
        }

        Categoria categoria = categoriaOpt.get();
        System.out.println("Nombre actual: " + categoria.getNombre());
        System.out.println("Descripcion actual: " + valorSeguro(categoria.getDescripcion()));

        System.out.print("Nuevo nombre (vacio para conservar): ");
        String nuevoNombre = SCANNER.nextLine().trim();
        System.out.print("Nueva descripcion (vacio para conservar): ");
        String nuevaDescripcion = SCANNER.nextLine().trim();

        if (!nuevoNombre.isEmpty()) {
            categoria.setNombre(nuevoNombre);
        }
        if (!nuevaDescripcion.isEmpty()) {
            categoria.setDescripcion(nuevaDescripcion);
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            System.out.println("Error: el nombre no puede quedar vacio.");
            return;
        }

        categoriaRepository.guardar(categoria);
        System.out.println("Categoria actualizada correctamente.");
    }

    private static List<Categoria> listarCategoriasActivas() {
        List<Categoria> categorias = categoriaRepository.listarActivos();
        System.out.println("\n--- LISTADO DE CATEGORIAS ACTIVAS ---");

        if (categorias.isEmpty()) {
            System.out.println("No hay categorias activas.");
            return categorias;
        }

        for (Categoria categoria : categorias) {
            System.out.printf("ID: %d | Nombre: %s | Descripcion: %s%n",
                    categoria.getId(),
                    valorSeguro(categoria.getNombre()),
                    valorSeguro(categoria.getDescripcion()));
        }
        return categorias;
    }

    private static void altaProducto() {
        System.out.println("\n--- ALTA DE PRODUCTO ---");

        List<Categoria> categorias = listarCategoriasActivas();
        if (categorias.isEmpty()) {
            System.out.println("No se puede crear un producto sin categorias activas.");
            return;
        }

        Long categoriaId = leerLongRequerido("ID de categoria: ");
        if (categoriaId == null) {
            return;
        }

        Optional<Categoria> categoriaOpt = categoriaRepository.buscarPorId(categoriaId);
        if (categoriaOpt.isEmpty() || categoriaOpt.get().isEliminado()) {
            System.out.println("Error: categoria invalida.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = SCANNER.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("Error: el nombre no puede estar vacio.");
            return;
        }

        System.out.print("Descripcion: ");
        String descripcion = SCANNER.nextLine().trim();

        Double precio = leerDoubleRequerido("Precio (> 0): ");
        if (precio == null) {
            return;
        }
        if (precio <= 0) {
            System.out.println("Error: el precio debe ser mayor a 0.");
            return;
        }

        Integer stock = leerIntegerRequerido("Stock (>= 0): ");
        if (stock == null) {
            return;
        }
        if (stock < 0) {
            System.out.println("Error: el stock debe ser mayor o igual a 0.");
            return;
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setDisponible(true);
        producto.setEliminado(false);
        producto.setCategoria(categoriaOpt.get());

        Producto guardado = productoRepository.guardar(producto);
        System.out.println("Producto creado con ID: " + guardado.getId()
                + " | Categoria: " + categoriaOpt.get().getNombre());
    }

    private static void bajaLogicaProducto() {
        System.out.println("\n--- BAJA LOGICA DE PRODUCTO ---");
        List<Producto> productos = listarProductosActivos();
        if (productos.isEmpty()) {
            return;
        }

        Long id = leerLongRequerido("ID de producto a eliminar: ");
        if (id == null) {
            return;
        }

        Optional<Producto> productoOpt = productoRepository.buscarPorId(id);
        if (productoOpt.isEmpty() || productoOpt.get().isEliminado()) {
            System.out.println("Error: el producto no existe o ya fue eliminado.");
            return;
        }

        String nombreProducto = productoOpt.get().getNombre();
        boolean eliminado = productoRepository.eliminarLogico(id);
        if (eliminado) {
            System.out.println("Producto eliminado logicamente: " + nombreProducto);
        } else {
            System.out.println("No se pudo eliminar el producto.");
        }
    }

    private static void modificarProducto() {
        System.out.println("\n--- MODIFICACION DE PRODUCTO ---");
        List<Producto> productos = listarProductosActivos();
        if (productos.isEmpty()) {
            return;
        }

        Long id = leerLongRequerido("ID de producto a modificar: ");
        if (id == null) {
            return;
        }

        Optional<Producto> productoOpt = productoRepository.buscarPorId(id);
        if (productoOpt.isEmpty() || productoOpt.get().isEliminado()) {
            System.out.println("Error: el producto no existe o fue eliminado.");
            return;
        }

        Producto producto = productoOpt.get();
        System.out.println("Nombre actual: " + valorSeguro(producto.getNombre()));
        System.out.println("Precio actual: " + valorSeguro(producto.getPrecio()));
        System.out.println("Stock actual: " + valorSeguro(producto.getStock()));

        System.out.print("Nuevo nombre (vacio para conservar): ");
        String nuevoNombre = SCANNER.nextLine().trim();

        System.out.print("Nuevo precio (vacio para conservar): ");
        String precioTexto = SCANNER.nextLine().trim();

        System.out.print("Nuevo stock (vacio para conservar): ");
        String stockTexto = SCANNER.nextLine().trim();

        if (!nuevoNombre.isEmpty()) {
            producto.setNombre(nuevoNombre);
        }

        if (!precioTexto.isEmpty()) {
            Double nuevoPrecio = parseDouble(precioTexto);
            if (nuevoPrecio == null) {
                System.out.println("Error: precio invalido.");
                return;
            }
            if (nuevoPrecio <= 0) {
                System.out.println("Error: el precio debe ser mayor a 0.");
                return;
            }
            producto.setPrecio(nuevoPrecio);
        }

        if (!stockTexto.isEmpty()) {
            Integer nuevoStock = parseInteger(stockTexto);
            if (nuevoStock == null) {
                System.out.println("Error: stock invalido.");
                return;
            }
            if (nuevoStock < 0) {
                System.out.println("Error: el stock debe ser mayor o igual a 0.");
                return;
            }
            producto.setStock(nuevoStock);
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            System.out.println("Error: el nombre no puede quedar vacio.");
            return;
        }

        productoRepository.guardar(producto);
        System.out.println("Producto actualizado correctamente.");
    }

    private static List<Producto> listarProductosActivos() {
        List<Producto> productos = productoRepository.listarActivos();
        System.out.println("\n--- LISTADO DE PRODUCTOS ACTIVOS ---");

        if (productos.isEmpty()) {
            System.out.println("No hay productos activos.");
            return productos;
        }

        for (Producto producto : productos) {
            String nombreCategoria = producto.getCategoria() != null
                    ? valorSeguro(producto.getCategoria().getNombre())
                    : "Sin categoria";
            System.out.printf("ID: %d | Nombre: %s | Precio: %.2f | Stock: %d | Categoria: %s%n",
                    producto.getId(),
                    valorSeguro(producto.getNombre()),
                    producto.getPrecio(),
                    producto.getStock(),
                    nombreCategoria);
        }
        return productos;
    }

    private static void reporteProductosPorCategoria() {
        System.out.println("\n--- REPORTE: PRODUCTOS POR CATEGORIA ---");

        List<Categoria> categorias = listarCategoriasActivas();
        if (categorias.isEmpty()) {
            return;
        }

        Long categoriaId = leerLongRequerido("ID de categoria: ");
        if (categoriaId == null) {
            return;
        }

        List<Producto> productos = productoRepository.buscarPorCategoria(categoriaId);
        if (productos.isEmpty()) {
            System.out.println("No hay productos activos para la categoria seleccionada.");
            return;
        }

        System.out.println("Productos encontrados:");
        for (Producto producto : productos) {
            System.out.printf("ID: %d | Nombre: %s | Precio: %.2f | Stock: %d%n",
                    producto.getId(),
                    valorSeguro(producto.getNombre()),
                    producto.getPrecio(),
                    producto.getStock());
        }
    }

    private static Long leerLongRequerido(String mensaje) {
        System.out.print(mensaje);
        String texto = SCANNER.nextLine().trim();
        Long valor = parseLong(texto);
        if (valor == null) {
            System.out.println("Entrada invalida. Debe ingresar un numero entero.");
        }
        return valor;
    }

    private static Double leerDoubleRequerido(String mensaje) {
        System.out.print(mensaje);
        String texto = SCANNER.nextLine().trim();
        Double valor = parseDouble(texto);
        if (valor == null) {
            System.out.println("Entrada invalida. Debe ingresar un numero decimal.");
        }
        return valor;
    }

    private static Integer leerIntegerRequerido(String mensaje) {
        System.out.print(mensaje);
        String texto = SCANNER.nextLine().trim();
        Integer valor = parseInteger(texto);
        if (valor == null) {
            System.out.println("Entrada invalida. Debe ingresar un numero entero.");
        }
        return valor;
    }

    private static Long parseLong(String texto) {
        try {
            return Long.parseLong(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double parseDouble(String texto) {
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInteger(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String valorSeguro(Object valor) {
        return valor == null ? "" : String.valueOf(valor);
    }
}
