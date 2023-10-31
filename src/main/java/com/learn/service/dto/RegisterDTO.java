package com.learn.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "Không để trống username")
    @Size(max = 255, message = "Username tối đa 255 ký tự")
    private String username;
    
    @NotBlank(message = "Không để trống password")
    @Size(max = 255, message = "Password tối đa 255 ký tự")
    private String password;
    
    @NotBlank(message = "Không để trống tên")
    @Size(max = 255, message = "Tên tối đa 255 ký tự")
    private String fullname;
    
    @Email(message = "Phải đúng định dạng email")
    @NotBlank(message = "Không để trống Email")
    @Size(max = 255, message = "Email tối đa 255 ký tự")
    private String email;
    
    @NotBlank(message = "Không để trống số điện thoại")
    @Size(max = 10, message = "Số điện thoại tối đa 10 ký tự")
//  @Pattern(regexp = "/^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$/", message = "Phải đúng định dạng số điện thoại")
    private String phone;
    
}
