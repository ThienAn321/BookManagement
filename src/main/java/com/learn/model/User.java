package com.learn.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learn.model.enumeration.Role;
import com.learn.model.enumeration.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", columnDefinition = "NVARCHAR(255)", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String fullname;

    @Column(name = "date_birth")
    private String dateBirth;

    @Column(name = "email", columnDefinition = "NVARCHAR(255)", unique = true, nullable = false)
    private String email;

    @Column(name = "phone", columnDefinition = "VARCHAR(10)", unique = true, nullable = false)
    private String phone;

    @Column(name = "failed_attempt", columnDefinition = "TINYINT")
    private Integer failedAttempt;

//    @Column(name = "create_at")
//    private String createAt;

    @OneToMany(mappedBy = "user")
    private List<BorrowManagement> borrowManagement;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_id")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserSession> userSession;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<VerificationToken> confirmToken;

}
