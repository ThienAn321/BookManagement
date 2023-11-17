package com.learn.model;

import java.time.Instant;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.learn.model.enumeration.TitleToken;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "verification_token")
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class VerificationToken extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "title", nullable = false)
    private TitleToken titleToken;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
}
