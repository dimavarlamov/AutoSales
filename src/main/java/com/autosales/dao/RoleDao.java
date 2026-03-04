package com.autosales.dao;

import com.autosales.model.Role;
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
public class RoleDao {

    private final JdbcTemplate jdbcTemplate;

    public RoleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class RoleRowMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getInt("role_id"));
            role.setName(rs.getString("name"));
            return role;
        }
    }

    public Optional<Role> findById(Integer id) {
        String sql = "SELECT * FROM roles WHERE role_id = ?";
        List<Role> roles = jdbcTemplate.query(sql, new RoleRowMapper(), id);
        return roles.stream().findFirst();
    }

    public Optional<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";
        List<Role> roles = jdbcTemplate.query(sql, new RoleRowMapper(), name);
        return roles.stream().findFirst();
    }

    public List<Role> findAll() {
        String sql = "SELECT * FROM roles";
        return jdbcTemplate.query(sql, new RoleRowMapper());
    }

    public Role save(Role role) {
        String sql = "INSERT INTO roles (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, role.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            role.setId(keyHolder.getKey().intValue());
        }
        return role;
    }

    public void update(Role role) {
        String sql = "UPDATE roles SET name = ? WHERE role_id = ?";
        jdbcTemplate.update(sql, role.getName(), role.getId());
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM roles WHERE role_id = ?";
        jdbcTemplate.update(sql, id);
    }
}