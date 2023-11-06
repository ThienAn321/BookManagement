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
import jakarta.persistence.OneToOne;
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
@Table(name = "verification_token")
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class VerificationToken extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token", columnDefinition = "NVARCHAR(255)", unique = true, nullable = false)
    private String token;

    @Column(name = "otp", columnDefinition = "VARCHAR(60)", unique = true, nullable = false)
    private String otp;

    @Column(name = "expire_at", nullable = false)
    private Instant expireAt;

    @Column(name = "is_verify", nullable = false)
    private boolean isVerify;
    
    @Column(name = "is_expire", nullable = false)
    private boolean isExpire;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    
}
