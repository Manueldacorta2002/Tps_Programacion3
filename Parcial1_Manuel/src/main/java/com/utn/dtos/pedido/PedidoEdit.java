package com.utn.dtos.pedido;

import com.utn.dtos.detallePedido.DetallePedidoCreate;
import com.utn.entities.Pedido;
import com.utn.enums.Estado;
import com.utn.enums.FormaPago;

import java.time.LocalDate;
import java.util.List;

public record PedidoEdit(
        LocalDate fecha,
        Estado estado,
        FormaPago formaPago,
        Long usuarioId,
        List<DetallePedidoCreate> detalles
) {
}

