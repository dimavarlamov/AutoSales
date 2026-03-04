package com.autosales.controller;

import com.autosales.dao.BrandDao;
import com.autosales.dao.CarDao;
import com.autosales.model.Brand;
import com.autosales.model.Car;
import com.autosales.model.Favorite;
import com.autosales.model.User;
import com.autosales.service.FavoriteService;
import com.autosales.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CatalogController {

    private final CarDao carDao;
    private final BrandDao brandDao;
    private final UserService userService;
    private final FavoriteService favoriteService;

    @Autowired
    public CatalogController(CarDao carDao, BrandDao brandDao,
                             UserService userService, FavoriteService favoriteService) {
        this.carDao = carDao;
        this.brandDao = brandDao;
        this.userService = userService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal UserDetails currentUser,
            Model model) {

        int size = 9;
        List<Car> cars = carDao.findWithFilters(search, brandId, minPrice, maxPrice, page, size);
        int total = carDao.countWithFilters(search, brandId, minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) total / size);
        List<Brand> brands = brandDao.findAll();

        // Безопасное построение favoriteStatus
        Map<Integer, Boolean> favoriteStatus = new HashMap<>();
        if (currentUser != null) {
            User user = userService.getUserByEmail(currentUser.getUsername());
            List<Favorite> favorites = favoriteService.getFavoritesByUserId(user.getId());
            Set<Integer> favoriteCarIds = favorites.stream()
                    .map(Favorite::getCarId)
                    .collect(Collectors.toSet());
            for (Car car : cars) {
                favoriteStatus.put(car.getId(), favoriteCarIds.contains(car.getId()));
            }
        } else {
            // для неавторизованных все false
            for (Car car : cars) {
                favoriteStatus.put(car.getId(), false);
            }
        }

        model.addAttribute("cars", cars);
        model.addAttribute("brands", brands);
        model.addAttribute("favoriteStatus", favoriteStatus);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "catalog";
    }
}