package com.utn.controllers;

import com.utn.dtos.producto.ProductoCreate;
import com.utn.dtos.producto.ProductoDto;
import com.utn.dtos.producto.ProductoEdit;
import com.utn.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDto> listar() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ProductoDto obtener(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductoDto> listarPorCategoria(@PathVariable Long categoryId) {
        return productoService.listarPorCategoria(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDto crear(@Valid @RequestBody ProductoCreate request) {
        return productoService.crear(request);
    }

    @PutMapping("/{id}")
    public ProductoDto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoEdit request) {
        return productoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }
}
