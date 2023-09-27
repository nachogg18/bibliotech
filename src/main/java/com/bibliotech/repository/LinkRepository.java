package com.bibliotech.repository;

import com.bibliotech.entity.Link;
import com.bibliotech.entity.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByFechaBajaNull();
}
