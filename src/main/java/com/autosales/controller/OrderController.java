package com.autosales.controller;

import com.autosales.dao.CarDao;
import com.autosales.dao.SaleDao;
import com.autosales.dao.SaleDetailDao;
import com.autosales.model.Car;
import com.autosales.model.Sale;
import com.autosales.model.SaleDetail;
import com.autosales.model.User;
import com.autosales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile/orders")
@RequiredArgsConstructor
public class OrderController {

    private final SaleDao saleDao;
    private final SaleDetailDao saleDetailDao;
    private final CarDao carDao;
    private final UserService userService;

    @GetMapping
    public String orders(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        User user = userService.getUserByEmail(currentUser.getUsername());
        List<Sale> sales = saleDao.findByUserId(user.getId());
        model.addAttribute("sales", sales);
        return "orders";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Integer id,
                               @AuthenticationPrincipal UserDetails currentUser,
                               Model model) {
        Sale sale = saleDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        User user = userService.getUserByEmail(currentUser.getUsername());

        if (!sale.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("У вас нет прав для просмотра этого заказа");
        }

        List<SaleDetail> details = saleDetailDao.findBySaleId(id);

        List<SaleDetailWithCar> detailWithCars = details.stream()
                .map(d -> {
                    Car car = carDao.findById(d.getCarId())
                            .orElseThrow(() -> new IllegalArgumentException("Автомобиль не найден"));
                    return new SaleDetailWithCar(d, car);
                })
                .collect(Collectors.toList());

        model.addAttribute("sale", sale);
        model.addAttribute("details", detailWithCars);

        return "order-details";
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    public static class SaleDetailWithCar {
        private SaleDetail detail;
        private Car car;
    }
}