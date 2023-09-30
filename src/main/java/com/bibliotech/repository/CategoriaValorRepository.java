package com.bibliotech.repository;

import com.bibliotech.entity.CategoriaValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaValorRepository extends JpaRepository<CategoriaValor, Long> {
    List<CategoriaValor> findByFechaBajaNull();
}
