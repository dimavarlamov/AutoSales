package com.autosales.service;

import com.autosales.dao.CarDao;
import com.autosales.dao.FavoriteDao;
import com.autosales.model.Car;
import com.autosales.model.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteDao favoriteDao;
    private final CarDao carDao;

    @Transactional
    public void toggleFavorite(Integer userId, Integer carId) {
        if (favoriteDao.exists(userId, carId)) {
            favoriteDao.delete(userId, carId);
        } else {
            Favorite fav = new Favorite();
            fav.setUserId(userId);
            fav.setCarId(carId);
            favoriteDao.save(fav);
        }
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Integer userId, Integer carId) {
        return favoriteDao.exists(userId, carId);
    }

    @Transactional(readOnly = true)
    public List<Car> getUserFavorites(Integer userId) {
        return favoriteDao.findByUserId(userId).stream()
                .map(fav -> carDao.findById(fav.getCarId()).orElse(null))
                .collect(Collectors.toList());
    }

    public List<Favorite> getFavoritesByUserId(Integer userId) {
        return favoriteDao.findByUserId(userId);
    }
}