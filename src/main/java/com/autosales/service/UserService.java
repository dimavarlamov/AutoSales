package com.autosales.service;

import com.autosales.dao.RoleDao;
import com.autosales.dao.UserDao;
import com.autosales.dao.VerificationTokenDao;
import com.autosales.dto.RegistrationDto;
import com.autosales.model.Role;
import com.autosales.model.User;
import com.autosales.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenDao verificationTokenDao;
    private final EmailService emailService;

    @Value("${app.url}")
    private String appUrl;

    @Transactional
    public User register(RegistrationDto dto) {
        if (userDao.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        if (!dto.getPassword().equals(dto.getMatchingPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPatronymic(dto.getPatronymic());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        Role customerRole = roleDao.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new IllegalStateException("Роль покупателя не найдена"));
        user.setRoleId(customerRole.getId());

        user.setBalance(BigDecimal.valueOf(100000.00));
        user.setEnabled(false);

        System.out.println("=== UserService.register ===");
        System.out.println("Email: " + dto.getEmail());
        System.out.println("FirstName: " + dto.getFirstName());
        System.out.println("LastName: " + dto.getLastName());
        System.out.println("Patronymic: " + dto.getPatronymic());
        System.out.println("Password hash: " + user.getPasswordHash()); // осторожно, но для отладки можно
        System.out.println("RoleId: " + user.getRoleId());
        System.out.println("Balance: " + user.getBalance());
        System.out.println("Enabled: " + user.getEnabled());

        User saved = userDao.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setUserId(saved.getId());
        vt.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenDao.save(vt);

        String verificationLink = appUrl + "/auth/verify?token=" + token;
        emailService.sendVerificationEmail(saved.getEmail(), verificationLink);

        return saved;
    }

    @Transactional
    public void verifyEmail(String token) {
        VerificationToken vt = verificationTokenDao.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Неверный токен"));

        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Срок действия токена истёк");
        }

        User user = userDao.findById(vt.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setEnabled(true);
        userDao.update(user);
        verificationTokenDao.delete(vt.getId());
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (user.getEnabled()) {
            throw new IllegalArgumentException("Аккаунт уже подтверждён");
        }

        verificationTokenDao.findByUserId(user.getId()).ifPresent(vt -> verificationTokenDao.delete(vt.getId()));

        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setUserId(user.getId());
        vt.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenDao.save(vt);

        String verificationLink = appUrl + "/auth/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        return userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    @Transactional
    public void updateProfile(Integer userId, User updated) {
        User existing = getUserById(userId);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPatronymic(updated.getPatronymic());
        existing.setPassportSeries(updated.getPassportSeries());
        existing.setPassportNumber(updated.getPassportNumber());
        existing.setAddress(updated.getAddress());
        existing.setPhone(updated.getPhone());
        userDao.update(existing);
    }

    @Transactional
    public void changePassword(Integer userId, String newPassword) {
        User user = getUserById(userId);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userDao.update(user);
    }

    @Transactional
    public void updateBalance(Integer userId, BigDecimal newBalance) {
        if (newBalance == null || newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным");
        }
        User user = getUserById(userId);
        user.setBalance(newBalance);
        userDao.update(user);
    }
}