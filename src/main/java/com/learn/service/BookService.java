package com.learn.service;

import org.springframework.web.multipart.MultipartFile;

import com.learn.service.dto.BookDTO;
import com.learn.service.dto.response.MessageDTO;

public interface BookService {

    MessageDTO create(BookDTO request);

    MessageDTO update(BookDTO request);

    MessageDTO importBooks(MultipartFile file);

}
