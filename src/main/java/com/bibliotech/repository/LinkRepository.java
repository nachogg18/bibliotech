package com.bibliotech.repository;

import com.bibliotech.entity.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    
    Optional<Link> findByIdAndAndFechaBajaNull(Long id);

    List<Link> findByFechaBajaNull();
}
