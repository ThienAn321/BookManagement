package com.learn.service.dto.request;

import com.learn.validation.ValidateEmail;
import com.learn.validation.ValidatePassword;

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
    
    @NotBlank(message = "{email.notblank}")
    @Size(max = 255, message = "{email.size}")
    @ValidateEmail(message = "{email.invalid}")
    private String email;
    
    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, max = 20, message = "{password.size}")
    @ValidatePassword(message = "{password.invalid}")
    private String password;

}
