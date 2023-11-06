package com.learn.service.dto;

import jakarta.validation.constraints.Email;
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
public class RefreshRequestDTO {

    @Email(message = "Phải đúng dạng email")
    @NotBlank(message = "Không để trống Email")
    @Size(max = 255, message = "Email tối đa 255 ký tự")
    private String email;

}
