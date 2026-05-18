package com.tup.programacion3;

import com.tup.programacion3.entities.*;
import com.tup.programacion3.enums.Estado;
import com.tup.programacion3.enums.FormaPago;
import com.tup.programacion3.enums.Rol;

import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // ===================== CATEGORIAS =====================
        Categoria hamburguesas   = new Categoria("Hamburguesas", "Hamburguesas artesanales");
        Categoria pizzas         = new Categoria("Pizzas", "Pizzas a la piedra");
        Categoria acompanamentos = new Categoria("Acompañamientos", "Bebidas, papas y más");

        // ===================== PRODUCTOS =====================
        Producto p1  = new Producto("Hamburguesa",       1500.0, "Hamburguesa clásica",      10, "burger.svg",       true,  hamburguesas);
        Producto p2  = new Producto("Hamburguesa Doble", 2200.0, "Doble medallón",           8,  "double-burger.svg",true,  hamburguesas);
        Producto p3  = new Producto("Pizza Muzza",       2800.0, "Mozzarella y tomate",      12, "pizza.svg",        true,  pizzas);
        Producto p4  = new Producto("Pizza Pepperoni",   3200.0, "Con pepperoni importado",  6,  "pizza.svg",        true,  pizzas);
        Producto p5  = new Producto("Papas Fritas",      1200.0, "Papas crujientes",         20, "fries.svg",        true,  acompanamentos);
        Producto p6  = new Producto("Empanada",           600.0, "Empanada de carne",        30, "empanada.svg",     true,  acompanamentos);
        Producto p7  = new Producto("Bebida Cola",        800.0, "Gaseosa 500ml",            50, "drink.svg",        true,  acompanamentos);
        Producto p8  = new Producto("Agua",               400.0, "Agua mineral 500ml",       60, "water.svg",        true,  acompanamentos);
        Producto p9  = new Producto("Helado",             900.0, "Helado de vainilla",       15, "ice-cream.svg",    true,  acompanamentos);
        Producto p10 = new Producto("Combo Simple",      3500.0, "Hamburguesa + papas + bebida", 5, "combo.svg",     true,  hamburguesas);

        // Asociar productos a categorías
        hamburguesas.addProducto(p1);   hamburguesas.addProducto(p2);   hamburguesas.addProducto(p10);
        pizzas.addProducto(p3);         pizzas.addProducto(p4);
        acompanamentos.addProducto(p5); acompanamentos.addProducto(p6); acompanamentos.addProducto(p7);
        acompanamentos.addProducto(p8); acompanamentos.addProducto(p9);

        // ===================== USUARIOS =====================
        Usuario admin   = new Usuario("Manuel",  "Da Corta",  "admin@tup.com", "2615000001", "admin123",   Rol.ADMIN);
        Usuario cliente = new Usuario("Laura",   "Martínez",  "laura@mail.com","2615000002", "pass456",    Rol.USUARIO);

        // ===================== PEDIDOS =====================
        // Pedido 1 — admin
        Pedido pedido1 = new Pedido(LocalDate.of(2025, 4, 10), Estado.CONFIRMADO, FormaPago.TARJETA);
        pedido1.addDetallePedido(2, p1);   // 2 x Hamburguesa
        pedido1.addDetallePedido(1, p5);   // 1 x Papas Fritas
        pedido1.addDetallePedido(2, p7);   // 2 x Bebida Cola
        admin.addPedido(pedido1);

        // Pedido 2 — admin
        Pedido pedido2 = new Pedido(LocalDate.of(2025, 4, 15), Estado.TERMINADO, FormaPago.EFECTIVO);
        pedido2.addDetallePedido(1, p3);   // 1 x Pizza Muzza
        pedido2.addDetallePedido(3, p6);   // 3 x Empanada
        admin.addPedido(pedido2);

        // Pedido 3 — cliente
        Pedido pedido3 = new Pedido(LocalDate.of(2025, 4, 20), Estado.PENDIENTE, FormaPago.TRANSFERENCIA);
        pedido3.addDetallePedido(1, p10);  // 1 x Combo Simple
        pedido3.addDetallePedido(2, p9);   // 2 x Helado
        pedido3.addDetallePedido(1, p8);   // 1 x Agua
        cliente.addPedido(pedido3);

        // ===================== COLECCION DE USUARIOS =====================
        List<Usuario> usuarios = new ArrayList<>(Arrays.asList(admin, cliente));

        // ===================== DEMO toString =====================
        System.out.println("===== toString: CATEGORIAS =====");
        System.out.println(hamburguesas);
        System.out.println(pizzas);
        System.out.println(acompanamentos);

        System.out.println("\n===== toString: PRODUCTOS =====");
        for (Producto p : List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)) {
            System.out.println(p);
        }

        System.out.println("\n===== toString: USUARIOS =====");
        for (Usuario u : usuarios) System.out.println(u);

        System.out.println("\n===== toString: PEDIDOS =====");
        System.out.println(pedido1);
        System.out.println(pedido2);
        System.out.println(pedido3);

        System.out.println("\n===== toString: DETALLES DEL PEDIDO 1 =====");
        for (DetallePedido d : pedido1.getDetalles()) System.out.println(d);

        // ===================== DEMO equals/hashCode =====================
        System.out.println("\n===== equals/hashCode: DUPLICADO EN SET =====");
        Set<Producto> setProductos = new HashSet<>(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10));
        System.out.println("Tamaño original del set: " + setProductos.size());

        Producto duplicado = new Producto("Hamburguesa", 9999.0, "Intento duplicado", 0, "", true, hamburguesas);
        System.out.println("contains(duplicado): " + setProductos.contains(duplicado));
        boolean agregado = setProductos.add(duplicado);
        System.out.println("add(duplicado) devuelve: " + agregado);
        System.out.println("Tamaño del set tras intento: " + setProductos.size());

        // ===================== DEMO Collections =====================
        System.out.println("\n===== Collections: USUARIO CON MAS PEDIDOS =====");
        Usuario conMasPedidos = Collections.max(usuarios,
                Comparator.comparingInt(u -> u.getPedidos().size()));
        System.out.println("Usuario con más pedidos: " + conMasPedidos.getNombre()
                + " " + conMasPedidos.getApellido()
                + " (" + conMasPedidos.getPedidos().size() + " pedidos)");

        System.out.println("\n===== Collections: PRODUCTO MAS CARO =====");
        List<Producto> listaProductos = new ArrayList<>(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10));
        Producto masCaro = Collections.max(listaProductos,
                Comparator.comparingDouble(Producto::getPrecio));
        System.out.println("Producto más caro: " + masCaro.getNombre() + " $" + masCaro.getPrecio());

        System.out.println("\n===== Collections: ORDENAR PRODUCTOS POR PRECIO =====");
        listaProductos.sort(Comparator.comparingDouble(Producto::getPrecio));
        listaProductos.forEach(p -> System.out.println("  " + p.getNombre() + " -> $" + p.getPrecio()));

        System.out.println("\n===== FIN =====");
    }
}
