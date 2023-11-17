package com.learn.service.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Integer id;

    @NotBlank(message = "{title.notblank}")
    @Size(min = 5, max = 255, message = "{title.size}")
    private String title;

    @NotNull(message = "{stock.notblank}")
    private Integer stock;

    private Integer rating;
    private String description;
    private Instant releaseDate;

    @NotBlank(message = "{author.notblank}")
    private String author;

    @NotBlank(message = "{cateogory.notblank}")
    private String category;

}
