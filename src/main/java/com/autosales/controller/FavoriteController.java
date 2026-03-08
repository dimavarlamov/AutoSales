package com.autosales.controller;

import com.autosales.model.Car;
import com.autosales.model.User;
import com.autosales.service.FavoriteService;
import com.autosales.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    @GetMapping
    public String favorites(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        User user = userService.getUserByEmail(currentUser.getUsername());
        List<Car> favoriteCars = favoriteService.getUserFavorites(user.getId());
        model.addAttribute("favoriteCars", favoriteCars);
        return "favorites";
    }

    @PostMapping("/toggle")
    public String toggleFavorite(@AuthenticationPrincipal UserDetails currentUser,
                                 @RequestParam Integer carId,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        User user = userService.getUserByEmail(currentUser.getUsername());
        favoriteService.toggleFavorite(user.getId(), carId);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/catalog");
    }
}