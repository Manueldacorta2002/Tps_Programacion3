package com.utn.repositories;

import com.utn.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByEliminadoFalse();
    Optional<Pedido> findByIdAndEliminadoFalse(Long id);
    List<Pedido> findAllByUsuarioIdAndEliminadoFalse(Long userId);
}
