package com.autosales.dao;

import com.autosales.model.CarImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CarImageDao {

    private final JdbcTemplate jdbcTemplate;

    public CarImageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class CarImageRowMapper implements RowMapper<CarImage> {
        @Override
        public CarImage mapRow(ResultSet rs, int rowNum) throws SQLException {
            CarImage image = new CarImage();
            image.setId(rs.getInt("id"));
            image.setCarId(rs.getInt("car_id"));
            image.setImagePath(rs.getString("image_path"));
            image.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            return image;
        }
    }

    public List<CarImage> findByCarId(Integer carId) {
        String sql = "SELECT * FROM car_images WHERE car_id = ? ORDER BY created_at";
        return jdbcTemplate.query(sql, new CarImageRowMapper(), carId);
    }

    public CarImage save(CarImage image) {
        String sql = "INSERT INTO car_images (car_id, image_path, created_at) VALUES (?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, image.getCarId());
            ps.setString(2, image.getImagePath());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            image.setId(keyHolder.getKey().intValue());
        }
        return image;
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM car_images WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByCarId(Integer carId) {
        String sql = "DELETE FROM car_images WHERE car_id = ?";
        jdbcTemplate.update(sql, carId);
    }
}