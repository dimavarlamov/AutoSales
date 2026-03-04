package com.autosales.dao;

import com.autosales.model.CarModel;
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
import java.util.Optional;

@Repository
public class CarModelDao {

    private final JdbcTemplate jdbcTemplate;

    public CarModelDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class CarModelRowMapper implements RowMapper<CarModel> {
        @Override
        public CarModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            CarModel model = new CarModel();
            model.setId(rs.getInt("model_id"));
            model.setBrandId(rs.getInt("brand_id"));
            model.setName(rs.getString("name"));
            model.setSteeringType(rs.getString("steering_type"));
            model.setTransmission(rs.getString("transmission"));
            model.setEngineType(rs.getString("engine_type"));
            model.setSteeringWheelSide(rs.getString("steering_wheel_side"));
            model.setBodyType(rs.getString("body_type"));
            model.setDoorsCount(rs.getInt("doors_count"));
            model.setSeatsCount(rs.getInt("seats_count"));
            try {
                model.setBrandName(rs.getString("brand_name"));
            } catch (SQLException e) {
                // ignore
            }
            return model;
        }
    }

    public Optional<CarModel> findById(Integer id) {
        String sql = "SELECT m.*, b.name as brand_name FROM models m " +
                "JOIN brands b ON m.brand_id = b.brand_id WHERE m.model_id = ?";
        List<CarModel> models = jdbcTemplate.query(sql, new CarModelRowMapper(), id);
        return models.stream().findFirst();
    }

    public List<CarModel> findByBrandId(Integer brandId) {
        String sql = "SELECT m.*, b.name as brand_name FROM models m " +
                "JOIN brands b ON m.brand_id = b.brand_id WHERE m.brand_id = ? ORDER BY m.name";
        return jdbcTemplate.query(sql, new CarModelRowMapper(), brandId);
    }

    public Optional<CarModel> findByBrandAndName(Integer brandId, String name) {
        String sql = "SELECT m.*, b.name as brand_name FROM models m " +
                "JOIN brands b ON m.brand_id = b.brand_id WHERE m.brand_id = ? AND m.name = ?";
        List<CarModel> models = jdbcTemplate.query(sql, new CarModelRowMapper(), brandId, name);
        return models.stream().findFirst();
    }

    public List<CarModel> findAll() {
        String sql = "SELECT m.*, b.name as brand_name FROM models m " +
                "JOIN brands b ON m.brand_id = b.brand_id ORDER BY b.name, m.name";
        return jdbcTemplate.query(sql, new CarModelRowMapper());
    }

    public CarModel save(CarModel model) {
        String sql = "INSERT INTO models (brand_id, name, steering_type, transmission, engine_type, steering_wheel_side, body_type, doors_count, seats_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, model.getBrandId());
            ps.setString(2, model.getName());
            ps.setString(3, model.getSteeringType());
            ps.setString(4, model.getTransmission());
            ps.setString(5, model.getEngineType());
            ps.setString(6, model.getSteeringWheelSide());
            ps.setString(7, model.getBodyType());
            ps.setInt(8, model.getDoorsCount());
            ps.setInt(9, model.getSeatsCount());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            model.setId(keyHolder.getKey().intValue());
        }
        return model;
    }

    public void update(CarModel model) {
        String sql = "UPDATE models SET brand_id = ?, name = ?, steering_type = ?, transmission = ?, engine_type = ?, steering_wheel_side = ?, body_type = ?, doors_count = ?, seats_count = ? WHERE model_id = ?";
        jdbcTemplate.update(sql,
                model.getBrandId(),
                model.getName(),
                model.getSteeringType(),
                model.getTransmission(),
                model.getEngineType(),
                model.getSteeringWheelSide(),
                model.getBodyType(),
                model.getDoorsCount(),
                model.getSeatsCount(),
                model.getId()
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM models WHERE model_id = ?";
        jdbcTemplate.update(sql, id);
    }
}