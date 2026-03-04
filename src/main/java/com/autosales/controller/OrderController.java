package com.autosales.controller;

import com.autosales.dao.SaleDao;
import com.autosales.dao.SaleDetailDao;
import com.autosales.model.Sale;
import com.autosales.model.User;
import com.autosales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile/orders")
@RequiredArgsConstructor
public class OrderController {

    private final SaleDao saleDao;
    private final SaleDetailDao saleDetailDao;
    private final UserService userService;

    @GetMapping
    public String orders(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        User user = userService.getUserByEmail(currentUser.getUsername());
        List<Sale> sales = saleDao.findByUserId(user.getId());
        model.addAttribute("sales", sales);
        return "orders";
    }
}