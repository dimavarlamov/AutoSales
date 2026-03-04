package com.autosales.dao;

import com.autosales.model.User;
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
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPatronymic(rs.getString("patronymic"));
            user.setPassportSeries(rs.getString("passport_series"));
            user.setPassportNumber(rs.getString("passport_number"));
            user.setAddress(rs.getString("address"));
            user.setPhone(rs.getString("phone"));
            user.setPaymentTypeDefault(rs.getString("payment_type_default"));
            user.setRoleId(rs.getInt("role_id"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setBalance(rs.getBigDecimal("balance"));
            user.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            user.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
            return user;
        }
    }

    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), email);
        return users.stream().findFirst();
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public List<User> findByRoleId(Integer roleId) {
        String sql = "SELECT * FROM users WHERE role_id = ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), roleId);
    }

    public User save(User user) {
        String sql = "INSERT INTO users (email, password_hash, first_name, last_name, patronymic, " +
                "passport_series, passport_number, address, phone, payment_type_default, " +
                "role_id, enabled, balance, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        System.out.println("SQL: " + sql);
        System.out.println("Params: " + user.getEmail() + ", " + user.getPasswordHash() + ", " +
                user.getFirstName() + ", " + user.getLastName() + ", " + user.getPatronymic() + ", " +
                user.getPassportSeries() + ", " + user.getPassportNumber() + ", " + user.getAddress() + ", " +
                user.getPhone() + ", " + user.getPaymentTypeDefault() + ", " + user.getRoleId() + ", " +
                user.getEnabled() + ", " + user.getBalance());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getPatronymic());
            ps.setString(6, user.getPassportSeries());
            ps.setString(7, user.getPassportNumber());
            ps.setString(8, user.getAddress());
            ps.setString(9, user.getPhone());
            ps.setString(10, user.getPaymentTypeDefault());
            ps.setInt(11, user.getRoleId());
            ps.setBoolean(12, user.getEnabled() != null ? user.getEnabled() : true);
            ps.setBigDecimal(13, user.getBalance());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().intValue());
            System.out.println("Generated ID: " + user.getId());
        } else {
            System.out.println("No key generated!");
        }
        return user;
    }

    public void update(User user) {
        String sql = "UPDATE users SET email = ?, password_hash = ?, first_name = ?, last_name = ?, patronymic = ?, " +
                "passport_series = ?, passport_number = ?, address = ?, phone = ?, payment_type_default = ?, " +
                "role_id = ?, enabled = ?, balance = ?, updated_at = NOW() " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPasswordHash(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPassportSeries(),
                user.getPassportNumber(),
                user.getAddress(),
                user.getPhone(),
                user.getPaymentTypeDefault(),
                user.getRoleId(),
                user.getEnabled(),
                user.getBalance(),
                user.getId()
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}