package com.bibliotech.security.repository;

import com.bibliotech.security.entity.VerificationCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    
    Optional<VerificationCode> findByCode(String code);

    List<VerificationCode> findByEmail(String email);
}