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

    public static Specification<Prestamo> hasId(Long id) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id);

            return idPredicate;
        };
    }

    public static Specification<Prestamo> hasMultaWithId(Long multaId) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Obtén la relación con la entidad multa
            root.fetch("multa");

            // Crea una condición para buscar por el ID de la multa
            Predicate multaIdPredicate = criteriaBuilder.equal(root.get("multa").get("id"), multaId);

            return multaIdPredicate;
        };
    }

    public static Specification<Prestamo> hasPrestamoEstadoWithId(Long prestamoEstadoId) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Obtén la relación con la entidad prestamo estado
            root.fetch("estado");

            // Crea una condición para buscar por el ID del prestamo estado
            Predicate prestamoEstadoIdPredicate = criteriaBuilder.equal(root.get("estado").get("id"), prestamoEstadoId);

            return prestamoEstadoIdPredicate;
        };
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

    public static Specification<Prestamo> hasEjemplarWithId(Long ejemplarId) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Obtén la relación con la entidad User
            root.fetch("ejemplar");

            // Crea una condición para buscar por el ID del usuario
            Predicate predicate = criteriaBuilder.equal(root.get("ejemplar").get("id"), ejemplarId);

            return predicate;
        };
    }

    public static Specification<Prestamo> hasUsuarioWithLegajo(String legajo) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Obtén la relación con la entidad User
            root.fetch("usuario");

            // Crea una condición para buscar por el ID del usuario
            Predicate predicate = criteriaBuilder.equal(root.get("usuario").get("legajo"), legajo);

            return predicate;
        };
    }


    public static Specification<Prestamo> hasUsuarioWithEmail(String email) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Obtén la relación con la entidad User
            root.fetch("usuario");

            // Crea una condición para buscar por el ID del usuario
            Predicate predicate = criteriaBuilder.equal(root.get("usuario").get("email"), email);

            return predicate;
        };
    }

    public static Specification<Prestamo> hasUsuarioWithDNI(String dni) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Obtén la relación con la entidad User
            root.fetch("usuario");

            // Crea una condición para buscar por el ID del usuario
            Predicate predicate = criteriaBuilder.equal(root.get("usuario").get("dni"), dni);

            return predicate;
        };
    }

    public static Specification<Prestamo> hasEstadoPrestamoWithNameAndFechaBajaNull(String nombreEstadoPrestamo) {
        return Specification.allOf(hasEstadoPrestamoWithName(nombreEstadoPrestamo), hasPrestamoEstadoWithFechaFinNull());
    }

    public static Specification<Prestamo> hasEstadoPrestamoWithName(String nombreEstadoPrestamo) {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            root.fetch("estado");


            Predicate ejemplarIdPredicate = criteriaBuilder.equal(root.get("estado").get("estado"), EstadoPrestamo.valueOf(nombreEstadoPrestamo));

            return ejemplarIdPredicate;
        };
    }

    public static Specification<Prestamo> hasPrestamoEstadoWithFechaFinNull() {
        return (Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            root.fetch("estado");


            Predicate ejemplarIdPredicate = criteriaBuilder.isNull(root.get("estado").get("fechaFin"));

            return ejemplarIdPredicate;
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
