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
public class RegisterDTO {

    @NotBlank(message = "{username.notblank}")
    @Size(min = 5, max = 255, message = "{username.size}")  
    private String username;
    
    @NotBlank(message = "{password.notblank}")
    @Size(min = 5, max = 255, message = "{password.size}")
    private String password;
    
    @NotBlank(message = "{fullname.notblank}")
    @Size(min = 5, max = 255, message = "{fullname.size}")
    private String fullname;
    
    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.notblank}")
    @Size(max = 255, message = "{email.size}")
    private String email;
    
    @NotBlank(message = "{phone.notblank}")
    @Size(max = 10, message = "{phone.size}")
//  @Pattern(regexp = "/^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$/", message = "Phải đúng định dạng số điện thoại")
    private String phone;
    
}
