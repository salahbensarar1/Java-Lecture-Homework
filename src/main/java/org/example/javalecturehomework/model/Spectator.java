package org.example.javalecturehomework.model;


public class Spectator {
    private int id;
    private String sname;
    private boolean male;
    private boolean hasPass;

    // Constructor
    public Spectator(int id, String sname, boolean male, boolean hasPass) {
        this.id = id;
        this.sname = sname;
        this.male = male;
        this.hasPass = hasPass;
    }

    public int getId() {
        return id;
    }

    public String getSname() {
        return sname;
    }

    public boolean isMale() {
        return male;
    }

    public boolean hasPass() {
        return hasPass;
    }

}