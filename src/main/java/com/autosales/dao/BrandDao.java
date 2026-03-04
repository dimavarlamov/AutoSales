package com.autosales.dao;

import com.autosales.model.Brand;
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
public class BrandDao {

    private final JdbcTemplate jdbcTemplate;

    public BrandDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class BrandRowMapper implements RowMapper<Brand> {
        @Override
        public Brand mapRow(ResultSet rs, int rowNum) throws SQLException {
            Brand brand = new Brand();
            brand.setId(rs.getInt("brand_id"));
            brand.setName(rs.getString("name"));
            brand.setCountry(rs.getString("country"));
            return brand;
        }
    }

    public Optional<Brand> findById(Integer id) {
        String sql = "SELECT * FROM brands WHERE brand_id = ?";
        List<Brand> brands = jdbcTemplate.query(sql, new BrandRowMapper(), id);
        return brands.stream().findFirst();
    }

    public Optional<Brand> findByName(String name) {
        String sql = "SELECT * FROM brands WHERE name = ?";
        List<Brand> brands = jdbcTemplate.query(sql, new BrandRowMapper(), name);
        return brands.stream().findFirst();
    }

    public List<Brand> findAll() {
        String sql = "SELECT * FROM brands";
        return jdbcTemplate.query(sql, new BrandRowMapper());
    }

    public Brand save(Brand brand) {
        String sql = "INSERT INTO brands (name, country) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, brand.getName());
            ps.setString(2, brand.getCountry());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            brand.setId(keyHolder.getKey().intValue());
        }
        return brand;
    }

    public void update(Brand brand) {
        String sql = "UPDATE brands SET name = ?, country = ? WHERE brand_id = ?";
        jdbcTemplate.update(sql, brand.getName(), brand.getCountry(), brand.getId());
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM brands WHERE brand_id = ?";
        jdbcTemplate.update(sql, id);
    }
}