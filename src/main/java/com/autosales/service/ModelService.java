package com.autosales.service;

import com.autosales.dao.CarModelDao;
import com.autosales.model.CarModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final CarModelDao carModelDao;

    @Transactional(readOnly = true)
    public List<CarModel> getAllModels() {
        return carModelDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<CarModel> getModelsByBrand(Integer brandId) {
        return carModelDao.findByBrandId(brandId);
    }

    @Transactional(readOnly = true)
    public CarModel getModelById(Integer id) {
        return carModelDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Модель не найдена"));
    }

    @Transactional
    public CarModel saveModel(CarModel model) {
        return carModelDao.save(model);
    }

    @Transactional
    public void updateModel(CarModel model) {
        carModelDao.update(model);
    }

    @Transactional
    public void deleteModel(Integer id) {
        carModelDao.delete(id);
    }
}