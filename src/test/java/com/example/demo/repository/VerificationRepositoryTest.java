package com.example.demo.repository;

import com.example.demo.entity.*;
import com.example.demo.util.SourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VerificationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VerificationRepository verificationRepository;

    private UUID testVerificationId;
    private CompanyEntity companyEntity1;
    private CompanyEntity companyEntity2;
    private VerificationEntity verificationEntity;

    @BeforeEach
    void setUp() {
        testVerificationId = UUID.randomUUID();

        companyEntity1 = CompanyEntity.builder()
                .cin("TEST123")
                .name("Test Company 1")
                .registrationDate("2024-01-01")
                .address("Test Address 1")
                .isActive(true)
                .build();

        companyEntity2 = CompanyEntity.builder()
                .cin("TEST456")
                .name("Test Company 2")
                .registrationDate("2024-01-02")
                .address("Test Address 2")
                .isActive(true)
                .build();

        verificationEntity = VerificationEntity.builder()
                .verificationId(testVerificationId)
                .queryText("TEST123")
                .timestamp(LocalDateTime.now())
                .source(SourceType.FREE)
                .result(SuccessResultEntityWithOtherResults.builder()
                        .result(companyEntity1)
                        .otherResults(Arrays.asList(companyEntity2))
                        .build())
                .build();
    }

    @Test
    void saveAndFindById() {
        entityManager.persist(verificationEntity);
        entityManager.flush();

        Optional<VerificationEntity> found = verificationRepository.findById(testVerificationId);

        assertTrue(found.isPresent());
        VerificationEntity savedVerification = found.get();
        assertEquals(testVerificationId, savedVerification.getVerificationId());
        assertEquals("TEST123", savedVerification.getQueryText());
        assertEquals(SourceType.FREE, savedVerification.getSource());
        assertTrue(savedVerification.getResult() instanceof SuccessResultEntityWithOtherResults);
        
        SuccessResultEntityWithOtherResults result = (SuccessResultEntityWithOtherResults) savedVerification.getResult();
        assertEquals(companyEntity1.getCin(), result.getResult().getCin());
        assertEquals(companyEntity1.getName(), result.getResult().getName());
        assertEquals(1, result.getOtherResults().size());
        assertEquals(companyEntity2.getCin(), result.getOtherResults().get(0).getCin());
        assertEquals(companyEntity2.getName(), result.getOtherResults().get(0).getName());
    }

    @Test
    void findAll() {
        VerificationEntity verificationEntity2 = VerificationEntity.builder()
                .verificationId(UUID.randomUUID())
                .queryText("TEST456")
                .timestamp(LocalDateTime.now())
                .source(SourceType.PREMIUM)
                .result(SuccessResultEntity.builder()
                        .result(companyEntity2)
                        .build())
                .build();

        entityManager.persist(verificationEntity);
        entityManager.persist(verificationEntity2);
        entityManager.flush();

        List<VerificationEntity> allVerifications = verificationRepository.findAll();

        assertEquals(2, allVerifications.size());

        assertTrue(allVerifications.stream().anyMatch(v -> v.getVerificationId().equals(testVerificationId)));
        assertTrue(allVerifications.stream().anyMatch(v -> v.getVerificationId().equals(verificationEntity2.getVerificationId())));
    }
} 