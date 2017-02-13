package edu.sjsu.cmpe275.lab2.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joji on 11/5/16.
 */

@Entity
public class Phone {
    /* Properties */
    @Id
    private String id;
    @Column(unique=true)
    private String number; // Note, phone numbers must be unique
    private String description;
    @Embedded
    private Address address;
    @ManyToMany(mappedBy = "phones", cascade = CascadeType.ALL)
    @JsonBackReference // To avoid infinite loop
    private List<User> users = new ArrayList<User>();

    /* Getters & Setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
