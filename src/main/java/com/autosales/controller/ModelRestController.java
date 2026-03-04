package com.autosales.controller;

import com.autosales.model.CarModel;
import com.autosales.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class ModelRestController {

    private final ModelService modelService;

    @GetMapping("/byBrand/{brandId}")
    public List<CarModel> getModelsByBrand(@PathVariable Integer brandId) {
        return modelService.getModelsByBrand(brandId);
    }
}