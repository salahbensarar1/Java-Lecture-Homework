package org.example.javalecturehomework.model;

import java.sql.Date;
import java.sql.Time;

public class Match {
    private int id;
    private Date mdate;
    private Time startsAt;
    private double ticketPrice;
    private String mtype;

    public Match(int id, Date mdate, Time startsAt, double ticketPrice, String mtype) {
        this.id = id;
        this.mdate = mdate;
        this.startsAt = startsAt;
        this.ticketPrice = ticketPrice;
        this.mtype = mtype;
    }

    public Date getMdate() {
        return mdate;
    }

    public Time getStartsAt() {
        return startsAt;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    public void setStartsAt(Time startsAt) {
        this.startsAt = startsAt;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", mdate=" + mdate +
                ", startsAt=" + startsAt +
                ", ticketPrice=" + ticketPrice +
                ", mtype='" + mtype + '\'' +
                '}';
    }

    // Getters and setters here
}
