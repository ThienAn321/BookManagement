package com.learn.controller.admin;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learn.repository.BookRepository;
import com.learn.service.BookQueryService;
import com.learn.service.BookService;
import com.learn.service.criteria.BookCriteria;
import com.learn.service.dto.BookDTO;
import com.learn.service.dto.response.MessageDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class BookManagement {

    private final Logger logger = LoggerFactory.getLogger(BookManagement.class);

    private final BookService bookService;

    private final BookQueryService bookQueryService;

    private final BookRepository bookRepository;

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> findByCriteria(BookCriteria criteria, Pageable pageable) {
        List<BookDTO> result = bookQueryService.findByCriteria(criteria, pageable);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/books")
    public ResponseEntity<MessageDTO> findByCriteria(@RequestBody @Valid BookDTO request) {
        logger.info("Book id {} is created at : {}", request.getId(), Instant.now());
        return new ResponseEntity<>(bookService.create(request), HttpStatus.CREATED);
    }

    @PostMapping("/books/import")
    public ResponseEntity<MessageDTO> importBook(@RequestPart("file") MultipartFile file) {
        logger.info("Import file book successfully at {}", Instant.now());
        return new ResponseEntity<>(bookService.importBooks(file), HttpStatus.CREATED);
    }

    @PutMapping("/books")
    public ResponseEntity<MessageDTO> updateBook(@RequestBody @Valid BookDTO request) {
        logger.info("Book id {} is created at : {}", request.getId(), Instant.now());
        return new ResponseEntity<>(bookService.update(request), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/books/{id}")
    @Transactional
    public ResponseEntity<String> deleteBook(@PathVariable("id") Integer id) {
        bookRepository.deleteBook(id); // update delete flag to true
        return ResponseEntity.accepted().body("Book was deleted successfully");
    }

}
