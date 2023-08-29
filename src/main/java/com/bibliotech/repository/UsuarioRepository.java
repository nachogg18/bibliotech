package com.bibliotech.repository;

import com.bibliotech.entity.Usuario;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Long> {

    List<Usuario> findByEmail(String email);
    
}
