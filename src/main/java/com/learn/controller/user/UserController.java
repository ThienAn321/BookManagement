package com.learn.controller.user;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.service.AuthenticationService;
import com.learn.service.RefreshTokenService;
import com.learn.service.ResendVerifyService;
import com.learn.service.UserService;
import com.learn.service.dto.UserDTO;
import com.learn.service.dto.request.AuthenticationRequestDTO;
import com.learn.service.dto.request.ChangePasswordDTO;
import com.learn.service.dto.request.ChangePhoneRequestDTO;
import com.learn.service.dto.request.EmailRequestDTO;
import com.learn.service.dto.response.AuthenticationResponseDTO;
import com.learn.service.dto.response.MessageDTO;
import com.learn.service.dto.response.RefreshResponseDTO;
import com.learn.service.dto.response.RefreshTokenDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final ResendVerifyService resendVerifyService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO request) {
        logger.info("User {} try to login at : {}", request.getEmail(), Instant.now());
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageDTO> register(@RequestBody @Valid UserDTO request) {
        logger.info("Register use {} at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<MessageDTO> resendResetPassword(@RequestBody @Valid EmailRequestDTO request) {
        logger.info("User {} send verify to change email at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(userService.resetPassword(request), HttpStatus.OK);
    }

    @PutMapping("/resetpassword")
    public ResponseEntity<MessageDTO> resetPassword(@RequestParam("value") String value,
            @RequestBody @Valid ChangePasswordDTO request, @RequestHeader(value = "email") String email) {
        logger.info("User {} reset password at : {}", email, Instant.now());
        return new ResponseEntity<>(userService.verifyResetPassword(value, value, request, email), HttpStatus.OK);
    }

    // verify reset password

    @PutMapping("/changepassword")
    public ResponseEntity<MessageDTO> changePassword(@RequestBody @Valid ChangePasswordDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User {} change password at : {}", authentication.getPrincipal(), Instant.now());
        return new ResponseEntity<>(userService.changePassword(request, authentication), HttpStatus.OK);
    }

    @PostMapping("/sendChangeEmail")
    public ResponseEntity<MessageDTO> changeEmail(@RequestBody @Valid EmailRequestDTO request, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("newEmail", request.getEmail());
        logger.info("User {} send verify to change email at : {}", authentication.getPrincipal(), Instant.now());
        return new ResponseEntity<>(userService.changeEmail(request, authentication), HttpStatus.OK);
    }

    @PutMapping("/verifyChangeEmail")
    public ResponseEntity<MessageDTO> verifyChangeEmail(@RequestParam("value") String value, HttpSession session) {
        String newEmail = (String) session.getAttribute("newEmail");
        logger.info("User {} verify change email success at : {}", "", Instant.now());
        return new ResponseEntity<>(userService.verifyChangeEmail(value, value, newEmail), HttpStatus.OK);
    }

    @PostMapping("/sendChangePhone")
    public ResponseEntity<MessageDTO> changePhone(@RequestBody @Valid ChangePhoneRequestDTO request,
            HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("newPhone", request.getNewPhone());
        logger.info("User {} send verify to change phone at : {}", authentication.getPrincipal(), Instant.now());
        return new ResponseEntity<>(userService.changePhone(request, authentication), HttpStatus.OK);
    }

    @PutMapping("/verifyChangePhone")
    public ResponseEntity<MessageDTO> verifyChangePhone(@RequestParam("value") String value, HttpSession session) {
        String newPhone = (String) session.getAttribute("newPhone");
        logger.info("User {} verify change email success at : {}", "", Instant.now());
        return new ResponseEntity<>(userService.verifyChangePhone(value, value, newPhone), HttpStatus.OK);
    }

    @PostMapping("/resendConfirmAccount")
    public ResponseEntity<MessageDTO> resendConfirmAccount(@RequestBody @Valid EmailRequestDTO request) {
        logger.info("Resend verify user {} at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(resendVerifyService.resendVerifyAccount(request), HttpStatus.OK);
    }

    @PostMapping("/resendChangeEmail")
    public ResponseEntity<MessageDTO> resendChangeEmail(@RequestBody @Valid EmailRequestDTO request) {
        logger.info("Resend verify user {} at : {}", request.getEmail(), Instant.now());
        return new ResponseEntity<>(resendVerifyService.resendChangeEmail(request), HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<MessageDTO> confirmByLink(@RequestParam("value") String value) {
        // value ở đây có thể là link verify hoặc otp
        // verifyRegisterAccount check link verify hoac otp
        logger.info("Token {} verify at {}", value, Instant.now());
        return new ResponseEntity<>(userService.verifyLinkAndOtpConfirmAccount(value, value), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenDTO request) {
        logger.info("Refresh token is call at : {}", Instant.now());
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

}
