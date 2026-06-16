package com.utn.repositories;

import com.utn.entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findAllByPedidoIdAndEliminadoFalse(Long pedidoId);
}
