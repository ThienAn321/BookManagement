package com.learn.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learn.exception.DataInvalidException;
import com.learn.exception.DataNotFoundException;
import com.learn.model.Author;
import com.learn.model.Book;
import com.learn.model.Category;
import com.learn.repository.AuthorRepository;
import com.learn.repository.BookRepository;
import com.learn.repository.CategoryRepository;
import com.learn.service.BookService;
import com.learn.service.dto.BookDTO;
import com.learn.service.dto.response.MessageDTO;
import com.learn.util.FileImportUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final FileImportUtil fileImportUlti;

//    private final BookDTOConverter bookDTOConverter;

    private final MessageSource message;

    @Override
    public MessageDTO create(BookDTO request) {
        Author author = authorRepository.findByFullname(request.getAuthor())
                .orElseThrow(() -> new DataNotFoundException("author",
                        message.getMessage("author.invalid", null, Locale.getDefault()), "author.error.notfound"));
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new DataNotFoundException("category",
                        message.getMessage("category.invalid", null, Locale.getDefault()), "category.error.notfound"));

        Book book = Book.builder().title(request.getTitle()).isDeleted(false).stock(request.getStock())
                .rating(request.getRating()).description(request.getDescription()).releaseDate(request.getReleaseDate())
                .author(author).category(category).build();
        bookRepository.save(book);
        return MessageDTO.builder().status(HttpStatus.CREATED.value())
                .message(message.getMessage("create.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    @Override
    public MessageDTO update(BookDTO request) {
        Book book = bookRepository.findById(request.getId()).orElseThrow(() -> new DataNotFoundException("id",
                message.getMessage("book.notfound", null, Locale.getDefault()), "id.error.notfound"));
        Author author = authorRepository.findByFullname(request.getAuthor())
                .orElseThrow(() -> new DataNotFoundException("author",
                        message.getMessage("author.invalid", null, Locale.getDefault()), "author.error.notfound"));
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new DataNotFoundException("category",
                        message.getMessage("category.invalid", null, Locale.getDefault()), "category.error.notfound"));

        book.setTitle(request.getTitle());
        book.setStock(request.getStock());
        book.setRating(request.getRating());
        book.setDescription(request.getDescription());
        book.setReleaseDate(request.getReleaseDate());
        book.setAuthor(author);
        book.setCategory(category);
        bookRepository.save(book);

        return MessageDTO.builder().status(HttpStatus.ACCEPTED.value())
                .message(message.getMessage("update.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

    @Override
    public MessageDTO importBooks(MultipartFile file) {
        if (!fileImportUlti.hasCSVFormat(file)) {
            throw new DataInvalidException("file", message.getMessage("input.invalid", null, Locale.getDefault()),
                    "file.error.invalid");
        }
        try {
            List<Book> books = fileImportUlti.csvToBooks(file.getInputStream());
            bookRepository.saveAll(books);
        } catch (IOException e) {
//            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
        return MessageDTO.builder().status(HttpStatus.CREATED.value())
                .message(message.getMessage("create.success", null, Locale.getDefault())).timestamp(Instant.now())
                .build();
    }

}
