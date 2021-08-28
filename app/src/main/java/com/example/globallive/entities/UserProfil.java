package com.example.globallive.entities;

public class UserProfil {
    int id;
    String profile_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return profile_name;
    }

}
