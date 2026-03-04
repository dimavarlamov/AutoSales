package com.autosales.service;

import com.autosales.dao.CarDao;
import com.autosales.dao.CarImageDao;
import com.autosales.dao.CarModelDao;
import com.autosales.dao.CarSpecificationDao;
import com.autosales.model.Car;
import com.autosales.model.CarImage;
import com.autosales.model.CarModel;
import com.autosales.model.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarDao carDao;
    private final CarModelDao carModelDao;
    private final CarSpecificationDao carSpecificationDao;
    private final CarImageDao carImageDao;

    @Transactional(readOnly = true)
    public List<Car> getAllCars() {
        return carDao.findAll();
    }

    @Transactional(readOnly = true)
    public Car getCarById(Integer id) {
        return carDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Автомобиль не найден"));
    }

    @Transactional(readOnly = true)
    public List<Car> searchCars(String query, Integer brandId, Integer modelId,
                                BigDecimal minPrice, BigDecimal maxPrice, Boolean inStockOnly) {
        // Здесь можно реализовать сложный поиск через DAO
        // Для простоты пока заглушка
        return carDao.findAll();
    }

    @Transactional
    public Car createCar(Car car, CarSpecification spec) {
        Car saved = carDao.save(car);
        spec.setCarId(saved.getId());
        carSpecificationDao.save(spec);
        return saved;
    }

    @Transactional
    public Car updateCar(Integer id, Car car, CarSpecification spec) {
        Car existing = getCarById(id);
        existing.setModelId(car.getModelId());
        existing.setPrice(car.getPrice());
        existing.setStockQuantity(car.getStockQuantity());
        existing.setExpectedDate(car.getExpectedDate());
        existing.setDescription(car.getDescription());
        existing.setImage(car.getImage());
        carDao.update(existing);

        CarSpecification existingSpec = carSpecificationDao.findByCarId(id).orElse(new CarSpecification());
        existingSpec.setCarId(id);
        existingSpec.setEngineVolume(spec.getEngineVolume());
        existingSpec.setHorsepower(spec.getHorsepower());
        existingSpec.setColor(spec.getColor());
        existingSpec.setAirConditioning(spec.getAirConditioning());

        if (carSpecificationDao.findByCarId(id).isPresent()) {
            carSpecificationDao.update(existingSpec);
        } else {
            carSpecificationDao.save(existingSpec);
        }
        return existing;
    }

    @Transactional
    public void deleteCar(Integer id) {
        // проверка, не используется ли в активных продажах
        carSpecificationDao.delete(id);
        carDao.delete(id);
    }

    @Transactional
    public void updateStock(Integer id, int newStock) {
        carDao.updateStock(id, newStock);
    }

    @Transactional(readOnly = true)
    public List<CarModel> getAllModels() {
        return carModelDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<CarModel> getModelsByBrand(Integer brandId) {
        return carModelDao.findByBrandId(brandId);
    }

    public List<Car> findCarsWithFilters(String search, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        return carDao.findWithFilters(search, brandId, minPrice, maxPrice, page, size);
    }

    public int countCarsWithFilters(String search, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice) {
        return carDao.countWithFilters(search, brandId, minPrice, maxPrice);
    }

    public Car saveCar(Car car) {
        return carDao.save(car);
    }

    public void updateCar(Car car) {
        carDao.update(car);
    }

    public List<CarImage> getCarImages(Integer carId) {
        return carImageDao.findByCarId(carId);
    }

    public void addCarImage(Integer carId, String imagePath) {
        CarImage image = new CarImage();
        image.setCarId(carId);
        image.setImagePath(imagePath);
        carImageDao.save(image);
    }

    public void deleteCarImage(Integer imageId) {
        carImageDao.delete(imageId);
    }
}