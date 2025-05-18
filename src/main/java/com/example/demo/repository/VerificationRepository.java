package com.example.demo.repository;

import com.example.demo.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationRepository extends JpaRepository<VerificationEntity, UUID> {
}
