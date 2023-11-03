package com.learn.model;

import java.time.Instant;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user_session")
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class UserSession extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "session_id", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String sessionID;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "expire_at")
    private Instant expireAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
