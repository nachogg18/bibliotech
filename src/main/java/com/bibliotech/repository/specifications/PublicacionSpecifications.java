package com.bibliotech.repository.specifications;

import com.bibliotech.entity.Autor;
import com.bibliotech.entity.Publicacion;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class PublicacionSpecifications {

    public static Specification<Publicacion> empty() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and();
    }

    public static Specification<Publicacion> hasIsbn(String isbn) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isbn"), isbn)
        );
    }

    public static Specification<Publicacion> hasTituloLike(String titulo) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.like(root.get("titulo"), "%"+titulo.toLowerCase(Locale.ROOT)+"%")
        );
    }

    public static Specification<Publicacion> hasAnio(String anio) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.equal(root.get("anio"), Integer.valueOf(anio)));

    }

    public static Specification<Publicacion> hasAutorWithFirstName(String nombreAutor) {
        return (root,query,criteriaBuilder) ->
        {
            Join<Autor, Publicacion> autorPublicacionJoin = root.join("autores");
            Predicate autorPredicate = criteriaBuilder.equal(autorPublicacionJoin.get("nombre"), nombreAutor);
            return autorPredicate;
        };
    }

    public static Specification<Publicacion> hasAutorWithLastName(String apellidoAutor) {
        return (root,query,criteriaBuilder) ->
        {
            Join<Autor, Publicacion> autorPublicacionJoin = root.join("autores");
            Predicate autorPredicate = criteriaBuilder.equal(autorPublicacionJoin.get("apellido"), apellidoAutor);
            return autorPredicate;
        };
    }

    public static Specification<Publicacion> hasAutorWithFirstOrLastName(String autor) {
        return hasAutorWithLastName(autor).or(hasAutorWithFirstName(autor));
    }

}
