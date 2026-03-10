package com.hotelia.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private final List<Reservation> reservations = new ArrayList<>();

    public Client() {}

    public Client(String firstName, String lastName, String email, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }


    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setPhone(String phone) {
        if(phone != null && !phone.isBlank()) {
            this.phone = phone;
        }
    }

    public void setAddress(String address) {
        if(address != null && !address.isBlank()) {
            this.address = address;
        }
    }

    public Reservation createReservation(Room room,
                                         LocalDate checkIn,
                                         LocalDate checkOut) {

        Reservation reservation = new Reservation(this, room, checkIn, checkOut);
        reservations.add(reservation);

        return reservation;
    }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name=" + getFullName() +
                ", email=" + email +
                ", phone=" + phone +
                ", address=" + address +
                '}';
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}