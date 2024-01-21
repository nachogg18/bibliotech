package com.bibliotech.repository;

import com.bibliotech.entity.Autor;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends BaseRepository<Autor, Long>{
    List<Autor> findByFechaBajaNull();
    List<Autor> findByApellidoContainingIgnoreCaseOrNombreContainingIgnoreCase(String apellido, String nombre);

    List<Autor> findByIdIn(Long[] ids);

    @Query("SELECT DISTINCT CONCAT(a.apellido, ' ', a.nombre) AS nombre_completo FROM Autor a WHERE a.fechaBaja IS NULL ORDER BY CONCAT(a.apellido, ' ', a.nombre)")
    List<String> obtenerNombres();
}
