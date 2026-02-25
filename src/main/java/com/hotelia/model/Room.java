package com.hotelia.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;
    private String type;
    private double pricePerNight;
    private String status;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations = new ArrayList<>();

    public Room() {}

    public Room(String roomNumber, String type, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = "AVAILABLE";
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {

        for (Reservation r : reservations) {

            if (!r.getStatus().equals("CANCELLED")) {

                boolean overlap =
                        checkIn.isBefore(r.getCheckOutDate()) &&
                                checkOut.isAfter(r.getCheckInDate());

                if (overlap) {
                    return false;
                }
            }
        }

        return true;
    }
}