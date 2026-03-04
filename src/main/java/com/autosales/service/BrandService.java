package com.autosales.service;

import com.autosales.dao.BrandDao;
import com.autosales.model.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandDao brandDao;

    @Transactional(readOnly = true)
    public List<Brand> getAllBrands() {
        return brandDao.findAll();
    }

    @Transactional(readOnly = true)
    public Brand getBrandById(Integer id) {
        return brandDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Марка не найдена"));
    }

    @Transactional
    public Brand saveBrand(Brand brand) {
        return brandDao.save(brand);
    }

    @Transactional
    public void updateBrand(Brand brand) {
        brandDao.update(brand);
    }

    @Transactional
    public void deleteBrand(Integer id) {
        brandDao.delete(id);
    }
}