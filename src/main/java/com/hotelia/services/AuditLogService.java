package com.hotelia.services;

import com.hotelia.daos.AuditLogDAO;
import com.hotelia.models.AuditLog;
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