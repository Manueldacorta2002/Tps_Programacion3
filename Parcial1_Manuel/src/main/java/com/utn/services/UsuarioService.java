package com.utn.services;

import com.utn.config.PasswordEncoderUtil;
import com.utn.dtos.usuario.UsuarioCreate;
import com.utn.dtos.usuario.UsuarioDto;
import com.utn.dtos.usuario.UsuarioEdit;
import com.utn.entities.Usuario;
import com.utn.enums.Rol;
import com.utn.exceptions.BusinessException;
import com.utn.exceptions.ResourceNotFoundException;
import com.utn.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioDto crear(UsuarioCreate dto) {
        if (usuarioRepository.existsByMailAndEliminadoFalse(dto.mail())) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .mail(dto.mail())
                .celular(dto.celular())
                .contrasenia(PasswordEncoderUtil.encode(dto.contrasenia()))
                .rol(dto.rol() != null ? dto.rol() : Rol.USUARIO)
                .build();

        return toDto(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarTodos() {
        return usuarioRepository.findAllByEliminadoFalse().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDto buscarPorId(Long id) {
        return toDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadPorId(Long id) {
        return usuarioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadPorMail(String mail) {
        return usuarioRepository.findByMailAndEliminadoFalse(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email " + mail));
    }

    @Transactional(readOnly = true)
    public UsuarioDto buscarPorMail(String mail) {
        return toDto(buscarEntidadPorMail(mail));
    }

    @Transactional
    public UsuarioDto actualizar(Long id, UsuarioEdit dto) {
        Usuario usuario = buscarEntidadPorId(id);

        if (!usuario.getMail().equalsIgnoreCase(dto.mail())
                && usuarioRepository.existsByMailAndEliminadoFalse(dto.mail())) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setMail(dto.mail());
        usuario.setCelular(dto.celular());
        usuario.setContrasenia(PasswordEncoderUtil.encode(dto.contrasenia()));
        usuario.setRol(dto.rol() != null ? dto.rol() : usuario.getRol());

        return toDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscarEntidadPorId(id);
        usuario.setEliminado(true);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public long contar() {
        return usuarioRepository.findAllByEliminadoFalse().size();
    }

    public UsuarioDto toDto(Usuario u) {
        return new UsuarioDto(u.getId(), u.getNombre(), u.getApellido(), u.getMail(), u.getCelular(), u.getRol());
    }
}
