package com.autosales.service;

import com.autosales.dao.*;
import com.autosales.model.Sale;
import com.autosales.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
    import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserDao userDao;
    private final SaleDao saleDao;
    private final CarDao carDao;
    private final BrandDao brandDao;
    private final CarModelDao carModelDao;
    private final SaleDetailDao saleDetailDao;

    // ------ Управление пользователями ------
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        return userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    @Transactional
    public void updateUserRole(Integer userId, Integer newRoleId) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setRoleId(newRoleId);
        userDao.update(user);
    }

    @Transactional
    public void toggleUserEnabled(Integer userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setEnabled(!user.getEnabled());
        userDao.update(user);
    }

    // ------ Отчёты ------

    /**
     * 1. Информация о наличии автомобилей определённой марки и модели.
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCarsInStockByBrandAndModel(String brandName, String modelName) {
        // TODO: реализовать при необходимости
        return List.of();
    }

    /**
     * 2. Технические данные заданной модели.
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTechnicalDataByModel(String modelName) {
        // TODO: реализовать при необходимости
        return List.of();
    }

    /**
     * 3. Информация обо всех проданных моделях некоторой марки.
     */
    @Transactional(readOnly = true)
    public List<Sale> getSoldCarsByBrand(Integer brandId) {
        return saleDao.findSoldByBrand(brandId);
    }

    /**
     * 4. Сумма продаж моделей каждой марки и общая сумма.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesSummary() {
        BigDecimal total = saleDao.getTotalSalesAmount();
        List<Map<String, Object>> byBrand = brandDao.findAll().stream()
                .map(brand -> {
                    BigDecimal sum = saleDao.getTotalSalesAmountByBrand(brand.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("brandId", brand.getId());
                    map.put("brandName", brand.getName());
                    map.put("totalSales", sum);
                    return map;
                })
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("totalSales", total);
        result.put("byBrand", byBrand);
        return result;
    }

    /**
     * 5. Полная или частичная информация о клиентах.
     */
    @Transactional(readOnly = true)
    public List<User> getCustomers(String search) {
        return userDao.findAll().stream()
                .filter(u -> u.getRoleId() == 1) // ROLE_CUSTOMER
                .filter(u -> search == null || u.getEmail().contains(search) || u.getLastName().contains(search))
                .collect(Collectors.toList());
    }

    /**
     * Продажи для админ-панели с фильтрами.
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSalesWithFilters(BigDecimal minAmount,
                                                         BigDecimal maxAmount,
                                                         LocalDate startDate,
                                                         LocalDate endDate,
                                                         Integer brandId,
                                                         Integer modelId) {
        return saleDao.findSalesWithFilters(minAmount, maxAmount, startDate, endDate, brandId, modelId);
    }
}