package com.utn.services;

import com.utn.dtos.detallePedido.DetallePedidoCreate;
import com.utn.dtos.detallePedido.DetallePedidoDto;
import com.utn.dtos.pedido.PedidoDto;
import com.utn.dtos.pedido.PedidoEdit;
import com.utn.entities.Pedido;
import com.utn.entities.Producto;
import com.utn.entities.Usuario;
import com.utn.repositories.PedidoRepository;
import com.utn.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository,
                          UsuarioRepository usuarioRepository,
                          ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoService = productoService;
    }

    @Transactional
    public PedidoDto crear(PedidoEdit dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado id=" + dto.usuarioId()));

        Pedido pedido = Pedido.builder()
                .fecha(dto.fecha())
                .estado(dto.estado())
                .formaPago(dto.formaPago())
                .usuario(usuario)
                .build();

        for (DetallePedidoCreate detalle : dto.detalles()) {
            Producto producto = productoService.findEntityById(detalle.productoId());
            pedido.addDetallePedido(detalle.cantidad(), producto);
        }

        // Pedido.addDetallePedido recalcula total
        Pedido guardado = pedidoRepository.save(pedido);
        return toDto(guardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoDto> listarTodos() {
        return pedidoRepository.findAll().stream().map(this::toDto).toList();
    }

    private PedidoDto toDto(Pedido p) {
        List<DetallePedidoDto> detallesDto = p.getDetalles().stream()
                .map(d -> new DetallePedidoDto(
                        d.getId(),
                        d.getCantidad(),
                        d.getSubtotal(),
                        d.getProducto() != null ? d.getProducto().getId() : null
                ))
                .toList();

        return new PedidoDto(
                p.getId(),
                p.getFecha(),
                p.getEstado(),
                p.getFormaPago(),
                p.getTotal(),
                p.getUsuario() != null ? p.getUsuario().getId() : null,
                detallesDto
        );
    }
}

