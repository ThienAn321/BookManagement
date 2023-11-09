package com.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.model.SmsOTP;

@Repository
public interface SmsOTPRepository extends JpaRepository<SmsOTP, Integer> {

}
