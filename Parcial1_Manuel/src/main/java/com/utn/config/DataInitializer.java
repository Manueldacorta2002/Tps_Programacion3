package com.utn.config;

import com.utn.entities.Categoria;
import com.utn.entities.Pedido;
import com.utn.entities.Producto;
import com.utn.entities.Usuario;
import com.utn.enums.Estado;
import com.utn.enums.FormaPago;
import com.utn.enums.Rol;
import com.utn.repositories.CategoriaRepository;
import com.utn.repositories.PedidoRepository;
import com.utn.repositories.ProductoRepository;
import com.utn.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           CategoriaRepository categoriaRepository,
                           ProductoRepository productoRepository,
                           PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        crearUsuarios();
        crearCategorias();
        crearPedidos();
    }

    private void crearUsuarios() {
        if (usuarioRepository.findByMailAndEliminadoFalse("admin@admin.com").isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .nombre("Admin")
                    .apellido("Food Store")
                    .mail("admin@admin.com")
                    .celular("2610000000")
                    .contrasenia(PasswordEncoderUtil.encode("123456"))
                    .rol(Rol.ADMIN)
                    .build());
        }

        if (usuarioRepository.findByMailAndEliminadoFalse("cliente@foodstore.com").isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .nombre("Cliente")
                    .apellido("Prueba")
                    .mail("cliente@foodstore.com")
                    .celular("2610000001")
                    .contrasenia(PasswordEncoderUtil.encode("123456"))
                    .rol(Rol.USUARIO)
                    .build());
        }
    }

    private void crearCategorias() {
        if (!categoriaRepository.findAllByEliminadoFalse().isEmpty()) {
            return; // Ya hay datos, no duplicar
        }

        Categoria pizzas = categoriaRepository.save(Categoria.builder()
                .nombre("Pizzas")
                .descripcion("Las mejores pizzas artesanales")
                .build());

        Categoria hamburguesas = categoriaRepository.save(Categoria.builder()
                .nombre("Hamburguesas")
                .descripcion("Hamburguesas gourmet con ingredientes frescos")
                .build());

        Categoria bebidas = categoriaRepository.save(Categoria.builder()
                .nombre("Bebidas")
                .descripcion("Refrescos, jugos y bebidas frias")
                .build());

        Categoria postres = categoriaRepository.save(Categoria.builder()
                .nombre("Postres")
                .descripcion("Dulces y postres para el final")
                .build());

        // Pizzas
        productoRepository.save(Producto.builder()
                .nombre("Pizza Mozzarella")
                .precio(1200.0)
                .descripcion("Pizza clasica con salsa de tomate y mozzarella")
                .stock(50)
                .disponible(true)
                .imagen("pizza_mozzarella.jpg")
                .categoria(pizzas)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Pizza Napolitana")
                .precio(1400.0)
                .descripcion("Pizza con tomate fresco, mozzarella y oregano")
                .stock(50)
                .disponible(true)
                .imagen("pizza_napolitana.jpg")
                .categoria(pizzas)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Pizza Cuatro Quesos")
                .precio(1600.0)
                .descripcion("Pizza con mozzarella, provolone, roquefort y parmesano")
                .stock(30)
                .disponible(true)
                .imagen("pizza_cuatro_quesos.jpg")
                .categoria(pizzas)
                .build());

        // Hamburguesas
        productoRepository.save(Producto.builder()
                .nombre("Hamburguesa Clasica")
                .precio(950.0)
                .descripcion("Pan brioche, carne 200g, lechuga, tomate y mayonesa")
                .stock(40)
                .disponible(true)
                .imagen("hamburguesa_clasica.jpg")
                .categoria(hamburguesas)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Hamburguesa BBQ")
                .precio(1100.0)
                .descripcion("Carne 200g, bacon, cheddar y salsa BBQ ahumada")
                .stock(40)
                .disponible(true)
                .imagen("hamburguesa_bbq.jpg")
                .categoria(hamburguesas)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Veggie Burger")
                .precio(900.0)
                .descripcion("Medallón de garbanzos, rúcula, tomate y hummus")
                .stock(25)
                .disponible(true)
                .imagen("veggie_burger.jpg")
                .categoria(hamburguesas)
                .build());

        // Bebidas
        productoRepository.save(Producto.builder()
                .nombre("Coca-Cola 500ml")
                .precio(350.0)
                .descripcion("Gaseosa Coca-Cola botella 500ml")
                .stock(100)
                .disponible(true)
                .imagen("coca_cola.jpg")
                .categoria(bebidas)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Agua Mineral 500ml")
                .precio(200.0)
                .descripcion("Agua mineral sin gas 500ml")
                .stock(100)
                .disponible(true)
                .imagen("agua_mineral.jpg")
                .categoria(bebidas)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Jugo de Naranja Natural")
                .precio(450.0)
                .descripcion("Jugo de naranja exprimido al momento, 400ml")
                .stock(30)
                .disponible(true)
                .imagen("jugo_naranja.jpg")
                .categoria(bebidas)
                .build());

        // Postres
        productoRepository.save(Producto.builder()
                .nombre("Tiramisú")
                .precio(700.0)
                .descripcion("Tiramisú casero con mascarpone y café")
                .stock(20)
                .disponible(true)
                .imagen("tiramisu.jpg")
                .categoria(postres)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Brownie con Helado")
                .precio(650.0)
                .descripcion("Brownie de chocolate caliente con helado de vainilla")
                .stock(25)
                .disponible(true)
                .imagen("brownie.jpg")
                .categoria(postres)
                .build());
    }

    private void crearPedidos() {
        if (!pedidoRepository.findAllByEliminadoFalse().isEmpty()) {
            return; // Ya hay pedidos, no duplicar
        }

        Usuario cliente = usuarioRepository.findByMailAndEliminadoFalse("cliente@foodstore.com").orElse(null);
        if (cliente == null) return;

        List<Producto> productos = productoRepository.findAllByEliminadoFalse();
        Map<String, Producto> mapaProductos = productos.stream()
                .collect(Collectors.toMap(Producto::getNombre, p -> p));

        // Pedido 1: terminado, efectivo — Pizza Mozzarella x2 ($2400) + Coca-Cola x1 ($350) = $2750
        Producto pizzaMozzarella = mapaProductos.get("Pizza Mozzarella");
        Producto cocaCola = mapaProductos.get("Coca-Cola 500ml");
        if (pizzaMozzarella != null && cocaCola != null) {
            Pedido pedido1 = Pedido.builder()
                    .fecha(LocalDate.now().minusDays(3))
                    .estado(Estado.TERMINADO)
                    .total(0.0)
                    .formaPago(FormaPago.EFECTIVO)
                    .usuario(cliente)
                    .build();
            pedido1.addDetallePedido(2, pizzaMozzarella);
            pedido1.addDetallePedido(1, cocaCola);
            pedidoRepository.save(pedido1);
        }

        // Pedido 2: confirmado, tarjeta — Hamburguesa Clasica x1 ($950) + Jugo Naranja x1 ($450) = $1400
        Producto hamburguesaClasica = mapaProductos.get("Hamburguesa Clasica");
        Producto jugoNaranja = mapaProductos.get("Jugo de Naranja Natural");
        if (hamburguesaClasica != null && jugoNaranja != null) {
            Pedido pedido2 = Pedido.builder()
                    .fecha(LocalDate.now().minusDays(1))
                    .estado(Estado.CONFIRMADO)
                    .total(0.0)
                    .formaPago(FormaPago.TARJETA)
                    .usuario(cliente)
                    .build();
            pedido2.addDetallePedido(1, hamburguesaClasica);
            pedido2.addDetallePedido(1, jugoNaranja);
            pedidoRepository.save(pedido2);
        }

        // Pedido 3: pendiente, transferencia — Tiramisú x2 ($1400) + Brownie con Helado x1 ($650) = $2050
        Producto tiramisu = mapaProductos.get("Tiramisú");
        Producto brownie = mapaProductos.get("Brownie con Helado");
        if (tiramisu != null && brownie != null) {
            Pedido pedido3 = Pedido.builder()
                    .fecha(LocalDate.now())
                    .estado(Estado.PENDIENTE)
                    .total(0.0)
                    .formaPago(FormaPago.TRANSFERENCIA)
                    .usuario(cliente)
                    .build();
            pedido3.addDetallePedido(2, tiramisu);
            pedido3.addDetallePedido(1, brownie);
            pedidoRepository.save(pedido3);
        }
    }
}
