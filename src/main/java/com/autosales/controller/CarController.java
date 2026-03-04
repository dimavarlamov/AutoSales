package com.autosales.controller;

import com.autosales.dao.CarSpecificationDao;
import com.autosales.model.Car;
import com.autosales.model.CarImage;
import com.autosales.model.CarModel;
import com.autosales.model.CarSpecification;
import com.autosales.service.CarService;
import com.autosales.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarSpecificationDao carSpecificationDao;
    private final ModelService modelService;

    @GetMapping("/cars/{id}")
    public String carDetails(@PathVariable Integer id, Model model) {
        Car car = carService.getCarById(id);
        CarModel carModel = modelService.getModelById(car.getModelId());
        CarSpecification spec = carSpecificationDao.findByCarId(id).orElse(null);
        List<CarImage> images = carService.getCarImages(id); // список дополнительных фото

        model.addAttribute("car", car);
        model.addAttribute("model", carModel);
        model.addAttribute("spec", spec);
        model.addAttribute("images", images);
        return "car-details";
    }
}