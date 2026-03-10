package com.hotelia.service;

import com.hotelia.dao.AuditLogDAO;
import com.hotelia.model.AuditLog;
import java.util.List;

public class AuditLogService {

    private final AuditLogDAO auditLogDAO;

    public AuditLogService(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }

    public void log(String action, String username, String details) {
        AuditLog log = new AuditLog(action, username, details);
        auditLogDAO.save(log);
    }

    public List<AuditLog> findAll() {
        return auditLogDAO.findAll();
    }
}