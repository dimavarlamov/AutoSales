package com.autosales.service;

import com.autosales.dao.AuditLogDao;
import com.autosales.model.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogDao auditLogDao;
    private final UserService userService;

    @Transactional
    public void logAction(String action, String tableName, Integer recordId,
                          String oldValues, String newValues, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setUserId(getCurrentUserId());
        log.setAction(action);
        log.setTableName(tableName);
        log.setRecordId(recordId);
        log.setOldValues(oldValues);
        log.setNewValues(newValues);
        log.setIpAddress(getClientIp(request));
        log.setTimestamp(LocalDateTime.now());
        auditLogDao.save(log);
    }

    private Integer getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            try {
                return userService.getUserByEmail(email).getId();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}