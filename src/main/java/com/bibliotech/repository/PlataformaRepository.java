package com.bibliotech.repository;

import com.bibliotech.entity.Plataforma;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {
    List<Plataforma> findByFechaBajaNull();

}
