package com.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.model.BorrowManagement;

@Repository
public interface BorrowManagementRepository extends JpaRepository<BorrowManagement, Integer> {

}
