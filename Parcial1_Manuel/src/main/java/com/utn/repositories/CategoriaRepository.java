package com.utn.repositories;

import com.utn.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAllByEliminadoFalse();
    Optional<Categoria> findByIdAndEliminadoFalse(Long id);
    boolean existsByNombreIgnoreCaseAndEliminadoFalse(String nombre);
}
