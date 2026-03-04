package com.autosales.dao;

import com.autosales.model.AuditLog;
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
public class AuditLogDao {

    private final JdbcTemplate jdbcTemplate;

    public AuditLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class AuditLogRowMapper implements RowMapper<AuditLog> {
        @Override
        public AuditLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            AuditLog log = new AuditLog();
            log.setId(rs.getLong("log_id"));
            log.setUserId(rs.getInt("user_id"));
            log.setAction(rs.getString("action"));
            log.setTableName(rs.getString("table_name"));
            log.setRecordId(rs.getInt("record_id"));
            log.setOldValues(rs.getString("old_values"));
            log.setNewValues(rs.getString("new_values"));
            log.setIpAddress(rs.getString("ip_address"));
            log.setTimestamp(rs.getTimestamp("timestamp") != null ? rs.getTimestamp("timestamp").toLocalDateTime() : null);
            return log;
        }
    }

    public AuditLog save(AuditLog log) {
        String sql = "INSERT INTO audit_log (user_id, action, table_name, record_id, old_values, new_values, ip_address, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, log.getUserId());
            ps.setString(2, log.getAction());
            ps.setString(3, log.getTableName());
            ps.setInt(4, log.getRecordId());
            ps.setString(5, log.getOldValues());
            ps.setString(6, log.getNewValues());
            ps.setString(7, log.getIpAddress());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            log.setId(keyHolder.getKey().longValue());
        }
        return log;
    }

    public List<AuditLog> findAll() {
        String sql = "SELECT * FROM audit_log ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, new AuditLogRowMapper());
    }

    public List<AuditLog> findByUserId(Integer userId) {
        String sql = "SELECT * FROM audit_log WHERE user_id = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, new AuditLogRowMapper(), userId);
    }

    public List<AuditLog> findByTableName(String tableName) {
        String sql = "SELECT * FROM audit_log WHERE table_name = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, new AuditLogRowMapper(), tableName);
    }
}