package com.utn.repositories;

import com.utn.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByMailAndEliminadoFalse(String mail);
    boolean existsByMailAndEliminadoFalse(String mail);
    List<Usuario> findAllByEliminadoFalse();
    Optional<Usuario> findByIdAndEliminadoFalse(Long id);
}
