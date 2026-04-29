package com.autosales.controller;

import com.autosales.model.User;
import com.autosales.service.AuditLogService;
import com.autosales.service.SaleService;
import com.autosales.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final SaleService saleService;
    private final UserService userService;
    private final AuditLogService auditLogService;

    @PostMapping("/buy/{carId}")
    public String buyCar(@PathVariable Integer carId,
                         @AuthenticationPrincipal UserDetails currentUser,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        try {
            User user = userService.getUserByEmail(currentUser.getUsername());
            saleService.purchaseCar(user.getId(), carId);
            auditLogService.logAction("PURCHASE", "cars", carId,
                    null, "Покупка пользователем " + user.getEmail(), request);
            redirectAttributes.addFlashAttribute("success", "Поздравляем с покупкой! Автомобиль ждёт вас в автосалоне.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Произошла ошибка: " + e.getMessage());
        }
        return "redirect:/cars/" + carId;
    }
}