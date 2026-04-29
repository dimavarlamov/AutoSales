package com.autosales.controller;

import com.autosales.dto.RegistrationDto;
import com.autosales.model.User;
import com.autosales.service.AuditLogService;
import com.autosales.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final AuditLogService auditLogService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new RegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegistrationDto dto,
                               BindingResult result,
                               HttpServletRequest request) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            User newUser = userService.register(dto);
            auditLogService.logAction("REGISTER", "users", newUser.getId(),
                    null, "Email: " + dto.getEmail(), request);
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "auth/register";
        }
        return "redirect:/auth/login?success";
    }
}