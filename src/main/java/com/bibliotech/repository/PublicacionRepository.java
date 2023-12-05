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


    @Query(nativeQuery = true, value = "SELECT p.id, p.titulo, p.sinopsis, p.anio, " +
            "tp.nombre AS tipo, p.isbn, " +
            "GROUP_CONCAT(DISTINCT e.nombre SEPARATOR ';') AS editoriales, " +
            "GROUP_CONCAT(DISTINCT CONCAT(a.nombre, ' ', a.apellido) SEPARATOR ';') as autores, " +
            "ed.nombre " +
            "FROM publicacion p " +
            "LEFT JOIN edicion ed ON p.edicion_id = ed.id " +
            "LEFT JOIN tipo_publicacion tp ON p.tipo_publicacion_id = tp.id " +
            "LEFT JOIN publicacion_editorial pe ON pe.publicacion_id = p.id " +
            "LEFT JOIN editorial e ON e.id = pe.editorial_id " +
            "LEFT JOIN publicacion_autor pa ON pa.publicacion_id = p.id " +
            "LEFT JOIN autor a ON a.id = pa.autor_id " +
            "WHERE p.anio = :input OR " +
            "      LOWER(p.titulo) LIKE CONCAT('%', :input, '%') OR " +
            "      LOWER(tp.nombre) LIKE CONCAT('%', :input, '%') OR " +
            "      p.isbn LIKE CONCAT('%', :input, '%') OR " +
            "      LOWER(e.nombre) LIKE CONCAT('%', :input, '%') OR " +
            "      LOWER(a.nombre) LIKE CONCAT('%', :input, '%') OR " +
            "      LOWER(a.apellido) LIKE CONCAT('%', :input, '%') " +
            "GROUP BY p.id, p.titulo, p.sinopsis, p.anio, tp.nombre, p.isbn")
    List<Object[]> obtenerPublicacionesItemMobile(@Param("input") String input);

//    Page<Publicacion> findAllByFechaBajaIsNullAndAutoresIdInAndEditorialesIdInAndAnioAndTituloAndEdicionNombre
//            (Pageable pageable, Collection<Long> autores_id, Collection<Long> editoriales_id, Integer anio, String titulo, String edicion_nombre);

}
