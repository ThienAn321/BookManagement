package com.learn.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.model.User;
import com.learn.model.User_;
import com.learn.repository.UserRepository;
import com.learn.service.convert.UserDTOConverter;
import com.learn.service.criteria.UserCriteria;
import com.learn.service.dto.UserDTO;

import lombok.RequiredArgsConstructor;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService extends QueryService<User> {

    private final Logger logger = LoggerFactory.getLogger(UserQueryService.class);

    private final UserRepository userRepository;

    private final UserDTOConverter userDTOConverter;

    @Transactional(readOnly = true)
    public List<UserDTO> findByCriteria(UserCriteria criteria, Pageable pageable) {
        logger.debug("find by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        Page<User> book = userRepository.findAll(specification, pageable);
        return book.stream().map(userDTOConverter::convertUserToUserDTO).toList();
    }

    protected Specification<User> createSpecification(UserCriteria criteria) {
        Specification<User> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), User_.id));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), User_.username));
            }
            if (criteria.getFullname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullname(), User_.fullname));
            }
            if (criteria.getDateBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateBirth(), User_.dateBirth));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), User_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), User_.phone));
            }
            if (criteria.getFailedAttempt() != null) {
                specification = specification
                        .and(buildRangeSpecification(criteria.getFailedAttempt(), User_.failedAttempt));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), User_.isDeleted));
            }
            if (criteria.getLockTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLockTime(), User_.lockTime));
            }
        }
        return specification;
    }

}
