package com.autosales.dao;

import com.autosales.model.SaleDetail;
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
public class SaleDetailDao {

    private final JdbcTemplate jdbcTemplate;

    public SaleDetailDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class SaleDetailRowMapper implements RowMapper<SaleDetail> {
        @Override
        public SaleDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            SaleDetail detail = new SaleDetail();
            detail.setId(rs.getInt("detail_id"));
            detail.setSaleId(rs.getInt("sale_id"));
            detail.setCarId(rs.getInt("car_id"));
            detail.setQuantity(rs.getInt("quantity"));
            detail.setPriceAtSale(rs.getBigDecimal("price_at_sale"));
            return detail;
        }
    }

    public List<SaleDetail> findBySaleId(Integer saleId) {
        String sql = "SELECT * FROM sale_details WHERE sale_id = ?";
        return jdbcTemplate.query(sql, new SaleDetailRowMapper(), saleId);
    }

    public List<SaleDetail> findByCarId(Integer carId) {
        String sql = "SELECT * FROM sale_details WHERE car_id = ?";
        return jdbcTemplate.query(sql, new SaleDetailRowMapper(), carId);
    }

    // Синоним для findBySaleId (можно оставить или удалить)
    public List<SaleDetail> findDetailsBySaleId(Integer saleId) {
        return findBySaleId(saleId);
    }

    public SaleDetail save(SaleDetail detail) {
        String sql = "INSERT INTO sale_details (sale_id, car_id, quantity, price_at_sale) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, detail.getSaleId());
            ps.setInt(2, detail.getCarId());
            ps.setInt(3, detail.getQuantity());
            ps.setBigDecimal(4, detail.getPriceAtSale());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            detail.setId(keyHolder.getKey().intValue());
        }
        return detail;
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM sale_details WHERE detail_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteBySaleId(Integer saleId) {
        String sql = "DELETE FROM sale_details WHERE sale_id = ?";
        jdbcTemplate.update(sql, saleId);
    }
}