package com.utn.services;

import com.utn.dtos.detallePedido.DetallePedidoCreate;
import com.utn.dtos.detallePedido.DetallePedidoDto;
import com.utn.dtos.pedido.PedidoDto;
import com.utn.dtos.pedido.PedidoEdit;
import com.utn.entities.DetallePedido;
import com.utn.entities.Pedido;
import com.utn.entities.Producto;
import com.utn.entities.Usuario;
import com.utn.enums.Estado;
import com.utn.exceptions.BusinessException;
import com.utn.exceptions.ResourceNotFoundException;
import com.utn.repositories.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioService usuarioService, ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @Transactional
    public PedidoDto crear(PedidoEdit dto) {
        Usuario usuario = usuarioService.buscarEntidadPorId(dto.usuarioId());

        Pedido pedido = Pedido.builder()
                .fecha(dto.fecha() != null ? dto.fecha() : LocalDate.now())
                .estado(dto.estado() != null ? dto.estado() : Estado.PENDIENTE)
                .formaPago(dto.formaPago())
                .total(0.0)
                .usuario(usuario)
                .build();

        for (DetallePedidoCreate detalleDto : dto.detalles()) {
            Producto producto = productoService.findEntityById(detalleDto.productoId());
            validarProductoParaPedido(producto, detalleDto.cantidad());
            producto.setStock(producto.getStock() - detalleDto.cantidad());
            pedido.addDetallePedido(detalleDto.cantidad(), producto);
        }

        pedido.calcularTotal();
        return toDto(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoDto> listarTodos() {
        return pedidoRepository.findAllByEliminadoFalse().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDto buscarPorId(Long id) {
        return toDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public List<PedidoDto> listarPorUsuario(Long userId) {
        usuarioService.buscarEntidadPorId(userId);
        return pedidoRepository.findAllByUsuarioIdAndEliminadoFalse(userId).stream().map(this::toDto).toList();
    }

    @Transactional
    public PedidoDto actualizarEstado(Long id, Estado estado) {
        Pedido pedido = buscarEntidadPorId(id);
        pedido.setEstado(estado);
        return toDto(pedidoRepository.save(pedido));
    }

    @Transactional
    public void eliminar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);
        pedido.setEliminado(true);
        pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Pedido buscarEntidadPorId(Long id) {
        return pedidoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id " + id));
    }

    private void validarProductoParaPedido(Producto producto, Integer cantidad) {
        if (Boolean.FALSE.equals(producto.getDisponible())) {
            throw new BusinessException("El producto " + producto.getNombre() + " no esta disponible");
        }
        if (producto.getStock() < cantidad) {
            throw new BusinessException("Stock insuficiente para el producto " + producto.getNombre());
        }
    }

    private PedidoDto toDto(Pedido p) {
        List<DetallePedidoDto> detallesDto = p.getDetalles().stream()
                .filter(detalle -> !detalle.isEliminado())
                .map(this::toDetalleDto)
                .toList();

        return new PedidoDto(p.getId(), p.getFecha(), p.getEstado(), p.getFormaPago(), p.getTotal(), p.getUsuario() != null ? p.getUsuario().getId() : null, detallesDto);
    }

    private DetallePedidoDto toDetalleDto(DetallePedido d) {
        return new DetallePedidoDto(d.getId(), d.getCantidad(), d.getSubtotal(), d.getProducto() != null ? d.getProducto().getId() : null);
    }
}
