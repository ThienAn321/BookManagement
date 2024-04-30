package com.learn.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "book")
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Book extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String title;
    
    @Column(name="is_deleted", columnDefinition = "BOOLEAN")
    private Boolean isDeleted;

    @Column(name = "stock", columnDefinition = "INT", nullable = false)
    private Integer stock;

    @Column(name = "rating", columnDefinition = "INT", nullable = true)
    private Integer rating;

    @Column(name = "description", columnDefinition = "NVARCHAR(255)", nullable = true)
    private String description;

    @Column(name = "release_date", columnDefinition = "DATETIME", nullable = false)
    private Instant releaseDate;

    @OneToMany(mappedBy = "book")
    private List<BorrowManagement> borrowManagement;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
