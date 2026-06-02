package com.utn.dtos.pedido;

import com.utn.enums.Estado;
import jakarta.validation.constraints.NotNull;

public record PedidoStatusUpdate(
        @NotNull(message = "El estado es obligatorio")
        Estado estado
) {
}
