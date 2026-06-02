package com.utn.services;

import com.utn.dtos.categoria.CategoriaCreate;
import com.utn.dtos.categoria.CategoriaDto;
import com.utn.dtos.categoria.CategoriaEdit;
import com.utn.entities.Categoria;
import com.utn.exceptions.BusinessException;
import com.utn.exceptions.ResourceNotFoundException;
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
        if (categoriaRepository.existsByNombreIgnoreCaseAndEliminadoFalse(dto.nombre())) {
            throw new BusinessException("Ya existe una categoria con ese nombre");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();

        return toDto(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public CategoriaDto buscarPorId(Long id) {
        return toDto(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public Categoria findEntityById(Long id) {
        return categoriaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + id));
    }

    @Transactional(readOnly = true)
    public List<CategoriaDto> listarTodos() {
        return categoriaRepository.findAllByEliminadoFalse().stream().map(this::toDto).toList();
    }

    @Transactional
    public CategoriaDto actualizar(Long id, CategoriaEdit dto) {
        Categoria categoria = findEntityById(id);
        categoria.setNombre(dto.nombre());
        categoria.setDescripcion(dto.descripcion());
        return toDto(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = findEntityById(id);
        categoria.setEliminado(true);
        categoriaRepository.save(categoria);
    }

    private CategoriaDto toDto(Categoria c) {
        return new CategoriaDto(c.getId(), c.getNombre(), c.getDescripcion());
    }
}
