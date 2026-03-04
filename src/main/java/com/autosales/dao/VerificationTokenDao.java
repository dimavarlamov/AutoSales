package com.autosales.dao;

import com.autosales.model.VerificationToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class VerificationTokenDao {

    private final JdbcTemplate jdbcTemplate;

    public VerificationTokenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class VerificationTokenRowMapper implements RowMapper<VerificationToken> {
        @Override
        public VerificationToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            VerificationToken token = new VerificationToken();
            token.setId(rs.getLong("id"));
            token.setToken(rs.getString("token"));
            token.setUserId(rs.getInt("user_id"));
            token.setExpiryDate(rs.getTimestamp("expiry_date") != null ? rs.getTimestamp("expiry_date").toLocalDateTime() : null);
            return token;
        }
    }

    public Optional<VerificationToken> findByToken(String token) {
        String sql = "SELECT * FROM verification_tokens WHERE token = ?";
        List<VerificationToken> list = jdbcTemplate.query(sql, new VerificationTokenRowMapper(), token);
        return list.stream().findFirst();
    }

    public Optional<VerificationToken> findByUserId(Integer userId) {
        String sql = "SELECT * FROM verification_tokens WHERE user_id = ?";
        List<VerificationToken> list = jdbcTemplate.query(sql, new VerificationTokenRowMapper(), userId);
        return list.stream().findFirst();
    }

    public VerificationToken save(VerificationToken token) {
        String sql = "INSERT INTO verification_tokens (token, user_id, expiry_date) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, token.getToken());
            ps.setInt(2, token.getUserId());
            ps.setTimestamp(3, token.getExpiryDate() != null ? java.sql.Timestamp.valueOf(token.getExpiryDate()) : null);
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            token.setId(keyHolder.getKey().longValue());
        }
        return token;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM verification_tokens WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByUserId(Integer userId) {
        String sql = "DELETE FROM verification_tokens WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}