package com.autosales.dao;

import com.autosales.model.CarSpecification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CarSpecificationDao {

    private final JdbcTemplate jdbcTemplate;

    public CarSpecificationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class CarSpecificationRowMapper implements RowMapper<CarSpecification> {
        @Override
        public CarSpecification mapRow(ResultSet rs, int rowNum) throws SQLException {
            CarSpecification spec = new CarSpecification();
            spec.setCarId(rs.getInt("car_id"));
            spec.setEngineVolume(rs.getBigDecimal("engine_volume"));
            spec.setHorsepower(rs.getInt("horsepower"));
            spec.setColor(rs.getString("color"));
            spec.setAirConditioning(rs.getBoolean("air_conditioning"));
            spec.setYear(rs.getInt("year"));
            spec.setMileage(rs.getInt("mileage")); // добавлено
            return spec;
        }
    }

    public Optional<CarSpecification> findByCarId(Integer carId) {
        String sql = "SELECT * FROM car_specifications WHERE car_id = ?";
        List<CarSpecification> list = jdbcTemplate.query(sql, new CarSpecificationRowMapper(), carId);
        return list.stream().findFirst();
    }

    public CarSpecification save(CarSpecification spec) {
        String sql = "INSERT INTO car_specifications (car_id, engine_volume, horsepower, color, air_conditioning, year, mileage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                spec.getCarId(),
                spec.getEngineVolume(),
                spec.getHorsepower(),
                spec.getColor(),
                spec.getAirConditioning(),
                spec.getYear(),
                spec.getMileage() // добавлено
        );
        return spec;
    }

    public void update(CarSpecification spec) {
        String sql = "UPDATE car_specifications SET engine_volume = ?, horsepower = ?, color = ?, air_conditioning = ?, year = ?, mileage = ? WHERE car_id = ?";
        jdbcTemplate.update(sql,
                spec.getEngineVolume(),
                spec.getHorsepower(),
                spec.getColor(),
                spec.getAirConditioning(),
                spec.getYear(),
                spec.getMileage(), // добавлено
                spec.getCarId()
        );
    }

    public void delete(Integer carId) {
        String sql = "DELETE FROM car_specifications WHERE car_id = ?";
        jdbcTemplate.update(sql, carId);
    }
}