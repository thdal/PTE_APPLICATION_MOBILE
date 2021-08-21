package com.example.globallive.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    public int id;
    private String eventName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date eventDate;
    private String eventLink;
    private String eventAddress;
    private String eventDescription;
    private boolean eventImg;
    private int typeEventId;
    private int canalEventId;
    private int userId;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public boolean isEventImg() {
        return eventImg;
    }

    public void setEventImg(boolean eventImg) {
        this.eventImg = eventImg;
    }

    public int getTypeEventId() {
        return typeEventId;
    }

    public void setTypeEventId(int typeEventId) {
        this.typeEventId = typeEventId;
    }

    public int getCanalEventId() {
        return canalEventId;
    }

    public void setCanalEventId(int canalEventId) {
        this.canalEventId = canalEventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
