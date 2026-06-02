package com.utn.dtos.pedido;

import com.utn.dtos.detallePedido.DetallePedidoDto;
import com.utn.enums.Estado;
import com.utn.enums.FormaPago;

import java.time.LocalDate;
import java.util.List;

public record PedidoDto(
        Long id,
        LocalDate fecha,
        Estado estado,
        FormaPago formaPago,
        Double total,
        Long usuarioId,
        List<DetallePedidoDto> detalles
) {
}

