package edu.sjsu.cmpe275.lab2.domain;

import javax.persistence.Embeddable;

/**
 * Created by joji on 11/5/16.
 */

@Embeddable
public class Address {

    /* Properties */
    String street;
    String city;
    String state;
    String zip;

    /* Getters & Setters */
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
