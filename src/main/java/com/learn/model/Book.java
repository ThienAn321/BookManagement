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
import jakarta.validation.constraints.NotNull;
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
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class Book extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "title", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String title;

    @NotNull
    @Column(name = "stock", columnDefinition = "INT", nullable = false)
    private Integer stock;

    @NotNull
    @Column(name = "rating", columnDefinition = "INT", nullable = true)
    private Integer rating;

    @Column(name = "description", columnDefinition = "NVARCHAR(255)", nullable = true)
    private String description;

    @Column(name = "release_date")
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
