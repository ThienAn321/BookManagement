package com.learn.service.dto;

import com.learn.validation.ValidateEmail;
import com.learn.validation.ValidatePassword;
import com.learn.validation.ValidatePhone;

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
public class RegisterDTO {

    @NotBlank(message = "{username.notblank}")
    @Size(min = 5, max = 255, message = "{username.size}")
    private String username;

    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, max = 20, message = "{password.size}")
    @ValidatePassword(message = "{password.invalid}")
    private String password;

    @NotBlank(message = "{fullname.notblank}")
    @Size(min = 5, max = 255, message = "{fullname.size}")
    private String fullname;

    @NotBlank(message = "{email.notblank}")
    @Size(max = 255, message = "{email.size}")
    @ValidateEmail(message = "{email.invalid}")
    private String email;

    @NotBlank(message = "{phone.notblank}")
    @Size(max = 12, message = "{phone.size}")
    @ValidatePhone(message = "{phone.validate}")
    private String phone;

}
