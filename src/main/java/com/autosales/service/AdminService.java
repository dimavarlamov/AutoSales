package com.autosales.service;

import com.autosales.dao.*;
import com.autosales.model.Sale;
import com.autosales.model.User;
import com.autosales.model.Car;
import com.autosales.model.SaleDetail;
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

    @Transactional(readOnly = true)
    public boolean isCarLinkedToSale(Integer carId) {
        return !saleDetailDao.findByCarId(carId).isEmpty();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCarsInStockByBrandAndModel(String brandName, String modelName) {
        // TODO: реализовать при необходимости
        return List.of();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTechnicalDataByModel(String modelName) {
        // TODO: реализовать при необходимости
        return List.of();
    }

    @Transactional(readOnly = true)
    public List<Sale> getSoldCarsByBrand(Integer brandId) {
        return saleDao.findSoldByBrand(brandId);
    }

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

    @Transactional(readOnly = true)
    public List<User> getCustomers(String search) {
        return userDao.findAll().stream()
                .filter(u -> u.getRoleId() == 1)
                .filter(u -> search == null || u.getEmail().contains(search) || u.getLastName().contains(search))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSalesWithFilters(BigDecimal minAmount,
                                                         BigDecimal maxAmount,
                                                         LocalDate startDate,
                                                         LocalDate endDate,
                                                         Integer brandId,
                                                         Integer modelId,
                                                         String userSearch) {
        return saleDao.findSalesWithFilters(minAmount, maxAmount, startDate, endDate, brandId, modelId, userSearch);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesAmountWithFilters(BigDecimal minAmount,
                                                     BigDecimal maxAmount,
                                                     LocalDate startDate,
                                                     LocalDate endDate,
                                                     Integer brandId,
                                                     Integer modelId,
                                                     String userSearch) {

        return saleDao.sumSalesWithFilters(
                minAmount,
                maxAmount,
                startDate,
                endDate,
                brandId,
                modelId,
                userSearch
        );
    }

    @Transactional
    public void cancelSale(Integer saleId) {
        Sale sale = saleDao.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Продажа не найдена"));

        User user = userDao.findById(sale.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        List<SaleDetail> details = saleDetailDao.findBySaleId(saleId);

        if (details.isEmpty()) {
            throw new IllegalArgumentException("Детали продажи не найдены");
        }

        for (SaleDetail detail : details) {
            Car car = carDao.findById(detail.getCarId())
                    .orElseThrow(() -> new IllegalArgumentException("Автомобиль не найден"));

            int currentStock = car.getStockQuantity() != null ? car.getStockQuantity() : 0;
            int returnedQuantity = detail.getQuantity() != null ? detail.getQuantity() : 0;

            carDao.updateStock(detail.getCarId(), currentStock + returnedQuantity);
        }

        BigDecimal currentBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
        BigDecimal refundAmount = sale.getTotalAmount() != null ? sale.getTotalAmount() : BigDecimal.ZERO;

        user.setBalance(currentBalance.add(refundAmount));
        userDao.update(user);

        saleDetailDao.deleteBySaleId(saleId);
        saleDao.delete(saleId);
    }
}