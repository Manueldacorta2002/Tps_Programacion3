package com.utn.dtos.pedido;

import com.utn.dtos.detallePedido.DetallePedidoCreate;
import com.utn.enums.Estado;
import com.utn.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PedidoEdit(
        // opcional: si no viene, se usa la fecha actual
        LocalDate fecha,
        // opcional: si no viene, se usa PENDIENTE por defecto
        Estado estado,
        @NotNull(message = "La forma de pago es obligatoria")
        FormaPago formaPago,
        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,
        @NotEmpty(message = "El pedido debe tener al menos un detalle")
        List<@Valid DetallePedidoCreate> detalles
) {
}
