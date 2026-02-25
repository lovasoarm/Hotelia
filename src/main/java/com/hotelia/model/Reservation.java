package com.hotelia.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Room room;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private String status;

    private LocalDateTime createdAt;

    public Reservation() {
    }

    public Reservation(Client client,
                       Room room,
                       LocalDate checkInDate,
                       LocalDate checkOutDate) {

        this.client = client;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public long getNumberOfNights() {
        return checkOutDate.toEpochDay() - checkInDate.toEpochDay();
    }

    public double calculateTotal() {
        return getNumberOfNights() * room.getPricePerNight();
    }

    public void confirm() {
        this.status = "CONFIRMED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }
}