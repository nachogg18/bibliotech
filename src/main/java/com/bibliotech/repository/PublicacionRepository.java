package com.bibliotech.repository;

import com.bibliotech.entity.Comentario;
import com.bibliotech.entity.Publicacion;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long>, JpaSpecificationExecutor<Publicacion> {
    List<Publicacion> findByCategoriaPublicacionListIdIn(Collection<Long> categoriaPublicacionList_id);

    List<Publicacion> findByCategoriaPublicacionListIdInAndAutoresIdIn(Collection<Long> categoriaPublicacionList_id, Collection<Long> autores_id);

    List<Publicacion> findByCategoriaPublicacionListIdInAndTituloContainingIgnoreCase(Collection<Long> categoriaPublicacionList_id, String titulo);

    Page<Publicacion> findAllByFechaBajaIsNull(Pageable pageable);

    @Query("SELECT p.comentarios FROM Publicacion p WHERE p.id = :publicacionId")
    List<Comentario> findComentariosByPublicacionId(@Param("publicacionId") Long publicacionId);

    Publicacion save(Publicacion publicacion);

//    Page<Publicacion> findAllByFechaBajaIsNullAndAutoresIdInAndEditorialesIdInAndAnioAndTituloAndEdicionNombre
//            (Pageable pageable, Collection<Long> autores_id, Collection<Long> editoriales_id, Integer anio, String titulo, String edicion_nombre);

}
