package com.learn.model;

import java.time.Instant;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user_session")
@EntityListeners(AuditingEntityListener.class)
public class UserSession extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "session_id", columnDefinition = "NVARCHAR(255)")
    private String sessionID;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_refresh_token")
    private boolean isRefreshToken;

    @Column(name = "expire_at")
    private Instant expireAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
