package com.autosales.controller;

import com.autosales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "unverified", required = false) String unverifiedEmail,
                        @RequestParam(value = "error", required = false) String error,
                        Model model) {
        if (unverifiedEmail != null) {
            model.addAttribute("unverifiedEmail", unverifiedEmail);
            model.addAttribute("error",
                    "Аккаунт не подтвержден. Пожалуйста, проверьте почту или запросите новое письмо с подтверждением.");
        } else if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }
        return "auth/login";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return "redirect:/auth/login?verified=true";
    }

    @PostMapping("/resend-verification")
    public String resendVerification(@RequestParam String email) {
        userService.resendVerificationEmail(email);
        return "redirect:/auth/login?resent=true";
    }
}