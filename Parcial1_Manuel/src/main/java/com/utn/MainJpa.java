package com.utn;

import com.utn.entities.*;
import com.utn.enums.Estado;
import com.utn.enums.FormaPago;
import com.utn.enums.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class MainJpa {

    private static final String UNIDAD = "miUnidad";

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIDAD);
        EntityManager em = emf.createEntityManager();

        try {
            // ==================== PERSISTIR DATOS ====================
            em.getTransaction().begin();

            Categoria hamburguesas = Categoria.builder()
                    .nombre("Hamburguesas")
                    .descripcion("Hamburguesas artesanales a la parrilla")
                    .build();

            Categoria pizzas = Categoria.builder()
                    .nombre("Pizzas")
                    .descripcion("Pizzas a la piedra con ingredientes frescos")
                    .build();

            Categoria bebidas = Categoria.builder()
                    .nombre("Bebidas")
                    .descripcion("Gaseosas, aguas y jugos")
                    .build();

            em.persist(hamburguesas);
            em.persist(pizzas);
            em.persist(bebidas);
            System.out.println("Categorías persistidas correctamente");

            Producto p1 = Producto.builder()
                    .nombre("Hamburguesa Clásica")
                    .precio(1500.0)
                    .descripcion("Medallón de carne con lechuga, tomate y mayonesa")
                    .stock(20)
                    .imagen("burger-classic.png")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build();

            Producto p2 = Producto.builder()
                    .nombre("Hamburguesa Doble")
                    .precio(2200.0)
                    .descripcion("Dos medallones, doble queso y bacon")
                    .stock(15)
                    .imagen("burger-doble.png")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build();

            Producto p3 = Producto.builder()
                    .nombre("Hamburguesa Veggie")
                    .precio(1800.0)
                    .descripcion("Medallón de garbanzo con verduras y pesto")
                    .stock(10)
                    .imagen("burger-veggie.png")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build();

            Producto p4 = Producto.builder()
                    .nombre("Pizza Muzzarella")
                    .precio(2500.0)
                    .descripcion("Tomate, mozzarella y albahaca")
                    .stock(12)
                    .imagen("pizza-muzza.png")
                    .disponible(true)
                    .categoria(pizzas)
                    .build();

            Producto p5 = Producto.builder()
                    .nombre("Pizza Pepperoni")
                    .precio(3000.0)
                    .descripcion("Salsa, queso y pepperoni importado")
                    .stock(8)
                    .imagen("pizza-pepperoni.png")
                    .disponible(true)
                    .categoria(pizzas)
                    .build();

            Producto p6 = Producto.builder()
                    .nombre("Pizza Napolitana")
                    .precio(2800.0)
                    .descripcion("Tomate, mozzarella, ajo y aceite de oliva")
                    .stock(10)
                    .imagen("pizza-napo.png")
                    .disponible(true)
                    .categoria(pizzas)
                    .build();

            Producto p7 = Producto.builder()
                    .nombre("Papas Fritas")
                    .precio(900.0)
                    .descripcion("Papas crujientes con sal y salsa a elección")
                    .stock(30)
                    .imagen("fries.png")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build();

            Producto p8 = Producto.builder()
                    .nombre("Gaseosa")
                    .precio(700.0)
                    .descripcion("Gaseosa 500ml a elección")
                    .stock(50)
                    .imagen("soda.png")
                    .disponible(true)
                    .categoria(bebidas)
                    .build();

            Producto p9 = Producto.builder()
                    .nombre("Agua Mineral")
                    .precio(400.0)
                    .descripcion("Agua mineral sin gas 500ml")
                    .stock(60)
                    .imagen("water.png")
                    .disponible(true)
                    .categoria(bebidas)
                    .build();

            Producto p10 = Producto.builder()
                    .nombre("Combo Familiar")
                    .precio(6500.0)
                    .descripcion("2 hamburguesas + 1 pizza + 4 bebidas")
                    .stock(5)
                    .imagen("combo-familiar.png")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build();

            List<Producto> productos = List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
            for (Producto producto : productos) {
                em.persist(producto);
            }
            System.out.println("Productos persistidos correctamente");

            Usuario usuario1 = Usuario.builder()
                    .nombre("Manuel")
                    .apellido("García")
                    .mail("manuel@foodstore.com")
                    .celular("2615001001")
                    .contrasenia("admin123")
                    .rol(Rol.ADMIN)
                    .build();

            Usuario usuario2 = Usuario.builder()
                    .nombre("Laura")
                    .apellido("Martínez")
                    .mail("laura@foodstore.com")
                    .celular("2615002002")
                    .contrasenia("cliente456")
                    .rol(Rol.USUARIO)
                    .build();

            em.persist(usuario1);
            em.persist(usuario2);
            System.out.println("Usuarios persistidos correctamente");

            Pedido pedido1 = Pedido.builder()
                    .fecha(LocalDate.of(2025, 5, 1))
                    .estado(Estado.CONFIRMADO)
                    .formaPago(FormaPago.TARJETA)
                    .usuario(usuario1)
                    .build();
            pedido1.addDetallePedido(2, p1);
            pedido1.addDetallePedido(1, p7);
            pedido1.addDetallePedido(2, p8);
            em.persist(pedido1);
            System.out.println("Pedido creado con total: $" + pedido1.getTotal());

            Pedido pedido2 = Pedido.builder()
                    .fecha(LocalDate.of(2025, 5, 3))
                    .estado(Estado.TERMINADO)
                    .formaPago(FormaPago.EFECTIVO)
                    .usuario(usuario1)
                    .build();
            pedido2.addDetallePedido(1, p4);
            pedido2.addDetallePedido(1, p5);
            pedido2.addDetallePedido(3, p9);
            em.persist(pedido2);
            System.out.println("Pedido creado con total: $" + pedido2.getTotal());

            Pedido pedido3 = Pedido.builder()
                    .fecha(LocalDate.of(2025, 5, 5))
                    .estado(Estado.PENDIENTE)
                    .formaPago(FormaPago.TRANSFERENCIA)
                    .usuario(usuario2)
                    .build();
            pedido3.addDetallePedido(1, p10);
            pedido3.addDetallePedido(2, p8);
            em.persist(pedido3);
            System.out.println("Pedido creado con total: $" + pedido3.getTotal());

            em.getTransaction().commit();

            // ==================== ACTUALIZAR PRODUCTOS ====================
            em.getTransaction().begin();

            p1.setPrecio(1600.0);
            p1.setStock(25);
            em.merge(p1);
            System.out.println("Producto actualizado: " + p1.getNombre()
                    + " | precio=$" + p1.getPrecio() + " | stock=" + p1.getStock());

            p8.setPrecio(750.0);
            p8.setDisponible(false);
            em.merge(p8);
            System.out.println("Producto actualizado: " + p8.getNombre()
                    + " | precio=$" + p8.getPrecio() + " | disponible=" + p8.getDisponible());

            em.getTransaction().commit();

            // ==================== BUSCAR USUARIO POR ID ====================
            em.getTransaction().begin();

            Long idBusqueda = usuario1.getId();
            Usuario usuarioPorId = em.find(Usuario.class, idBusqueda);
            if (usuarioPorId != null) {
                System.out.println("Usuario encontrado por id: " + usuarioPorId);
            } else {
                System.out.println("No se encontró usuario con id: " + idBusqueda);
            }

            em.getTransaction().commit();

            // ==================== BUSCAR USUARIO POR MAIL (JPQL) ====================
            em.getTransaction().begin();

            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.mail = :mail", Usuario.class);
            query.setParameter("mail", "laura@foodstore.com");
            List<Usuario> usuariosPorMail = query.getResultList();

            if (!usuariosPorMail.isEmpty()) {
                System.out.println("Usuario encontrado por mail: " + usuariosPorMail.get(0));
            } else {
                System.out.println("No se encontró usuario con mail: laura@foodstore.com");
            }

            em.getTransaction().commit();

            // ==================== BORRAR PRODUCTO SIN REFERENCIAS ====================
            // p3 (Hamburguesa Veggie) no se usa en ningún detalle de pedido
            em.getTransaction().begin();

            Producto productoAEliminar = em.find(Producto.class, p3.getId());
            if (productoAEliminar != null) {
                em.remove(productoAEliminar);
                System.out.println("Producto eliminado correctamente: Hamburguesa Veggie (id=" + p3.getId() + ")");
            }

            em.getTransaction().commit();

            System.out.println("\n=== TP JPA finalizado correctamente ===");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error en la operación JPA: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

}
