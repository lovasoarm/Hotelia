package com.hotelia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String username;
    private String details;

    private LocalDateTime date;

    public AuditLog() {}

    public AuditLog(String action, String username, String details) {
        this.action   = action;
        this.username = username;
        this.details  = details;
        this.date     = LocalDateTime.now();
    }

    public Long getId()          { return id; }
    public String getAction()    { return action; }
    public String getUsername()  { return username; }
    public String getDetails()   { return details; }
    public LocalDateTime getDate() { return date; }
}