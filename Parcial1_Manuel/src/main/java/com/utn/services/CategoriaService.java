package com.utn.services;

import com.utn.dtos.categoria.CategoriaCreate;
import com.utn.dtos.categoria.CategoriaDto;
import com.utn.entities.Categoria;
import com.utn.repositories.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaDto crear(CategoriaCreate dto) {
        Categoria categoria = Categoria.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();

        Categoria guardado = categoriaRepository.save(categoria);
        return toDto(guardado);
    }

    @Transactional(readOnly = true)
    public Categoria findEntityById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada id=" + id));
    }

    @Transactional(readOnly = true)
    public List<CategoriaDto> listarTodos() {
        return categoriaRepository.findAll().stream().map(this::toDto).toList();
    }

    private CategoriaDto toDto(Categoria c) {
        return new CategoriaDto(
                c.getId(),
                c.getNombre(),
                c.getDescripcion()
        );
    }
}

