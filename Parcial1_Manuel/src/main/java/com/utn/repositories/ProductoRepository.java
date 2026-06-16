package com.utn.repositories;

import com.utn.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByEliminadoFalse();
    Optional<Producto> findByIdAndEliminadoFalse(Long id);
    List<Producto> findAllByCategoriaIdAndEliminadoFalse(Long categoriaId);
}
