package com.autosales.dao;

import com.autosales.model.Sale;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SaleDao {

    private final JdbcTemplate jdbcTemplate;

    public SaleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class SaleRowMapper implements RowMapper<Sale> {
        @Override
        public Sale mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sale sale = new Sale();
            sale.setId(rs.getInt("sale_id"));
            sale.setUserId(rs.getInt("user_id"));
            sale.setSaleDate(rs.getTimestamp("sale_date") != null ? rs.getTimestamp("sale_date").toLocalDateTime() : null);
            sale.setTotalAmount(rs.getBigDecimal("total_amount"));
            sale.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            return sale;
        }
    }

    public Optional<Sale> findById(Integer id) {
        String sql = "SELECT * FROM sales WHERE sale_id = ?";
        List<Sale> sales = jdbcTemplate.query(sql, new SaleRowMapper(), id);
        return sales.stream().findFirst();
    }

    public List<Sale> findByUserId(Integer userId) {
        String sql = "SELECT * FROM sales WHERE user_id = ? ORDER BY sale_date DESC";
        return jdbcTemplate.query(sql, new SaleRowMapper(), userId);
    }

    public List<Sale> findAll() {
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";
        return jdbcTemplate.query(sql, new SaleRowMapper());
    }

    // Для отчёта "проданные модели по марке"
    public List<Sale> findSoldByBrand(Integer brandId) {
        String sql = "SELECT DISTINCT s.* FROM sales s " +
                "JOIN sale_details sd ON s.sale_id = sd.sale_id " +
                "JOIN cars c ON sd.car_id = c.car_id " +
                "JOIN models m ON c.model_id = m.model_id " +
                "WHERE m.brand_id = ? " +
                "ORDER BY s.sale_date DESC";
        return jdbcTemplate.query(sql, new SaleRowMapper(), brandId);
    }

    // Общая сумма продаж
    public BigDecimal getTotalSalesAmount() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }

    // Сумма продаж по бренду
    public BigDecimal getTotalSalesAmountByBrand(Integer brandId) {
        String sql = "SELECT COALESCE(SUM(s.total_amount), 0) FROM sales s " +
                "JOIN sale_details sd ON s.sale_id = sd.sale_id " +
                "JOIN cars c ON sd.car_id = c.car_id " +
                "JOIN models m ON c.model_id = m.model_id " +
                "WHERE m.brand_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, brandId);
    }

    public Sale save(Sale sale) {
        String sql = "INSERT INTO sales (user_id, sale_date, total_amount, created_at) VALUES (?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, sale.getUserId());
            ps.setTimestamp(2, sale.getSaleDate() != null ? java.sql.Timestamp.valueOf(sale.getSaleDate()) : java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setBigDecimal(3, sale.getTotalAmount());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            sale.setId(keyHolder.getKey().intValue());
        }
        return sale;
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM sales WHERE sale_id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Поиск продаж с деталями автомобиля и фильтрами для админ-панели.
     * Каждая строка соответствует одной записи "продажа + конкретный автомобиль".
     */
    public List<Map<String, Object>> findSalesWithFilters(BigDecimal minAmount,
                                                          BigDecimal maxAmount,
                                                          LocalDate startDate,
                                                          LocalDate endDate,
                                                          Integer brandId,
                                                          Integer modelId) {
        StringBuilder sql = new StringBuilder(
                "SELECT s.sale_id      AS saleId, " +
                        "s.sale_date    AS saleDate, " +
                        "s.total_amount AS totalAmount, " +
                        "s.user_id      AS userId, " +
                        "CONCAT_WS(' ', u.last_name, u.first_name, u.patronymic) AS userFullName, " +
                        "c.car_id       AS carId, " +
                        "c.image        AS carImage, " +
                        "b.name         AS brandName, " +
                        "m.name         AS modelName " +
                        "FROM sales s " +
                        "JOIN users u        ON s.user_id = u.user_id " +
                        "JOIN sale_details sd ON s.sale_id = sd.sale_id " +
                        "JOIN cars c         ON sd.car_id = c.car_id " +
                        "JOIN models m       ON c.model_id = m.model_id " +
                        "JOIN brands b       ON m.brand_id = b.brand_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (minAmount != null) {
            sql.append(" AND s.total_amount >= ? ");
            params.add(minAmount);
        }
        if (maxAmount != null) {
            sql.append(" AND s.total_amount <= ? ");
            params.add(maxAmount);
        }
        if (startDate != null) {
            sql.append(" AND s.sale_date >= ? ");
            params.add(Timestamp.valueOf(startDate.atStartOfDay()));
        }
        if (endDate != null) {
            sql.append(" AND s.sale_date < ? ");
            params.add(Timestamp.valueOf(endDate.plusDays(1).atStartOfDay()));
        }
        if (brandId != null) {
            sql.append(" AND m.brand_id = ? ");
            params.add(brandId);
        }
        if (modelId != null) {
            sql.append(" AND m.model_id = ? ");
            params.add(modelId);
        }

        sql.append(" ORDER BY s.sale_date DESC ");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }
}