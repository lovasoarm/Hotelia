package com.hotelia.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
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

    public Client(String firstName, String lastName, String email, String p, String a) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = p;
        this.address = a;
    }

    public Long getId() {
        return id;
    }

    @OneToMany(mappedBy = "client")
    private List<Reservation> reservations;

    public Client() {}


    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name=" + firstName + " " + lastName + ", email=" + email + ", phone="+phone+", address: "+address+'}';
    }


}