package com.utn;

import com.utn.dtos.UsuarioDTO;
import com.utn.entities.*;
import com.utn.enums.Estado;
import com.utn.enums.FormaPago;
import com.utn.enums.Rol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        // ==================== CATEGORÍAS ====================
        Categoria hamburguesas = Categoria.builder()
                .id(1L)
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Hamburguesas")
                .descripcion("Hamburguesas artesanales a la parrilla")
                .build();

        Categoria pizzas = Categoria.builder()
                .id(2L)
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Pizzas")
                .descripcion("Pizzas a la piedra con ingredientes frescos")
                .build();

        Categoria bebidas = Categoria.builder()
                .id(3L)
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Bebidas")
                .descripcion("Gaseosas, aguas y jugos")
                .build();

        // ==================== PRODUCTOS ====================
        Producto p1 = Producto.builder()
                .id(1L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Hamburguesa Clásica")
                .precio(1500.0)
                .descripcion("Medallón de carne con lechuga, tomate y mayonesa")
                .stock(20)
                .imagen("burger-classic.png")
                .disponible(true)
                .categoria(hamburguesas)
                .build();

        Producto p2 = Producto.builder()
                .id(2L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Hamburguesa Doble")
                .precio(2200.0)
                .descripcion("Dos medallones, doble queso y bacon")
                .stock(15)
                .imagen("burger-doble.png")
                .disponible(true)
                .categoria(hamburguesas)
                .build();

        Producto p3 = Producto.builder()
                .id(3L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Hamburguesa Veggie")
                .precio(1800.0)
                .descripcion("Medallón de garbanzo con verduras y pesto")
                .stock(10)
                .imagen("burger-veggie.png")
                .disponible(true)
                .categoria(hamburguesas)
                .build();

        Producto p4 = Producto.builder()
                .id(4L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Pizza Muzzarella")
                .precio(2500.0)
                .descripcion("Tomate, mozzarella y albahaca")
                .stock(12)
                .imagen("pizza-muzza.png")
                .disponible(true)
                .categoria(pizzas)
                .build();

        Producto p5 = Producto.builder()
                .id(5L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Pizza Pepperoni")
                .precio(3000.0)
                .descripcion("Salsa, queso y pepperoni importado")
                .stock(8)
                .imagen("pizza-pepperoni.png")
                .disponible(true)
                .categoria(pizzas)
                .build();

        Producto p6 = Producto.builder()
                .id(6L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Pizza Napolitana")
                .precio(2800.0)
                .descripcion("Tomate, mozzarella, ajo y aceite de oliva")
                .stock(10)
                .imagen("pizza-napo.png")
                .disponible(true)
                .categoria(pizzas)
                .build();

        Producto p7 = Producto.builder()
                .id(7L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Papas Fritas")
                .precio(900.0)
                .descripcion("Papas crujientes con sal y salsa a elección")
                .stock(30)
                .imagen("fries.png")
                .disponible(true)
                .categoria(hamburguesas)
                .build();

        Producto p8 = Producto.builder()
                .id(8L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Gaseosa")
                .precio(700.0)
                .descripcion("Gaseosa 500ml a elección")
                .stock(50)
                .imagen("soda.png")
                .disponible(true)
                .categoria(bebidas)
                .build();

        Producto p9 = Producto.builder()
                .id(9L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Agua Mineral")
                .precio(400.0)
                .descripcion("Agua mineral sin gas 500ml")
                .stock(60)
                .imagen("water.png")
                .disponible(true)
                .categoria(bebidas)
                .build();

        Producto p10 = Producto.builder()
                .id(10L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Combo Familiar")
                .precio(6500.0)
                .descripcion("2 hamburguesas + 1 pizza + 4 bebidas")
                .stock(5)
                .imagen("combo-familiar.png")
                .disponible(true)
                .categoria(hamburguesas)
                .build();

        // Lista de todos los productos
        List<Producto> productos = new ArrayList<>(
                List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
        );

        // ==================== USUARIOS ====================
        Usuario usuario1 = Usuario.builder()
                .id(1L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Manuel")
                .apellido("García")
                .mail("manuel@foodstore.com")
                .celular("2615001001")
                .contrasenia("admin123")   // no se imprime gracias a @ToString.Exclude
                .rol(Rol.ADMIN)
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(2L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Laura")
                .apellido("Martínez")
                .mail("laura@foodstore.com")
                .celular("2615002002")
                .contrasenia("cliente456") // no se imprime gracias a @ToString.Exclude
                .rol(Rol.USUARIO)
                .build();

        // ==================== PEDIDOS ====================

        // Pedido 1 — usuario1
        Pedido pedido1 = Pedido.builder()
                .id(1L).eliminado(false).createdAt(LocalDateTime.now())
                .fecha(LocalDate.of(2025, 5, 1))
                .estado(Estado.CONFIRMADO)
                .formaPago(FormaPago.TARJETA)
                .usuario(usuario1)
                .build();
        pedido1.addDetallePedido(2, p1);   // 2x Hamburguesa Clásica
        pedido1.addDetallePedido(1, p7);   // 1x Papas Fritas
        pedido1.addDetallePedido(2, p8);   // 2x Gaseosa
        pedido1.calcularTotal();

        // Pedido 2 — usuario1 (mismo usuario para que tenga más pedidos)
        Pedido pedido2 = Pedido.builder()
                .id(2L).eliminado(false).createdAt(LocalDateTime.now())
                .fecha(LocalDate.of(2025, 5, 3))
                .estado(Estado.TERMINADO)
                .formaPago(FormaPago.EFECTIVO)
                .usuario(usuario1)
                .build();
        pedido2.addDetallePedido(1, p4);   // 1x Pizza Muzzarella
        pedido2.addDetallePedido(1, p5);   // 1x Pizza Pepperoni
        pedido2.addDetallePedido(3, p9);   // 3x Agua Mineral
        pedido2.calcularTotal();

        // Pedido 3 — usuario2
        Pedido pedido3 = Pedido.builder()
                .id(3L).eliminado(false).createdAt(LocalDateTime.now())
                .fecha(LocalDate.of(2025, 5, 5))
                .estado(Estado.PENDIENTE)
                .formaPago(FormaPago.TRANSFERENCIA)
                .usuario(usuario2)
                .build();
        pedido3.addDetallePedido(1, p10);  // 1x Combo Familiar
        pedido3.addDetallePedido(2, p8);   // 2x Gaseosa
        pedido3.calcularTotal();

        List<Pedido> pedidos = List.of(pedido1, pedido2, pedido3);

        // ============================================================
        // A. UN PRODUCTO CON TOSTRING
        // ============================================================
        System.out.println("--- PRODUCTO CON TOSTRING ---");
        System.out.println(p1);

        // ============================================================
        // B. LISTADO COMPLETO DE PRODUCTOS
        // ============================================================
        System.out.println("\n--- LISTADO DE PRODUCTOS ---");
        for (Producto p : productos) {
            System.out.println(p);
        }

        // ============================================================
        // C. PEDIDOS DEL USUARIO CON MÁS PEDIDOS
        // ============================================================
        System.out.println("\n--- PEDIDOS DEL USUARIO CON MÁS PEDIDOS ---");

        // Contamos cuántos pedidos tiene cada usuario
        Map<Long, Long> pedidosPorUsuario = pedidos.stream()
                .collect(Collectors.groupingBy(
                        ped -> ped.getUsuario().getId(),
                        Collectors.counting()
                ));

        // Buscamos el id del usuario con más pedidos
        Long idConMasPedidos = pedidosPorUsuario.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Filtramos y mostramos
        pedidos.stream()
                .filter(ped -> ped.getUsuario().getId().equals(idConMasPedidos))
                .forEach(ped -> {
                    System.out.println("Usuario: " + ped.getUsuario());
                    System.out.println("Pedido:  " + ped);
                    System.out.println();
                });

        // ============================================================
        // D. COMPARACIÓN DE PRODUCTO REPETIDO CON EQUALS
        // ============================================================
        System.out.println("--- COMPARACIÓN DE PRODUCTO REPETIDO CON EQUALS ---");

        // Producto nuevo con el mismo nombre que p1 (debe dar true al comparar)
        Producto productoRepetido = Producto.builder()
                .id(99L).eliminado(false).createdAt(LocalDateTime.now())
                .nombre("Hamburguesa Clásica")
                .precio(9999.0)
                .descripcion("Copia de prueba")
                .stock(0)
                .imagen("test.png")
                .disponible(false)
                .categoria(hamburguesas)
                .build();

        for (Producto p : productos) {
            boolean resultado = productoRepetido.equals(p);
            System.out.println("Comparando con: " + p.getNombre() + " -> " + resultado);
        }

        // ============================================================
        // E. USUARIO DTO SIN DATOS SENSIBLES
        // ============================================================
        System.out.println("\n--- USUARIO DTO SIN DATOS SENSIBLES ---");
        UsuarioDTO usuarioDTO = UsuarioDTO.fromUsuario(usuario1);
        System.out.println(usuarioDTO);
    }

}
