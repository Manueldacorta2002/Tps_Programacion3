package com.utn.services;

import com.utn.dtos.producto.ProductoCreate;
import com.utn.dtos.producto.ProductoDto;
import com.utn.entities.Categoria;
import com.utn.entities.Producto;
import com.utn.repositories.CategoriaRepository;
import com.utn.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProductoDto crear(ProductoCreate dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada id=" + dto.categoriaId()));

        Producto producto = Producto.builder()
                .nombre(dto.nombre())
                .precio(dto.precio())
                .descripcion(dto.descripcion())
                .stock(dto.stock())
                .imagen(dto.imagen())
                .disponible(dto.disponible())
                .categoria(categoria)
                .build();

        Producto guardado = productoRepository.save(producto);
        return toDto(guardado);
    }

    @Transactional(readOnly = true)
    public Producto findEntityById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado id=" + id));
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarTodos() {
        return productoRepository.findAll().stream().map(this::toDto).toList();
    }

    private ProductoDto toDto(Producto p) {
        Long categoriaId = (p.getCategoria() != null) ? p.getCategoria().getId() : null;
        String categoriaNombre = (p.getCategoria() != null) ? p.getCategoria().getNombre() : null;

        return new ProductoDto(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getDescripcion(),
                p.getStock(),
                p.getImagen(),
                p.getDisponible(),
                categoriaId,
                categoriaNombre
        );
    }
}

