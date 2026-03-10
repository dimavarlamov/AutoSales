package com.autosales.controller;

import com.autosales.dao.BrandDao;
import com.autosales.dao.CarDao;
import com.autosales.dao.CarModelDao;
import com.autosales.dao.CarSpecificationDao;
import com.autosales.model.Brand;
import com.autosales.model.Car;
import com.autosales.model.CarModel;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CatalogController {

    private final CarDao carDao;
    private final BrandDao brandDao;
    private final CarModelDao carModelDao;
    private final CarSpecificationDao carSpecificationDao;
    private final UserService userService;
    private final FavoriteService favoriteService;

    @Autowired
    public CatalogController(CarDao carDao,
                             BrandDao brandDao,
                             CarModelDao carModelDao,
                             CarSpecificationDao carSpecificationDao,
                             UserService userService,
                             FavoriteService favoriteService) {
        this.carDao = carDao;
        this.brandDao = brandDao;
        this.carModelDao = carModelDao;
        this.carSpecificationDao = carSpecificationDao;
        this.userService = userService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer modelId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String steeringSide,
            @RequestParam(required = false) String engineType,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Integer mileageFrom,
            @RequestParam(required = false) Integer mileageTo,
            @RequestParam(required = false) Integer hpFrom,
            @RequestParam(required = false) Integer hpTo,
            @RequestParam(required = false) String bodyType,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Boolean airConditioning,
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal UserDetails currentUser,
            Model model) {

        int size = 9;
        List<Car> cars = carDao.findWithAdvancedFilters(
                search,
                brandId,
                modelId,
                minPrice,
                maxPrice,
                country,
                steeringSide,
                engineType,
                yearFrom,
                yearTo,
                mileageFrom,
                mileageTo,
                hpFrom,
                hpTo,
                bodyType,
                color,
                airConditioning,
                page,
                size
        );
        int total = carDao.countWithAdvancedFilters(
                search,
                brandId,
                modelId,
                minPrice,
                maxPrice,
                country,
                steeringSide,
                engineType,
                yearFrom,
                yearTo,
                mileageFrom,
                mileageTo,
                hpFrom,
                hpTo,
                bodyType,
                color,
                airConditioning
        );
        int totalPages = (int) Math.ceil((double) total / size);
        List<Brand> brands = brandDao.findAll();
        List<CarModel> models = carModelDao.findAll();
        List<String> engineTypes = carModelDao.findDistinctEngineTypes();
        List<String> bodyTypes = carModelDao.findDistinctBodyTypes();
        List<String> colors = carSpecificationDao.findDistinctColors();

        // список стран для фильтрации по стране производителя
        List<String> countries = brands.stream()
                .map(Brand::getCountry)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

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
        model.addAttribute("models", models);
        model.addAttribute("countries", countries);
        List<Map<String, String>> engineTypeOptions = new ArrayList<>();
        Map<String, String> translation = new HashMap<>();
        translation.put("electric", "Электро");
        translation.put("hybrid", "Гибрид");
        translation.put("petrol", "Бензин");
        translation.put("diesel", "Дизель");

        for (String et : engineTypes) {
            Map<String, String> option = new HashMap<>();
            option.put("code", et);
            option.put("name", translation.getOrDefault(et, et)); // если вдруг появится другое значение
            engineTypeOptions.add(option);
        }
        model.addAttribute("engineTypeOptions", engineTypeOptions);
        model.addAttribute("bodyTypes", bodyTypes);
        model.addAttribute("colors", colors);
        model.addAttribute("favoriteStatus", favoriteStatus);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("selectedModelId", modelId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("selectedCountry", country);
        model.addAttribute("selectedSteeringSide", steeringSide);
        model.addAttribute("selectedEngineType", engineType);
        model.addAttribute("yearFrom", yearFrom);
        model.addAttribute("yearTo", yearTo);
        model.addAttribute("mileageFrom", mileageFrom);
        model.addAttribute("mileageTo", mileageTo);
        model.addAttribute("hpFrom", hpFrom);
        model.addAttribute("hpTo", hpTo);
        model.addAttribute("selectedBodyType", bodyType);
        model.addAttribute("selectedColor", color);
        model.addAttribute("selectedAirConditioning", airConditioning);

        return "catalog";
    }
}