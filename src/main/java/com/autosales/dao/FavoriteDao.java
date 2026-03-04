package com.autosales.dao;

import com.autosales.model.Favorite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class FavoriteDao {

    private final JdbcTemplate jdbcTemplate;

    public FavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class FavoriteRowMapper implements RowMapper<Favorite> {
        @Override
        public Favorite mapRow(ResultSet rs, int rowNum) throws SQLException {
            Favorite fav = new Favorite();
            fav.setUserId(rs.getInt("user_id"));
            fav.setCarId(rs.getInt("car_id"));
            fav.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            return fav;
        }
    }

    public List<Favorite> findByUserId(Integer userId) {
        String sql = "SELECT * FROM favorites WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new FavoriteRowMapper(), userId);
    }

    public Optional<Favorite> findByUserAndCar(Integer userId, Integer carId) {
        String sql = "SELECT * FROM favorites WHERE user_id = ? AND car_id = ?";
        List<Favorite> list = jdbcTemplate.query(sql, new FavoriteRowMapper(), userId, carId);
        return list.stream().findFirst();
    }

    public Favorite save(Favorite favorite) {
        String sql = "INSERT INTO favorites (user_id, car_id, created_at) VALUES (?, ?, NOW())";
        jdbcTemplate.update(sql, favorite.getUserId(), favorite.getCarId());
        return favorite;
    }

    public void delete(Integer userId, Integer carId) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND car_id = ?";
        jdbcTemplate.update(sql, userId, carId);
    }

    public boolean exists(Integer userId, Integer carId) {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND car_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, carId);
        return count != null && count > 0;
    }
}