package com.bibliotech.repository;

import com.bibliotech.entity.Autor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends BaseRepository<Autor, Long>{
    List<Autor> findByApellidoContainingIgnoreCaseOrNombreContainingIgnoreCase(String apellido, String nombre);
}
