package com.utn.services;

import com.utn.dtos.usuario.UsuarioCreate;
import com.utn.dtos.usuario.UsuarioDto;
import com.utn.entities.Usuario;
import com.utn.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioDto crear(UsuarioCreate dto) {
        Usuario usuario = Usuario.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .mail(dto.mail())
                .celular(dto.celular())
                .contrasenia(dto.contrasenia())
                .rol(dto.rol())
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return toDto(guardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioDto> buscarPorId(Long id) {
        return usuarioRepository.findById(id).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioDto> buscarPorMail(String mail) {
        return usuarioRepository.findByMail(mail).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public long contar() {
        return usuarioRepository.count();
    }

    private UsuarioDto toDto(Usuario u) {
        return new UsuarioDto(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getMail(),
                u.getCelular(),
                u.getRol()
        );
    }
}

