package com.bibliotech.repository.specifications;

import com.bibliotech.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class PrestamoSpecifications {
    public static Specification<Prestamo> emptyRestrictions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or();
    }

    public static Specification<Prestamo> withoutResults() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or();
    }

    public static Specification<Prestamo> hasUserWithId(Long userId) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Obtén la relación con la entidad User
            root.fetch("usuario");

            // Crea una condición para buscar por el ID del usuario
            Predicate userIdPredicate = criteriaBuilder.equal(root.get("usuario").get("id"), userId);

            return userIdPredicate;
        };
    }

    public static <T> Specification<T> fechaInicioEstimadaDesde(Instant fechaDesde) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Crea una condición para buscar préstamos con fecha estimada mayor o igual a "fechaDesde"
            Predicate fechaInicioEstimadaPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("fechaInicioEstimada"), fechaDesde);

            return fechaInicioEstimadaPredicate;
        };
    }

    public static <T> Specification<T> fechaInicioEstimadaHasta(Instant fechaHasta) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Crea una condición para buscar préstamos con fecha estimada mayor o igual a "fechaDesde"
            Predicate fechaInicioEstimadaPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("fechaInicioEstimada"), fechaHasta);

            return fechaInicioEstimadaPredicate;
        };
    }

    public static <T> Specification<T> fechaFinEstimadaDesde(Instant fechaDesde) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Crea una condición para buscar préstamos con fecha estimada mayor o igual a "fechaDesde"
            Predicate fechaInicioEstimadaPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("fechaFinEstimada"), fechaDesde);

            return fechaInicioEstimadaPredicate;
        };
    }

    public static <T> Specification<T> fechaFinEstimadaHasta(Instant fechaHasta) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Crea una condición para buscar préstamos con fecha estimada mayor o igual a "fechaDesde"
            Predicate fechaInicioEstimadaPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("fechaFinEstimada"), fechaHasta);

            return fechaInicioEstimadaPredicate;
        };
    }
}
