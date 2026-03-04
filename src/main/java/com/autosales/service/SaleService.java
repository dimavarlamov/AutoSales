package com.autosales.service;

import com.autosales.dao.CarDao;
import com.autosales.dao.SaleDao;
import com.autosales.dao.SaleDetailDao;
import com.autosales.dao.UserDao;
import com.autosales.model.Car;
import com.autosales.model.Sale;
import com.autosales.model.SaleDetail;
import com.autosales.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {

    private final SaleDao saleDao;
    private final SaleDetailDao saleDetailDao;
    private final UserDao userDao;
    private final CarDao carDao;

    @Transactional
    public Sale purchaseCar(Integer userId, Integer carId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Car car = carDao.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Автомобиль не найден"));

        if (car.getStockQuantity() < 1) {
            throw new IllegalStateException("Автомобиль отсутствует на складе");
        }

        BigDecimal total = car.getPrice();

        if (user.getBalance().compareTo(total) < 0) {
            throw new IllegalStateException("Недостаточно средств на балансе");
        }

        // Создаём продажу
        Sale sale = new Sale();
        sale.setUserId(userId);
        sale.setSaleDate(LocalDateTime.now());
        sale.setTotalAmount(total);
        Sale savedSale = saleDao.save(sale);

        // Детали продажи
        SaleDetail detail = new SaleDetail();
        detail.setSaleId(savedSale.getId());
        detail.setCarId(carId);
        detail.setQuantity(1);
        detail.setPriceAtSale(car.getPrice());
        saleDetailDao.save(detail);

        // Уменьшаем остаток и баланс
        car.setStockQuantity(car.getStockQuantity() - 1);
        carDao.update(car);

        user.setBalance(user.getBalance().subtract(total));
        userDao.update(user);

        return savedSale;
    }
}