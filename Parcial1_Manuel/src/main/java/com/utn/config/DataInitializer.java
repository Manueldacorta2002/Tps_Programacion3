package com.utn.config;

import com.utn.dtos.categoria.CategoriaCreate;
import com.utn.dtos.categoria.CategoriaDto;
import com.utn.dtos.detallePedido.DetallePedidoCreate;
import com.utn.dtos.pedido.PedidoDto;
import com.utn.dtos.pedido.PedidoEdit;
import com.utn.dtos.producto.ProductoCreate;
import com.utn.dtos.producto.ProductoDto;
import com.utn.dtos.usuario.UsuarioCreate;
import com.utn.dtos.usuario.UsuarioDto;
import com.utn.enums.Estado;
import com.utn.enums.FormaPago;
import com.utn.enums.Rol;
import com.utn.services.CategoriaService;
import com.utn.services.PedidoService;
import com.utn.services.ProductoService;
import com.utn.services.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final CategoriaService categoriaService;
    private final ProductoService productoService;
    private final PedidoService pedidoService;

    public DataInitializer(UsuarioService usuarioService,
                            CategoriaService categoriaService,
                            ProductoService productoService,
                            PedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    @Override
    public void run(String... args) {
        if (usuarioService.contar() > 0) {
            System.out.println("Datos ya inicializados, omitiendo carga en Spring.");
            return;
        }

        // ==================== USUARIOS ====================
        UsuarioDto usuario1 = usuarioService.crear(new UsuarioCreate(
                "Manuel",
                "García",
                "manuel@foodstore.com",
                "2615001001",
                "admin123",
                Rol.ADMIN
        ));

        UsuarioDto usuario2 = usuarioService.crear(new UsuarioCreate(
                "Laura",
                "Martínez",
                "laura@foodstore.com",
                "2615002002",
                "cliente456",
                Rol.USUARIO
        ));

        System.out.println("Usuarios persistidos: id1=" + usuario1.id() + ", id2=" + usuario2.id());

        // ==================== CATEGORÍAS ====================
        CategoriaDto hamburguesas = categoriaService.crear(new CategoriaCreate(
                "Hamburguesas",
                "Hamburguesas artesanales a la parrilla"
        ));
        CategoriaDto pizzas = categoriaService.crear(new CategoriaCreate(
                "Pizzas",
                "Pizzas a la piedra con ingredientes frescos"
        ));
        CategoriaDto bebidas = categoriaService.crear(new CategoriaCreate(
                "Bebidas",
                "Gaseosas, aguas y jugos"
        ));

        System.out.println("Categorías persistidas correctamente.");

        // ==================== PRODUCTOS ====================
        ProductoDto p1 = productoService.crear(new ProductoCreate(
                "Hamburguesa Clásica", 1500.0, "Medallón de carne con lechuga, tomate y mayonesa", 20,
                "burger-classic.png", true, hamburguesas.id()
        ));
        ProductoDto p2 = productoService.crear(new ProductoCreate(
                "Hamburguesa Doble", 2200.0, "Dos medallones, doble queso y bacon", 15,
                "burger-doble.png", true, hamburguesas.id()
        ));
        ProductoDto p3 = productoService.crear(new ProductoCreate(
                "Hamburguesa Veggie", 1800.0, "Medallón de garbanzo con verduras y pesto", 10,
                "burger-veggie.png", true, hamburguesas.id()
        ));
        ProductoDto p4 = productoService.crear(new ProductoCreate(
                "Pizza Muzzarella", 2500.0, "Tomate, mozzarella y albahaca", 12,
                "pizza-muzza.png", true, pizzas.id()
        ));
        ProductoDto p5 = productoService.crear(new ProductoCreate(
                "Pizza Pepperoni", 3000.0, "Salsa, queso y pepperoni importado", 8,
                "pizza-pepperoni.png", true, pizzas.id()
        ));
        ProductoDto p6 = productoService.crear(new ProductoCreate(
                "Pizza Napolitana", 2800.0, "Tomate, mozzarella, ajo y aceite de oliva", 10,
                "pizza-napo.png", true, pizzas.id()
        ));
        ProductoDto p7 = productoService.crear(new ProductoCreate(
                "Papas Fritas", 900.0, "Papas crujientes con sal y salsa a elección", 30,
                "fries.png", true, hamburguesas.id()
        ));
        ProductoDto p8 = productoService.crear(new ProductoCreate(
                "Gaseosa", 700.0, "Gaseosa 500ml a elección", 50,
                "soda.png", true, bebidas.id()
        ));
        ProductoDto p9 = productoService.crear(new ProductoCreate(
                "Agua Mineral", 400.0, "Agua mineral sin gas 500ml", 60,
                "water.png", true, bebidas.id()
        ));
        ProductoDto p10 = productoService.crear(new ProductoCreate(
                "Combo Familiar", 6500.0, "2 hamburguesas + 1 pizza + 4 bebidas", 5,
                "combo-familiar.png", true, hamburguesas.id()
        ));

        System.out.println("Productos persistidos: 10 items.");

        // ==================== PEDIDOS (mínimo 2 detalles cada uno) ====================
        PedidoDto pedido1 = pedidoService.crear(new PedidoEdit(
                LocalDate.of(2025, 5, 1),
                Estado.CONFIRMADO,
                FormaPago.TARJETA,
                usuario1.id(),
                List.of(
                        new DetallePedidoCreate(2, p1.id()),
                        new DetallePedidoCreate(1, p8.id())
                )
        ));

        PedidoDto pedido2 = pedidoService.crear(new PedidoEdit(
                LocalDate.of(2025, 5, 3),
                Estado.TERMINADO,
                FormaPago.EFECTIVO,
                usuario1.id(),
                List.of(
                        new DetallePedidoCreate(1, p4.id()),
                        new DetallePedidoCreate(1, p5.id()),
                        new DetallePedidoCreate(3, p9.id())
                )
        ));

        PedidoDto pedido3 = pedidoService.crear(new PedidoEdit(
                LocalDate.of(2025, 5, 5),
                Estado.PENDIENTE,
                FormaPago.TRANSFERENCIA,
                usuario2.id(),
                List.of(
                        new DetallePedidoCreate(1, p10.id()),
                        new DetallePedidoCreate(2, p8.id())
                )
        ));

        System.out.println("Pedidos persistidos correctamente.");
        System.out.println("Pedido1 total: " + pedido1.total());
        System.out.println("Pedido2 total: " + pedido2.total());
        System.out.println("Pedido3 total: " + pedido3.total());
    }
}

