package com.autosales.controller;

import com.autosales.model.Car;
import com.autosales.model.CarImage;
import com.autosales.service.CarService;
import com.autosales.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/cars/{carId}/photos")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CarPhotoController {

    private final CarService carService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String managePhotos(@PathVariable Integer carId, Model model) {
        Car car = carService.getCarById(carId);
        List<CarImage> images = carService.getCarImages(carId);
        model.addAttribute("car", car);
        model.addAttribute("images", images);
        return "admin/cars/photos";
    }

    @PostMapping
    public String uploadPhotos(@PathVariable Integer carId,
                               @RequestParam("photos") MultipartFile[] files,
                               RedirectAttributes redirectAttributes) {
        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String imagePath = fileStorageService.storeFile(file);
                    carService.addCarImage(carId, imagePath);
                }
            }
            redirectAttributes.addFlashAttribute("success", "Фотографии добавлены");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке: " + e.getMessage());
        }
        // Добавляем параметр timestamp, чтобы браузер не кешировал страницу
        return "redirect:/admin/cars/{carId}/photos?t=" + System.currentTimeMillis();
    }

    @PostMapping("/delete/{imageId}")
    public String deletePhoto(@PathVariable Integer carId,
                              @PathVariable Integer imageId,
                              RedirectAttributes redirectAttributes) {
        carService.deleteCarImage(imageId);
        redirectAttributes.addFlashAttribute("success", "Фото удалено");
        return "redirect:/admin/cars/{carId}/photos";
    }

    @PostMapping("/set-main/{imageId}")
    public String setMainPhoto(@PathVariable Integer carId,
                               @PathVariable Integer imageId,
                               RedirectAttributes redirectAttributes) {
        // Получаем путь к фото
        CarImage image = carService.getCarImages(carId).stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Фото не найдено"));

        Car car = carService.getCarById(carId);
        car.setImage(image.getImagePath());
        carService.updateCar(car);

        redirectAttributes.addFlashAttribute("success", "Основное фото изменено");
        return "redirect:/admin/cars/{carId}/photos";
    }
}