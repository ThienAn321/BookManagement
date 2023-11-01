package com.learn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token", columnDefinition = "NVARCHAR(255)", unique = true, nullable = false)
    private String token;
    
    @Column(name = "otp", columnDefinition = "VARCHAR(60)", unique = true, nullable = false)
    private String otp;

    @Column(name = "create_at", nullable = false)
    private String createdAt;
    
    @Column(name = "expire_at", nullable = false)
    private String expireAt;
    
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
