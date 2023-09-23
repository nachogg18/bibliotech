package com.bibliotech.repository;

import com.bibliotech.entity.Usuario;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    List<Usuario> findByEmail(String username);
}
