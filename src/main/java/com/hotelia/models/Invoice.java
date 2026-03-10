package com.hotelia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime issueDate;
    private double totalAmount;
    private boolean isPaid;

    @OneToOne
    private Reservation reservation;

    public Invoice() {}

    public Invoice(Reservation reservation) {
        this.reservation = reservation;
        this.issueDate = LocalDateTime.now();
        this.isPaid = false;
        calculate();
    }
    public Long getId()                  { return id; }
    public LocalDateTime getIssueDate()  { return issueDate; }
    public double getTotalAmount()       { return totalAmount; }
    public boolean isPaid()              { return isPaid; }
    public Reservation getReservation()  { return reservation; }

    public void calculate() {
        this.totalAmount = reservation.calculateTotal();
    }

    public void markAsPaid() {
        this.isPaid = true;
    }
}