package com.learn.service.dto.request;

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
public class ChangePasswordDTO {

    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, max = 20, message = "{password.size}")
    @ValidatePassword(message = "{password.invalid}")
    private String oldPassword;

    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, max = 20, message = "{password.size}")
    @ValidatePassword(message = "{password.invalid}")
    private String newPassword;

    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, max = 20, message = "{password.size}")
    @ValidatePassword(message = "{password.invalid}")
    private String confirmNewPassword;

}
