package com.learn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.learn.exception.DataNotFoundException;
import com.learn.model.Author;
import com.learn.model.Book;
import com.learn.model.Category;
import com.learn.repository.AuthorRepository;
import com.learn.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileImportUtil {

    private final String[] HEADERS = { "title", "stock", "rating", "description", "releaseDate", "author", "category" };

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final MessageSource message;

    public boolean hasCSVFormat(MultipartFile file) {
        String type = "text/csv";
        return type.equals(file.getContentType());
    }

    public List<Book> csvToBooks(InputStream inputStream) {
        List<Book> listBook = new ArrayList<>();
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.builder().setHeader(HEADERS).setSkipHeaderRecord(true).setTrim(true).build());
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Author author = authorRepository.findByFullname(csvRecord.get("author"))
                        .orElseThrow(() -> new DataNotFoundException("author",
                                message.getMessage("author.invalid", null, Locale.getDefault()),
                                "author.error.notfound"));
                Category category = categoryRepository.findByName(csvRecord.get("category"))
                        .orElseThrow(() -> new DataNotFoundException("category",
                                message.getMessage("category.invalid", null, Locale.getDefault()),
                                "category.error.notfound"));
                Book book = Book.builder().title(csvRecord.get("title")).isDeleted(false)
                        .stock(Integer.parseInt(csvRecord.get("stock"))).description(csvRecord.get("description"))
                        .rating(Integer.parseInt(csvRecord.get("rating")))
                        .releaseDate(Instant.parse(csvRecord.get("releaseDate"))).author(author).category(category)
                        .build();

                listBook.add(book);
            }
            csvParser.close();

        } catch (IOException ex) {

        }

        return listBook;
    }

}
