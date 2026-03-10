package com.autosales.controller;

import com.autosales.model.User;
import com.autosales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        User user = userService.getUserByEmail(currentUser.getUsername());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails currentUser,
                                User updatedUser,
                                RedirectAttributes redirectAttributes) {
        User existing = userService.getUserByEmail(currentUser.getUsername());
        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setPatronymic(updatedUser.getPatronymic());
        existing.setPassportSeries(updatedUser.getPassportSeries());
        existing.setPassportNumber(updatedUser.getPassportNumber());
        existing.setAddress(updatedUser.getAddress());
        existing.setPhone(updatedUser.getPhone());

        userService.updateProfile(existing.getId(), existing);
        redirectAttributes.addFlashAttribute("success", "Данные профиля обновлены");
        return "redirect:/profile";
    }
}