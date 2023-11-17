package com.learn.service.convert;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learn.model.Book;
import com.learn.service.dto.BookDTO;

@Component
public class BookDTOConverter {
    
    @Autowired
    private ModelMapper modelMapper;

    public BookDTO convertBookToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setStock(book.getStock());
        bookDTO.setRating(book.getRating());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setReleaseDate(book.getReleaseDate());
        bookDTO.setAuthor(book.getAuthor().getFullname());
        bookDTO.setCategory(book.getCategory().getName());

        return bookDTO;
    }
    
    public Book convertBookDTOToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }
    
}
