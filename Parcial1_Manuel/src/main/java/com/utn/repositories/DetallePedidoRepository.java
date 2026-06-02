package com.utn.repositories;

import com.utn.entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findAllByPedidoIdAndEliminadoFalse(Long pedidoId);
}
