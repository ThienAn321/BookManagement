package com.learn.model;

import java.time.Instant;

import jakarta.persistence.Column;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity {

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    @JsonIgnore
    private Instant createdAt = Instant.now();

    public Instant getCreatedDate() {
        return createdAt;
    }

    public void setCreatedDate(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
