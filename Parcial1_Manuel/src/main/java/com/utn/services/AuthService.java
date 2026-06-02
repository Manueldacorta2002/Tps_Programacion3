package com.utn.services;

import com.utn.config.PasswordEncoderUtil;
import com.utn.dtos.auth.LoginRequest;
import com.utn.dtos.usuario.UsuarioCreate;
import com.utn.dtos.usuario.UsuarioDto;
import com.utn.entities.Usuario;
import com.utn.enums.Rol;
import com.utn.exceptions.BusinessException;
import com.utn.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioDto register(UsuarioCreate request) {
        UsuarioCreate registerRequest = new UsuarioCreate(
                request.nombre(),
                request.apellido(),
                request.mail(),
                request.celular(),
                request.contrasenia(),
                Rol.USUARIO
        );
        return usuarioService.crear(registerRequest);
    }

    @Transactional(readOnly = true)
    public UsuarioDto login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByMailAndEliminadoFalse(request.mail())
                .orElseThrow(() -> new BusinessException("Credenciales invalidas"));

        if (!PasswordEncoderUtil.matches(request.contrasenia(), usuario.getContrasenia())) {
            throw new BusinessException("Credenciales invalidas");
        }

        return usuarioService.toDto(usuario);
    }
}
