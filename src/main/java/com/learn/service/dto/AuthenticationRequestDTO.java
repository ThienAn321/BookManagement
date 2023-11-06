package com.learn.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {
    
    @Email(message = "Phải đúng dạng email")
    @NotBlank(message = "Không để trống Email")
    @Size(max = 255, message = "Email tối đa 255 ký tự")
    private String email;
    
    @NotBlank(message = "Không để trống password")
    @Size(max = 255, message = "Password tối đa 255 ký tự")
    private String password;

}
