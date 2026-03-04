package com.autosales.controller;

import com.autosales.dao.CarSpecificationDao;
import com.autosales.model.Car;
import com.autosales.model.CarSpecification;
import com.autosales.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cars")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CarSpecificationController {

    private final CarService carService;
    private final CarSpecificationDao carSpecificationDao;

    @GetMapping("/{id}/spec")
    public String editSpec(@PathVariable Integer id, Model model) {
        Car car = carService.getCarById(id);
        CarSpecification spec = carSpecificationDao.findByCarId(id).orElse(new CarSpecification());
        model.addAttribute("car", car);
        model.addAttribute("spec", spec);
        return "admin/cars/spec-form";
    }

    @PostMapping("/{id}/spec")
    public String saveSpec(@PathVariable Integer id,
                           @ModelAttribute CarSpecification spec,
                           RedirectAttributes redirectAttributes) {
        spec.setCarId(id);
        if (carSpecificationDao.findByCarId(id).isPresent()) {
            carSpecificationDao.update(spec);
        } else {
            carSpecificationDao.save(spec);
        }
        redirectAttributes.addFlashAttribute("success", "Характеристики сохранены");
        return "redirect:/admin/cars/edit/" + id;
    }
}