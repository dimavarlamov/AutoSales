package com.autosales.dao;

import com.autosales.model.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CarDao {

    private final JdbcTemplate jdbcTemplate;

    public CarDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class CarRowMapper implements RowMapper<Car> {
        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            Car car = new Car();
            car.setId(rs.getInt("car_id"));
            car.setModelId(rs.getInt("model_id"));
            car.setPrice(rs.getBigDecimal("price"));
            car.setStockQuantity(rs.getInt("stock_quantity"));
            car.setExpectedDate(rs.getDate("expected_date") != null ? rs.getDate("expected_date").toLocalDate() : null);
            car.setDescription(rs.getString("description"));
            car.setImage(rs.getString("image"));
            car.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            car.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
            car.setBrandName(rs.getString("brand_name"));
            car.setModelName(rs.getString("model_name"));
            car.setBrandCountry(rs.getString("brand_country"));
            return car;
        }
    }

    public Optional<Car> findById(Integer id) {
        String sql = "SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name FROM cars c " +
                "JOIN models m ON c.model_id = m.model_id " +
                "JOIN brands b ON m.brand_id = b.brand_id " +
                "WHERE c.car_id = ?";
        List<Car> cars = jdbcTemplate.query(sql, new CarRowMapper(), id);
        return cars.stream().findFirst();
    }

    public List<Car> findByModelId(Integer modelId) {
        String sql = "SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name FROM cars c " +
                "JOIN models m ON c.model_id = m.model_id " +
                "JOIN brands b ON m.brand_id = b.brand_id " +
                "WHERE c.model_id = ?";
        return jdbcTemplate.query(sql, new CarRowMapper(), modelId);
    }

    public List<Car> findByBrandId(Integer brandId) {
        String sql = "SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name FROM cars c " +
                "JOIN models m ON c.model_id = m.model_id " +
                "JOIN brands b ON m.brand_id = b.brand_id " +
                "WHERE b.brand_id = ?";
        return jdbcTemplate.query(sql, new CarRowMapper(), brandId);
    }

    public List<Car> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name FROM cars c " +
                "JOIN models m ON c.model_id = m.model_id " +
                "JOIN brands b ON m.brand_id = b.brand_id " +
                "WHERE c.price BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new CarRowMapper(), minPrice, maxPrice);
    }

    public List<Car> findInStock() {
        String sql = "SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name FROM cars c " +
                "JOIN models m ON c.model_id = m.model_id " +
                "JOIN brands b ON m.brand_id = b.brand_id " +
                "WHERE c.stock_quantity > 0";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public List<Car> findAll() {
        String sql = "SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name FROM cars c " +
                "JOIN models m ON c.model_id = m.model_id " +
                "JOIN brands b ON m.brand_id = b.brand_id " +
                "ORDER BY c.car_id DESC";
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    public Car save(Car car) {
        String sql = "INSERT INTO cars (model_id, price, stock_quantity, expected_date, description, image, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, car.getModelId());
            ps.setBigDecimal(2, car.getPrice());
            ps.setInt(3, car.getStockQuantity() != null ? car.getStockQuantity() : 0);
            if (car.getExpectedDate() != null) {
                ps.setDate(4, java.sql.Date.valueOf(car.getExpectedDate()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setString(5, car.getDescription());
            ps.setString(6, car.getImage());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            car.setId(keyHolder.getKey().intValue());
        }
        return car;
    }

    public void update(Car car) {
        String sql = "UPDATE cars SET model_id = ?, price = ?, stock_quantity = ?, expected_date = ?, description = ?, image = ?, updated_at = NOW() WHERE car_id = ?";
        jdbcTemplate.update(sql,
                car.getModelId(),
                car.getPrice(),
                car.getStockQuantity(),
                car.getExpectedDate() != null ? java.sql.Date.valueOf(car.getExpectedDate()) : null,
                car.getDescription(),
                car.getImage(),
                car.getId()
        );
    }

    public void updateStock(Integer id, Integer newQuantity) {
        String sql = "UPDATE cars SET stock_quantity = ?, updated_at = NOW() WHERE car_id = ?";
        jdbcTemplate.update(sql, newQuantity, id);
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM cars WHERE car_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM cars WHERE car_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public List<Car> findWithFilters(String search, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        StringBuilder sql = new StringBuilder("SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name FROM cars c ");
        sql.append("JOIN models m ON c.model_id = m.model_id ");
        sql.append("JOIN brands b ON m.brand_id = b.brand_id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (b.name LIKE ? OR m.name LIKE ?) ");
            String like = "%" + search + "%";
            params.add(like);
            params.add(like);
        }
        if (brandId != null && brandId > 0) {
            sql.append("AND b.brand_id = ? ");
            params.add(brandId);
        }
        if (minPrice != null) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }

        sql.append("ORDER BY c.car_id DESC LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);

        return jdbcTemplate.query(sql.toString(), new CarRowMapper(), params.toArray());
    }

    public int countWithFilters(String search, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM cars c ");
        sql.append("JOIN models m ON c.model_id = m.model_id ");
        sql.append("JOIN brands b ON m.brand_id = b.brand_id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (b.name LIKE ? OR m.name LIKE ?) ");
            String like = "%" + search + "%";
            params.add(like);
            params.add(like);
        }
        if (brandId != null && brandId > 0) {
            sql.append("AND b.brand_id = ? ");
            params.add(brandId);
        }
        if (minPrice != null) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }

        return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }

    /**
     * Расширенный поиск для каталога с учётом характеристик и модели.
     * Не используется в админке, чтобы не сломать существующую логику.
     */
    public List<Car> findWithAdvancedFilters(String search,
                                             Integer brandId,
                                             Integer modelId,
                                             BigDecimal minPrice,
                                             BigDecimal maxPrice,
                                             String country,
                                             String steeringSide,
                                             String engineType,
                                             Integer yearFrom,
                                             Integer yearTo,
                                             Integer mileageFrom,
                                             Integer mileageTo,
                                             Integer hpFrom,
                                             Integer hpTo,
                                             String bodyType,
                                             String color,
                                             Boolean airConditioning,
                                             int page,
                                             int size) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.*, b.name AS brand_name, b.country AS brand_country, m.name AS model_name " +
                        "FROM cars c " +
                        "JOIN models m ON c.model_id = m.model_id " +
                        "JOIN brands b ON m.brand_id = b.brand_id " +
                        "LEFT JOIN car_specifications cs ON cs.car_id = c.car_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (b.name LIKE ? OR m.name LIKE ?) ");
            String like = "%" + search + "%";
            params.add(like);
            params.add(like);
        }
        if (brandId != null && brandId > 0) {
            sql.append("AND b.brand_id = ? ");
            params.add(brandId);
        }
        if (modelId != null && modelId > 0) {
            sql.append("AND m.model_id = ? ");
            params.add(modelId);
        }
        if (minPrice != null) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }
        if (country != null && !country.isBlank()) {
            sql.append("AND b.country = ? ");
            params.add(country);
        }
        if (steeringSide != null && !steeringSide.isBlank()) {
            sql.append("AND m.steering_wheel_side = ? ");
            params.add(steeringSide);
        }
        if (engineType != null && !engineType.isBlank()) {
            sql.append("AND m.engine_type = ? ");
            params.add(engineType);
        }
        if (yearFrom != null) {
            sql.append("AND cs.year >= ? ");
            params.add(yearFrom);
        }
        if (yearTo != null) {
            sql.append("AND cs.year <= ? ");
            params.add(yearTo);
        }
        if (mileageFrom != null) {
            sql.append("AND cs.mileage >= ? ");
            params.add(mileageFrom);
        }
        if (mileageTo != null) {
            sql.append("AND cs.mileage <= ? ");
            params.add(mileageTo);
        }
        if (hpFrom != null) {
            sql.append("AND cs.horsepower >= ? ");
            params.add(hpFrom);
        }
        if (hpTo != null) {
            sql.append("AND cs.horsepower <= ? ");
            params.add(hpTo);
        }
        if (bodyType != null && !bodyType.isBlank()) {
            sql.append("AND m.body_type = ? ");
            params.add(bodyType);
        }
        if (color != null && !color.isBlank()) {
            sql.append("AND cs.color = ? ");
            params.add(color);
        }
        if (airConditioning != null) {
            sql.append("AND cs.air_conditioning = ? ");
            params.add(airConditioning);
        }

        sql.append("ORDER BY c.car_id DESC LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);

        return jdbcTemplate.query(sql.toString(), new CarRowMapper(), params.toArray());
    }

    public int countWithAdvancedFilters(String search,
                                        Integer brandId,
                                        Integer modelId,
                                        BigDecimal minPrice,
                                        BigDecimal maxPrice,
                                        String country,
                                        String steeringSide,
                                        String engineType,
                                        Integer yearFrom,
                                        Integer yearTo,
                                        Integer mileageFrom,
                                        Integer mileageTo,
                                        Integer hpFrom,
                                        Integer hpTo,
                                        String bodyType,
                                        String color,
                                        Boolean airConditioning) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM cars c " +
                        "JOIN models m ON c.model_id = m.model_id " +
                        "JOIN brands b ON m.brand_id = b.brand_id " +
                        "LEFT JOIN car_specifications cs ON cs.car_id = c.car_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (b.name LIKE ? OR m.name LIKE ?) ");
            String like = "%" + search + "%";
            params.add(like);
            params.add(like);
        }
        if (brandId != null && brandId > 0) {
            sql.append("AND b.brand_id = ? ");
            params.add(brandId);
        }
        if (modelId != null && modelId > 0) {
            sql.append("AND m.model_id = ? ");
            params.add(modelId);
        }
        if (minPrice != null) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }
        if (country != null && !country.isBlank()) {
            sql.append("AND b.country = ? ");
            params.add(country);
        }
        if (steeringSide != null && !steeringSide.isBlank()) {
            sql.append("AND m.steering_wheel_side = ? ");
            params.add(steeringSide);
        }
        if (engineType != null && !engineType.isBlank()) {
            sql.append("AND m.engine_type = ? ");
            params.add(engineType);
        }
        if (yearFrom != null) {
            sql.append("AND cs.year >= ? ");
            params.add(yearFrom);
        }
        if (yearTo != null) {
            sql.append("AND cs.year <= ? ");
            params.add(yearTo);
        }
        if (mileageFrom != null) {
            sql.append("AND cs.mileage >= ? ");
            params.add(mileageFrom);
        }
        if (mileageTo != null) {
            sql.append("AND cs.mileage <= ? ");
            params.add(mileageTo);
        }
        if (hpFrom != null) {
            sql.append("AND cs.horsepower >= ? ");
            params.add(hpFrom);
        }
        if (hpTo != null) {
            sql.append("AND cs.horsepower <= ? ");
            params.add(hpTo);
        }
        if (bodyType != null && !bodyType.isBlank()) {
            sql.append("AND m.body_type = ? ");
            params.add(bodyType);
        }
        if (color != null && !color.isBlank()) {
            sql.append("AND cs.color = ? ");
            params.add(color);
        }
        if (airConditioning != null) {
            sql.append("AND cs.air_conditioning = ? ");
            params.add(airConditioning);
        }

        return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }
}