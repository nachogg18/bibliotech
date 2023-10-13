package com.bibliotech.repository;

import com.bibliotech.entity.Multa;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.security.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class MultaSpecifications {

    public static Specification<Multa> empty() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and();
    }

    public static Specification<Multa> hasFechaDesdeLessThanOrEqualTo(Instant date) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("fechaInicio"), date)
        );
    }

    public static Specification<Multa> hasFechaHastaGreaterThanOrEqualTo(Instant date) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("fechaFin"), date)
        );
    }

    public static Specification<Multa> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            Join<User, Multa> userMultaJoin = root.join("user");
            return criteriaBuilder.equal(userMultaJoin.get("id"), Long.valueOf(userId));
        };
    }

    public static Specification<Multa> hasPrestamoId(String prestamoId) {
        return (root, query, criteriaBuilder) -> {
            Join<Prestamo, Multa> userMultaJoin = root.join("prestamo");
            return criteriaBuilder.equal(userMultaJoin.get("id"), Long.valueOf(prestamoId));
        };
    }

}
