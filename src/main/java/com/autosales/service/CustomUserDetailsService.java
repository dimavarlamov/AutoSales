package com.autosales.service;

import com.autosales.dao.RoleDao;
import com.autosales.dao.UserDao;
import com.autosales.model.Role;
import com.autosales.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Role role = roleDao.findById(user.getRoleId())
                .orElseThrow(() -> new IllegalStateException("Роль не найдена"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .disabled(!user.getEnabled())
                .authorities(role.getName()) // роль уже с префиксом ROLE_
                .build();
    }
}