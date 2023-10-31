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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_session")
public class UserSession {
    
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
    
    @Column(name = "create_at")
    private String createAt; 
    
    @Column(name = "expire_at")
    private String expireAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
