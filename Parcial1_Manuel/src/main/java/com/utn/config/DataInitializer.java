package com.utn.config;

import com.utn.entities.Categoria;
import com.utn.entities.Producto;
import com.utn.entities.Usuario;
import com.utn.enums.Rol;
import com.utn.repositories.CategoriaRepository;
import com.utn.repositories.ProductoRepository;
import com.utn.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           CategoriaRepository categoriaRepository,
                           ProductoRepository productoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        crearUsuarios();
        crearCategorias();
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
}

