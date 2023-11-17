package com.learn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query("SELECT book FROM Book book LEFT JOIN FETCH book.author LEFT JOIN FETCH book.category")
    Page<Book> findAllCustom(Specification<Book> specification, Pageable pageable);

    @Modifying
    @Query("UPDATE Book b SET b.isDeleted = true WHERE b.id = ?1")
    void deleteBook(Integer email);

}
