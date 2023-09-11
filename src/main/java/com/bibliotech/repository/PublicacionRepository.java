package com.bibliotech.repository;

import com.bibliotech.entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByCategoriaPublicacionListIdIn(Collection<Long> categoriaPublicacionList_id);

    List<Publicacion> findByCategoriaPublicacionListIdInAndAutoresIdIn(Collection<Long> categoriaPublicacionList_id, Collection<Long> autores_id);

    List<Publicacion> findByCategoriaPublicacionListIdInAndTituloContainingIgnoreCase(Collection<Long> categoriaPublicacionList_id, String titulo);

    Page<Publicacion> findAllByFechaBajaIsNull(Pageable pageable);

//    Page<Publicacion> findAllByFechaBajaIsNullAndAutoresIdInAndEditorialesIdInAndAnioAndTituloAndEdicionNombre
//            (Pageable pageable, Collection<Long> autores_id, Collection<Long> editoriales_id, Integer anio, String titulo, String edicion_nombre);

}
