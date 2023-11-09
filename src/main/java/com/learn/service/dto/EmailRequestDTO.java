package com.learn.service.dto;

import com.learn.validation.ValidateEmail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailRequestDTO {

    @NotBlank(message = "{email.notblank}")
    @Size(max = 255, message = "{email.size}")
    @ValidateEmail(message = "{email.invalid}")
    private String email;

}
