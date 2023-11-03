package com.learn.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "author")
@EqualsAndHashCode(callSuper = false)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String fullname;

    @Column(name = "date_birth", columnDefinition = "DATETIME", nullable = false)
    private Instant dateBirth;

    @OneToMany(mappedBy = "author")
    private List<Book> book;
}
