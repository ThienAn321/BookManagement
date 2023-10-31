package com.learn.model;

import java.time.LocalDate;

import com.learn.model.enumeration.BorrowStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "borrow_management")
public class BorrowManagement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @Column(name = "quantity", columnDefinition = "INT", nullable = false)
    private Integer quantity;
    
    @Column(name = "create_date")
    private LocalDate createDate;
    
    @Column(name = "borrow_date")
    private LocalDate borrowDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_id")
    private BorrowStatus borrowStatus;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
