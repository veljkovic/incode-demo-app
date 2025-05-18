package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.util.SourceType;

@Entity
@Data
@Builder
@Table(name = "verifications")
@NoArgsConstructor
@AllArgsConstructor
public class VerificationEntity {

    @Id
    @NotNull
    @Column(name = "verification_id", updatable = false)
    private UUID verificationId;

    @NotBlank
    @Column(name = "query_text", nullable = false)
    private String queryText;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private ResultEntity<?> result;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private SourceType source;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (verificationId == null) {
            verificationId = UUID.randomUUID();
        }
    }
}
