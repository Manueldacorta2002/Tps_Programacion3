package com.utn.controllers;

import com.utn.dtos.pedido.PedidoDto;
import com.utn.dtos.pedido.PedidoEdit;
import com.utn.dtos.pedido.PedidoStatusUpdate;
import com.utn.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoDto> listar() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public PedidoDto obtener(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping("/user/{userId}")
    public List<PedidoDto> listarPorUsuario(@PathVariable Long userId) {
        return pedidoService.listarPorUsuario(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDto crear(@Valid @RequestBody PedidoEdit request) {
        return pedidoService.crear(request);
    }

    @PatchMapping("/{id}/status")
    public PedidoDto actualizarEstado(@PathVariable Long id, @Valid @RequestBody PedidoStatusUpdate request) {
        return pedidoService.actualizarEstado(id, request.estado());
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
    }
}
