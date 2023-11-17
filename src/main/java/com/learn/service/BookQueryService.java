package com.learn.service;

import java.util.List;

import jakarta.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.model.Author_;
import com.learn.model.Book;
import com.learn.model.Book_;
import com.learn.model.BorrowManagement_;
import com.learn.model.Category_;
import com.learn.repository.BookRepository;
import com.learn.service.convert.BookDTOConverter;
import com.learn.service.criteria.BookCriteria;
import com.learn.service.dto.BookDTO;

import lombok.RequiredArgsConstructor;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookQueryService extends QueryService<Book> {

    private final Logger logger = LoggerFactory.getLogger(BookQueryService.class);

    private final BookRepository bookRepository;

    private final BookDTOConverter bookDTOConverter;

    @Transactional(readOnly = true)
    public List<BookDTO> findByCriteria(BookCriteria criteria, Pageable pageable) {
        logger.debug("find by criteria : {}", criteria);
        final Specification<Book> specification = createSpecification(criteria);
        Page<Book> book = bookRepository.findAllCustom(specification, pageable);
        return book.stream().map(bookDTOConverter::convertBookToBookDTO).toList();
    }

    protected Specification<Book> createSpecification(BookCriteria criteria) {
        Specification<Book> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Book_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Book_.title));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), Book_.stock));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Book_.rating));
            }
            if (criteria.getDescription() != null) {
                specification = specification
                        .and(buildStringSpecification(criteria.getDescription(), Book_.description));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification
                        .and(buildRangeSpecification(criteria.getReleaseDate(), Book_.releaseDate));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Book_.title));
            }
            if (criteria.getBorrowManagementId() != null) {
                specification = specification.and(buildSpecification(criteria.getBorrowManagementId(),
                        root -> root.join(Book_.borrowManagement, JoinType.LEFT).get(BorrowManagement_.id)));
            }
            if (criteria.getAuthorId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthorId(),
                        root -> root.join(Book_.author, JoinType.LEFT).get(Author_.id)));
            }
            if (criteria.getAuthorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                        root -> root.join(Book_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
