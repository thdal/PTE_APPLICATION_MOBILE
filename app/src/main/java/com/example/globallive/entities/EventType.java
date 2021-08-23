package com.example.globallive.entities;

public class EventType {

    public int id;
    public String typeEventName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeEventName() {
        return typeEventName;
    }

    public void setTypeEventName(String typeEventName) {
        this.typeEventName = typeEventName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return typeEventName;
    }

}
