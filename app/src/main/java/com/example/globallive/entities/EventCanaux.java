package com.example.globallive.entities;

public class EventCanaux {

    public int id;
    public String canalEventName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCanalEventName() {
        return canalEventName;
    }

    public void setCanalEventName(String canalEventName) {
        this.canalEventName = canalEventName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return canalEventName;
    }

}
