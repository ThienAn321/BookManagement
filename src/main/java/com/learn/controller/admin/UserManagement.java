package com.learn.controller.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.UserQueryService;
import com.learn.service.criteria.UserCriteria;
import com.learn.service.dto.UserDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class UserManagement {

    private final UserQueryService userQueryService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(UserCriteria criteria, Pageable pageable) {
        List<UserDTO> result = userQueryService.findByCriteria(criteria, pageable);
        return ResponseEntity.ok().body(result);
    }

}
