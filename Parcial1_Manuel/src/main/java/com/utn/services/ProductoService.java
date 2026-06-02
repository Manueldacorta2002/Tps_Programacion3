package com.utn.services;

import com.utn.dtos.producto.ProductoCreate;
import com.utn.dtos.producto.ProductoDto;
import com.utn.dtos.producto.ProductoEdit;
import com.utn.entities.Categoria;
import com.utn.entities.Producto;
import com.utn.exceptions.ResourceNotFoundException;
import com.utn.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepository, CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional
    public ProductoDto crear(ProductoCreate dto) {
        Categoria categoria = categoriaService.findEntityById(dto.categoriaId());

        Producto producto = Producto.builder()
                .nombre(dto.nombre())
                .precio(dto.precio())
                .descripcion(dto.descripcion())
                .stock(dto.stock())
                .imagen(dto.imagen())
                .disponible(dto.disponible())
                .categoria(categoria)
                .build();

        return toDto(productoRepository.save(producto));
    }

    @Transactional(readOnly = true)
    public ProductoDto buscarPorId(Long id) {
        return toDto(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public Producto findEntityById(Long id) {
        return productoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarTodos() {
        return productoRepository.findAllByEliminadoFalse().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarPorCategoria(Long categoriaId) {
        categoriaService.findEntityById(categoriaId);
        return productoRepository.findAllByCategoriaIdAndEliminadoFalse(categoriaId).stream().map(this::toDto).toList();
    }

    @Transactional
    public ProductoDto actualizar(Long id, ProductoEdit dto) {
        Producto producto = findEntityById(id);
        Categoria categoria = categoriaService.findEntityById(dto.categoriaId());

        producto.setNombre(dto.nombre());
        producto.setPrecio(dto.precio());
        producto.setDescripcion(dto.descripcion());
        producto.setStock(dto.stock());
        producto.setImagen(dto.imagen());
        producto.setDisponible(dto.disponible());
        producto.setCategoria(categoria);

        return toDto(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = findEntityById(id);
        producto.setEliminado(true);
        productoRepository.save(producto);
    }

    public ProductoDto toDto(Producto p) {
        Long categoriaId = p.getCategoria() != null ? p.getCategoria().getId() : null;
        String categoriaNombre = p.getCategoria() != null ? p.getCategoria().getNombre() : null;
        return new ProductoDto(p.getId(), p.getNombre(), p.getPrecio(), p.getDescripcion(), p.getStock(), p.getImagen(), p.getDisponible(), categoriaId, categoriaNombre);
    }
}
