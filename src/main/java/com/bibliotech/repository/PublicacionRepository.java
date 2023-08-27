package com.bibliotech.repository;

import com.bibliotech.entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByCategoriaPublicacionListIdIn(Collection<Long> categoriaPublicacionList_id);
    List<Publicacion> findByCategoriaPublicacionListIdInAndAutoresIdIn(Collection<Long> categoriaPublicacionList_id, Collection<Long> autores_id);
    List<Publicacion> findByCategoriaPublicacionListIdInAndTituloContainingIgnoreCase(Collection<Long> categoriaPublicacionList_id, String titulo);
    Page<Publicacion> findAllByFechaBajaIsNull(Pageable pageable);

}
