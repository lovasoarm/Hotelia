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

    public void calculate() {
        this.totalAmount = reservation.calculateTotal();
    }

    public void markAsPaid() {
        this.isPaid = true;
    }
}