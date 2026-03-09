package com.hotelia.model;

import com.hotelia.enums.ReservationStatus;
import com.hotelia.enums.RoomStatus;
import com.hotelia.enums.RoomType;
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
    private double pricePerNight;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations = new ArrayList<>();

    public Room() {}

    public Room(String roomNumber, RoomType type, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = RoomStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public RoomType getType() {
        return type;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void updateStatus(RoomStatus status) {
        this.status = status;
    }

    public boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
        for (Reservation r : reservations) {
            if (!r.getStatus().equals(ReservationStatus.CANCELLED)) {
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

    public List<Reservation> getReservations() {
        return reservations;
    }
}