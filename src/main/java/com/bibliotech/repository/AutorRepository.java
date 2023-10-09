package com.bibliotech.repository;

import com.bibliotech.entity.Autor;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends BaseRepository<Autor, Long>{
    List<Autor> findByFechaBajaNull();
    List<Autor> findByApellidoContainingIgnoreCaseOrNombreContainingIgnoreCase(String apellido, String nombre);

    List<Autor> findByIdIn(Long[] ids);
}
