package com.bibliotech.repository;

import com.bibliotech.entity.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {
    List<Plataforma> findByFechaBajaNull();

}
