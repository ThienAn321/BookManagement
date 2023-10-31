package com.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
